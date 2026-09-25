package com.example.gerenciador_tarefas.dto;

import com.example.gerenciador_tarefas.model.StatusTarefa;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TarefaResponseDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private StatusTarefa status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataConclusao;
}