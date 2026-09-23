package br.insper.cursos.controller;

import br.insper.cursos.dto.TarefaDto;
import br.insper.cursos.entity.Tarefa;
import br.insper.cursos.service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/tarefa")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @GetMapping("/tarefa")
    public Collection<Tarefa> getTarefa() {
        return tarefaService.listarTodos();
    }

    @PostMapping
    public ResponseEntity<Tarefa> criar(@RequestBody TarefaDto dto) {
        Tarefa tarefa = tarefaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarefa);
    }

    @DeleteMapping("/tarefa/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        tarefaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    //@PutMapping("/{id}")
   // public Tarefa atualizarTarefa(@PathVariable Long id, @RequestBody Tarefa dados) {
   //     return TarefaService.atualizarTarefa(id, dados);
   // }
}