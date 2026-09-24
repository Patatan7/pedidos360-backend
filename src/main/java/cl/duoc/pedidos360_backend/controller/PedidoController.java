package cl.duoc.pedidos360_backend.controller;

import cl.duoc.pedidos360_backend.model.Pedido;
import cl.duoc.pedidos360_backend.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    // USER y ADMIN: ver sus propios pedidos
    @GetMapping("/mis-pedidos")
    public List<Pedido> misPedidos(@AuthenticationPrincipal Jwt jwt) {
        String oid = jwt.getClaimAsString("oid");
        return service.obtenerPorUsuario(oid);
    }

    // USER y ADMIN: crear pedido
    @PostMapping
    public Pedido crear(@RequestBody Pedido pedido,
                        @AuthenticationPrincipal Jwt jwt) {
        pedido.setUsuarioOid(jwt.getClaimAsString("oid"));
        return service.crear(pedido);
    }

    // ADMIN: ver todos los pedidos
    @GetMapping
    public List<Pedido> todos(@AuthenticationPrincipal Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");
        if (roles == null || !roles.contains("admin")) {
            throw new org.springframework.security.access.AccessDeniedException("Solo administradores");
        }
        return service.obtenerTodos();
    }

    // ADMIN: actualizar cualquier pedido
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizar(@PathVariable Long id,
                                             @RequestBody Pedido datos,
                                             @AuthenticationPrincipal Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");
        if (roles == null || !roles.contains("admin")) {
            return ResponseEntity.status(403).build();
        }
        return service.actualizar(id, datos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ADMIN: eliminar pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id,
                                         @AuthenticationPrincipal Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");
        if (roles == null || !roles.contains("admin")) {
            return ResponseEntity.status(403).build();
        }
        return service.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}