package br.insper.cursos.service;

import br.insper.cursos.dto.TarefaDto;
import br.insper.cursos.entity.Tarefa;
import br.insper.cursos.exception.TarefaNaoEncontradoException;
import br.insper.cursos.exception.ValidacaoTarefaException;
import br.insper.cursos.repository.TarefaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TarefasServiceTests {

    @InjectMocks
    private TarefaService tarefaService;

    @Mock
    private TarefaRepository tarefaRepository;

    @Test
    public void testListarSemFiltro() {
        Tarefa curso = createCurso();
        when(tarefaRepository.findByDeletadoFalse()).thenReturn(List.of(curso));

        List<Tarefa> resultado = tarefaService.listar(null);

        assertEquals(1, resultado.size());
        verify(tarefaRepository).findByDeletadoFalse();
        verify(tarefaRepository, never()).findByDeletadoFalseAndTituloStartingWith(anyString());
    }

    @Test
    public void testListarComFiltro() {
        Tarefa curso = createCurso();
        when(tarefaRepository.findByDeletadoFalseAndTituloStartingWith("Java")).thenReturn(List.of(curso));

        List<Tarefa> resultado = tarefaService.listar("Java");

        assertEquals(1, resultado.size());
        verify(tarefaRepository).findByDeletadoFalseAndTituloStartingWith("Java");
    }

    @Test
    public void testCriarComSucesso() {
        TarefaDto dto = createCursoDto();
        Tarefa entity = createCurso();

        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(entity);

        Tarefa resultado = tarefaService.criar(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Java", resultado.getTitulo());
        assertFalse(resultado.isDeletado());
    }

    @Test
    public void testCriarSemNomeLancaExcecao() {
        TarefaDto dto = createCursoDto();
        dto.setTitulo("");

        assertThrows(ValidacaoTarefaException.class, () -> tarefaService.criar(dto));
        verify(tarefaRepository, never()).save(any(Tarefa.class));
    }

    @Test
    public void testDeletarComSucesso() {
        Tarefa curso = createCurso();
        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(curso);

        tarefaService.deletar(1L);

        assertTrue(curso.isDeletado());
        verify(tarefaRepository).save(curso);
    }

    @Test
    public void testDeletarCursoInexistenteLancaExcecao() {
        when(tarefaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TarefaNaoEncontradoException.class, () -> tarefaService.deletar(99L));
        verify(tarefaRepository, never()).save(any(Tarefa.class));
    }

    private TarefaDto createCursoDto() {
        TarefaDto dto = new TarefaDto();
        dto.setTitulo("Java");
        dto.setDescricao("Curso de Java");
        return dto;
    }

    private Tarefa createCurso() {
        Tarefa curso = new Tarefa();
        curso.setId(1L);
        curso.setTitulo("Java");
        curso.setDescricao("Curso de Java");
        curso.setDeletado(false);
        return curso;
    }
}
