package canard.intern.post.following.backend.dto;

import canard.intern.post.following.backend.enums.PoeType;
import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class PoeTypeCountPoeDto {
    private PoeType poeType;
    private long countPoe;
}
