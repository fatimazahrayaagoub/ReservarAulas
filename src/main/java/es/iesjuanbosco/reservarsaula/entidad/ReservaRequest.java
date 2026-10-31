
package es.iesjuanbosco.reservarsaula.entidad;

import java.time.LocalDate;

public record ReservaRequest(
        LocalDate fecha,
        Long profesorId,
        Long aulaId,
        Long franjaId
) {}
