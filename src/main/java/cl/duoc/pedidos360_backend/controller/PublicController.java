package cl.duoc.pedidos360_backend.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {

    @GetMapping("/hola")
    public Map<String, String> hola() {
        return Map.of(
                "mensaje", "API pública de Pedidos360: no se requiere token.",
                "sistema", "Pedidos360",
                "recurso", "/public/hola"
        );
    }
}