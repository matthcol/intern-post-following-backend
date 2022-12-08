package canard.intern.post.following.backend.controller;

import canard.intern.post.following.backend.controller.fixture.TraineeJsonProvider;
import canard.intern.post.following.backend.dto.TraineeDto;
import canard.intern.post.following.backend.enums.Gender;
import canard.intern.post.following.backend.error.UpdateException;
import canard.intern.post.following.backend.service.TraineeService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.NestedServletException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TraineeController.class)
class TraineeControllerTest {

    final static String BASE_URL = "/api/trainees";
    final static String URL_TEMPLATE_ID = BASE_URL + "/{id}";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TraineeService traineeService;

    @Test
    void getAll() throws Exception {
        var traineesDtoResponse = List.of(
                TraineeDto.builder()
                        .id(1)
                        .lastname("Bond")
                        .firstname("James")
                        .gender(Gender.M)
                        .birthdate(LocalDate.of(1945, 6, 16))
                        .build(),
                TraineeDto.builder()
                        .id(2)
                        .lastname("Aubert")
                        .firstname("Jean-Luc")
                        .build(),
                TraineeDto.builder()
                        .id(3)
                        .lastname("Neymar")
                        .firstname("Jean")
                        .gender(Gender.X)
                        .birthdate(LocalDate.of(1999,1,14))
                        .build(),
                TraineeDto.builder()
                        .id(3)
                        .lastname("Moneypenny")
                        .firstname("Miss")
                        .gender(Gender.F)
                        .build());
        given(traineeService.getAll())
                .willReturn(traineesDtoResponse);

        mockMvc.perform(get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(traineesDtoResponse.size())));
    }

    @Test
    void getByLastname_OK_FoundSeveral() throws Exception {
        String lastnamePartialArg = "Bond";

        var traineesDtoResponse = Set.of(
                TraineeDto.builder()
                        .id(1)
                        .lastname("Bond")
                        .firstname("James")
                        .gender(Gender.M)
                        .birthdate(LocalDate.of(1945, 6, 16))
                        .email("james.bond@007.org")
                        .build(),
                TraineeDto.builder()
                        .id(2)
                        .lastname("Bond")
                        .firstname("Jane")
                        .gender(Gender.F)
                        .birthdate(LocalDate.of(1944, 7, 15))
                        .email("jane.bond@007.org")
                        .build(),
                TraineeDto.builder()
                        .id(3)
                        .lastname("Notbond")
                        .firstname("Jean")
                        .gender(Gender.X)
                        .birthdate(LocalDate.of(1999,1,14))
                        .email("jean.notbond@007.org")
                        .build(),
                TraineeDto.builder()
                        .id(4)
                        .lastname("Bonding")
                        .firstname("Jane")
                        .gender(Gender.F)
                        .birthdate(LocalDate.of(1969,1,14))
                        .email("jean.bonding@007.org")
                        .build(),
                TraineeDto.builder()
                        .id(4)
                        .lastname("Notbonding")
                        .firstname("Jane")
                        .gender(Gender.F)
                        .birthdate(LocalDate.of(1969,1,14))
                        .email("jane.notbonding@007.org")
                        .build());
        given(traineeService.getByLastnameContaining(eq(lastnamePartialArg)))
                .willReturn(traineesDtoResponse);

        mockMvc.perform(get(BASE_URL + "/search/byLastname")
                        .queryParam("ln", lastnamePartialArg)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(traineesDtoResponse.size())))
                .andExpect(
                        jsonPath("$[*].lastname",
                                Matchers.everyItem(
                                        Matchers.containsStringIgnoringCase(lastnamePartialArg)
                                )
                        ));
    }

    @Test
    void getByLastname_OK_FoundNone() throws Exception {
        String lastnamePartialArg = "Bond";

        Set<TraineeDto> traineesDtoResponse = Set.of();
        given(traineeService.getByLastnameContaining(eq(lastnamePartialArg)))
                .willReturn(traineesDtoResponse);

        mockMvc.perform(get(BASE_URL + "/search/byLastname")
                        .queryParam("ln", lastnamePartialArg)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getById_OK_idFound() throws Exception {
        int id = 2;

        // prepare mock response of trainee service
        var traineeDto = TraineeDto.builder()
                .id(id)
                .lastname("Bond")
                .firstname("James")
                .birthdate(LocalDate.of(1950,6, 12))
                .email("james.bond@007.org")
                .build();
        given(traineeService.getById(id))
                        .willReturn(Optional.of(traineeDto));

        // call controller with mock http client
        mockMvc.perform(get(URL_TEMPLATE_ID,  id)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //.andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(id));

        // verify mock service has been called by controller
        then(traineeService)
                .should()
                .getById(id);
    }

    @Test
    void getById_KO_idNotFound() throws Exception {
        int id = 2;

        // prepare mock response of trainee service
        given(traineeService.getById(id))
                .willReturn(Optional.empty());

        // call controller with mock http client
        mockMvc.perform(get(URL_TEMPLATE_ID,  id)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());

        // verify mock service has been called by controller
        then(traineeService)
                .should()
                .getById(id);
    }

    @Test
    void getById_KO_xmlNotAcceptable() throws Exception {
        int id = 2;
        var traineeDto = TraineeDto.builder()
                .id(id)
                .lastname("Bond")
                .firstname("James")
                .birthdate(LocalDate.of(1950,6, 12))
                .email("james.bond@007.org")
                .build();
        given(traineeService.getById(id))
                .willReturn(Optional.of(traineeDto));

        mockMvc.perform(get(URL_TEMPLATE_ID, id)
                        .accept(MediaType.APPLICATION_XML)
                )
                .andDo(print())
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void create_OK_allFieldsValid() throws Exception {
        // prepare data
        int idGenerated = 12345;
        String lastname = "Bond";
        String firstname = "James";
        LocalDate birthdate = LocalDate.of(1950,6, 12);
        String email = "james.bond@007.org";
        String phoneNumber = "+33700700700";
        Gender gender = Gender.M;

        var traineeDtoResponse = TraineeDto.builder()
                .id(idGenerated)
                .lastname(lastname)
                .firstname(firstname)
                .birthdate(birthdate)
                .email(email)
                .phoneNumber(phoneNumber)
                .gender(gender)
                .build();

        String traineeJsonRequest = TraineeJsonProvider.traineeJsonWithoutId(traineeDtoResponse);

        given(traineeService.create(any())).willReturn(traineeDtoResponse);

        // call controller with mock http client
        mockMvc.perform(post(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(traineeJsonRequest)
                )
                .andDo(print())
                // verify response
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.lastname").value(lastname))
                .andExpect(jsonPath("$.firstname").value(firstname))
                .andExpect(jsonPath("$.birthdate").value(birthdate.toString()))
                .andExpect(jsonPath("$.phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.gender").value(gender.toString()));

        // verify mock has been called
        then(traineeService)
                .should()
                .create(any());
    }

    // NB: packagenames.Classname#methodename
    @ParameterizedTest
    @MethodSource("canard.intern.post.following.backend.controller.fixture.TraineeJsonProvider#traineeJsonMissingNonRequiredField")
    void create_OK_missingNonRequiredField(TraineeDto traineeDtoResponse, String traineeJson) throws Exception {

        given(traineeService.create(any()))
                .willReturn(traineeDtoResponse);

        mockMvc.perform(post(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(traineeJson)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists());

        then(traineeService)
                .should()
                .create(any());
    }


    @Test
    void create_KO_invalidBirthdate() throws Exception {
        var traineeJson = TraineeJsonProvider.traineeJsonInvalidBirthdateAgeLessThan18();
        mockMvc.perform(post(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(traineeJson)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    // NB: can replace two previous tests
    @ParameterizedTest
    @MethodSource("canard.intern.post.following.backend.controller.fixture.TraineeJsonProvider#traineeJsonInvalid")
    void create_KO_invalidPayload(String traineeJson) throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(traineeJson)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_KO_updateException() throws Exception {
        // prepare
        given(traineeService.create(any()))
                .willThrow(UpdateException.class);

        var traineeJson =  TraineeJsonProvider.traineeJsonAllFieldsValid();

        // call
        var ex = assertThrows(NestedServletException.class, () ->
            mockMvc.perform(post(BASE_URL)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(traineeJson)
                    )
        );

        assertEquals(UpdateException.class, ex.getCause().getClass());

        // check mock service has been called
        then(traineeService)
                .should()
                .create(any());
    }

    @Test
    void update_OK() throws Exception {
        // prepare data
        int id = 12345;
        String lastname = "Bond";
        String firstname = "James";
        LocalDate birthdate = LocalDate.of(1950,6, 12);
        String email = "james.bond@007.org";
        String phoneNumber = "+33700700700";
        Gender gender = Gender.M;

        var traineeDtoResponse = TraineeDto.builder()
                .id(id)
                .lastname(lastname)
                .firstname(firstname)
                .birthdate(birthdate)
                .email(email)
                .phoneNumber(phoneNumber)
                .gender(gender)
                .build();

        String traineeJsonRequest = TraineeJsonProvider.traineeJsonWithId(traineeDtoResponse);

        given(traineeService.update(eq(id), any()))
                .willReturn(Optional.of(traineeDtoResponse));

        // call controller
        mockMvc.perform(put(URL_TEMPLATE_ID, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(traineeJsonRequest)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.lastname").value(lastname))
                .andExpect(jsonPath("$.firstname").value(firstname))
                .andExpect(jsonPath("$.birthdate").value(birthdate.toString()))
                .andExpect(jsonPath("$.phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.gender").value(gender.toString()));

       then(traineeService)
                .should()
                .update(eq(id), any());
    }

    @ParameterizedTest
    @MethodSource("canard.intern.post.following.backend.controller.fixture.TraineeJsonProvider#traineeJsonInvalid")
    void update_KO_notValid(String traineeJson) throws Exception {
        // - id found +
        //      . invalid field(s)
        int id = 12345;
        mockMvc.perform(put(URL_TEMPLATE_ID, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(traineeJson)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    void update_KO_idMismatch() throws Exception {
        //  all fields valid + id in body different from id in path
        int id = 54321;
        String traineeJson = TraineeJsonProvider.traineeJsonValidWithId12345();
        mockMvc.perform(put(URL_TEMPLATE_ID, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(traineeJson)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_KO_idNotFound() throws Exception {
        // prepare data
        int id = 12345;

        String traineeJsonRequest = TraineeJsonProvider.traineeJsonValidWithId12345();

        given(traineeService.update(eq(id), any()))
                .willReturn(Optional.empty());

        // call controller
        mockMvc.perform(put(URL_TEMPLATE_ID, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(traineeJsonRequest)
                )
                .andDo(print())
                .andExpect(status().isNotFound());

        then(traineeService)
                .should()
                .update(eq(id), any());
    }

    // TODO: void update_KO_updateException

    @Test
    void delete_OK() throws Exception {
        int id = 12345;

        given(traineeService.delete(id))
                .willReturn(true);

        mockMvc.perform(delete(URL_TEMPLATE_ID, id))
                .andDo(print())
                .andExpect(status().isNoContent());

        then(traineeService)
                .should()
                .delete(id);
    }

    @Test
    void delete_KO() throws Exception {
        int id = 12345;

        given(traineeService.delete(id))
                .willReturn(false);

        mockMvc.perform(delete(URL_TEMPLATE_ID, id))
                .andDo(print())
                .andExpect(status().isNotFound());

        then(traineeService)
                .should()
                .delete(id);
    }

    // TODO: delete: id found but can't be deleted
}