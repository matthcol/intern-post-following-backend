package canard.intern.post.following.backend.jpa.learn;

import canard.intern.post.following.backend.entity.Trainee;
import canard.intern.post.following.backend.enums.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

// use H2 Database (in memory) by default
@DataJpaTest
class TraineeLearn {

    // ORM Hibernate
    @Autowired
    EntityManager entityManager;

    @Test
    void saveAndRead(){
        var trainee = Trainee.builder()
                .lastname("Bond")
                .firstname("James")
                .gender(Gender.M)
                .birthdate(LocalDate.of(1950,1,6))
                .email("james.bond@im6.org")
                .phoneNumber("+33700700700")
                .build();
        entityManager.persist(trainee); // INSERT
        // force synchro between Hibernate and DB
        entityManager.flush();
        System.out.println(trainee);
        int idGenerated = trainee.getId();

        // clear Hibernate cache
        entityManager.clear();
        var traineeRead = entityManager.find(Trainee.class, idGenerated); // SELECT
        System.out.println(traineeRead);
    }

}