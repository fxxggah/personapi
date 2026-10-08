package one.digitalinnovation.personapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import one.digitalinnovation.personapi.dto.MessageResponseDTO;
import one.digitalinnovation.personapi.dto.request.PersonDTO;
import one.digitalinnovation.personapi.exception.PersonNotFoundException;
import one.digitalinnovation.personapi.service.PersonService;
import one.digitalinnovation.personapi.utils.PersonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PersonControllerTest {

    private static final String PEOPLE_API_URL_PATH = "/api/v1/people";

    private MockMvc mockMvc;

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(personController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void testWhenPOSTIsCalledThenAPersonShouldBeCreated() throws Exception {
        PersonDTO expectedPersonDTO = PersonUtils.createFakeDTO();
        MessageResponseDTO expectedResponseMessage = MessageResponseDTO.builder()
                .message("Person created with ID: 1")
                .build();

        when(personService.createPerson(expectedPersonDTO)).thenReturn(expectedResponseMessage);

        mockMvc.perform(post(PEOPLE_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(expectedPersonDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is(expectedResponseMessage.getMessage())));
    }

    @Test
    void testWhenGETWithValidIsCalledThenAPersonShouldBeReturned() throws Exception {
        Long expectedValidId = 1L;
        PersonDTO expectedPersonDTO = PersonUtils.createFakeDTO();

        when(personService.findById(expectedValidId)).thenReturn(expectedPersonDTO);

        mockMvc.perform(get(PEOPLE_API_URL_PATH + "/" + expectedValidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(expectedPersonDTO.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(expectedPersonDTO.getLastName())))
                .andExpect(jsonPath("$.cpf", is(expectedPersonDTO.getCpf())));
    }

    @Test
    void testWhenGETWithInvalidIsCalledThenAnErrorMessageShouldBeReturned() throws Exception {
        Long expectedInvalidId = 2L;

        when(personService.findById(expectedInvalidId)).thenThrow(PersonNotFoundException.class);

        mockMvc.perform(get(PEOPLE_API_URL_PATH + "/" + expectedInvalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testWhenGETListIsCalledThenAListOfPeopleShouldBeReturned() throws Exception {
        PersonDTO expectedPersonDTO = PersonUtils.createFakeDTO();

        when(personService.findAll()).thenReturn(Collections.singletonList(expectedPersonDTO));

        mockMvc.perform(get(PEOPLE_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is(expectedPersonDTO.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(expectedPersonDTO.getLastName())))
                .andExpect(jsonPath("$[0].cpf", is(expectedPersonDTO.getCpf())));
    }

    @Test
    void testWhenPUTIsCalledThenAPersonShouldBeUpdated() throws Exception {
        Long expectedValidId = 1L;
        PersonDTO expectedPersonDTO = PersonUtils.createFakeDTO();
        MessageResponseDTO expectedResponseMessage = MessageResponseDTO.builder()
                .message("Person updated with ID: 1")
                .build();

        when(personService.updateById(expectedValidId, expectedPersonDTO)).thenReturn(expectedResponseMessage);

        mockMvc.perform(put(PEOPLE_API_URL_PATH + "/" + expectedValidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(expectedPersonDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(expectedResponseMessage.getMessage())));
    }

    @Test
    void testWhenPUTWithInvalidIdIsCalledThenAnErrorMessageShouldBeReturned() throws Exception {
        Long expectedInvalidId = 2L;
        PersonDTO expectedPersonDTO = PersonUtils.createFakeDTO();

        when(personService.updateById(expectedInvalidId, expectedPersonDTO)).thenThrow(PersonNotFoundException.class);

        mockMvc.perform(put(PEOPLE_API_URL_PATH + "/" + expectedInvalidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(expectedPersonDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testWhenDELETEIsCalledThenAPersonShouldBeDeleted() throws Exception {
        Long expectedValidId = 1L;

        doNothing().when(personService).deleteById(expectedValidId);

        mockMvc.perform(delete(PEOPLE_API_URL_PATH + "/" + expectedValidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testWhenDELETEWithInvalidIdIsCalledThenAnErrorMessageShouldBeReturned() throws Exception {
        Long expectedInvalidId = 2L;

        doThrow(PersonNotFoundException.class).when(personService).deleteById(expectedInvalidId);

        mockMvc.perform(delete(PEOPLE_API_URL_PATH + "/" + expectedInvalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /**
     * Utilitário para converter objetos Java para String em formato JSON.
     * Registra o JavaTimeModule para processar a data corretamente com LocalDate.
     */
    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}