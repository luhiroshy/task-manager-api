package com.example.gerenciador_tarefas.service;

import com.example.gerenciador_tarefas.dto.*;
import com.example.gerenciador_tarefas.exception.RegraDeNegocioException;
import com.example.gerenciador_tarefas.model.*;
import com.example.gerenciador_tarefas.repository.TarefaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    @Mock
    private TarefaRepository repository;

    @InjectMocks
    private TarefaService service;

    @Test
    void deveLancarExcecaoAoTransitarDePendenteParaConcluida() {
        Tarefa tarefa = Tarefa.builder().id(1L).status(StatusTarefa.PENDENTE).build();
        TarefaRequestDTO dto = new TarefaRequestDTO();
        dto.setTitulo("Título Válido");
        dto.setStatus(StatusTarefa.CONCLUIDA);

        when(repository.findById(1L)).thenReturn(Optional.of(tarefa));

        assertThrows(RegraDeNegocioException.class, () -> service.atualizar(1L, dto));
    }

    @Test
    void deveLancarExcecaoAoExcluirTarefaConcluida() {
        Tarefa tarefa = Tarefa.builder().id(1L).status(StatusTarefa.CONCLUIDA).build();
        when(repository.findById(1L)).thenReturn(Optional.of(tarefa));

        assertThrows(RegraDeNegocioException.class, () -> service.deletar(1L));
    }
}