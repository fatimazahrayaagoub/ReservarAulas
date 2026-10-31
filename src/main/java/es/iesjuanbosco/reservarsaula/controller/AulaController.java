
package es.iesjuanbosco.reservarsaula.controller;


import es.iesjuanbosco.reservarsaula.entidad.Aula;
import es.iesjuanbosco.reservarsaula.repository.AulaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/aulas")
@CrossOrigin(origins = "*")
public class AulaController {
    @Autowired
    private AulaRepository aulaRepository;

    @GetMapping
    public ResponseEntity<List<Aula>>listarAulas(){
        List<Aula> aulas =aulaRepository.findAll();
        return ResponseEntity.ok(aulas);
    }

    @GetMapping("{id}")
    public ResponseEntity<Aula> listarPorId(@PathVariable Long id){
        Aula aula=aulaRepository.findById(id).orElse(null);
        if(aula==null){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(aula);
    }

    @PostMapping
    public ResponseEntity<Aula> insertarAula(@RequestBody Aula aula) {
        aula.setIdAula(null);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(aulaRepository.save(aula));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aula> actualizarAulaPorId(
            @PathVariable Long id,
            @RequestBody Aula aula) {

        if (!aulaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        aula.setIdAula(id);

        return ResponseEntity.ok(aulaRepository.save(aula));
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminarTodo(){
        aulaRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Aula> eliminarPorId(@PathVariable Long id){
        Aula aula=aulaRepository.findById(id).orElse(null);
        if(aula==null){
            return  ResponseEntity.notFound().build();
        }
        aulaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ordenadores")
    public ResponseEntity<List<Aula> >findByOrdenadores(@RequestParam boolean ordenadores){
       List<Aula> aulas =aulaRepository.findByOrdenadores(ordenadores);
       return ResponseEntity.ok(aulas);
    }

    @GetMapping("/capacidad")
    public  ResponseEntity<List<Aula>> findByCapacidadGreaterThan(@RequestParam int capacidad){
        List<Aula> aulas=aulaRepository.findByCapacidadGreaterThan(capacidad);
        return ResponseEntity.ok(aulas);
    }




}
