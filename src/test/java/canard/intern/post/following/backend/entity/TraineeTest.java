package canard.intern.post.following.backend.entity;

import canard.intern.post.following.backend.enums.Gender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

// use H2 Database (in memory) by default
@DataJpaTest
@ActiveProfiles("testu")
class TraineeTest {

    // ORM Hibernate
    @Autowired
    EntityManager entityManager;

    @Test
    void mapping_OK_allRequiredFields(){
        // given
        String lastname = "Bond";
        String firstname = "James";
        Gender gender = Gender.M;
        LocalDate birthdate = LocalDate.of(1950,1,6);
        String email = "james.bond@im6.org";
        String phoneNumber = "+33700700700";
        var trainee = Trainee.builder()
                .lastname(lastname)
                .firstname(firstname)
                .gender(gender)
                .birthdate(birthdate)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
        // then
        entityManager.persist(trainee); // INSERT
        // force synchro between Hibernate and DB
        entityManager.flush();

        // verify: ID has been generated
        Integer idGenerated = trainee.getId();
        assertNotNull(idGenerated);

        // verify: data has been inserted in database
        // clear Hibernate cache
        entityManager.clear();
        var traineeRead = entityManager.find(Trainee.class, idGenerated); // SELECT
        assertNotNull(traineeRead);
        // following assertions are not necessary
        assertAll(
                () -> assertEquals(idGenerated, traineeRead.getId(), "trainee id"),
                () -> assertEquals(lastname, traineeRead.getLastname(), "trainee lastname"),
                () -> assertEquals(firstname, traineeRead.getFirstname(), "trainee firstname"),
                () -> assertEquals(gender, traineeRead.getGender(), "trainee gender"),
                () -> assertEquals(email, traineeRead.getEmail(), "trainee email"),
                () -> assertEquals(phoneNumber, traineeRead.getPhoneNumber(), "trainee phone number"),
                () -> assertEquals(birthdate, traineeRead.getBirthdate(), "trainee birthdate")
        );
    }

