package com.example.gerenciador_tarefas.service;

import com.example.gerenciador_tarefas.dto.*;
import com.example.gerenciador_tarefas.exception.*;
import com.example.gerenciador_tarefas.model.*;
import com.example.gerenciador_tarefas.repository.TarefaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TarefaService {

    private final TarefaRepository repository;

    public TarefaService(TarefaRepository repository) {
        this.repository = repository;
    }

    public TarefaResponseDTO criar(TarefaRequestDTO dto) {
        if (repository.existsByTitulo(dto.getTitulo())) {
            throw new RegraDeNegocioException("Já existe uma tarefa com este título.");
        }

        Tarefa tarefa = new Tarefa();
        BeanUtils.copyProperties(dto, tarefa);
        
        if (tarefa.getStatus() == null) {
            tarefa.setStatus(StatusTarefa.PENDENTE);
        }

        Tarefa salva = repository.save(tarefa);
        return converterParaDTO(salva);
    }

    public List<TarefaResponseDTO> listarTodas() {
        return repository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public TarefaResponseDTO buscarPorId(Long id) {
        Tarefa tarefa = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa não encontrada."));
        return converterParaDTO(tarefa);
    }

    public TarefaResponseDTO atualizar(Long id, TarefaRequestDTO dto) {
        Tarefa tarefa = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa não encontrada."));

        // Validar transição de status: PENDENTE -> CONCLUIDA não é permitido sem passar por EM_ANDAMENTO
        if (tarefa.getStatus() == StatusTarefa.PENDENTE && dto.getStatus() == StatusTarefa.CONCLUIDA) {
            throw new RegraDeNegocioException("Não é permitido transitar diretamente de PENDENTE para CONCLUIDA.");
        }

        tarefa.setTitulo(dto.getTitulo());
        tarefa.setDescricao(dto.getDescricao());
        
        if (dto.getStatus() != null) {
            tarefa.setStatus(dto.getStatus());
            if (dto.getStatus() == StatusTarefa.CONCLUIDA && tarefa.getDataConclusao() == null) {
                tarefa.setDataConclusao(LocalDateTime.now());
            }
        }

        return converterParaDTO(repository.save(tarefa));
    }

    public void deletar(Long id) {
        Tarefa tarefa = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa não encontrada."));

        if (tarefa.getStatus() == StatusTarefa.CONCLUIDA) {
            throw new RegraDeNegocioException("Não é possível excluir uma tarefa CONCLUÍDA.");
        }

        repository.delete(tarefa);
    }

    private TarefaResponseDTO converterParaDTO(Tarefa tarefa) {
        TarefaResponseDTO dto = new TarefaResponseDTO();
        BeanUtils.copyProperties(tarefa, dto);
        return dto;
    }
}