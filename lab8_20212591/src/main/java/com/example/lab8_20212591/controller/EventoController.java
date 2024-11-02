package com.example.lab8_20212591.controller;

import com.example.lab8_20212591.entity.Evento;
import com.example.lab8_20212591.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    @Autowired
    private EventoRepository eventoRepository;

    @GetMapping("/listar")
    public List<Evento> listarEventos(@RequestParam(required = false) LocalDate fecha) {
        if (fecha != null) { //Si se desea utilizar el filtro (fecha ingresada en la URL)
            //No se entiende bien el enunciado porque menciona un filtro
            //Lo que hice fue que se ingrese una fecha y se ordene los eventos
            // a partir de esa fecha y los posteriores a esa fecha
            return eventoRepository.filtroFecha(fecha);
        }
        return eventoRepository.findAll();
    }

    @PostMapping("/crear")
    public ResponseEntity<HashMap<String, Object>> crearEvento(@RequestBody Evento evento,
                                                               @RequestParam(value = "fetchId", required = false) boolean fetchId) {
        HashMap<String, Object> response = new HashMap<>();

        if (evento.getFecha().isBefore(LocalDate.now())) {
            response.put("result","error");
            response.put("estado", "Error al crear el evento. La fecha ingresada debe ser futura");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        evento.setReservasactuales(0); // Aquí inicializamos un evento con 0 reservas =) (pq es nuevo)
        eventoRepository.save(evento);
        if (fetchId) {
            response.put("id", evento.getId());
        }
        response.put("estado", "Evento creado!");
        response.put("result","ok");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
