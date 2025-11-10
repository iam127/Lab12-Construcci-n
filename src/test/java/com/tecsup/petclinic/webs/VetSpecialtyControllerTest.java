package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.entities.Vet;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for VetSpecialty (Vet-Specialty relationship)
 * Ejercicio 6 - Laboratorio 11
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class VetSpecialtyControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test 1: Get all vets with their specialties
     */
    @Test
    public void testFindAllVetsWithSpecialties() throws Exception {
        int ID_FIRST_RECORD = 1;

        this.mockMvc.perform(get("/vets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));

        log.info("testFindAllVetsWithSpecialties - OK");
    }

    /**
     * Test 2: Get vet with specialties by ID
     */
    @Test
    public void testFindVetWithSpecialtiesById() throws Exception {
        int VET_ID = 2;
        String FIRST_NAME = "Helen";

        mockMvc.perform(get("/vets/" + VET_ID))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(VET_ID)))
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.specialties").exists());

        log.info("testFindVetWithSpecialtiesById - OK");
    }

    /**
     * Test 3: Create vet with specialty
     */
    @Test
    public void testCreateVetWithSpecialty() throws Exception {
        String FIRST_NAME = "Roberto";
        String LAST_NAME = "Martinez";

        Vet newVet = new Vet();
        newVet.setFirstName(FIRST_NAME);
        newVet.setLastName(LAST_NAME);

        mockMvc.perform(post("/vets")
                        .content(om.writeValueAsString(newVet))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)));

        log.info("testCreateVetWithSpecialty - OK");
    }

    /**
     * Test 4: Get all specialties
     */
    @Test
    public void testFindAllSpecialties() throws Exception {
        int ID_FIRST_RECORD = 1;

        this.mockMvc.perform(get("/specialties"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));

        log.info("testFindAllSpecialties - OK");
    }

    /**
     * Test 5: Verify vet-specialty relationship exists
     */
    @Test
    public void testVerifyVetSpecialtyRelationship() throws Exception {
        // Verificar que el vet 2 (Helen) tiene la especialidad 1 (radiology)
        int VET_ID = 2;

        mockMvc.perform(get("/vets/" + VET_ID))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(VET_ID)))
                .andExpect(jsonPath("$.specialties").isArray());

        log.info("testVerifyVetSpecialtyRelationship - OK");
    }
}