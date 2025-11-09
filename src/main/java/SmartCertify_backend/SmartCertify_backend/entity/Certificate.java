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

    private String fileUrl;

    private String email;

    private LocalDateTime uploadedAt;

    private Long userId;

//    @ManyToOne
//    @JoinColumn(name = "course_id")
//    private Course course;
}
