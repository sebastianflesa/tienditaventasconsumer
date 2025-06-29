package com.tiendita.tienditaventasconsumer.repository;
import com.tiendita.tienditaventasconsumer.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
}
