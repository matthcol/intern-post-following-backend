package canard.intern.post.following.backend.dto;

import canard.intern.post.following.backend.enums.Gender;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import canard.intern.post.following.backend.validator.DateLessThan;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TraineeDto {
    private Integer id;

    @NotBlank
    private String lastname;

    @NotBlank
    private String firstname;

    private Gender gender;

    @NotNull
    @DateLessThan
    private LocalDate birthdate;

    @Pattern(regexp = "^\\+(?:[0-9]●?){6,14}[0-9]$")
    private String phoneNumber;

    @NotNull
    @Email // or @Pattern
    private String email;
}
