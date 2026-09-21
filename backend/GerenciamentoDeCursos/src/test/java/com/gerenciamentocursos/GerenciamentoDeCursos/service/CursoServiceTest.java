package com.gerenciamentocursos.GerenciamentoDeCursos.service;

import com.gerenciamentocursos.GerenciamentoDeCursos.dto.request.CursoRequestDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.dto.response.CursoResponseDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.entity.Curso;
import com.gerenciamentocursos.GerenciamentoDeCursos.exception.CursoNotFoundException;
import com.gerenciamentocursos.GerenciamentoDeCursos.repository.CursoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoService cursoService;

    @Test
    void deveCriarCurso() {

        CursoRequestDTO dto = new CursoRequestDTO(
                "Java",
                "Curso de Java",
                80
        );

        Curso curso = new Curso();
        curso.setId(UUID.randomUUID());
        curso.setNome("Java");
        curso.setDescricao("Curso de Java");
        curso.setCargaHoraria(80);

        when(cursoRepository.save(any(Curso.class)))
                .thenReturn(curso);

        CursoResponseDTO resultado =
                cursoService.criarCurso(dto);

        assertEquals("Java", resultado.nome());
        assertEquals("Curso de Java", resultado.descricao());
        assertEquals(80, resultado.cargaHoraria());

        verify(cursoRepository).save(any(Curso.class));
    }

    @Test
    void deveListarCursos() {

        Curso curso = new Curso();
        curso.setId(UUID.randomUUID());
        curso.setNome("Java");
        curso.setDescricao("Curso de Java");
        curso.setCargaHoraria(80);

        when(cursoRepository.findAll())
                .thenReturn(List.of(curso));

        List<CursoResponseDTO> resultado =
                cursoService.listarCursos();

        assertEquals(1, resultado.size());
        assertEquals("Java", resultado.get(0).nome());

        verify(cursoRepository).findAll();
    }

    @Test
    void deveBuscarCursoPorId() {

        UUID id = UUID.randomUUID();

        Curso curso = new Curso();
        curso.setId(id);
        curso.setNome("Java");
        curso.setDescricao("Curso de Java");
        curso.setCargaHoraria(80);

        when(cursoRepository.findById(id))
                .thenReturn(Optional.of(curso));

        CursoResponseDTO resultado =
                cursoService.buscarPorId(id);

        assertEquals(id, resultado.id());
        assertEquals("Java", resultado.nome());

        verify(cursoRepository).findById(id);
    }

    @Test
    void deveLancarExcecaoQuandoCursoNaoExistir() {

        UUID id = UUID.randomUUID();

        when(cursoRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                CursoNotFoundException.class,
                () -> cursoService.buscarPorId(id)
        );

        verify(cursoRepository).findById(id);
    }

    @Test
    void deveAtualizarCurso() {

        UUID id = UUID.randomUUID();

        Curso curso = new Curso();
        curso.setId(id);
        curso.setNome("Java");
        curso.setDescricao("Curso de Java");
        curso.setCargaHoraria(80);

        CursoRequestDTO dto = new CursoRequestDTO(
                "Java Spring",
                "Curso de Java com Spring",
                100
        );

        when(cursoRepository.findById(id))
                .thenReturn(Optional.of(curso));

        when(cursoRepository.save(any(Curso.class)))
                .thenReturn(curso);

        CursoResponseDTO resultado =
                cursoService.atualizarCurso(id, dto);

        assertEquals("Java Spring", resultado.nome());
        assertEquals("Curso de Java com Spring", resultado.descricao());
        assertEquals(100, resultado.cargaHoraria());

        verify(cursoRepository).findById(id);
        verify(cursoRepository).save(curso);
    }

    @Test
    void deveLancarExcecaoAoAtualizarCursoInexistente() {

        UUID id = UUID.randomUUID();

        CursoRequestDTO dto = new CursoRequestDTO(
                "Java",
                "Curso de Java",
                80
        );

        when(cursoRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                CursoNotFoundException.class,
                () -> cursoService.atualizarCurso(id, dto)
        );

        verify(cursoRepository).findById(id);
    }

    @Test
    void deveExcluirCurso() {

        UUID id = UUID.randomUUID();

        Curso curso = new Curso();
        curso.setId(id);

        when(cursoRepository.findById(id))
                .thenReturn(Optional.of(curso));

        cursoService.excluirCurso(id);

        verify(cursoRepository).findById(id);
        verify(cursoRepository).delete(curso);
    }

    @Test
    void deveLancarExcecaoAoExcluirCursoInexistente() {

        UUID id = UUID.randomUUID();

        when(cursoRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                CursoNotFoundException.class,
                () -> cursoService.excluirCurso(id)
        );

        verify(cursoRepository).findById(id);
    }
}