    @ParameterizedTest
    @EnumSource(Gender.class)
    @NullSource
    void mapping_OK_genderNullable(Gender gender){
        // given
        String lastname = "Bond";
        String firstname = "James";
        LocalDate birthdate = LocalDate.of(1950,1,6);
        String email = "james.bond@im6.org";
        String phoneNumber = "+33700700700";
        var trainee = Trainee.builder()
                .lastname(lastname)
                .firstname(firstname)
                .gender(gender)
                .birthdate(birthdate)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
        // then
        entityManager.persist(trainee); // INSERT
        // force synchro between Hibernate and DB
        entityManager.flush();
        System.out.println(trainee);

        // verify: ID has been generated
        Integer idGenerated = trainee.getId();
        assertNotNull(idGenerated);

        // verify: data has been inserted in database
        // clear Hibernate cache
        entityManager.clear();
        var traineeRead = entityManager.find(Trainee.class, idGenerated); // SELECT
        assertNotNull(traineeRead);
        // following assertions are not necessary
        assertAll(
                () -> assertEquals(idGenerated, traineeRead.getId(), "trainee id"),
                () -> assertEquals(lastname, traineeRead.getLastname(), "trainee lastname"),
                () -> assertEquals(firstname, traineeRead.getFirstname(), "trainee firstname"),
                () -> assertEquals(gender, traineeRead.getGender(), "trainee gender"),
                () -> assertEquals(email, traineeRead.getEmail(), "trainee email"),
                () -> assertEquals(phoneNumber, traineeRead.getPhoneNumber(), "trainee phone number"),
                () -> assertEquals(birthdate, traineeRead.getBirthdate(), "trainee birthdate")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"B", "Bond", "TQKbtsWCRKMdNcqtIMHrdGKFjsGBVVHxeRzkYcWOXchFGlhzCo"})
    void mapping_OK_requiredLastname(String lastname){
        // given
        String firstname = "James";
        Gender gender = Gender.M;
        LocalDate birthdate = LocalDate.of(1950,1,6);
        String email = "james.bond@im6.org";
        String phoneNumber = "+33700700700";
        var trainee = Trainee.builder()
                .lastname(lastname)
                .firstname(firstname)
                .gender(gender)
                .birthdate(birthdate)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
        // then
        entityManager.persist(trainee); // INSERT
        // force synchro between Hibernate and DB
        entityManager.flush();

        // verify: ID has been generated
        Integer idGenerated = trainee.getId();
        assertNotNull(idGenerated);

        // verify: data has been inserted in database
        // clear Hibernate cache
        entityManager.clear();
        var traineeRead = entityManager.find(Trainee.class, idGenerated); // SELECT
        assertNotNull(traineeRead);
        // following assertions are not necessary
        assertAll(
                () -> assertEquals(idGenerated, traineeRead.getId(), "trainee id"),
                () -> assertEquals(lastname, traineeRead.getLastname(), "trainee lastname"),
                () -> assertEquals(firstname, traineeRead.getFirstname(), "trainee firstname"),
                () -> assertEquals(gender, traineeRead.getGender(), "trainee gender"),
                () -> assertEquals(email, traineeRead.getEmail(), "trainee email"),
                () -> assertEquals(phoneNumber, traineeRead.getPhoneNumber(), "trainee phone number"),
                () -> assertEquals(birthdate, traineeRead.getBirthdate(), "trainee birthdate")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "kvJsnKAbAmzyycyPeezDwIBldDAeaLJGeczocrAkegvRcyJAqBJwx"})
    @NullSource
    void mapping_KO_requiredLastname(String lastname) {
        //given
        String firstname = "James";
        Gender gender = Gender.M;
        LocalDate birthdate = LocalDate.of(1950, 1, 6);
        String email = "james.bond@im6.org";
        String phoneNumber = "+33700700700";
        var trainee = Trainee.builder()
                .lastname(lastname)
                .firstname(firstname)
                .gender(gender)
                .birthdate(birthdate)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();

        //then and verify
        assertThrows(PersistenceException.class, () -> {

            entityManager.persist(trainee); //Générate an insert
            // force synchro between hibernate and DB
            entityManager.flush();
            //System.out.println(trainee);
        } );
    }

    @ParameterizedTest
    @CsvSource({
            "Bond,James,M,1950-01-12,james.bond@im6.org,+33700700700",
            "Bond,Jane,F,1950-12-01,jane.bond@im6.org,+33700700700",
            "Neymar,Jean,,1951-01-12,jean.neymar@marre.org,+33701700701",
            "Spectre,Le,X,1949-01-12,le.spectre@badguy.org,"
    })
    void mapping_OK_allRequiredFields_CSV(
            String lastname,
            String firstname,
            Gender gender,
            LocalDate birthdate,
            String email,
            String phoneNumber
    ){
        // given
        var trainee = Trainee.builder()
                .lastname(lastname)
                .firstname(firstname)
                .gender(gender)
                .birthdate(birthdate)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
        // then
        entityManager.persist(trainee); // INSERT
        // force synchro between Hibernate and DB
        entityManager.flush();

        // verify: ID has been generated
        Integer idGenerated = trainee.getId();
        assertNotNull(idGenerated);

        // verify: data has been inserted in database
        // clear Hibernate cache
        entityManager.clear();
        var traineeRead = entityManager.find(Trainee.class, idGenerated); // SELECT
        assertNotNull(traineeRead);
        // following assertions are not necessary
        assertAll(
                () -> assertEquals(idGenerated, traineeRead.getId(), "trainee id"),
                () -> assertEquals(lastname, traineeRead.getLastname(), "trainee lastname"),
                () -> assertEquals(firstname, traineeRead.getFirstname(), "trainee firstname"),
                () -> assertEquals(gender, traineeRead.getGender(), "trainee gender"),
                () -> assertEquals(email, traineeRead.getEmail(), "trainee email"),
                () -> assertEquals(phoneNumber, traineeRead.getPhoneNumber(), "trainee phone number"),
                () -> assertEquals(birthdate, traineeRead.getBirthdate(), "trainee birthdate")
        );
    }

}