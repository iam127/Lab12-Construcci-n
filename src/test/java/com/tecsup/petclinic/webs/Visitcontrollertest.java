package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.entities.Visit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for Visit Controller
 * Ejercicio 4 - Laboratorio 11
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class VisitControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test 1: Get all visits
     */
    @Test
    public void testFindAllVisits() throws Exception {
        int ID_FIRST_RECORD = 1;

        this.mockMvc.perform(get("/visits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));

        log.info("testFindAllVisits - OK");
    }

    /**
     * Test 2: Get visit by ID
     */
    @Test
    public void testFindVisitById() throws Exception {
        Long VISIT_ID = 1L;
        String DESCRIPTION = "rabies shot";

        mockMvc.perform(get("/visits/" + VISIT_ID))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(VISIT_ID.intValue())))
                .andExpect(jsonPath("$.description", is(DESCRIPTION)));

        log.info("testFindVisitById - OK");
    }

    /**
     * Test 3: Create a new visit
     */
    @Test
    public void testCreateVisit() throws Exception {
        String DESCRIPTION = "checkup general";
        String VISIT_DATE = "2024-11-10";

        Visit newVisit = new Visit();
        newVisit.setDescription(DESCRIPTION);
        newVisit.setVisitDate(LocalDate.parse(VISIT_DATE));

        mockMvc.perform(post("/visits")
                        .content(om.writeValueAsString(newVisit))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description", is(DESCRIPTION)));

        log.info("testCreateVisit - OK");
    }

    /**
     * Test 4: Update an existing visit
     */
    @Test
    public void testUpdateVisit() throws Exception {
        String DESCRIPTION = "vaccination";
        String VISIT_DATE = "2024-11-10";

        String UP_DESCRIPTION = "vaccination and checkup";

        // Crear visit
        Visit newVisit = new Visit();
        newVisit.setDescription(DESCRIPTION);
        newVisit.setVisitDate(LocalDate.parse(VISIT_DATE));

        ResultActions mvcActions = mockMvc.perform(post("/visits")
                        .content(om.writeValueAsString(newVisit))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        log.info(">>> Visit created with ID: " + id);

        // Actualizar visit
        Visit upVisit = new Visit();
        upVisit.setDescription(UP_DESCRIPTION);
        upVisit.setVisitDate(LocalDate.parse(VISIT_DATE));

        mockMvc.perform(put("/visits/" + id)
                        .content(om.writeValueAsString(upVisit))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(UP_DESCRIPTION)));

        log.info("testUpdateVisit - OK");
    }

    /**
     * Test 5: Delete a visit
     */
    @Test
    public void testDeleteVisit() throws Exception {
        String DESCRIPTION = "dental cleaning";
        String VISIT_DATE = "2024-11-10";

        // Crear visit
        Visit newVisit = new Visit();
        newVisit.setDescription(DESCRIPTION);
        newVisit.setVisitDate(LocalDate.parse(VISIT_DATE));

        ResultActions mvcActions = mockMvc.perform(post("/visits")
                        .content(om.writeValueAsString(newVisit))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        log.info(">>> Visit to delete with ID: " + id);

        // Eliminar visit
        mockMvc.perform(delete("/visits/" + id))
                .andExpect(status().isOk());

        log.info("testDeleteVisit - OK");
    }
}