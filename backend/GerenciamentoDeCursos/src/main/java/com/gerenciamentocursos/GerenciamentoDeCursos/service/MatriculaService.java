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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável por gerenciar o vínculo (Matrícula) entre Alunos e Cursos.
 */
@Service
@RequiredArgsConstructor
public class MatriculaService {
    private final MatriculaRepository matriculaRepository;
    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;

    /**
     * Realiza a matrícula de um aluno em um curso.
     *
     * <p>Regra de negócio: Um aluno não pode possuir mais de uma matrícula {@link Status#ATIVA}
     * no mesmo curso simultaneamente.</p>
     *
     * @param dto DTO contendo o ID do aluno e o ID do curso.
     * @return {@link MatriculaResponseDTO} Informações da matrícula efetuada.
     * @throws AlunoNotFoundException Se o aluno especificado não existir.
     * @throws CursoNotFoundException Se o curso especificado não existir.
     * @throws MatriculaConflictException Se o aluno já possuir uma matrícula ativa no curso informado.
     */
    public MatriculaResponseDTO criarMatricula(MatriculaRequestDTO dto) {
        Aluno aluno = alunoRepository.findById(dto.alunoId())
                .orElseThrow(() ->
                        new AlunoNotFoundException(
                                "Aluno não encontrado: " + dto.alunoId()));

        Curso curso = cursoRepository.findById(dto.cursoId())
                .orElseThrow(() ->
                        new CursoNotFoundException(
                                "Curso não encontrado: " + dto.cursoId()));

        if (matriculaRepository.existsByAlunoIdAndCursoIdAndStatus(
                dto.alunoId(), dto.cursoId(), Status.ATIVA)) {
                    throw new MatriculaConflictException(
                      "Aluno já está matriculado neste curso!"
                    );
        }


        Matricula matricula = new Matricula();

        matricula.setAluno(aluno);
        matricula.setCurso(curso);

        matricula.setDataPrevisaoConclusao(
                dto.dataPrevisaoConclusao()
        );

        Matricula matriculaSalva = matriculaRepository.save(matricula);

        return toResponseDTO(matriculaSalva);
    }

    /**
     * Lista todas as matrículas cadastradas.
     *
     * @return Lista de {@link MatriculaResponseDTO}.
     */
    public List<MatriculaResponseDTO> listarMatriculas() {
        return matriculaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Atualiza o status de uma matrícula existente.
     *
     * <p>Regras de negócio:</p>
     * <ul>
     *   <li>Matrículas com status {@link Status#CANCELADA} ou {@link Status#CONCLUIDA} são finais e não podem ser alteradas.</li>
     *   <li>Matrículas ativas só podem transicionar para CANCELADA ou CONCLUIDA.</li>
     * </ul>
     *
     * @param id Identificador da matrícula.
     * @param dto DTO contendo o novo status desejado.
     * @return {@link MatriculaResponseDTO} Matrícula com status alterado.
     * @throws MatriculaNotFoundException Se a matrícula não for encontrada.
     * @throws IllegalStateException Se tentar alterar uma matrícula já finalizada (cancelada/concluída).
     * @throws IllegalArgumentException Se o novo status informado for inválido para transição.
     */
    public MatriculaResponseDTO atualizarStatus(
            UUID id,
            MatriculaStatusRequestDTO dto) {

        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() ->
                        new MatriculaNotFoundException(
                                "Matrícula não encontrada: "+ id));

        if (matricula.getStatus() == Status.CANCELADA || matricula.getStatus() == Status.CONCLUIDA){
            throw new IllegalStateException(
                    "Não é possível alterar uma matrícula cancelada ou concluída.");
        }

        if (dto.status() != Status.CANCELADA &&
                dto.status() != Status.CONCLUIDA) {

            throw new IllegalArgumentException(
                    "Uma matrícula ativa só pode ser cancelada ou concluída.");
        }

        matricula.setStatus(dto.status());

        Matricula matriculaAtualizada = matriculaRepository.save(matricula);

        return toResponseDTO(matriculaAtualizada);
    }

    /**
     * Busca todos os alunos matriculados em um determinado curso.
     *
     * @param cursoId Identificador do curso.
     * @return Lista de {@link AlunoResponseDTO} pertencentes ao curso.
     */
    public List<AlunoResponseDTO> buscarAlunosPorCurso(UUID cursoId){

        cursoRepository.findById(cursoId)
                .orElseThrow(() ->
                        new CursoNotFoundException(
                                "Curso não encontrado: " + cursoId));

        return matriculaRepository.findByCursoId(cursoId)
                .stream()
                .map(Matricula::getAluno)
                .map(this::toAlunoResponseDTO)
                .toList();
    }

    /**
     * Busca todos os cursos em que um determinado aluno está matriculado.
     *
     * @param alunoId Identificador do aluno.
     * @return Lista de {@link CursoResponseDTO} associados ao aluno.
     */
    public List<CursoResponseDTO> buscarCursosPorAluno(UUID alunoId){

        alunoRepository.findById(alunoId)
                .orElseThrow(() ->
                        new AlunoNotFoundException(
                                "Aluno não encontrado: " + alunoId));

        return matriculaRepository.findByAlunoId(alunoId)
                .stream()
                .map(Matricula::getCurso)
                .map(this::toCursoResponseDTO)
                .toList();
    }

    private CursoResponseDTO toCursoResponseDTO(Curso curso){
        return new CursoResponseDTO(
                curso.getId(),
                curso.getNome(),
                curso.getDescricao(),
                curso.getCargaHoraria(),
                curso.getCriadoEm()
        );
    }

    private AlunoResponseDTO toAlunoResponseDTO(Aluno aluno) {
        return new AlunoResponseDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getDataNascimento(),
                aluno.getCriadoEm()
        );
    }

    private MatriculaResponseDTO toResponseDTO(Matricula matricula) {
        return new MatriculaResponseDTO(
                matricula.getId(),
                matricula.getAluno().getId(),
                matricula.getAluno().getNome(),
                matricula.getCurso().getId(),
                matricula.getCurso().getNome(),
                matricula.getDataMatricula(),
                matricula.getDataPrevisaoConclusao(),
                matricula.getStatus()
        );
    }


}
