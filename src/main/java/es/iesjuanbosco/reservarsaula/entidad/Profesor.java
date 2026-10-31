package es.iesjuanbosco.reservarsaula.entidad;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Profesor {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProfesor;

    @Column(nullable = false,length = 50)
    private String name;

    @Column(length = 50,nullable = false)
    private String apellido;

    @Column(length = 200,nullable = false)
    private String email;


    @Column(nullable=false)
    private LocalDateTime fechaNaci;

    @Column(length = 9,nullable = false)
    private String dni;

    @Column(length=100,nullable = false)
    private String telefono;




}
