package com.gerenciamentocursos.GerenciamentoDeCursos.repository;

import com.gerenciamentocursos.GerenciamentoDeCursos.entity.Aluno;
import com.gerenciamentocursos.GerenciamentoDeCursos.entity.Curso;
import com.gerenciamentocursos.GerenciamentoDeCursos.entity.Matricula;
import com.gerenciamentocursos.GerenciamentoDeCursos.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MatriculaRepository extends JpaRepository<Matricula, UUID> {
    List<Matricula> findByStatus(Status status);

    List<Matricula> findByAlunoId(UUID alunoId);

    List<Matricula> findByCursoId(UUID cursoId);

    boolean existsByAlunoIdAndCursoIdAndStatus(UUID alunoId, UUID cursoId, Status status);
}
