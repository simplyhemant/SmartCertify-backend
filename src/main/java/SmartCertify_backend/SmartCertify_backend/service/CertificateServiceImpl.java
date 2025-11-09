package SmartCertify_backend.SmartCertify_backend.service;

import SmartCertify_backend.SmartCertify_backend.config.JwtProvider;
import SmartCertify_backend.SmartCertify_backend.entity.Certificate;
import SmartCertify_backend.SmartCertify_backend.entity.User;
import SmartCertify_backend.SmartCertify_backend.exception.CertificateException;
import SmartCertify_backend.SmartCertify_backend.exception.TokenValidationException;
import SmartCertify_backend.SmartCertify_backend.exception.UserException;
import SmartCertify_backend.SmartCertify_backend.repo.CertificateRepository;
import SmartCertify_backend.SmartCertify_backend.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CertificateServiceImpl implements CertificateService {

    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;
    private final CertificateRepository certificateRepository;
    private final JwtProvider jwtProvider;

    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/png",
            "image/jpeg",
            "image/jpg",
            "application/pdf"
    );

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    @Override
    public Certificate uploadCertificate(String jwt, MultipartFile file) throws CertificateException {

        // Get email from Security Context (set by JWT filter)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = null;

        if (authentication != null && authentication.isAuthenticated()) {
            email = authentication.getName();
        }

        // Fallback: try to get email from JWT if security context doesn't have it
        if (email == null || email.equals("anonymousUser")) {
            if (jwt == null || jwt.trim().isEmpty()) {
                throw new UserException("User must sign up or login before uploading the certificate.");
            }

            try {
                email = jwtProvider.getEmailFromJwtToken(jwt);
                if (email == null || email.trim().isEmpty()) {
                    throw new TokenValidationException("Invalid token: unable to extract email");
                }
            } catch (Exception e) {
                throw new TokenValidationException("Invalid or expired token: " + e.getMessage());
            }
        }

        // Find user
        String finalEmail = email;
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("User not found with email: " + finalEmail));

        // Validate file
        validateFile(file);

        try {
            // Upload to Cloudinary using the service
            String fileUrl = cloudinaryService.uploadCertificate(file);

            // Create and save certificate
            Certificate certificate = new Certificate();
            certificate.setFileUrl(fileUrl);
            certificate.setUserId(user.getId());
            certificate.setEmail(email);
            certificate.setUploadedAt(LocalDateTime.now());

            Certificate saved = certificateRepository.save(certificate);
            log.info("Certificate uploaded successfully for user: {}", email);

            return saved;

        } catch (Exception e) {
            log.error("Error uploading certificate: {}", e.getMessage(), e);
            throw new CertificateException("Error uploading file to Cloudinary: " + e.getMessage());
        }
    }

    private void validateFile(MultipartFile file) throws CertificateException {
        if (file == null || file.isEmpty()) {
            throw new CertificateException("Please upload a valid certificate file (PNG, JPG, or PDF).");
        }

        // Validate file size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new CertificateException("File size exceeds maximum limit of 10MB.");
        }

        // Validate content type
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new CertificateException(
                    "Invalid file type. Only PNG, JPG, JPEG, and PDF files are allowed."
            );
        }

        // Validate original filename
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new CertificateException("File must have a valid filename.");
        }
    }
}