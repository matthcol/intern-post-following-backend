package canard.intern.post.following.backend.repository.play;


import canard.intern.post.following.backend.enums.PoeType;
import canard.intern.post.following.backend.repository.PoeRepository;
import canard.intern.post.following.backend.repository.TraineeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Collection;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

// this is not a unit test
// goal: play with Jpa Repository queries
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class JpaRepositoryQueries {

    @Autowired
    TraineeRepository traineeRepository;

    @Autowired
    PoeRepository poeRepository;

    private static void displayCollection(Collection<?> collection) {
        for (var t: collection) {
            System.out.println("\t- " + t);
        }
    }

    @Test
    void traineesByLastnamePartialIgnoreCase() {
        String partialName = "money";
        var trainees = traineeRepository.findByLastnameContainingIgnoreCase(partialName);
        displayCollection(trainees);
    }

    @Test
    void poesByTitleIgnoreCase() {
        String title = "java fullstack";
        var poes = poeRepository.findByTitleIgnoreCase(title);
        displayCollection(poes);
    }

    @Test
    void poesByType() {
        PoeType poeType = PoeType.POEC;
        // String poeType = "POEC";
        var poes = poeRepository.findByPoeType(poeType);
        displayCollection(poes);
    }

    @Test
    void poesStartingYear(){
        int year = 2022;
        var poes = poeRepository.findByBeginDateBetween(
                LocalDate.of(year, 1, 1),
                LocalDate.of(year, 12, 31)
        );
        displayCollection(poes);
    }

    @Test
    void traineesByPoeTitleIgnoreCase() {
        String title = "java fullstack";
        var trainees = traineeRepository.findByPoeTitleIgnoreCase(title);
        displayCollection(trainees);
    }

}
