package com.tiendita.tienditaventasconsumer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StockUpdateDTO {
    @JsonProperty("productoId")
    private Long productoId;
    
    @JsonProperty("cantidad")
    private Integer cantidad;

    @JsonProperty("clienteId")
    private Long clienteId;

    @JsonProperty("carroId")
    private String carroId;
}