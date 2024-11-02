package com.example.lab8_20212591.repository;

import com.example.lab8_20212591.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Integer> {
    @Query("SELECT e FROM Evento e WHERE e.fecha >= :fecha ORDER BY e.fecha ASC")
    List<Evento> filtroFecha(@Param("fecha") LocalDate fecha);
}
