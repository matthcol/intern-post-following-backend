package canard.intern.post.following.backend.repository;

import canard.intern.post.following.backend.entity.Trainee;
import canard.intern.post.following.backend.enums.Gender;
import canard.intern.post.following.backend.repository.TraineeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// NB: test on H2 database by default, even if application is on a specific database
@DataJpaTest
@ActiveProfiles("testu")
class TraineeRepositoryTest {

    // component under test
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
        int idTrainee = trainee.getId();
        entityManager.clear();
        var traineeRead = entityManager.find(Trainee.class, idTrainee);
        assertNotNull(traineeRead);
        assertAll(
                () -> assertEquals(idTrainee, traineeRead.getId(), "trainee id"),
                () -> assertEquals(lastname, traineeRead.getLastname(), "trainee lastname"),
                () -> assertEquals(firstname, traineeRead.getFirstname(), "trainee firstname"),
                () -> assertEquals(gender, traineeRead.getGender(), "trainee gender"),
                () -> assertEquals(email, traineeRead.getEmail(), "trainee email"),
                () -> assertEquals(phoneNumber, traineeRead.getPhoneNumber(), "trainee phone number"),
                () -> assertEquals(birthdate, traineeRead.getBirthdate(), "trainee birthdate")
        );

    }

    @ParameterizedTest(name="integrityErrorExpected={0}")
    @CsvSource({
            "lastname null,,James,M,1950-01-12,james.bond@im6.org,+33700700700",
            "firstname null,Bond,,M,1950-01-12,james.bond@im6.org,+33700700700",
            "birthdate null,Bond,James,M,,james.bond@im6.org,+33700700700",
            "email null,Bond,James,M,1950-01-12,,+33700700700"
    })
    void save_KO_dataIntegrityException_CSV(
            String integrityErrorExpected,
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

        assertThrows(DataIntegrityViolationException.class, () ->
            traineeRepository.saveAndFlush(trainee)
        );
    }

    @Test
    void delete_OK_found() {
        // given this trainee in db
        String lastname = "Bond";
        String firstname = "James";
        Gender gender = Gender.M;
        LocalDate birthdate = LocalDate.of(1950,1,12);
        String email = "james.bond@im6.org";
        String phoneNumber = "+33700700700";

        var traineeDb = Trainee.builder()
                .lastname(lastname)
                .firstname(firstname)
                .gender(gender)
                .birthdate(birthdate)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
        entityManager.persistAndFlush(traineeDb);
        int idTrainee = traineeDb.getId();;

        entityManager.clear();

        // when deleting it
        assertDoesNotThrow(() -> {
            traineeRepository.deleteById(idTrainee);
            traineeRepository.flush();
        });

        // then verify trainee has been deleted
        var traineeRead = entityManager.find(Trainee.class, idTrainee);
        assertNull(traineeRead);

    }

    @Test
    void delete_KO_DataIntegrityException() {
        // TODO in the future if some entity is linked with this one  with a Foreign Key
    }

    @Test
    void update_OK_found() {
        // given this trainee in db
        String lastname = "Bond";
        String firstname = "James";
        Gender gender = Gender.M;
        LocalDate birthdate = LocalDate.of(1950,1,12);
        String email = "james.bond@im6.org";
        String phoneNumber = "+33700700700";

        var traineeDb = Trainee.builder()
                .lastname(lastname)
                .firstname(firstname)
                .gender(gender)
                .birthdate(birthdate)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
        entityManager.persistAndFlush(traineeDb);
        int idTrainee = traineeDb.getId();;

        entityManager.clear();

        // given new properties
        String newLastname = "Moneypenny";
        String newFirstname = "Miss";
        Gender newGender = Gender.F;
        LocalDate newBirthdate = LocalDate.of(1949,2,13);
        String newEmail = "miss.moneypenny@im6.org";
        String newPhoneNumber = "+33701701701";

        // when update existing trainee in db with new values
        var optTraineeToModified = traineeRepository.findById(idTrainee);
        assertTrue(optTraineeToModified.isPresent());
        var traineeToModified = optTraineeToModified.get();
        traineeToModified.setLastname(newLastname);
        traineeToModified.setFirstname(newFirstname);
        traineeToModified.setGender(newGender);
        traineeToModified.setBirthdate(newBirthdate);
        traineeToModified.setEmail(newEmail);
        traineeToModified.setPhoneNumber(newPhoneNumber);
        traineeRepository.flush();

        // then verify trainee has been updated in DB
        entityManager.clear();
        var traineeRead = entityManager.find(Trainee.class, idTrainee);
        assertNotNull(traineeRead);
        assertAll(
                () -> assertEquals(idTrainee, traineeRead.getId(), "trainee id"),
                () -> assertEquals(newLastname, traineeRead.getLastname(), "trainee lastname"),
                () -> assertEquals(newFirstname, traineeRead.getFirstname(), "trainee firstname"),
                () -> assertEquals(newGender, traineeRead.getGender(), "trainee gender"),
                () -> assertEquals(newEmail, traineeRead.getEmail(), "trainee email"),
                () -> assertEquals(newPhoneNumber, traineeRead.getPhoneNumber(), "trainee phone number"),
                () -> assertEquals(newBirthdate, traineeRead.getBirthdate(), "trainee birthdate")
        );
    }

    @Test
    void update_KO_DataIntegrityException() {
        // given two trainees with different emails in DB
        String existingEmail = "james.bond@im6.org";
        var firstTraineeDb = Trainee.builder()
                .lastname("Bond")
                .firstname("James")
                .birthdate(LocalDate.of(1950,1,12))
                .email(existingEmail)
                .build();
        var secondTraineeDb = Trainee.builder()
                .lastname("Moneypenny")
                .firstname("Miss")
                .birthdate(LocalDate.of(1951,2,13))
                .email("miss.moneypenny@im6.org")
                .build();
        // insert 2 trainees in DB
        entityManager.persist(firstTraineeDb);
        int idSecondTrainee = entityManager.persistAndGetId(secondTraineeDb, Integer.class);
        entityManager.flush();
        // clear Hibernate cache
        entityManager.clear();

        // when read and modify 2nd trainee via repository
        var optTraineeToModified = traineeRepository.findById(idSecondTrainee);
        assertTrue(optTraineeToModified.isPresent());
        var traineeToModified = optTraineeToModified.get();

        // modify email with an email existing for another trainee in DB
        // and then verity exception has been thrown
        assertThrows(DataIntegrityViolationException.class, () -> {
                traineeToModified.setEmail(existingEmail);
                traineeRepository.flush();
        });
    }

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