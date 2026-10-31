package es.iesjuanbosco.reservarsaula.controller;


import es.iesjuanbosco.reservarsaula.entidad.Profesor;
import es.iesjuanbosco.reservarsaula.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profesores")
@CrossOrigin(origins = "*")
public class ProfesorController {
    @Autowired
    private ProfesorRepository profesorRepository;


    @GetMapping
    public ResponseEntity<List<Profesor>> listarTodo() {
        List<Profesor> profesores = profesorRepository.findAll();
        return  ResponseEntity.ok(profesores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profesor> listarPorId(@PathVariable Long id) {
        return profesorRepository.findById(id).
                map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminarTodo() {
        profesorRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Profesor> eliminarPorId(@PathVariable Long id){
        return profesorRepository.findById(id).map(
                profesor -> {
                    profesorRepository.deleteById(id);
                    return ResponseEntity.noContent().<Profesor>build();
                }

        ).orElseGet(()->ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Profesor> insertProfesor(@RequestBody Profesor profesor) {
        profesor.setIdProfesor(null);
        Profesor profesorNuevo= profesorRepository.save(profesor);
        return ResponseEntity.
                status(HttpStatus.CREATED).body(profesorNuevo);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Profesor> actualizarProfesorPorId(@PathVariable Long id, @RequestBody Profesor profesor) {

        return profesorRepository.findById(id).
                map(profesor1 -> {
                    profesor1.setName(profesor.getName());
                    profesor1.setApellido(profesor.getApellido());
                    profesor1.setDni(profesor.getDni());
                    profesor1.setFechaNaci(profesor.getFechaNaci());
                    profesor1.setEmail(profesor.getEmail());
                    profesor1.setTelefono(profesor.getTelefono());
                    Profesor nuevo=profesorRepository.save(profesor1);
                    return ResponseEntity.ok(nuevo);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
