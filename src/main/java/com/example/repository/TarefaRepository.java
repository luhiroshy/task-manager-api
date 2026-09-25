package com.example.gerenciador_tarefas.repository;

import com.example.gerenciador_tarefas.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    boolean existsByTitulo(String titulo);
    Optional<Tarefa> findByTitulo(String titulo);
}