
package es.iesjuanbosco.reservarsaula.entidad;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAula;

    @Column( length = 50, nullable = false)
    private String nombre;

    @Column(nullable = false)
    private int capacidad;

    @Column(nullable = false)
    private boolean ordenadores;

}
