package canard.intern.post.following.backend.repository;

import canard.intern.post.following.backend.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Integer> {

}
