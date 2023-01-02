package canard.intern.post.following.backend.repository;

import canard.intern.post.following.backend.entity.Poe;
import canard.intern.post.following.backend.entity.Trainee;
import canard.intern.post.following.backend.enums.Gender;
import canard.intern.post.following.backend.enums.PoeType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("testu")
class PoeRepositoryTest {

    // component under test
    @Autowired
    PoeRepository poeRepository;

    // useful wrapper of EntityManager only for test
    // - inject in db data hypothesis
    // - check results in db
    @Autowired
    TestEntityManager entityManager;

    @ParameterizedTest
    @CsvSource({
            "Java Fullstack,2022-10-24,2023-01-27,POEI",
            "Java Fullstack,2022-11-02,2023-02-03,POEC"
    })
    void save_OK_allRequiredFields_CSV(
            String title,
            LocalDate beginDate,
            LocalDate endDate,
            PoeType poeType
    ) {
        // given
        var poe = Poe.builder()
                .title(title)
                .beginDate(beginDate)
                .endDate(endDate)
                .poeType(poeType)
                .build();

        // traineeRepository.save(trainee);
        poeRepository.saveAndFlush(poe);

        // verify 1: Id has been generated
        assertNotNull(poe.getId());

        // (optional) verify 2: read data from db to check if data has been inserted
        int idPoe = poe.getId();
        entityManager.clear();
        var poeRead = entityManager.find(Poe.class, idPoe);
        assertNotNull(poeRead);
        assertAll(
                () -> assertEquals(idPoe, poeRead.getId(), "poe id"),
                () -> assertEquals(title, poeRead.getTitle(), "poe title"),
                () -> assertEquals(beginDate, poeRead.getBeginDate(), "poe begin date"),
                () -> assertEquals(endDate, poeRead.getEndDate(), "poe end date"),
                () -> assertEquals(poeType, poeRead.getPoeType(), "poe type")
        );

    }

    @ParameterizedTest(name="integrityErrorExpected={0}")
    @CsvSource({
            "title null,,2022-10-24,2023-01-27,POEI",
            "begin date null,Java Fullstack,,2023-01-27,POEI",
            "end date null,Java Fullstack,2022-10-24,,POEI",
            "poe type null,Java Fullstack,2022-10-24,2023-01-27,",
            "inconsistent dates,Java Fullstack,2023-01-27,2022-10-24,POEI"
    })
    void save_KO_dataIntegrityException_CSV(
            String integrityErrorExpected,
            String title,
            LocalDate beginDate,
            LocalDate endDate,
            PoeType poeType
    ) {
        // given
        var poe = Poe.builder()
                .title(title)
                .beginDate(beginDate)
                .endDate(endDate)
                .poeType(poeType)
                .build();

        // when/then
        assertThrows(DataIntegrityViolationException.class, () ->
                poeRepository.saveAndFlush(poe)
        );
    }

}