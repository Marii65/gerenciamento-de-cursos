package com.gerenciamentocursos.GerenciamentoDeCursos.controller;

import com.gerenciamentocursos.GerenciamentoDeCursos.dto.request.MatriculaRequestDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.dto.request.MatriculaStatusRequestDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.dto.response.AlunoResponseDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.dto.response.CursoResponseDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.dto.response.MatriculaResponseDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.service.MatriculaService;
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
@RequestMapping("/api/matriculas")
@RequiredArgsConstructor
@Tag(name = "Matrículas", description = "Endpoints para realização, consulta e atualização de matrículas")
public class MatriculaController {
    private final MatriculaService matriculaService;

    @Operation(summary = "Realizar matrícula", description = "Vincular um aluno a um curso. Impede mais de uma matrícula ativa do mesmo aluno no mesmo curso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Matrícula efetuada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Aluno ou Curso não encontrado"),
            @ApiResponse(responseCode = "409", description = "Aluno já matriculado ativamente neste curso")
    })
    @PostMapping
    public ResponseEntity<MatriculaResponseDTO> criarMatricula(
            @Valid @RequestBody MatriculaRequestDTO dto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(matriculaService.criarMatricula(dto));
    }

    @Operation(summary = "Listar todas as matrículas", description = "Retorna o histórico completo de matrículas.")
    @ApiResponse(responseCode = "200", description = "Lista obtida com sucesso")
    @GetMapping
    public ResponseEntity<List<MatriculaResponseDTO>>  listarMatriculas(){
        return ResponseEntity.ok(
                matriculaService.listarMatriculas()
        );
    }

    @Operation(summary = "Atualizar status da matrícula", description = "Permite alterar o status da matrícula para CANCELADA ou CONCLUIDA.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status alterado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Transição de status inválida"),
            @ApiResponse(responseCode = "404", description = "Matrícula não encontrada")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<MatriculaResponseDTO> atualizarStatus(
            @PathVariable UUID id,
            @Valid @RequestBody MatriculaStatusRequestDTO dto) {

        return ResponseEntity.ok(
                matriculaService.atualizarStatus(id, dto)
        );
    }

    @Operation(summary = "Listar alunos por curso", description = "Retorna todos os alunos matriculados em um curso específico.")
    @ApiResponse(responseCode = "200", description = "Lista de alunos retornada com sucesso")
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<AlunoResponseDTO>> buscarAlunosPorCurso(
            @PathVariable UUID cursoId){
        return ResponseEntity.ok(
                matriculaService.buscarAlunosPorCurso(cursoId)
        );
    }

    @Operation(summary = "Listar cursos por aluno", description = "Retorna todos os cursos nos quais um aluno está matriculado.")
    @ApiResponse(responseCode = "200", description = "Lista de cursos retornada com sucesso")
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<CursoResponseDTO>> buscarCursosPorAluno(
            @PathVariable UUID alunoId){
        return ResponseEntity.ok(
                matriculaService.buscarCursosPorAluno(alunoId)
        );
    }


}
