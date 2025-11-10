package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.services.SpecialtyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/specialties")
@Slf4j
public class SpecialtyController {

    @Autowired
    private SpecialtyService specialtyService;

    @GetMapping
    public ResponseEntity<List<Specialty>> findAll() {
        List<Specialty> specialties = specialtyService.findAll();
        log.info("Specialties found: " + specialties.size());
        return ResponseEntity.ok(specialties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Specialty> findById(@PathVariable Integer id) throws SpecialtyNotFoundException {
        Specialty specialty = specialtyService.findById(id);
        log.info("Specialty found: " + specialty);
        return ResponseEntity.ok(specialty);
    }

    @PostMapping
    public ResponseEntity<Specialty> create(@RequestBody Specialty specialty) {
        Specialty newSpecialty = specialtyService.create(specialty);
        log.info("Specialty created: " + newSpecialty);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSpecialty);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Specialty> update(@PathVariable Integer id, @RequestBody Specialty specialty) throws SpecialtyNotFoundException {
        specialty.setId(id);
        Specialty updatedSpecialty = specialtyService.update(specialty);
        log.info("Specialty updated: " + updatedSpecialty);
        return ResponseEntity.ok(updatedSpecialty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws SpecialtyNotFoundException {
        specialtyService.delete(id);
        log.info("Specialty deleted with ID: " + id);
        return ResponseEntity.ok().build();
    }
}