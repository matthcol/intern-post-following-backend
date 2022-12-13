package canard.intern.post.following.backend.repository;

import canard.intern.post.following.backend.entity.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraineeRepository extends JpaRepository<Trainee, Integer> {
}
