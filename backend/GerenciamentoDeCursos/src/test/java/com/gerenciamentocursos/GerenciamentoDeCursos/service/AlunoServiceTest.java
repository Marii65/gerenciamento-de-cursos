package com.gerenciamentocursos.GerenciamentoDeCursos.service;

import com.gerenciamentocursos.GerenciamentoDeCursos.dto.request.AlunoRequestDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.dto.response.AlunoResponseDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.entity.Aluno;
import com.gerenciamentocursos.GerenciamentoDeCursos.exception.AlunoNotFoundException;
import com.gerenciamentocursos.GerenciamentoDeCursos.repository.AlunoRepository;

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
public class AlunoServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @InjectMocks
    private AlunoService alunoService;

    @Test
    void deveCriarAluno (){
        AlunoRequestDTO dto = new AlunoRequestDTO(
                "Maria",
                "maria@email.com",
                LocalDate.of(2007, 7, 4)
        );

        Aluno aluno = new Aluno();
        aluno.setId(UUID.randomUUID());
        aluno.setNome( "Maria" );
        aluno.setEmail("maria@email.com");
        aluno.setDataNascimento(LocalDate.of(2007, 7, 4));

        when(alunoRepository.save(any(Aluno.class)))
                .thenReturn(aluno);

        AlunoResponseDTO resultado = alunoService.criarAluno(dto);

        assertEquals("Maria",resultado.nome());
        assertEquals("maria@email.com",resultado.email());
        assertEquals(LocalDate.of(2007, 7, 4),resultado.dataNascimento());

        verify(alunoRepository).save(any(Aluno.class));
    }
    @Test
    void deveListarAlunos() {

        Aluno aluno = new Aluno();
        aluno.setId(UUID.randomUUID());
        aluno.setNome("Maria");
        aluno.setEmail("maria@email.com");
        aluno.setDataNascimento(LocalDate.of(2007, 7, 4));

        when(alunoRepository.findAll())
                .thenReturn(List.of(aluno));

        List<AlunoResponseDTO> resultado =
                alunoService.listarAlunos();

        assertEquals(1, resultado.size());
        assertEquals("Maria", resultado.get(0).nome());

        verify(alunoRepository).findAll();
    }

    @Test
    void deveBuscarAlunoPorId() {

        UUID id = UUID.randomUUID();

        Aluno aluno = new Aluno();
        aluno.setId(id);
        aluno.setNome("Maria");
        aluno.setEmail("maria@email.com");

        when(alunoRepository.findById(id))
                .thenReturn(Optional.of(aluno));

        AlunoResponseDTO resultado =
                alunoService.buscarPorId(id);

        assertEquals(id, resultado.id());
        assertEquals("Maria", resultado.nome());

        verify(alunoRepository).findById(id);
    }

    @Test
    void deveLancarExcecaoQuandoAlunoNaoExistir(){

        UUID id = UUID.randomUUID();

        when(alunoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                AlunoNotFoundException.class,
                () -> alunoService.buscarPorId(id)
        );

    }
    @Test
    void deveAtualizarAluno() {

        UUID id = UUID.randomUUID();

        Aluno aluno = new Aluno();
        aluno.setId(id);
        aluno.setNome("Maria");
        aluno.setEmail("maria@email.com");

        AlunoRequestDTO dto = new AlunoRequestDTO(
                "Maria Luiza",
                "marialuiza@email.com",
                LocalDate.of(2007, 7, 4)
        );

        when(alunoRepository.findById(id))
                .thenReturn(Optional.of(aluno));

        when(alunoRepository.save(any(Aluno.class)))
                .thenReturn(aluno);

        AlunoResponseDTO resultado =
                alunoService.atualizarAluno(id, dto);

        assertEquals("Maria Luiza", resultado.nome());
        assertEquals("marialuiza@email.com", resultado.email());

        verify(alunoRepository).findById(id);
        verify(alunoRepository).save(aluno);
    }

    @Test
    void deveLancarExcecaoAoAtualizarAlunoInexistente() {

        UUID id = UUID.randomUUID();

        AlunoRequestDTO dto = new AlunoRequestDTO(
                "Maria",
                "maria@email.com",
                LocalDate.of(2007, 7, 4)
        );

        when(alunoRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                AlunoNotFoundException.class,
                () -> alunoService.atualizarAluno(id, dto)
        );

        verify(alunoRepository).findById(id);
    }

    @Test
    void deveExcluirAluno() {

        UUID id = UUID.randomUUID();

        Aluno aluno = new Aluno();
        aluno.setId(id);

        when(alunoRepository.findById(id))
                .thenReturn(Optional.of(aluno));

        alunoService.excluirAluno(id);

        verify(alunoRepository).findById(id);
        verify(alunoRepository).delete(aluno);
    }

    @Test
    void deveLancarExcecaoAoExcluirAlunoInexistente() {

        UUID id = UUID.randomUUID();

        when(alunoRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                AlunoNotFoundException.class,
                () -> alunoService.excluirAluno(id)
        );

        verify(alunoRepository).findById(id);
    }
}
