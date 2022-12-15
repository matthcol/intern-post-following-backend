package canard.intern.post.following.backend.repository;

import canard.intern.post.following.backend.entity.Poe;
import canard.intern.post.following.backend.enums.PoeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PoeRepository extends JpaRepository<Poe, Integer> {
    List<Poe> findByPoeType(PoeType poeType);

    List<Poe> findByTitleIgnoreCase(String title);

    List<Poe> findByBeginDateBetween(LocalDate date1, LocalDate date2);

    // List<Poe> findByBeginDateYear(int year);
}
