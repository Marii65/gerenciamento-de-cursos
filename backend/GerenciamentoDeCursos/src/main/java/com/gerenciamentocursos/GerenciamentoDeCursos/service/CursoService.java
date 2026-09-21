package com.gerenciamentocursos.GerenciamentoDeCursos.service;

import com.gerenciamentocursos.GerenciamentoDeCursos.dto.request.CursoRequestDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.dto.response.CursoResponseDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.entity.Curso;
import com.gerenciamentocursos.GerenciamentoDeCursos.exception.CursoNotFoundException;
import com.gerenciamentocursos.GerenciamentoDeCursos.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável por gerenciar as operações de negócio relacionadas aos Cursos.
 */
@Service
@RequiredArgsConstructor
public class CursoService {
    private final CursoRepository cursoRepository;

    /**
     * Cadastra um novo curso no sistema.
     *
     * @param dto Dados de criação do curso (nome, descrição, carga horária).
     * @return {@link CursoResponseDTO} Dados do curso criado.
     */
    public CursoResponseDTO criarCurso(CursoRequestDTO dto) {
        Curso curso = new Curso();

        curso.setNome(dto.nome());
        curso.setDescricao(dto.descricao());
        curso.setCargaHoraria(dto.cargaHoraria());

        Curso cursoSalvo = cursoRepository.save(curso);

        return toResponseDTO(cursoSalvo);
    }

    /**
     * Retorna a lista completa de cursos cadastrados.
     *
     * @return Lista de {@link CursoResponseDTO}.
     */
    public List<CursoResponseDTO> listarCursos() {
        return cursoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Busca um curso pelo seu identificador único (UUID).
     *
     * @param id Identificador do curso.
     * @return {@link CursoResponseDTO} Dados do curso retornado.
     * @throws CursoNotFoundException Se o curso não for encontrado.
     */
    public CursoResponseDTO buscarPorId(UUID id){
        Curso curso = buscarCursoPorId(id);
        return toResponseDTO(curso);
    }

    /**
     * Atualiza as informações de um curso existente.
     *
     * @param id Identificador do curso a ser atualizado.
     * @param dto Novos dados a serem aplicados ao curso.
     * @return {@link CursoResponseDTO} Curso atualizado.
     * @throws CursoNotFoundException Se o curso não for encontrado.
     */
    public CursoResponseDTO atualizarCurso(UUID id, CursoRequestDTO dto){
        Curso curso = buscarCursoPorId(id);

        curso.setNome(dto.nome());
        curso.setDescricao(dto.descricao());
        curso.setCargaHoraria(dto.cargaHoraria());

        Curso cursoAtualizado = cursoRepository.save(curso);

        return toResponseDTO(cursoAtualizado);
    }

    /**
     * Exclui um curso do sistema a partir do seu ID.
     *
     * @param id Identificador do curso.
     * @throws CursoNotFoundException Se o curso não existir.
     */
    public void excluirCurso(UUID id){
        Curso curso = buscarCursoPorId(id);

        cursoRepository.delete(curso);
    }

    /**
     * Método utilitário privado para encontrar um curso por ID ou lançar exceção.
     */
    private Curso buscarCursoPorId(UUID id){
        return cursoRepository.findById(id)
                .orElseThrow(() ->
                        new CursoNotFoundException(
                                "Curso não encontrado: "+ id));
    }

    /**
     * Mapeia a entidade {@link Curso} para o DTO de resposta.
     */
    private CursoResponseDTO toResponseDTO(Curso curso){
        return new CursoResponseDTO(
                curso.getId(),
                curso.getNome(),
                curso.getDescricao(),
                curso.getCargaHoraria(),
                curso.getCriadoEm()
        );
    }




}
