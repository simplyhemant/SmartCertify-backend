package SmartCertify_backend.SmartCertify_backend.repo;

import SmartCertify_backend.SmartCertify_backend.entity.Certificate;
import SmartCertify_backend.SmartCertify_backend.entity.Course;
import SmartCertify_backend.SmartCertify_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    List<Certificate> findByUser(User user);

    List<Certificate> findByCourse(Course course);

    Optional<Certificate> findByCertificateCode(String certificateCode);

    boolean existsByCertificateCode(String certificateCode);

}
