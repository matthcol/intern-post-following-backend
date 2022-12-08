package canard.intern.post.following.backend.controller.fixture;

import canard.intern.post.following.backend.dto.TraineeDto;
import canard.intern.post.following.backend.enums.Gender;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.params.provider.Arguments;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Stream;

public class TraineeJsonProvider {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static ObjectNode traineeJsonObjectRequiredFields(){
        var trainee = OBJECT_MAPPER.createObjectNode();
        return trainee.put("lastname", "Solo")
                .put("firstname", "Han")
                .put("birthdate", "1950-11-05")
                .put("email", "han.solo@faucon-millenium.fr");
    }

    public static String traineeJsonAllFieldsValid(){
        return traineeJsonObjectRequiredFields()
                .put("gender", "M")
                .put("phoneNumber", "+33707070707")
                .toPrettyString();
    }

    public static Stream<Arguments> traineeJsonMissingNonRequiredField(){
        return Stream.of(
                traineeDtoAndJsonWithoutId(TraineeDto.builder()
                        .id(12345)
                        .lastname("Solo")
                        .firstname("Han")
                        .birthdate(LocalDate.of(1950,11,5))
                        .email("han.solo@faucon-millenium.fr")
                        .build()),
                traineeDtoAndJsonWithoutId(TraineeDto.builder()
                        .id(12345)
                        .lastname("Solo")
                        .firstname("Han")
                        .birthdate(LocalDate.of(1950,11,5))
                        .email("han.solo@faucon-millenium.fr")
                        .gender(Gender.M)
                        .build()),
                traineeDtoAndJsonWithoutId(TraineeDto.builder()
                        .id(12345)
                        .lastname("Solo")
                        .firstname("Han")
                        .birthdate(LocalDate.of(1950,11,5))
                        .email("han.solo@faucon-millenium.fr")
                        .phoneNumber("+33707070707")
                        .build())
        );
    }

    public static String traineeJsonValidWithId12345(){
        return traineeJsonObjectRequiredFields()
                .put("gender", "M")
                .put("phoneNumber", "+33707070707")
                .put("id", 12345)
                .toPrettyString();
    }

    // NB: summary of two previous providers
    public static Stream<String> traineeJsonValid() {
        return //Stream.concat(
                Stream.of(
                        traineeJsonAllFieldsValid(),
                        traineeJsonValidWithId12345()
                ); //,
                //traineeJsonMissingNonRequiredField()
        //);
    }

    public static String traineeJsonInvalidMissingRequiredLastname() {
        var trainee = OBJECT_MAPPER.createObjectNode();
        return trainee
                .put("firstname", "Han")
                .put("birthdate", "1950-11-05")
                .put("email", "han.solo@faucon-millenium.fr")
                .toPrettyString();
    }

    public static String traineeJsonInvalidMissingRequiredFirstname() {
        var trainee = OBJECT_MAPPER.createObjectNode();
        return trainee.put("lastname", "Solo")
                .put("birthdate", "1950-11-05")
                .put("email", "han.solo@faucon-millenium.fr")
                .toPrettyString();
    }

    public static String traineeJsonInvalidMissingRequiredBirthdate() {
        var trainee = OBJECT_MAPPER.createObjectNode();
        return trainee.put("lastname", "Solo")
                .put("firstname", "Han")
                .put("email", "han.solo@faucon-millenium.fr")
                .toPrettyString();
    }

    public static String traineeJsonInvalidBirthdateAgeLessThan18() {
        var wrongBirthdate = LocalDate.now()
                .minusYears(18L)
                .plusDays(1L);
        var trainee = OBJECT_MAPPER.createObjectNode();
        return trainee.put("lastname", "Solo")
                .put("firstname", "Han")
                .put("birthdate", wrongBirthdate.toString())
                .put("email", "han.solo@faucon-millenium.fr")
                .toPrettyString();
    }

    public static String traineeJsonInvalidMissingRequiredEmail() {
        var wrongBirthdate = LocalDate.now()
                .minusYears(18L)
                .plusDays(1L);
        var trainee = OBJECT_MAPPER.createObjectNode();
        return trainee.put("lastname", "Solo")
                .put("firstname", "Han")
                .put("birthdate", "1950-11-05")
                .toPrettyString();
    }

    public static String traineeJsonInvalidEmailWrongPattern() {
        var trainee = OBJECT_MAPPER.createObjectNode();
        return trainee.put("lastname", "Solo")
                .put("birthdate", "1950-11-05")
                .put("email", "han.solo#faucon-millenium.fr")
                .toPrettyString();
    }
    public static Stream<String> traineeJsonInvalid() {
        return Stream.of(
                traineeJsonInvalidMissingRequiredLastname(),
                traineeJsonInvalidMissingRequiredFirstname(),
                traineeJsonInvalidMissingRequiredBirthdate(),
                traineeJsonInvalidBirthdateAgeLessThan18(),
                traineeJsonInvalidMissingRequiredEmail(),
                traineeJsonInvalidEmailWrongPattern()
        );
    }

    private static ObjectNode traineeJsonObjectWithoutId(TraineeDto traineeDto) {
        var traineeJsonObject =   OBJECT_MAPPER.createObjectNode()
                .put("lastname", traineeDto.getLastname())
                .put("firstname", traineeDto.getFirstname())
                .put("birthdate", traineeDto.getBirthdate().toString())  // ISO format
                .put("email", traineeDto.getEmail())
                .put("phoneNumber", traineeDto.getPhoneNumber());
        if (Objects.nonNull(traineeDto.getGender())) {
            traineeJsonObject.put("gender", traineeDto.getGender().toString());  // enum as string
        }
        return traineeJsonObject;
    }

    public static String traineeJsonWithoutId(TraineeDto traineeDto) {
        return traineeJsonObjectWithoutId(traineeDto)
                .toPrettyString();
    }

    public static String traineeJsonWithId(TraineeDto traineeDto) {
        return traineeJsonObjectWithoutId(traineeDto)
                .put("id", traineeDto.getId())
                .toPrettyString();
    }

    private static Arguments traineeDtoAndJsonWithoutId(TraineeDto traineeDto) {
        return Arguments.of(traineeDto, traineeJsonWithoutId(traineeDto));
    }

}
