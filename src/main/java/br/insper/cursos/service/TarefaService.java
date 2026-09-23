package br.insper.cursos.service;

import br.insper.cursos.dto.TarefaDto;
import br.insper.cursos.entity.Tarefa;
import br.insper.cursos.exception.TarefaNaoEncontradoException;
import br.insper.cursos.exception.ValidacaoTarefaException;
import br.insper.cursos.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

@Service
public class TarefaService {

    private HashMap<Long, Tarefa> tarefa = new HashMap<>();

    @Autowired
    private TarefaRepository tarefaRepository;

    public List<Tarefa> listar(String titulo) {
        if (StringUtils.hasText(titulo)) {
            return tarefaRepository.findByDeletadoFalseAndTituloStartingWith(titulo);
        }
        return tarefaRepository.findByDeletadoFalse();
    }

    public List<Tarefa> listarTodos() {
        return tarefaRepository.findAll();
    }





    public Tarefa criar(TarefaDto dto) {
        if (!StringUtils.hasText(dto.getTitulo())) {
            throw new ValidacaoTarefaException("Título da tarefa é obrigatório");
        }

        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(dto.getTitulo());
        tarefa.setDescricao(dto.getDescricao());
        tarefa.setDeletado(false);

        return tarefaRepository.save(tarefa);
    }

    public Tarefa getTarefa(Long tarefaId) {
        return tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada"));
    }

    public void deletar(Long id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new TarefaNaoEncontradoException("Tarefa não encontrada"));

        tarefa.setDeletado(true);
        tarefaRepository.save(tarefa);
    }

    public Tarefa atualizarTarefa(Long id, Tarefa dados){
        Tarefa tarefa = getTarefa(id);

        if (dados.getTitulo() != null) tarefa.setTitulo(dados.getTitulo());
        //if (dados.getDescricao() != null) tarefa.getDescricao(dados.getDescricao());

        return tarefa;
    }

}
