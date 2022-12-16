package canard.intern.post.following.backend.dto;

import canard.intern.post.following.backend.enums.PoeType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@ToString
@SuperBuilder
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
