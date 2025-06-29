package com.tiendita.tienditaventasconsumer.service;
import java.io.IOException;
import org.springframework.amqp.core.Message;
import com.rabbitmq.client.Channel;

public interface MensajeService {
    void recibirMensaje(Message mensaje, Channel canal);
    void recibirDeadLetter(Object mensaje);
    void procesarActualizacionStock(String mensajeJson);
}
