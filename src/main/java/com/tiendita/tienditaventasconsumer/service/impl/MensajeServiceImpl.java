package com.tiendita.tienditaventasconsumer.service.impl;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.rabbitmq.client.Channel;

import com.tiendita.tienditaventasconsumer.config.RabbitMQConfig;
import com.tiendita.tienditaventasconsumer.dto.StockUpdateDTO;
import com.tiendita.tienditaventasconsumer.service.MensajeService;
import com.tiendita.tienditaventasconsumer.service.ProductoService;

@Service
public class MensajeServiceImpl implements MensajeService {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    
    @Autowired
    private ProductoService productoService;

    public MensajeServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = new ObjectMapper();
    }

    @RabbitListener(id = "listener-ventas", queues = RabbitMQConfig.MAIN_QUEUE)
    @Override
    public void recibirMensaje(Message mensaje, Channel canal) {
        try {
            String mensajeJson = new String(mensaje.getBody());
            System.out.println("Mensaje recibido en ventas: " + mensajeJson);
            
            //mensaje de actualizacion de stock
            procesarActualizacionStock(mensajeJson);
            
        } catch (Exception e) {
            System.err.println("Error procesando mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void procesarActualizacionStock(String mensajeJson) {
        try {
            String jsonLimpio = mensajeJson.trim();
            
            if (jsonLimpio.startsWith("\"") && jsonLimpio.endsWith("\"")) {
                jsonLimpio = objectMapper.readValue(jsonLimpio, String.class);
            }
                      
            JsonNode jsonNode = objectMapper.readTree(jsonLimpio);
            
            if (!jsonNode.has("productoId") || !jsonNode.has("cantidad") || !jsonNode.has("usuarioId")) {
                System.out.println("Mensaje recibido no es una actualización de stock válida: " + jsonLimpio);
                return;
            }
            
            StockUpdateDTO stockUpdate = objectMapper.readValue(jsonLimpio, StockUpdateDTO.class);
            
            System.out.println("Procesando actualización de stock: " + stockUpdate);
            
            boolean resultado = false;
			resultado = productoService.reducirStock(stockUpdate.getProductoId(), stockUpdate.getCantidad());
            if (resultado) {
                System.out.println("Stock actualizado " + stockUpdate.getProductoId() + 
                                     ", cantidad: " + stockUpdate.getCantidad());
            } else {
                System.err.println("ERROR al actualizar stock " + stockUpdate.getProductoId() + 
                                     ". Stock insuficiente o producto no encontrado.");
            }

            
        } catch (Exception e) {
            System.err.println("Error parseando mensaje JSON: " + e.getMessage());
            System.out.println("Mensaje recibido (no es actualización de stock): " + mensajeJson);
        }
    }

    @RabbitListener(id = "listener-dlx-queue", queues = RabbitMQConfig.DLX_QUEUE)
    @Override
    public void recibirDeadLetter(Object objeto) {
        System.out.println("Mensaje recibido en DLQ: " + objeto);
    }
}
