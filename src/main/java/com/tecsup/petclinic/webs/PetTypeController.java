package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.entities.PetType;
import com.tecsup.petclinic.exceptions.PetTypeNotFoundException;
import com.tecsup.petclinic.services.PetTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/types")
@Slf4j
public class PetTypeController {

    @Autowired
    private PetTypeService petTypeService;

    @GetMapping
    public ResponseEntity<List<PetType>> findAll() {
        List<PetType> types = petTypeService.findAll();
        log.info("PetTypes found: " + types.size());
        return ResponseEntity.ok(types);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetType> findById(@PathVariable Integer id) throws PetTypeNotFoundException {
        PetType petType = petTypeService.findById(id);
        log.info("PetType found: " + petType);
        return ResponseEntity.ok(petType);
    }

    @PostMapping
    public ResponseEntity<PetType> create(@RequestBody PetType petType) {
        PetType newPetType = petTypeService.create(petType);
        log.info("PetType created: " + newPetType);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPetType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetType> update(@PathVariable Integer id, @RequestBody PetType petType) throws PetTypeNotFoundException {
        petType.setId(id);
        PetType updatedPetType = petTypeService.update(petType);
        log.info("PetType updated: " + updatedPetType);
        return ResponseEntity.ok(updatedPetType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws PetTypeNotFoundException {
        petTypeService.delete(id);
        log.info("PetType deleted with ID: " + id);
        return ResponseEntity.ok().build();
    }
}