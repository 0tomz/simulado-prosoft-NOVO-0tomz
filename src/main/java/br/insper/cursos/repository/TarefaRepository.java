package br.insper.cursos.repository;

import br.insper.cursos.entity.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    List<Tarefa> findByDeletadoFalse();

    List<Tarefa> findByDeletadoFalseAndTituloStartingWith(String titulo);
}
