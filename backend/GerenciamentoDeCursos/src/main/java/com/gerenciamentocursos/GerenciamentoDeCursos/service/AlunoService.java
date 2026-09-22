package com.gerenciamentocursos.GerenciamentoDeCursos.service;

import com.gerenciamentocursos.GerenciamentoDeCursos.dto.request.AlunoRequestDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.dto.response.AlunoResponseDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.entity.Aluno;
import com.gerenciamentocursos.GerenciamentoDeCursos.exception.AlunoNotFoundException;
import com.gerenciamentocursos.GerenciamentoDeCursos.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import com.gerenciamentocursos.GerenciamentoDeCursos.exception.AlunoComMatriculaException;
import com.gerenciamentocursos.GerenciamentoDeCursos.repository.MatriculaRepository;


/**
 * Serviço responsável por gerenciar as operações de negócio relacionadas aos Alunos.
 */
@Service
@RequiredArgsConstructor
public class AlunoService {
    private final AlunoRepository alunoRepository;
    private final MatriculaRepository matriculaRepository;

    /**
     * Cadastra um novo aluno no sistema.
     *
     * @param dto Objeto contendo os dados do aluno a ser criado (nome, e-mail, data de nascimento).
     * @return {@link AlunoResponseDTO} Dados do aluno cadastrado com ID e data de criação.
     */
    public AlunoResponseDTO criarAluno(AlunoRequestDTO dto) {
        Aluno aluno = new Aluno();

        aluno.setNome(dto.nome());
        aluno.setEmail(dto.email());
        aluno.setDataNascimento(dto.dataNascimento());

        Aluno alunoSalvo = alunoRepository.save(aluno);

        return toResponseDTO(alunoSalvo);
    }

    /**
     * Retorna a lista de todos os alunos cadastrados.
     *
     * @return Lista de {@link AlunoResponseDTO}. Retorna uma lista vazia caso não existam registros.
     */
    public List<AlunoResponseDTO> listarAlunos() {
        return alunoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Busca um aluno pelo seu identificador único (UUID).
     *
     * @param id Identificador único do aluno.
     * @return {@link AlunoResponseDTO} Dados do aluno encontrado.
     * @throws AlunoNotFoundException Se nenhum aluno for encontrado com o ID informado.
     */
    public AlunoResponseDTO buscarPorId(UUID id) {
        Aluno aluno = buscarAlunoPorId(id);

        return toResponseDTO(aluno);
    }

    /**
     * Atualiza os dados de um aluno existente.
     *
     * @param id Identificador único do aluno a ser atualizado.
     * @param dto Novos dados do aluno.
     * @return {@link AlunoResponseDTO} Dados atualizados do aluno.
     * @throws AlunoNotFoundException Se o aluno com o ID fornecido não for encontrado.
     */
    public AlunoResponseDTO atualizarAluno(UUID id, AlunoRequestDTO dto){
        Aluno aluno = buscarAlunoPorId(id);
        aluno.setNome(dto.nome());
        aluno.setEmail(dto.email());
        aluno.setDataNascimento(dto.dataNascimento());
        Aluno alunoSalvo = alunoRepository.save(aluno);
        return toResponseDTO(alunoSalvo);
    }

    /**
     * Remove um aluno da base de dados pelo seu ID.
     *
     * @param id Identificador único do aluno a ser excluído.
     * @throws AlunoNotFoundException Se o aluno com o ID fornecido não for encontrado.
     */
    public void excluirAluno(UUID id) {
        Aluno aluno = buscarAlunoPorId(id);

        if (!matriculaRepository.findByAlunoId(id).isEmpty()) {
            throw new AlunoComMatriculaException(
                    "Não é possível excluir o aluno porque ele possui matrículas cadastradas."
            );
        }

        alunoRepository.delete(aluno);
    }

    /**
     * Método utilitário privado para localizar um aluno ou lançar exceção.
     *
     * @param id Identificador do aluno.
     * @return Instância da entidade {@link Aluno}.
     * @throws AlunoNotFoundException Se não encontrar o registro.
     */
    private Aluno buscarAlunoPorId(UUID id) {
        return alunoRepository.findById(id)
                .orElseThrow(() ->
                        new AlunoNotFoundException(
                                "Aluno não encontrado: " + id));
    }

    /**
     * Converte a entidade {@link Aluno} para seu DTO de resposta.
     */
    private AlunoResponseDTO toResponseDTO(Aluno aluno) {
        return new AlunoResponseDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getDataNascimento(),
                aluno.getCriadoEm()
        );
    }




}
