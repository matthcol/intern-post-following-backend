package canard.intern.post.following.backend.dto;

import canard.intern.post.following.backend.enums.PoeType;
import lombok.*;

import java.time.LocalDate;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class PoeDto {
    private Integer id;
    private String title;
    private LocalDate beginDate;
    private LocalDate endDate;
    private PoeType poeType;
}
