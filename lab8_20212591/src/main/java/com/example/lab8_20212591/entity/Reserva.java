package com.example.lab8_20212591.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="Reserva")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idReserva", nullable = false)
    private Integer id;

    @Column(name = "nombrePersona", length = 45)
    private String nombre;

    @Column(name = "correoPersona", length = 45)
    private String correo;

    @Column(name = "cuposreserva", nullable = false)
    private Integer cupos;

    @ManyToOne
    @JoinColumn(name = "idevento")
    private Evento evento;
}
