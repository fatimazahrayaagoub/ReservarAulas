
package es.iesjuanbosco.reservarsaula.entidad;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "franjas_horarias")
@NoArgsConstructor
public class FranjaHoraria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFranja;

    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalTime horaFin;

}
