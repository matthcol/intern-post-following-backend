package canard.intern.post.following.backend.entity;

import canard.intern.post.following.backend.enums.PoeType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "poes")
public class Poe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false)
    private LocalDate beginDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private PoeType poeType;

    // @OneToMany
    // private Set<Trainee> trainees;
}
