package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.services.OwnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owners")
@Slf4j
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @GetMapping
    public ResponseEntity<List<Owner>> findAll() {
        List<Owner> owners = ownerService.findAll();
        log.info("Owners found: " + owners.size());
        return ResponseEntity.ok(owners);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Owner> findById(@PathVariable Long id) throws OwnerNotFoundException {
        Owner owner = ownerService.findById(id);
        log.info("Owner found: " + owner);
        return ResponseEntity.ok(owner);
    }

    @PostMapping
    public ResponseEntity<Owner> create(@RequestBody Owner owner) {
        Owner newOwner = ownerService.create(owner);
        log.info("Owner created: " + newOwner);
        return ResponseEntity.status(HttpStatus.CREATED).body(newOwner);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Owner> update(@PathVariable Long id, @RequestBody Owner owner) throws OwnerNotFoundException {
        // Asignar el ID del path al owner
        owner.setId(id);
        Owner updatedOwner = ownerService.update(owner);
        log.info("Owner updated: " + updatedOwner);
        return ResponseEntity.ok(updatedOwner);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws OwnerNotFoundException {
        ownerService.delete(id);
        log.info("Owner deleted with ID: " + id);
        return ResponseEntity.ok().build();
    }
}