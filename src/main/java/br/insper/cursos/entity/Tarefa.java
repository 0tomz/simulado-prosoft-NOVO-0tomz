package br.insper.cursos.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String descricao;

    private LocalDate dataCriacao;

    private boolean deletado;


    public Tarefa() {
    }

    public Tarefa(String titulo, String descricao, LocalDate dataCriacao) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        this.titulo = titulo;
        return titulo;
    }

    public String getDescricao() {
        this.descricao = descricao;
        return descricao;
    }


    public enum TarefaStatus {
        TODO, DOING, DONE
    }



}
