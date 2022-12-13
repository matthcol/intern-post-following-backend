package canard.intern.post.following.backend.repository;

import canard.intern.post.following.backend.entity.Trainee;
import canard.intern.post.following.backend.enums.Gender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TraineeRepositoryTest {

    // component to test
    @Autowired
    TraineeRepository traineeRepository;

    // useful wrapper of EntityManager only for test
    // - inject in db data hypothesis
    // - check results in db
    @Autowired
    // EntityManager entityManager;
    TestEntityManager entityManager;

    // @Rollback(value = false)
    @ParameterizedTest
    @CsvSource({
            "Bond,James,M,1950-01-12,james.bond@im6.org,+33700700700",
            "Bond,Jane,F,1950-12-01,jane.bond@im6.org,+33700700700",
            "Neymar,Jean,,1951-01-12,jean.neymar@marre.org,+33701700701",
            "Spectre,Le,X,1949-01-12,le.spectre@badguy.org,"
    })
    void save_OK_allRequiredFields_CSV(
            String lastname,
            String firstname,
            Gender gender,
            LocalDate birthdate,
            String email,
            String phoneNumber
    ) {
        // given
        var trainee = Trainee.builder()
                .lastname(lastname)
                .firstname(firstname)
                .gender(gender)
                .birthdate(birthdate)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();

        // traineeRepository.save(trainee);
        traineeRepository.saveAndFlush(trainee);

        // verify 1: Id has been generated
        assertNotNull(trainee.getId());

        // (optional) verify 2: read data from db to check if data has been inserted
    }

    // TODO: test delete

    // TODO: test update


    @Test
    void findAll() {
        // given: save data in database
        var traineesDatabase = List.of(
                Trainee.builder()
                        .lastname("Bond")
                        .firstname("Jane")
                        .gender(Gender.F)
                        .birthdate(LocalDate.of(1950,1,2))
                        .email("jane.bond@im6.org")
                        .phoneNumber("+33700700701")
                        .build(),
                Trainee.builder()
                        .lastname("Bond")
                        .firstname("James")
                        .gender(Gender.M)
                        .birthdate(LocalDate.of(1950,2,3))
                        .email("james.bond@im6.org")
                        .phoneNumber("+33700700700")
                        .build(),
                Trainee.builder()
                        .lastname("Spectre")
                        .firstname("Le")
                        .gender(Gender.X)
                        .birthdate(LocalDate.of(1950,3,4))
                        .email("le.spectre@badguy.org")
                        .phoneNumber("+33666666666")
                        .build()
        );
        traineesDatabase.forEach(t -> entityManager.persist(t));
        entityManager.flush();
        entityManager.clear(); // empty hibernate cache

        // when: read all trainees from database
        var traineesRead = traineeRepository.findAll();
        System.out.println(traineesRead);

        // verify: all data has been read (size and content)
        assertEquals(traineesDatabase.size(), traineesRead.size());

        // TODO: check content
    }

    @Test
    void findById_present() {
        // given:
        var traineeDatabase = Trainee.builder()
                .lastname("Bond")
                .firstname("Jane")
                .gender(Gender.F)
                .birthdate(LocalDate.of(1950,1,2))
                .email("jane.bond@im6.org")
                .phoneNumber("+33700700701")
                .build();
        int id = entityManager.persistAndGetId(traineeDatabase, Integer.class);
        entityManager.flush();
        entityManager.clear(); // empty hibernate cache

        // when
        var optTrainee = traineeRepository.findById(id);

        // verify
        assertTrue(optTrainee.isPresent(), "trainee is present");
    }

    @Test
    void findById_absent() {
        // given: empty database
        int id = 12345;

        // when
        var optTrainee = traineeRepository.findById(id);

        // verify
        assertTrue(optTrainee.isEmpty(), "trainee is absent");
    }

    // TODO: find by lastname (partial, ignoring case)

}