package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.entities.Owner;
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
 * Integration tests for Owner Controller
 * Ejercicio 2 - Laboratorio 11
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class OwnerControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test 1: Get all owners
     * @throws Exception
     */
    @Test
    public void testFindAllOwners() throws Exception {

        int ID_FIRST_RECORD = 1;

        this.mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));

        log.info("testFindAllOwners - OK");
    }

    /**
     * Test 2: Get owner by ID
     * @throws Exception
     */
    @Test
    public void testFindOwnerById() throws Exception {

        int OWNER_ID = 1;
        String FIRST_NAME = "George";
        String LAST_NAME = "Franklin";
        String ADDRESS = "110 W. Liberty St.";
        String CITY = "Madison";
        String TELEPHONE = "6085551023";

        mockMvc.perform(get("/owners/" + OWNER_ID))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(OWNER_ID)))
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)))
                .andExpect(jsonPath("$.address", is(ADDRESS)))
                .andExpect(jsonPath("$.city", is(CITY)))
                .andExpect(jsonPath("$.telephone", is(TELEPHONE)));

        log.info("testFindOwnerById - OK");
    }

    /**
     * Test 3: Create a new owner
     * @throws Exception
     */
    @Test
    public void testCreateOwner() throws Exception {

        String FIRST_NAME = "Carlos";
        String LAST_NAME = "Gomez";
        String ADDRESS = "Av. Siempre Viva 123";
        String CITY = "Lima";
        String TELEPHONE = "987654321";

        Owner newOwner = new Owner();
        newOwner.setFirstName(FIRST_NAME);
        newOwner.setLastName(LAST_NAME);
        newOwner.setAddress(ADDRESS);
        newOwner.setCity(CITY);
        newOwner.setTelephone(TELEPHONE);

        mockMvc.perform(post("/owners")
                        .content(om.writeValueAsString(newOwner))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)))
                .andExpect(jsonPath("$.address", is(ADDRESS)))
                .andExpect(jsonPath("$.city", is(CITY)))
                .andExpect(jsonPath("$.telephone", is(TELEPHONE)));

        log.info("testCreateOwner - OK");
    }

    /**
     * Test 4: Update an existing owner
     * @throws Exception
     */
    @Test
    public void testUpdateOwner() throws Exception {

        String FIRST_NAME = "Maria";
        String LAST_NAME = "Rodriguez";
        String ADDRESS = "Jr. Los Pinos 456";
        String CITY = "Arequipa";
        String TELEPHONE = "954123456";

        String UP_FIRST_NAME = "Maria Elena";
        String UP_LAST_NAME = "Rodriguez Perez";
        String UP_ADDRESS = "Jr. Los Robles 789";
        String UP_CITY = "Cusco";
        String UP_TELEPHONE = "984567890";

        // Paso 1: Crear un nuevo owner
        Owner newOwner = new Owner();
        newOwner.setFirstName(FIRST_NAME);
        newOwner.setLastName(LAST_NAME);
        newOwner.setAddress(ADDRESS);
        newOwner.setCity(CITY);
        newOwner.setTelephone(TELEPHONE);

        ResultActions mvcActions = mockMvc.perform(post("/owners")
                        .content(om.writeValueAsString(newOwner))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();

        // Paso 2: Extraer el ID del owner creado
        Integer id = JsonPath.parse(response).read("$.id");

        log.info(">>> Owner created with ID: " + id);

        // Paso 3: Actualizar el owner (SIN setId)
        Owner upOwner = new Owner();
        upOwner.setFirstName(UP_FIRST_NAME);
        upOwner.setLastName(UP_LAST_NAME);
        upOwner.setAddress(UP_ADDRESS);
        upOwner.setCity(UP_CITY);
        upOwner.setTelephone(UP_TELEPHONE);

        mockMvc.perform(put("/owners/" + id)
                        .content(om.writeValueAsString(upOwner))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(UP_FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(UP_LAST_NAME)))
                .andExpect(jsonPath("$.address", is(UP_ADDRESS)))
                .andExpect(jsonPath("$.city", is(UP_CITY)))
                .andExpect(jsonPath("$.telephone", is(UP_TELEPHONE)));

        log.info("testUpdateOwner - OK");
    }

    /**
     * Test 5: Delete an owner
     * @throws Exception
     */
    @Test
    public void testDeleteOwner() throws Exception {

        String FIRST_NAME = "Pedro";
        String LAST_NAME = "Silva";
        String ADDRESS = "Calle Falsa 123";
        String CITY = "Trujillo";
        String TELEPHONE = "944789012";

        // Paso 1: Crear un owner para eliminar
        Owner newOwner = new Owner();
        newOwner.setFirstName(FIRST_NAME);
        newOwner.setLastName(LAST_NAME);
        newOwner.setAddress(ADDRESS);
        newOwner.setCity(CITY);
        newOwner.setTelephone(TELEPHONE);

        ResultActions mvcActions = mockMvc.perform(post("/owners")
                        .content(om.writeValueAsString(newOwner))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();

        // Paso 2: Extraer el ID
        Integer id = JsonPath.parse(response).read("$.id");

        log.info(">>> Owner to delete with ID: " + id);

        // Paso 3: Eliminar el owner
        mockMvc.perform(delete("/owners/" + id))
                .andExpect(status().isOk());

        log.info("testDeleteOwner - OK");
    }
}