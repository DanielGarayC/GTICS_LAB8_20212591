package com.example.lab8_20212591.controller;

import com.example.lab8_20212591.entity.Evento;
import com.example.lab8_20212591.entity.Reserva;
import com.example.lab8_20212591.repository.EventoRepository;
import com.example.lab8_20212591.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
public class ReservaController {
    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @PostMapping("/reservar")
    public ResponseEntity<HashMap<String, Object>> reservarCupos(@RequestBody Reserva reserva) {

        HashMap<String, Object> response = new HashMap<>();

        // Evento por su id
        Optional<Evento> optionalEvento = eventoRepository.findById(reserva.getEvento().getId());
        //Validación de la existencia del evento
        if (!optionalEvento.isPresent()) {
            response.put("result", "error");
            response.put("msg", "Evento no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Evento evento = optionalEvento.get();

        // Validación para los cupos disponibles
        if (evento.getReservasactuales() + reserva.getCupos() > evento.getCapacidadmaxima()) {
            response.put("result", "error");
            response.put("msg", "No hay cupos suficientes disponibles.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Actualizamos las reservas del evento
        evento.setReservasactuales(evento.getReservasactuales() + reserva.getCupos());
        eventoRepository.save(evento);

        reserva.setNombre(reserva.getNombre());
        reserva.setCorreo(reserva.getCorreo());
        reserva.setCupos(reserva.getCupos());
        reserva.setEvento(evento);

        Reserva nuevaReserva = reservaRepository.save(reserva);

        response.put("estado", "Reserva creada!");
        response.put("reservaId", nuevaReserva.getId());
        response.put("nombre", nuevaReserva.getNombre());
        response.put("correo", nuevaReserva.getCorreo());
        response.put("cupos", nuevaReserva.getCupos());
        response.put("eventoId", evento.getId());
        response.put("result", "ok");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/cancelar/{id}")
    public ResponseEntity<HashMap<String, Object>> cancelarReserva(@PathVariable("id") int reservaId) {
        HashMap<String, Object> response = new HashMap<>();

        // Reserva por su id
        Optional<Reserva> optionalReserva = reservaRepository.findById(reservaId);
        if (!optionalReserva.isPresent()) {
            response.put("result", "error");
            response.put("msg", "Reserva no encontrada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Reserva reserva = optionalReserva.get();
        Evento evento = reserva.getEvento();

        // Actualización del número de reservas actuales en el evento
        evento.setReservasactuales(evento.getReservasactuales() - reserva.getCupos());
        eventoRepository.save(evento);

        // Ahora si c elimina la reserva =)
        reservaRepository.delete(reserva);

        response.put("estado", "Reserva cancelada!");
        response.put("reservaId", reservaId);
        response.put("eventoId", evento.getId());
        response.put("cuposLiberados", reserva.getCupos());
        response.put("result", "ok");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
