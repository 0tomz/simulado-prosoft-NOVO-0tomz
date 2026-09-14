package br.insper.cursos.controller;

import br.insper.cursos.dto.CursoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.ObjectMapper;

import tools.jackson.databind.JsonNode;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class CursoControllerTests {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("cursos_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCriarEListarCurso() throws Exception {
        CursoDto dto = new CursoDto();
        dto.setNome("Java Avançado");
        dto.setDescricao("Curso avançado de Java");
        dto.setCargaHoraria(60);

        mockMvc.perform(post("/cursos")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Java Avançado"))
                .andExpect(jsonPath("$.deletado").value(false));

        mockMvc.perform(get("/cursos").param("nome", "Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.nome == 'Java Avançado')]").exists());
    }

    @Test
    public void testCriarSemNomeRetorna400() throws Exception {
        CursoDto dto = new CursoDto();
        dto.setDescricao("Sem nome");

        mockMvc.perform(post("/cursos")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeletarRemoveDaListagem() throws Exception {
        CursoDto dto = new CursoDto();
        dto.setNome("Curso Temporário");
        dto.setDescricao("Será deletado");
        dto.setCargaHoraria(20);

        MvcResult criado = mockMvc.perform(post("/cursos")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode node = objectMapper.readTree(criado.getResponse().getContentAsString());
        long id = node.get("id").asLong();

        mockMvc.perform(delete("/cursos/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/cursos").param("nome", "Curso Temporário"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + id + ")]").doesNotExist());
    }

    @Test
    public void testDeletarCursoInexistenteRetorna404() throws Exception {
        mockMvc.perform(delete("/cursos/{id}", 999999))
                .andExpect(status().isNotFound());
    }
}
