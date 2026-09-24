package cl.duoc.pedidos360_backend.service;

import cl.duoc.pedidos360_backend.model.Pedido;
import cl.duoc.pedidos360_backend.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository repository;

    public PedidoService(PedidoRepository repository) {
        this.repository = repository;
    }

    public List<Pedido> obtenerTodos() {
        return repository.findAll();
    }

    public List<Pedido> obtenerPorUsuario(String oid) {
        return repository.findByUsuarioOid(oid);
    }

    public Optional<Pedido> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public Pedido crear(Pedido pedido) {
        return repository.save(pedido);
    }

    public Optional<Pedido> actualizar(Long id, Pedido datos) {
        return repository.findById(id).map(p -> {
            p.setDescripcion(datos.getDescripcion());
            p.setEstado(datos.getEstado());
            return repository.save(p);
        });
    }

    public boolean eliminar(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}