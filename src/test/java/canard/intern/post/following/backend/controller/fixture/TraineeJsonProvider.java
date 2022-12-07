package canard.intern.post.following.backend.controller.fixture;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.LocalDate;
import java.util.stream.Stream;

public class TraineeJsonProvider {

    private static ObjectNode traineeJsonObjectRequiredFields(){
        var objectMapper = new ObjectMapper();
        var trainee = objectMapper.createObjectNode();
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

    public static Stream<String> traineeJsonMissingNonRequiredField(){
        return Stream.of(
                traineeJsonObjectRequiredFields().toPrettyString(),
                traineeJsonObjectRequiredFields()
                        .put("gender", "M")
                        .toPrettyString(),
                traineeJsonObjectRequiredFields()
                        .put("phoneNumber", "+33707070707")
                        .toPrettyString()
        );
    }

    public static String traineeJsonInvalidBirthdate() {
        var wrongBirthdate = LocalDate.now()
                .minusYears(18L)
                .plusDays(1L);
        var objectMapper = new ObjectMapper();
        var trainee = objectMapper.createObjectNode();
        return trainee.put("lastname", "Solo")
                .put("firstname", "Han")
                .put("birthdate", wrongBirthdate.toString())
                .put("email", "han.solo@faucon-millenium.fr")
                .toPrettyString();
    }
}
