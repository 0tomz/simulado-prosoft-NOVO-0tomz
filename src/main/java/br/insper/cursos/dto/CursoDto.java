package br.insper.cursos.dto;

import lombok.Data;

@Data
public class CursoDto {

    private String nome;

    private String descricao;

    private Integer cargaHoraria;
}
