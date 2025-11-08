package SmartCertify_backend.SmartCertify_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String instructor;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Certificate> certificates;
}
