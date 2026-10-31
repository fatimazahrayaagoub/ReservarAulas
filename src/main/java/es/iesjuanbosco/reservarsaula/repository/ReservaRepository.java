package es.iesjuanbosco.reservarsaula.repository;

import es.iesjuanbosco.reservarsaula.entidad.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository  extends JpaRepository<Reserva,Long> {
    boolean  existsByFechaAndAula_IdAulaAndFranjaHoraria_IdFranja(java.time.LocalDate fecha, Long aulaId, Long franjaId);
    boolean existsByFechaAndAula_IdAulaAndFranjaHoraria_IdFranjaAndIdNot(java.time.LocalDate fecha, Long aulaId, Long franjaId ,Long idReserva);
}
