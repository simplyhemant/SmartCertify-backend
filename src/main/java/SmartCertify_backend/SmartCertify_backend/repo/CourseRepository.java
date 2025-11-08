package SmartCertify_backend.SmartCertify_backend.repo;

import SmartCertify_backend.SmartCertify_backend.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
