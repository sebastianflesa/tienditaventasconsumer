package com.tiendita.tienditaventasconsumer.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiendita.tienditaventasconsumer.dto.StockUpdateDTO;
import com.tiendita.tienditaventasconsumer.models.Producto;
import com.tiendita.tienditaventasconsumer.repository.ProductoRepository;
import com.tiendita.tienditaventasconsumer.service.MensajeService;
import com.tiendita.tienditaventasconsumer.service.ProductoService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.tiendita.tienditaventasconsumer.config.RabbitMQConfig;

@RestController
@RequestMapping("/api")
public class MensajeController {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private ProductoService productoService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final MensajeService mensajeService;
    private final ObjectMapper objectMapper;
    
    public MensajeController(MensajeService mensajeService) {
        this.mensajeService = mensajeService;
        this.objectMapper = new ObjectMapper();
    }

    @GetMapping("")
    public ResponseEntity<String> index() {
        List<Producto> productos = productoRepository.findAll();
        Boolean hayProductos = !productos.isEmpty();
        if (!hayProductos) {
            return ResponseEntity.ok("No hay productos disponibles.");
        }

        return ResponseEntity.ok("Microservicio de ventas API, consumer de mensajes RabbitMQ. Conexión BD: " + hayProductos);
    }

    
}
