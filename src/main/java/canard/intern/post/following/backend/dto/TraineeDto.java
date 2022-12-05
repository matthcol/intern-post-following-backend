package canard.intern.post.following.backend.dto;

import canard.intern.post.following.backend.enums.Gender;

import java.time.LocalDate;

public class TraineeDto {
    private int id;
    private String lastname;
    private String firstname;
    private Gender gender;
    private LocalDate birthdate;
    private String phoneNumber;
    private String email;
}
