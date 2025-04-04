package aceleramaker.project.service;

import aceleramaker.project.dto.CreatePostagemDto;
import aceleramaker.project.dto.UpdatePostagemDto;
import aceleramaker.project.entity.Postagem;
import aceleramaker.project.entity.Tema;
import aceleramaker.project.entity.Usuario;
import aceleramaker.project.repository.PostagemRepository;
import aceleramaker.project.repository.TemaRepository;
import aceleramaker.project.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PostagemService {

    @Autowired
    private PostagemRepository postagemRepository;

    @Autowired
    private TemaRepository temaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Postagem> listarTodas() {
        return postagemRepository.findAll();
    }

    public Optional<Postagem> buscarPorId(Long id) {
        return postagemRepository.findById(id);
    }

    public List<Postagem> buscarPorTitulo(String titulo) {
        return postagemRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public Postagem criar(CreatePostagemDto dto) {
        Postagem postagem = new Postagem();
        postagem.setTitulo(dto.titulo());
        postagem.setTexto(dto.texto());

        Tema tema = temaRepository.findById(dto.temaId())
                .orElseThrow(() -> new RuntimeException("Tema não encontrado"));
        postagem.setTema(tema);

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        postagem.setUsuario(usuario);

        return postagemRepository.save(postagem);
    }

    public Optional<Postagem> atualizar(Long id, UpdatePostagemDto dto) {
        return postagemRepository.findById(id).map(postagem -> {
            postagem.setTitulo(dto.titulo());
            postagem.setTexto(dto.texto());

            if (dto.temaId() != null) {
                Tema tema = temaRepository.findById(dto.temaId())
                        .orElseThrow(() -> new RuntimeException("Tema não encontrado"));
                postagem.setTema(tema);
            }

            return postagemRepository.save(postagem);
        });
    }

    public void deletar(Long id) {
        postagemRepository.deleteById(id);
    }

    public List<Postagem> buscarPorTema(Long temaId) {
        return postagemRepository.findByTemaId(temaId);
    }

    public List<Postagem> buscarPorUsuario(Long usuarioId) {
        return postagemRepository.findByUsuarioId(usuarioId);
    }
}
