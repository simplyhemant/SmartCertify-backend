package SmartCertify_backend.SmartCertify_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String certificateName;
    private String certificateCode;  // unique identifier

    private String fileUrl; // store file path or cloud URL (e.g., S3, local uploads)

    private LocalDateTime issuedAt;
    private LocalDateTime expiryAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
