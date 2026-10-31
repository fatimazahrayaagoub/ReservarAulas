package es.iesjuanbosco.reservarsaula.controller;


import es.iesjuanbosco.reservarsaula.entidad.*;
import es.iesjuanbosco.reservarsaula.repository.AulaRepository;
import es.iesjuanbosco.reservarsaula.repository.FranjaHorariaRepository;
import es.iesjuanbosco.reservarsaula.repository.ProfesorRepository;
import es.iesjuanbosco.reservarsaula.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private ProfesorRepository profesorRepository;
    @Autowired
    private AulaRepository aulaRepository;
    @Autowired
    private FranjaHorariaRepository franjaHorariaRepository;

    @GetMapping
    public ResponseEntity<List<Reserva>> listarReservas(){
        List<Reserva> reservas =reservaRepository.findAll();

        return  ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> listarReservaPorid(@PathVariable Long id){
        return reservaRepository.findById(id).map(ResponseEntity::ok).
                orElseGet(()-> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<Reserva> insertarReserva(@RequestBody ReservaRequest datos) {

        if (datos.fecha() == null
                || datos.profesorId() == null
                || datos.aulaId() == null
                || datos.franjaId() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (datos.fecha().isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().build();
        }

        Profesor profesor = profesorRepository.findById(datos.profesorId()).orElse(null);
        Aula aula = aulaRepository.findById(datos.aulaId()).orElse(null);
        FranjaHoraria franja = franjaHorariaRepository.findById(datos.franjaId()).orElse(null);

        if (profesor == null || aula == null || franja == null) {
            return ResponseEntity.notFound().build();
        }

        boolean ocupada = reservaRepository
                .existsByFechaAndAula_IdAulaAndFranjaHoraria_IdFranja(
                        datos.fecha(), aula.getIdAula(), franja.getIdFranja());

        if (ocupada) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setFecha(datos.fecha());
        nuevaReserva.setProfesor(profesor);
        nuevaReserva.setAula(aula);
        nuevaReserva.setFranjaHoraria(franja);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservaRepository.save(nuevaReserva));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Reserva> actualizarReserva(@PathVariable Long id, @RequestBody Reserva reserva) {

        // Comprobamos que existe la reserva
        Reserva reservaActual = reservaRepository.findById(id).orElse(null);

        if (reservaActual == null) {
            return ResponseEntity.notFound().build();
        }

        // Comprobamos los datos recibidos
        if (reserva.getFecha() == null
                || reserva.getProfesor() == null
                || reserva.getProfesor().getIdProfesor() == null
                || reserva.getAula() == null
                || reserva.getAula().getIdAula() == null
                || reserva.getFranjaHoraria() == null
                || reserva.getFranjaHoraria().getIdFranja() == null) {

            return ResponseEntity.badRequest().build();
        }

        // Comprobamos que la fecha no sea anterior a hoy
        if (reserva.getFecha().isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().build();
        }

        Profesor profesor = profesorRepository
                .findById(reserva.getProfesor().getIdProfesor()).orElse(null);

        Aula aula = aulaRepository
                .findById(reserva.getAula().getIdAula()).orElse(null);

        FranjaHoraria franja = franjaHorariaRepository
                .findById(reserva.getFranjaHoraria().getIdFranja()).orElse(null);

        if (profesor == null || aula == null || franja == null) {
            return ResponseEntity.notFound().build();
        }

        // Comprobamos que no esté ocupada por OTRA reserva
        boolean ocupada = reservaRepository
                .existsByFechaAndAula_IdAulaAndFranjaHoraria_IdFranjaAndIdNot(
                        reserva.getFecha(),
                        aula.getIdAula(),
                        franja.getIdFranja(),
                        id
                );

        if (ocupada) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Actualizamos los datos
        reservaActual.setFecha(reserva.getFecha());
        reservaActual.setProfesor(profesor);
        reservaActual.setAula(aula);
        reservaActual.setFranjaHoraria(franja);

        return ResponseEntity.ok(reservaRepository.save(reservaActual));


}

    @DeleteMapping
    public ResponseEntity<Reserva> eliminarTodo(){
        reservaRepository.deleteAll();
        return  ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Reserva> eliminarPorId(@PathVariable Long id){
        return reservaRepository.findById(id).map(
                reserva -> {
                    reservaRepository.deleteById(id);
                    return ResponseEntity.noContent().<Reserva>build();
                }
        ).orElseGet(()-> ResponseEntity.notFound().build());
    }
}
