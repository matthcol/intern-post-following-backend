package canard.intern.post.following.backend.entity;

import canard.intern.post.following.backend.enums.Gender;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDate;

@ToString(exclude = "poe")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "trainees")
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String lastname;

    @Column(length = 50, nullable = false)
    private String firstname;

    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(length = 15)
    private String phoneNumber;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    // @Transient // do not persist this attribute
    @ManyToOne
        //(fetch = FetchType.LAZY)  // on demand (default Eager i.e. always)
    // @JoinColumn(name = "id_poe") // customize Foreign Key column
    private Poe poe;
}
