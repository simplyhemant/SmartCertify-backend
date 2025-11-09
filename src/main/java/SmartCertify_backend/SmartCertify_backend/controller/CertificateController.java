package SmartCertify_backend.SmartCertify_backend.controller;

import SmartCertify_backend.SmartCertify_backend.entity.Certificate;
import SmartCertify_backend.SmartCertify_backend.exception.CertificateException;
import SmartCertify_backend.SmartCertify_backend.exception.TokenValidationException;
import SmartCertify_backend.SmartCertify_backend.exception.UserException;
import SmartCertify_backend.SmartCertify_backend.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadCertificate(
            @RequestHeader(value = "Authorization", required = false) String jwt,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            Certificate saved = certificateService.uploadCertificate(jwt, file);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);

        } catch (UserException | TokenValidationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "message", e.getMessage(),
                    "timestamp", LocalDateTime.now().toString()
            ));

        } catch (CertificateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", e.getMessage(),
                    "timestamp", LocalDateTime.now().toString()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", "Something went wrong. Please try again later.",
                    "details", e.getMessage(),
                    "timestamp", LocalDateTime.now().toString()
            ));
        }
    }
}