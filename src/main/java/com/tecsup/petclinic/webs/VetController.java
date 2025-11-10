package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.services.VetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vets")
@Slf4j
public class VetController {

    @Autowired
    private VetService vetService;

    @GetMapping
    public ResponseEntity<List<Vet>> findAllVets() {
        List<Vet> vets = vetService.findAll();
        return ResponseEntity.ok(vets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vet> findVetById(@PathVariable Integer id) {
        try {
            Vet vet = vetService.findById(id);
            return ResponseEntity.ok(vet);
        } catch (VetNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Vet> createVet(@RequestBody Vet vet) {
        Vet newVet = vetService.create(vet);
        return ResponseEntity.status(HttpStatus.CREATED).body(newVet);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vet> updateVet(@PathVariable Integer id, @RequestBody Vet vet) {
        try {
            Vet updatedVet = vetService.update(vet);
            return ResponseEntity.ok(updatedVet);
        } catch (VetNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVet(@PathVariable Integer id) {
        try {
            vetService.delete(id);
            return ResponseEntity.ok().build();
        } catch (VetNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}