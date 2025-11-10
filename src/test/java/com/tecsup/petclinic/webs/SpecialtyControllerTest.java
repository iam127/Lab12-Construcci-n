package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.entities.Specialty;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for Specialty Controller
 * Ejercicio 3 - Laboratorio 11
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class SpecialtyControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test 1: Get all specialties
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
     * Test 2: Get specialty by ID
     */
    @Test
    public void testFindSpecialtyById() throws Exception {
        int SPECIALTY_ID = 1;
        String NAME = "radiology";

        mockMvc.perform(get("/specialties/" + SPECIALTY_ID))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(SPECIALTY_ID)))
                .andExpect(jsonPath("$.name", is(NAME)));

        log.info("testFindSpecialtyById - OK");
    }

    /**
     * Test 3: Create a new specialty
     */
    @Test
    public void testCreateSpecialty() throws Exception {
        String NAME = "cardiology";

        Specialty newSpecialty = new Specialty();
        newSpecialty.setName(NAME);

        mockMvc.perform(post("/specialties")
                        .content(om.writeValueAsString(newSpecialty))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(NAME)));

        log.info("testCreateSpecialty - OK");
    }

    /**
     * Test 4: Update an existing specialty
     */
    @Test
    public void testUpdateSpecialty() throws Exception {
        String NAME = "neurology";
        String UP_NAME = "neurosurgery";

        // Crear specialty
        Specialty newSpecialty = new Specialty();
        newSpecialty.setName(NAME);

        ResultActions mvcActions = mockMvc.perform(post("/specialties")
                        .content(om.writeValueAsString(newSpecialty))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        log.info(">>> Specialty created with ID: " + id);

        // Actualizar specialty
        Specialty upSpecialty = new Specialty();
        upSpecialty.setName(UP_NAME);

        mockMvc.perform(put("/specialties/" + id)
                        .content(om.writeValueAsString(upSpecialty))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(UP_NAME)));

        log.info("testUpdateSpecialty - OK");
    }

    /**
     * Test 5: Delete a specialty
     */
    @Test
    public void testDeleteSpecialty() throws Exception {
        String NAME = "ophthalmology";

        // Crear specialty
        Specialty newSpecialty = new Specialty();
        newSpecialty.setName(NAME);

        ResultActions mvcActions = mockMvc.perform(post("/specialties")
                        .content(om.writeValueAsString(newSpecialty))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        log.info(">>> Specialty to delete with ID: " + id);

        // Eliminar specialty
        mockMvc.perform(delete("/specialties/" + id))
                .andExpect(status().isOk());

        log.info("testDeleteSpecialty - OK");
    }
}mvn test -Dtest=SpecialtyControllerTest
