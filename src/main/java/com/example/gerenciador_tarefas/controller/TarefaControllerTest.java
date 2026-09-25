package com.example.gerenciador_tarefas.controller;

import com.example.gerenciador_tarefas.service.TarefaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TarefaController.class)
class TarefaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TarefaService service;

    @Test
    void deveRetornarStatus200AoListarTarefas() throws Exception {
        mockMvc.perform(get("/api/tarefas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}