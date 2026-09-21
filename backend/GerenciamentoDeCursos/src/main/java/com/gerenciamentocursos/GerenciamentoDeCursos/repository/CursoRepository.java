package com.gerenciamentocursos.GerenciamentoDeCursos.repository;

import com.gerenciamentocursos.GerenciamentoDeCursos.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CursoRepository extends JpaRepository<Curso, UUID> {
}
