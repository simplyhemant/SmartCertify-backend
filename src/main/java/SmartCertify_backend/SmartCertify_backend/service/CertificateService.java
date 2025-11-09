package SmartCertify_backend.SmartCertify_backend.service;

import SmartCertify_backend.SmartCertify_backend.entity.Certificate;
import SmartCertify_backend.SmartCertify_backend.exception.CertificateException;
import org.springframework.web.multipart.MultipartFile;

public interface CertificateService {

    Certificate uploadCertificate(String jwt, MultipartFile file) throws CertificateException;

}
