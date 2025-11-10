package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;
import com.tecsup.petclinic.services.VisitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visits")
@Slf4j
public class VisitController {

    @Autowired
    private VisitService visitService;

    @GetMapping
    public ResponseEntity<List<Visit>> findAll() {
        List<Visit> visits = visitService.findAll();
        log.info("Visits found: " + visits.size());
        return ResponseEntity.ok(visits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Visit> findById(@PathVariable Long id) throws VisitNotFoundException {
        Visit visit = visitService.findById(id);
        log.info("Visit found: " + visit);
        return ResponseEntity.ok(visit);
    }

    @PostMapping
    public ResponseEntity<Visit> create(@RequestBody Visit visit) {
        Visit newVisit = visitService.create(visit);
        log.info("Visit created: " + newVisit);
        return ResponseEntity.status(HttpStatus.CREATED).body(newVisit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Visit> update(@PathVariable Long id, @RequestBody Visit visit) throws VisitNotFoundException {
        visit.setId(id);
        Visit updatedVisit = visitService.update(visit);
        log.info("Visit updated: " + updatedVisit);
        return ResponseEntity.ok(updatedVisit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws VisitNotFoundException {
        visitService.delete(id);
        log.info("Visit deleted with ID: " + id);
        return ResponseEntity.ok().build();
    }
}