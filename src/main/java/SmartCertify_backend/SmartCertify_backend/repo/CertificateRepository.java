package SmartCertify_backend.SmartCertify_backend.repo;

import SmartCertify_backend.SmartCertify_backend.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    List<Certificate> findByEmail(String email);
    List<Certificate> findByUserId(Long userId);
}
