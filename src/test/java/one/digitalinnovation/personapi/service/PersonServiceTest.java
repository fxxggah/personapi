package one.digitalinnovation.personapi.service;

import one.digitalinnovation.personapi.dto.MessageResponseDTO;
import one.digitalinnovation.personapi.dto.request.PersonDTO;
import one.digitalinnovation.personapi.entity.Person;
import one.digitalinnovation.personapi.exception.PersonNotFoundException;
import one.digitalinnovation.personapi.mapper.PersonMapper;
import one.digitalinnovation.personapi.repository.PersonRepository;
import one.digitalinnovation.personapi.utils.PersonUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static one.digitalinnovation.personapi.utils.PersonUtils.createFakeEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonService personService;

    @Test
    void testGivenPersonDTOThenReturnSavedMessage() {
        PersonDTO personDTO = PersonUtils.createFakeDTO();
        Person expectedSavedPerson = createFakeEntity();

        Mockito.when(personMapper.toModel(personDTO)).thenReturn(expectedSavedPerson);
        Mockito.when(personRepository.save(any(Person.class))).thenReturn(expectedSavedPerson);

        MessageResponseDTO expectedSuccessMessage = createExpectedMessage("Person created with ID: ", expectedSavedPerson.getId());
        MessageResponseDTO successMessage = personService.createPerson(personDTO);

        assertEquals(expectedSuccessMessage, successMessage);
    }

    @Test
    void testGivenValidPersonIdThenReturnThisPerson() {
        PersonDTO expectedPersonDTO = PersonUtils.createFakeDTO();
        Person expectedSavedPerson = createFakeEntity();

        Mockito.when(personRepository.findById(expectedSavedPerson.getId())).thenReturn(Optional.of(expectedSavedPerson));
        Mockito.when(personMapper.toDTO(expectedSavedPerson)).thenReturn(expectedPersonDTO);

        PersonDTO personDTO = personService.findById(expectedSavedPerson.getId());

        assertEquals(expectedPersonDTO, personDTO);
    }

    @Test
    void testGivenInvalidPersonIdThenThrowException() {
        Long invalidPersonId = 2L;

        Mockito.when(personRepository.findById(invalidPersonId)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> personService.findById(invalidPersonId));
    }

    @Test
    void testGivenNoDataThenReturnAllPeopleRegistered() {
        List<Person> expectedRegisteredPeople = Collections.singletonList(createFakeEntity());
        PersonDTO personDTO = PersonUtils.createFakeDTO();

        Mockito.when(personRepository.findAll()).thenReturn(expectedRegisteredPeople);
        Mockito.when(personMapper.toDTO(any(Person.class))).thenReturn(personDTO);

        List<PersonDTO> expectedPeopleDTOList = personService.findAll();

        assertEquals(expectedRegisteredPeople.size(), expectedPeopleDTOList.size());
        assertEquals(expectedPeopleDTOList.get(0).getFirstName(), personDTO.getFirstName());
    }

    @Test
    void testGivenValidPersonIdAndUpdateInfoThenReturnSuccessMessage() {
        Long personId = 1L;
        PersonDTO updatePersonDTO = PersonUtils.createFakeDTO();
        Person expectedSavedPerson = createFakeEntity();

        Mockito.when(personRepository.findById(personId)).thenReturn(Optional.of(expectedSavedPerson));
        Mockito.when(personMapper.toModel(updatePersonDTO)).thenReturn(expectedSavedPerson);
        Mockito.when(personRepository.save(any(Person.class))).thenReturn(expectedSavedPerson);

        MessageResponseDTO expectedSuccessMessage = createExpectedMessage("Person updated with ID: ", personId);
        MessageResponseDTO successMessage = personService.updateById(personId, updatePersonDTO);

        assertEquals(expectedSuccessMessage, successMessage);
    }

    @Test
    void testGivenInvalidPersonIdAndUpdateInfoThenThrowException() {
        Long invalidPersonId = 2L;
        PersonDTO updatePersonDTO = PersonUtils.createFakeDTO();

        Mockito.when(personRepository.findById(invalidPersonId)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> personService.updateById(invalidPersonId, updatePersonDTO));
    }

    @Test
    void testGivenValidPersonIdThenReturnSuccessOnDelete() {
        Long personId = 1L;
        Person expectedDeletedPerson = createFakeEntity();

        Mockito.when(personRepository.findById(personId)).thenReturn(Optional.of(expectedDeletedPerson));
        Mockito.doNothing().when(personRepository).deleteById(personId);

        personService.deleteById(personId);

        verify(personRepository, times(1)).findById(personId);
        verify(personRepository, times(1)).deleteById(personId);
    }

    @Test
    void testGivenInvalidPersonIdThenThrowExceptionOnDelete() {
        Long invalidPersonId = 2L;

        Mockito.when(personRepository.findById(invalidPersonId)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> personService.deleteById(invalidPersonId));
    }

    private static MessageResponseDTO createExpectedMessage(String message, Long id) {
        return MessageResponseDTO.builder()
                .message(message + id)
                .build();
    }
}