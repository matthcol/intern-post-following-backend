package canard.intern.post.following.backend.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class TraineeDetailDto extends  TraineeDto {
    private PoeDto poe;
}
