package SmartCertify_backend.SmartCertify_backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadCertificate(MultipartFile file) throws IOException {
        try {
            // Upload with proper configuration
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "auto",   // handles image/pdf automatically
                            "folder", "certificates",
                            "use_filename", true,      // preserve original filename
                            "unique_filename", true,   // add unique suffix to avoid conflicts
                            "overwrite", false         // don't overwrite existing files
                    )
            );

            // Validate upload result
            if (uploadResult == null || !uploadResult.containsKey("secure_url")) {
                throw new IOException("Failed to upload file: No secure URL returned");
            }

            String secureUrl = (String) uploadResult.get("secure_url");

            if (secureUrl == null || secureUrl.trim().isEmpty()) {
                throw new IOException("Failed to upload file: Invalid secure URL");
            }

            log.info("Successfully uploaded file to Cloudinary: {}", secureUrl);
            return secureUrl;

        } catch (IOException e) {
            log.error("Error uploading file to Cloudinary: {}", e.getMessage(), e);
            throw new IOException("Failed to upload file to Cloudinary: " + e.getMessage(), e);
        }
    }
}