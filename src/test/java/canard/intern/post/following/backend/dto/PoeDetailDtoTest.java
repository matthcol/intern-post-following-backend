package canard.intern.post.following.backend.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PoeDetailDtoTest {

    @Test
    void builder(){
        var poeDetailDto = PoeDetailDto.builder()
                .id(1)
                .title("Java Fullstack")
                .trainees(List.of(
                        TraineeDto.builder()
                                .id(33)
                                .lastname("Doe")
                                .build(),
                        TraineeDto.builder()
                                .id(54)
                                .lastname("Bond")
                                .build()
                ))
                .build();
        System.out.println(poeDetailDto);
        assertSame(PoeDetailDto.class, poeDetailDto.getClass());
        // TODO: assert all has been set
    }

}