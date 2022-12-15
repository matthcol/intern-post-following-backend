package canard.intern.post.following.backend.repository;

import canard.intern.post.following.backend.dto.IPoeTypeCountPoeDto;
import canard.intern.post.following.backend.dto.PoeTypeCountPoeDto;
import canard.intern.post.following.backend.entity.Poe;
import canard.intern.post.following.backend.enums.PoeType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Tuple;
import java.time.LocalDate;
import java.util.List;

public interface PoeRepository extends JpaRepository<Poe, Integer> {
    List<Poe> findByPoeType(PoeType poeType);

    List<Poe> findByTitleIgnoreCaseOrderByBeginDate(String title);

    List<Poe> findByBeginDateBetween(LocalDate date1, LocalDate date2);

    List<Poe> findByBeginDateBetween(LocalDate date1, LocalDate date2, Sort sort);

    //@Query("SELECT p FROM Poe p WHERE EXTRACT(YEAR FROM p.beginDate) = :year")
    @Query("SELECT p FROM Poe p WHERE YEAR(p.beginDate) = :year ORDER BY p.beginDate")
    List<Poe> findByBeginDateInYear(int year);

    @Query("SELECT new canard.intern.post.following.backend.dto.PoeTypeCountPoeDto(p.poeType, COUNT(p))" +
            " FROM Poe p GROUP BY p.poeType")
    List<PoeTypeCountPoeDto> countPoeByPoeType();

    @Query("SELECT p.poeType as poeType, COUNT(p) as countPoe" +
            " FROM Poe p GROUP BY p.poeType")
    List<IPoeTypeCountPoeDto> countPoeByPoeType2();

//    SELECT
//    p.*,
//    count(t.id) as trainee_count
//    From poes p
//    LEFT OUTER JOIN trainees t ON  t.poe_id= p.id
//    GROUP BY p.id
//    order by trainee_count;

    @Query("SELECT p.id as id, p.title as title, " +
            "   p.beginDate as beginDate, p.poeType as poeType, " +
            "   COUNT(t.id) as traineeCount " +
            "FROM Trainee t RIGHT OUTER JOIN t.poe p " +
            "GROUP BY p " +
            "ORDER BY traineeCount")
    List<Tuple> countTraineesByPoe();



}
