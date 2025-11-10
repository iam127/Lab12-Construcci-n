package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.entities.PetType;
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
 * Integration tests for PetType Controller
 * Ejercicio 5 - Laboratorio 11
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class PetTypeControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test 1: Get all pet types
     */
    @Test
    public void testFindAllPetTypes() throws Exception {
        int ID_FIRST_RECORD = 1;

        this.mockMvc.perform(get("/types"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));

        log.info("testFindAllPetTypes - OK");
    }

    /**
     * Test 2: Get pet type by ID
     */
    @Test
    public void testFindPetTypeById() throws Exception {
        int TYPE_ID = 1;
        String NAME = "cat";

        mockMvc.perform(get("/types/" + TYPE_ID))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(TYPE_ID)))
                .andExpect(jsonPath("$.name", is(NAME)));

        log.info("testFindPetTypeById - OK");
    }

    /**
     * Test 3: Create a new pet type
     */
    @Test
    public void testCreatePetType() throws Exception {
        String NAME = "guinea pig";

        PetType newPetType = new PetType();
        newPetType.setName(NAME);

        mockMvc.perform(post("/types")
                        .content(om.writeValueAsString(newPetType))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(NAME)));

        log.info("testCreatePetType - OK");
    }

    /**
     * Test 4: Update an existing pet type
     */
    @Test
    public void testUpdatePetType() throws Exception {
        String NAME = "parrot";
        String UP_NAME = "macaw";

        // Crear pet type
        PetType newPetType = new PetType();
        newPetType.setName(NAME);

        ResultActions mvcActions = mockMvc.perform(post("/types")
                        .content(om.writeValueAsString(newPetType))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        log.info(">>> PetType created with ID: " + id);

        // Actualizar pet type
        PetType upPetType = new PetType();
        upPetType.setName(UP_NAME);

        mockMvc.perform(put("/types/" + id)
                        .content(om.writeValueAsString(upPetType))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(UP_NAME)));

        log.info("testUpdatePetType - OK");
    }

    /**
     * Test 5: Delete a pet type
     */
    @Test
    public void testDeletePetType() throws Exception {
        String NAME = "fish";

        // Crear pet type
        PetType newPetType = new PetType();
        newPetType.setName(NAME);

        ResultActions mvcActions = mockMvc.perform(post("/types")
                        .content(om.writeValueAsString(newPetType))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        log.info(">>> PetType to delete with ID: " + id);

        // Eliminar pet type
        mockMvc.perform(delete("/types/" + id))
                .andExpect(status().isOk());

        log.info("testDeletePetType - OK");
    }
}
