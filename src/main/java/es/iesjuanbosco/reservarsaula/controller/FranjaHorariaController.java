package es.iesjuanbosco.reservarsaula.controller;

import es.iesjuanbosco.reservarsaula.entidad.FranjaHoraria;
import es.iesjuanbosco.reservarsaula.repository.FranjaHorariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/franjasHorarias")
@CrossOrigin(origins = "*")
public class FranjaHorariaController {
    @Autowired
    FranjaHorariaRepository franjaHorariaRepository;

    @GetMapping
    public ResponseEntity<List<FranjaHoraria>> listarFranjas(){
        return ResponseEntity.ok(franjaHorariaRepository.findAll());
    }

    @GetMapping("{id}")
    public  ResponseEntity<FranjaHoraria> listarFranjaPorId(@PathVariable Long id){
        return franjaHorariaRepository.findById(id).map(ResponseEntity::ok).
                orElseGet(()-> ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<FranjaHoraria> insertarFranjaHoraria(@RequestBody FranjaHoraria franjaHoraria){
        franjaHoraria.setIdFranja(null);
        FranjaHoraria franjaHoraria1=franjaHorariaRepository.save(franjaHoraria);
        return ResponseEntity.status(HttpStatus.CREATED).body(franjaHoraria1);
    }

    @PutMapping("{id}")
    public ResponseEntity<FranjaHoraria> actulizarFranja(@PathVariable Long id,@RequestBody FranjaHoraria franjaHoraria){
        FranjaHoraria franjaHoraria1=franjaHorariaRepository.findById(id).orElse(null);
        if (franjaHoraria1==null){
            return  ResponseEntity.notFound().build();
        }
        franjaHoraria1.setHoraInicio(franjaHoraria.getHoraInicio());
        franjaHoraria1.setHoraFin(franjaHoraria.getHoraFin());
        FranjaHoraria franjaHoraria2=franjaHorariaRepository.save(franjaHoraria1);

        return ResponseEntity.ok(franjaHoraria2);
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminarTodo(){
        franjaHorariaRepository.deleteAll();
        return  ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<FranjaHoraria> eliminarPorId(@PathVariable Long id){
        FranjaHoraria franjaHoraria=franjaHorariaRepository.findById(id).orElse(null);
        if(franjaHoraria==null){
            return  ResponseEntity.notFound().build();
        }
        franjaHorariaRepository.deleteById(id);
        return ResponseEntity.noContent().build();

    }
}
