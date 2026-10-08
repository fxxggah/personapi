package one.digitalinnovation.personapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import one.digitalinnovation.personapi.dto.MessageResponseDTO;
import one.digitalinnovation.personapi.dto.request.PersonDTO;
import one.digitalinnovation.personapi.exception.PersonNotFoundException;
import one.digitalinnovation.personapi.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/people")
@Tag(name = "Gerenciamento de Pessoas", description = "API responsável pelo cadastro, consulta, atualização e remoção de pessoas.")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar uma nova pessoa", description = "Cadastra uma nova pessoa no sistema e retorna a mensagem de sucesso com o ID gerado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pessoa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos na requisição")
    })
    public MessageResponseDTO createPerson(@RequestBody PersonDTO personDTO) {
        return personService.createPerson(personDTO);
    }

    @GetMapping
    @Operation(summary = "Listar todas as pessoas", description = "Retorna uma lista com todas as pessoas cadastradas no sistema.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public List<PersonDTO> findAllPerson() {
        return personService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pessoa por ID", description = "Retorna os detalhes de uma pessoa específica com base no ID informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pessoa encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada")
    })
    public PersonDTO findById(
            @Parameter(description = "ID da pessoa a ser buscada", example = "1")
            @PathVariable Long id) throws PersonNotFoundException {
        return personService.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pessoa por ID", description = "Atualiza os dados de uma pessoa existente com base no ID informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pessoa atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada para atualização"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public MessageResponseDTO updateById(
            @Parameter(description = "ID da pessoa a ser atualizada", example = "1")
            @PathVariable Long id,
            @RequestBody PersonDTO personDTO) throws PersonNotFoundException {
        return personService.updateById(id, personDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar pessoa por ID", description = "Remove do sistema uma pessoa cadastrada com base no ID informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pessoa deletada com sucesso (sem conteúdo de retorno)"),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada")
    })
    public void deleteById(
            @Parameter(description = "ID da pessoa a ser deletada", example = "1")
            @PathVariable Long id) throws PersonNotFoundException {
        personService.deleteById(id);
    }

}