package canard.intern.post.following.backend.repository;

import canard.intern.post.following.backend.entity.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TraineeRepository extends JpaRepository<Trainee, Integer> {

    // JPQL auto generated according to Spring Data Jpa vocabulary
    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
    List<Trainee> findByPoeId(int poeId);

    List<Trainee> findByPoeTitleIgnoreCase(String title);

    List<Trainee> findByLastnameContainingIgnoreCase(String partialName);
}
