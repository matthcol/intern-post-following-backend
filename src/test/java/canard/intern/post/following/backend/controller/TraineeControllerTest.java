package canard.intern.post.following.backend.controller;

import canard.intern.post.following.backend.controller.fixture.TraineeJsonProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TraineeController.class)
class TraineeControllerTest {

    final static String BASE_URL = "/api/trainees";
    final static String URL_TEMPLATE_ID = BASE_URL + "/{id}";

    @Autowired
    MockMvc mockMvc;

    @Test
    void getAll() {
        fail("Test not defined yet");
    }

    @Test
    void getById() throws Exception {
        int id = 2;
        mockMvc.perform(get(URL_TEMPLATE_ID,  id)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //.andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void getById_KO_idNotFound(){
        // TODO
        fail("Test not defined yet");
    }

    @Test
    void getById_KO_xmlNotAcceptable() throws Exception {
        int id = 2;
        mockMvc.perform(get(URL_TEMPLATE_ID, id)
                        .accept(MediaType.APPLICATION_XML)
                )
                .andDo(print())
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void create_OK_allFieldsValid() throws Exception {
        var traineeJson = TraineeJsonProvider.traineeJsonAllFieldsValid();
        mockMvc.perform(post(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(traineeJson)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists());
        // NB: check all fields in response are equals to request fields
    }

    // NB: packagenames.Classname#methodename
    @ParameterizedTest
    @MethodSource("canard.intern.post.following.backend.controller.fixture.TraineeJsonProvider#traineeJsonMissingNonRequiredField")
    void create_OK_missingNonRequiredField(String traineeJson) throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(traineeJson)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void create_KO_missingRequiredField(){
        //  lastname, firstname, email, birthdate,
        fail("Test not defined yet");
    }

    // TODO: all invalids fields (email, birthdate 18 years, phoneNumber pattern)

    @Test
    void create_KO_invalidBirthdate() throws Exception {
        var traineeJson = TraineeJsonProvider.traineeJsonInvalidBirthdate();
        mockMvc.perform(post(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(traineeJson)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    void update_OK() {
        // id found +
        // - all fields valid + no id in body
        // - all fields valid + id in body equals to id in path
        fail("Test not defined yet");
    }

    @Test
    void update_KO() {
        // - id not found
        // - id found +
        //      . invalid field(s)
        //      . all fields valid + id in body different from id in path
        fail("Test not defined yet");
    }

    @Test
    void delete_OK() {
        // id found
        fail("Test not defined yet");
    }

    @Test
    void delete_KO() {
        // id not found
        fail("Test not defined yet");
    }
}