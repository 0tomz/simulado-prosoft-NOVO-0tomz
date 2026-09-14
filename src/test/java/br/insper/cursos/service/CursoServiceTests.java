package br.insper.cursos.service;

import br.insper.cursos.dto.CursoDto;
import br.insper.cursos.entity.Curso;
import br.insper.cursos.exception.CursoNaoEncontradoException;
import br.insper.cursos.exception.ValidacaoCursoException;
import br.insper.cursos.repository.CursoRepository;
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
public class CursoServiceTests {

    @InjectMocks
    private CursoService cursoService;

    @Mock
    private CursoRepository cursoRepository;

    @Test
    public void testListarSemFiltro() {
        Curso curso = createCurso();
        when(cursoRepository.findByDeletadoFalse()).thenReturn(List.of(curso));

        List<Curso> resultado = cursoService.listar(null);

        assertEquals(1, resultado.size());
        verify(cursoRepository).findByDeletadoFalse();
        verify(cursoRepository, never()).findByDeletadoFalseAndNomeStartingWith(anyString());
    }

    @Test
    public void testListarComFiltro() {
        Curso curso = createCurso();
        when(cursoRepository.findByDeletadoFalseAndNomeStartingWith("Java")).thenReturn(List.of(curso));

        List<Curso> resultado = cursoService.listar("Java");

        assertEquals(1, resultado.size());
        verify(cursoRepository).findByDeletadoFalseAndNomeStartingWith("Java");
    }

    @Test
    public void testCriarComSucesso() {
        CursoDto dto = createCursoDto();
        Curso entity = createCurso();

        when(cursoRepository.save(any(Curso.class))).thenReturn(entity);

        Curso resultado = cursoService.criar(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Java", resultado.getNome());
        assertFalse(resultado.isDeletado());
    }

    @Test
    public void testCriarSemNomeLancaExcecao() {
        CursoDto dto = createCursoDto();
        dto.setNome("");

        assertThrows(ValidacaoCursoException.class, () -> cursoService.criar(dto));
        verify(cursoRepository, never()).save(any(Curso.class));
    }

    @Test
    public void testDeletarComSucesso() {
        Curso curso = createCurso();
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        cursoService.deletar(1L);

        assertTrue(curso.isDeletado());
        verify(cursoRepository).save(curso);
    }

    @Test
    public void testDeletarCursoInexistenteLancaExcecao() {
        when(cursoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CursoNaoEncontradoException.class, () -> cursoService.deletar(99L));
        verify(cursoRepository, never()).save(any(Curso.class));
    }

    private CursoDto createCursoDto() {
        CursoDto dto = new CursoDto();
        dto.setNome("Java");
        dto.setDescricao("Curso de Java");
        dto.setCargaHoraria(40);
        return dto;
    }

    private Curso createCurso() {
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNome("Java");
        curso.setDescricao("Curso de Java");
        curso.setCargaHoraria(40);
        curso.setDeletado(false);
        return curso;
    }
}
