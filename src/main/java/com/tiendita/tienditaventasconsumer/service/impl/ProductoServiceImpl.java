package com.tiendita.tienditaventasconsumer.service.impl;

import com.tiendita.tienditaventasconsumer.models.Producto;
import com.tiendita.tienditaventasconsumer.repository.ProductoRepository;
import com.tiendita.tienditaventasconsumer.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public boolean actualizarStock(Long productoId, Integer nuevoStock) {
        Optional<Producto> productoOpt = productoRepository.findById(productoId);
        
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setStock(nuevoStock);
            productoRepository.save(producto);
            return true;
        }
        return false;
    }

    @Override
    public boolean reducirStock(Long productoId, Integer cantidad) {
        Optional<Producto> productoOpt = productoRepository.findById(productoId);
        
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            Integer stockActual = producto.getStock();
            
            if (stockActual >= cantidad) {
                producto.setStock(stockActual - cantidad);
                productoRepository.save(producto);
                return true;
            }
        }
        return false;
    }

    @Override
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}