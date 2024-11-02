package com.example.lab8_20212591.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name="Evento")
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEvento", nullable = false)
    private Integer id;

    @Column(name = "nombreEvento", length = 45)
    private String nombre;

    @Column(name = "fechaEvento")
    private LocalDate fecha;

    @Column(name = "categoria", length = 45)
    private String categoria;

    @Column(name="capacidadMax")
    private Integer capacidadmaxima;

    @Column(name="reservasAct")
    private int reservasactuales;
}
