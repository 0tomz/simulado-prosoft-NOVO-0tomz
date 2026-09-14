package br.insper.cursos.service;

import br.insper.cursos.dto.CursoDto;
import br.insper.cursos.entity.Curso;
import br.insper.cursos.exception.CursoNaoEncontradoException;
import br.insper.cursos.exception.ValidacaoCursoException;
import br.insper.cursos.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public List<Curso> listar(String nome) {
        if (StringUtils.hasText(nome)) {
            return cursoRepository.findByDeletadoFalseAndNomeStartingWith(nome);
        }
        return cursoRepository.findByDeletadoFalse();
    }

    public Curso criar(CursoDto dto) {
        if (!StringUtils.hasText(dto.getNome())) {
            throw new ValidacaoCursoException("Nome do curso é obrigatório");
        }

        Curso curso = new Curso();
        curso.setNome(dto.getNome());
        curso.setDescricao(dto.getDescricao());
        curso.setCargaHoraria(dto.getCargaHoraria());
        curso.setDeletado(false);

        return cursoRepository.save(curso);
    }

    public void deletar(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new CursoNaoEncontradoException("Curso não encontrado"));

        curso.setDeletado(true);
        cursoRepository.save(curso);
    }
}
