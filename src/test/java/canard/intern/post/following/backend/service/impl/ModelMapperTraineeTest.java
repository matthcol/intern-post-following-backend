package canard.intern.post.following.backend.service.impl;

import canard.intern.post.following.backend.dto.TraineeDto;
import canard.intern.post.following.backend.entity.Trainee;
import canard.intern.post.following.backend.enums.Gender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ModelMapperTraineeTest {

    static ModelMapper modelMapper;

    @BeforeAll
    static void initMapper(){
        modelMapper = new ModelMapper();
    }

    @Test
    void mapTraineeEntityToDto(){
        int id = 12345;
        String lastname = "Bond";
        String firstname = "James";
        Gender gender = Gender.M;
        LocalDate birthdate = LocalDate.of(1950,1,6);
        String email = "james.bond@im6.org";
        String phoneNumber = "+33700700700";
        var traineeEntity = Trainee.builder()
                .id(id)
                .lastname(lastname)
                .firstname(firstname)
                .gender(gender)
                .birthdate(birthdate)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
        var traineeDto = modelMapper.map(traineeEntity, TraineeDto.class);
        assertNotNull(traineeDto);
        assertSame(TraineeDto.class, traineeDto.getClass());
        assertAll(
                () -> assertEquals(id, traineeDto.getId(), "trainee dto id"),
                () -> assertEquals(lastname, traineeDto.getLastname(), "trainee dto lastname"),
                () -> assertEquals(firstname, traineeDto.getFirstname(), "trainee dto firstname"),
                () -> assertEquals(birthdate, traineeDto.getBirthdate(), "trainee dto birthdate"),
                () -> assertEquals(gender, traineeDto.getGender(), "trainee dto gender"),
                () -> assertEquals(email, traineeDto.getEmail(), "trainee dto email"),
                () -> assertEquals(phoneNumber, traineeDto.getPhoneNumber(), "trainee dto phone number")
        );
    }

}
