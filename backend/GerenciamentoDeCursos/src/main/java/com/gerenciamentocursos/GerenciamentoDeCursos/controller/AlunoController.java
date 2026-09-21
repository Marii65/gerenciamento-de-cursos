package com.gerenciamentocursos.GerenciamentoDeCursos.controller;

import com.gerenciamentocursos.GerenciamentoDeCursos.dto.request.AlunoRequestDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.dto.response.AlunoResponseDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.service.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/alunos")
@RequiredArgsConstructor
@Tag(name = "Alunos", description = "Endpoints para o gerenciamento de alunos")
public class AlunoController {
    private final AlunoService alunoService;

    @Operation(summary = "Cadastrar aluno", description = "Cadastra um novo aluno no sistema com os dados informados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Aluno cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou incorretos")
    })
    @PostMapping
    public ResponseEntity<AlunoResponseDTO> criarAluno(
            @Valid @RequestBody AlunoRequestDTO dto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(alunoService.criarAluno(dto));
    }

    @Operation(summary = "Listar alunos", description = "Retorna uma lista com todos os alunos cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista de alunos retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<AlunoResponseDTO>> listarAlunos(){
        return ResponseEntity.ok(
                alunoService.listarAlunos()
        );
    }

    @Operation(summary = "Buscar aluno por ID", description = "Recupera os detalhes de um aluno específico pelo seu UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aluno encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> buscarPorId(
            @PathVariable UUID id){
        return ResponseEntity.ok(
                alunoService.buscarPorId(id)
        );
    }

    @Operation(summary = "Atualizar aluno", description = "Atualiza os dados de um aluno existente a partir do seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aluno atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> atualizarAluno(
            @PathVariable UUID id,
            @Valid @RequestBody AlunoRequestDTO dto){

        return ResponseEntity.ok(
                alunoService.atualizarAluno(id,dto));
    }

    @Operation(summary = "Excluir aluno", description = "Remove um aluno do banco de dados pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Aluno removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirAluno(
            @PathVariable UUID id){
        alunoService.excluirAluno(id);
        return ResponseEntity.noContent().build();
    }
}
