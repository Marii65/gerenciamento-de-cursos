package com.gerenciamentocursos.GerenciamentoDeCursos.service;

import com.gerenciamentocursos.GerenciamentoDeCursos.dto.request.MatriculaRequestDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.dto.request.MatriculaStatusRequestDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.dto.response.AlunoResponseDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.dto.response.CursoResponseDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.dto.response.MatriculaResponseDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.entity.Aluno;
import com.gerenciamentocursos.GerenciamentoDeCursos.entity.Curso;
import com.gerenciamentocursos.GerenciamentoDeCursos.entity.Matricula;
import com.gerenciamentocursos.GerenciamentoDeCursos.entity.Status;
import com.gerenciamentocursos.GerenciamentoDeCursos.exception.AlunoNotFoundException;
import com.gerenciamentocursos.GerenciamentoDeCursos.exception.CursoNotFoundException;
import com.gerenciamentocursos.GerenciamentoDeCursos.exception.MatriculaConflictException;
import com.gerenciamentocursos.GerenciamentoDeCursos.exception.MatriculaNotFoundException;
import com.gerenciamentocursos.GerenciamentoDeCursos.repository.AlunoRepository;
import com.gerenciamentocursos.GerenciamentoDeCursos.repository.CursoRepository;
import com.gerenciamentocursos.GerenciamentoDeCursos.repository.MatriculaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatriculaServiceTest {

    @Mock
    private MatriculaRepository matriculaRepository;

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private MatriculaService matriculaService;

    @Test
    void deveCriarMatricula() {

        UUID alunoId = UUID.randomUUID();
        UUID cursoId = UUID.randomUUID();

        Aluno aluno = new Aluno();
        aluno.setId(alunoId);
        aluno.setNome("Maria");
        aluno.setEmail("maria@email.com");
        aluno.setDataNascimento(
                LocalDate.of(2007, 7, 4)
        );

        Curso curso = new Curso();
        curso.setId(cursoId);
        curso.setNome("Java");
        curso.setDescricao("Curso de Java");
        curso.setCargaHoraria(80);

        Matricula matricula = new Matricula();
        matricula.setId(UUID.randomUUID());
        matricula.setAluno(aluno);
        matricula.setCurso(curso);
        matricula.setDataMatricula(LocalDate.now());
        matricula.setStatus(Status.ATIVA);

        MatriculaRequestDTO dto =
                new MatriculaRequestDTO(alunoId, cursoId);

        when(alunoRepository.findById(alunoId))
                .thenReturn(Optional.of(aluno));

        when(cursoRepository.findById(cursoId))
                .thenReturn(Optional.of(curso));

        when(matriculaRepository
                .existsByAlunoIdAndCursoIdAndStatus(
                        alunoId,
                        cursoId,
                        Status.ATIVA))
                .thenReturn(false);

        when(matriculaRepository.save(any(Matricula.class)))
                .thenReturn(matricula);

        MatriculaResponseDTO resultado =
                matriculaService.criarMatricula(dto);

        assertEquals(alunoId, resultado.alunoId());
        assertEquals(cursoId, resultado.cursoId());
        assertEquals(Status.ATIVA, resultado.status());

        verify(alunoRepository).findById(alunoId);
        verify(cursoRepository).findById(cursoId);
        verify(matriculaRepository)
                .existsByAlunoIdAndCursoIdAndStatus(
                        alunoId,
                        cursoId,
                        Status.ATIVA
                );
        verify(matriculaRepository)
                .save(any(Matricula.class));
    }

    @Test
    void deveLancarExcecaoQuandoAlunoNaoExistir() {

        UUID alunoId = UUID.randomUUID();
        UUID cursoId = UUID.randomUUID();

        MatriculaRequestDTO dto =
                new MatriculaRequestDTO(alunoId, cursoId);

        when(alunoRepository.findById(alunoId))
                .thenReturn(Optional.empty());

        assertThrows(
                AlunoNotFoundException.class,
                () -> matriculaService.criarMatricula(dto)
        );

        verify(alunoRepository).findById(alunoId);
    }

    @Test
    void deveLancarExcecaoQuandoCursoNaoExistir() {

        UUID alunoId = UUID.randomUUID();
        UUID cursoId = UUID.randomUUID();

        Aluno aluno = new Aluno();
        aluno.setId(alunoId);

        MatriculaRequestDTO dto =
                new MatriculaRequestDTO(alunoId, cursoId);

        when(alunoRepository.findById(alunoId))
                .thenReturn(Optional.of(aluno));

        when(cursoRepository.findById(cursoId))
                .thenReturn(Optional.empty());

        assertThrows(
                CursoNotFoundException.class,
                () -> matriculaService.criarMatricula(dto)
        );

        verify(alunoRepository).findById(alunoId);
        verify(cursoRepository).findById(cursoId);
    }

    @Test
    void deveImpedirMatriculaAtivaDuplicada() {

        UUID alunoId = UUID.randomUUID();
        UUID cursoId = UUID.randomUUID();

        Aluno aluno = new Aluno();
        aluno.setId(alunoId);

        Curso curso = new Curso();
        curso.setId(cursoId);

        MatriculaRequestDTO dto =
                new MatriculaRequestDTO(alunoId, cursoId);

        when(alunoRepository.findById(alunoId))
                .thenReturn(Optional.of(aluno));

        when(cursoRepository.findById(cursoId))
                .thenReturn(Optional.of(curso));

        when(matriculaRepository
                .existsByAlunoIdAndCursoIdAndStatus(
                        alunoId,
                        cursoId,
                        Status.ATIVA))
                .thenReturn(true);

        assertThrows(
                MatriculaConflictException.class,
                () -> matriculaService.criarMatricula(dto)
        );

        verify(matriculaRepository)
                .existsByAlunoIdAndCursoIdAndStatus(
                        alunoId,
                        cursoId,
                        Status.ATIVA
                );
    }

    @Test
    void deveListarMatriculas() {

        UUID alunoId = UUID.randomUUID();
        UUID cursoId = UUID.randomUUID();

        Aluno aluno = new Aluno();
        aluno.setId(alunoId);
        aluno.setNome("Maria");

        Curso curso = new Curso();
        curso.setId(cursoId);
        curso.setNome("Java");

        Matricula matricula = new Matricula();
        matricula.setId(UUID.randomUUID());
        matricula.setAluno(aluno);
        matricula.setCurso(curso);
        matricula.setDataMatricula(LocalDate.now());
        matricula.setStatus(Status.ATIVA);

        when(matriculaRepository.findAll())
                .thenReturn(List.of(matricula));

        List<MatriculaResponseDTO> resultado =
                matriculaService.listarMatriculas();

        assertEquals(1, resultado.size());
        assertEquals("Maria", resultado.get(0).alunoNome());
        assertEquals("Java", resultado.get(0).cursoNome());
        assertEquals(Status.ATIVA, resultado.get(0).status());

        verify(matriculaRepository).findAll();
    }

    @Test
    void deveAtualizarStatusDaMatricula() {

        UUID id = UUID.randomUUID();

        Matricula matricula = new Matricula();
        matricula.setId(id);
        matricula.setStatus(Status.ATIVA);

        MatriculaStatusRequestDTO dto =
                new MatriculaStatusRequestDTO(Status.CONCLUIDA);

        when(matriculaRepository.findById(id))
                .thenReturn(Optional.of(matricula));

        when(matriculaRepository.save(any(Matricula.class)))
                .thenReturn(matricula);

        MatriculaResponseDTO resultado =
                matriculaService.atualizarStatus(id, dto);

        assertEquals(Status.CONCLUIDA, matricula.getStatus());

        verify(matriculaRepository).findById(id);
        verify(matriculaRepository).save(matricula);
    }

    @Test
    void deveLancarExcecaoAoAtualizarMatriculaInexistente() {

        UUID id = UUID.randomUUID();

        MatriculaStatusRequestDTO dto =
                new MatriculaStatusRequestDTO(Status.CONCLUIDA);

        when(matriculaRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                MatriculaNotFoundException.class,
                () -> matriculaService.atualizarStatus(id, dto)
        );

        verify(matriculaRepository).findById(id);
    }

    @Test
    void deveBuscarAlunosPorCurso() {

        UUID alunoId = UUID.randomUUID();
        UUID cursoId = UUID.randomUUID();

        Aluno aluno = new Aluno();
        aluno.setId(alunoId);
        aluno.setNome("Maria");
        aluno.setEmail("maria@email.com");

        Curso curso = new Curso();
        curso.setId(cursoId);
        curso.setNome("Java");

        Matricula matricula = new Matricula();
        matricula.setId(UUID.randomUUID());
        matricula.setAluno(aluno);
        matricula.setCurso(curso);
        matricula.setStatus(Status.ATIVA);

        when(matriculaRepository.findByCursoId(cursoId))
                .thenReturn(List.of(matricula));

        List<AlunoResponseDTO> resultado =
                matriculaService.buscarAlunosPorCurso(cursoId);

        assertEquals(1, resultado.size());
        assertEquals(alunoId, resultado.get(0).id());
        assertEquals("Maria", resultado.get(0).nome());

        verify(matriculaRepository).findByCursoId(cursoId);
    }

    @Test
    void deveBuscarCursosPorAluno() {

        UUID alunoId = UUID.randomUUID();
        UUID cursoId = UUID.randomUUID();

        Aluno aluno = new Aluno();
        aluno.setId(alunoId);
        aluno.setNome("Maria");

        Curso curso = new Curso();
        curso.setId(cursoId);
        curso.setNome("Java");

        Matricula matricula = new Matricula();
        matricula.setId(UUID.randomUUID());
        matricula.setAluno(aluno);
        matricula.setCurso(curso);
        matricula.setStatus(Status.ATIVA);

        when(matriculaRepository.findByAlunoId(alunoId))
                .thenReturn(List.of(matricula));

        List<CursoResponseDTO> resultado =
                matriculaService.buscarCursosPorAluno(alunoId);

        assertEquals(1, resultado.size());
        assertEquals(cursoId, resultado.get(0).id());
        assertEquals("Java", resultado.get(0).nome());

        verify(matriculaRepository).findByAlunoId(alunoId);
    }
}
