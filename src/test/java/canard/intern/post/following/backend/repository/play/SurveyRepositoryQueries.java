package canard.intern.post.following.backend.repository.play;

import canard.intern.post.following.backend.repository.SurveyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;


/**
 * This is not a Unit Test
 * Just here to validate Hibernate mapping
 * by reading data in the database (of the application)
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class SurveyRepositoryQueries {

    @Autowired
    SurveyRepository surveyRepository;

    @Test
    void displayOneSurvey(){
        surveyRepository.findById(1)
                .ifPresent(survey -> {
                    System.out.println(survey);
                    survey.getQuestions()
                            .forEach(question -> {
                                System.out.println("\t- " + question);
                                question.getChoices()
                                        .forEach(choice -> System.out.println("\t\t* " + choice));
                            });
                });
    }

}