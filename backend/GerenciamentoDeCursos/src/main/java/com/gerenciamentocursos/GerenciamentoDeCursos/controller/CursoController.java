package com.gerenciamentocursos.GerenciamentoDeCursos.controller;

import com.gerenciamentocursos.GerenciamentoDeCursos.dto.request.CursoRequestDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.dto.response.CursoResponseDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.service.CursoService;
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
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
@Tag(name = "Cursos", description = "Endpoints para o gerenciamento de cursos")
public class CursoController {
    private final CursoService cursoService;

    @Operation(summary = "Cadastrar curso", description = "Cria um novo curso na base de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Curso cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados do curso inválidos")
    })
    @PostMapping
    public ResponseEntity<CursoResponseDTO> criarCurso(
            @Valid @RequestBody CursoRequestDTO dto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cursoService.criarCurso(dto));
    }

    @Operation(summary = "Listar cursos", description = "Retorna a lista de todos os cursos cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista obtida com sucesso")
    @GetMapping
    public ResponseEntity<List<CursoResponseDTO>>  listarCursos(){
        return ResponseEntity.ok(
                cursoService.listarCursos()
        );
    }


    @Operation(summary = "Buscar curso por ID", description = "Busca os detalhes de um curso específico pelo UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso encontrado"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> buscarPorId(
            @PathVariable UUID id){
        return ResponseEntity.ok(
                cursoService.buscarPorId(id)
        );
    }

    @Operation(summary = "Atualizar curso", description = "Altera as informações de um curso pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados informados inválidos"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> atualizarCurso(
            @PathVariable UUID id,
            @Valid @RequestBody CursoRequestDTO dto){
        return ResponseEntity.ok(
                cursoService.atualizarCurso(id, dto));
    }

    @Operation(summary = "Excluir curso", description = "Remove um curso da aplicação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Curso excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCurso(
            @PathVariable UUID id){
        cursoService.excluirCurso(id);
        return ResponseEntity.noContent().build();
    }
}
