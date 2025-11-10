package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.entities.Vet;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for VetController
 */
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class VetControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test para obtener todos los veterinarios
     */
    @Test
    public void testFindAllVets() throws Exception {
        final int ID_FIRST_RECORD = 1;

        this.mockMvc.perform(get("/vets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));
    }

    /**
     * Test para encontrar un veterinario por ID - Caso exitoso
     */
    @Test
    public void testFindVetOK() throws Exception {
        int VET_ID = 1;

        this.mockMvc.perform(get("/vets/" + VET_ID))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(VET_ID)))
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.lastName").exists());
    }

    /**
     * Test para encontrar un veterinario por ID - Caso no encontrado
     */
    @Test
    public void testFindVetKO() throws Exception {
        mockMvc.perform(get("/vets/999"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test para crear un nuevo veterinario
     */
    @Test
    public void testCreateVet() throws Exception {
        String FIRST_NAME = "Juan";
        String LAST_NAME = "Gomez";

        Vet newVet = new Vet();
        newVet.setFirstName(FIRST_NAME);
        newVet.setLastName(LAST_NAME);

        this.mockMvc.perform(post("/vets")
                        .content(om.writeValueAsString(newVet))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)))
                .andExpect(jsonPath("$.id").exists());
    }

    /**
     * Test para eliminar un veterinario
     */
    @Test
    public void testDeleteVet() throws Exception {
        String FIRST_NAME = "Carlos";
        String LAST_NAME = "Perez";

        Vet newVet = new Vet();
        newVet.setFirstName(FIRST_NAME);
        newVet.setLastName(LAST_NAME);

        // Crear el veterinario
        ResultActions mvcActions = mockMvc.perform(post("/vets")
                        .content(om.writeValueAsString(newVet))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        // Eliminar el veterinario
        mockMvc.perform(delete("/vets/" + id))
                .andExpect(status().isOk());
    }

    /**
     * Test para eliminar un veterinario que no existe
     */
    @Test
    public void testDeleteVetKO() throws Exception {
        mockMvc.perform(delete("/vets/9999"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test para actualizar un veterinario
     */
    @Test
    public void testUpdateVet() throws Exception {
        String FIRST_NAME = "Maria";
        String LAST_NAME = "Lopez";
        String UP_FIRST_NAME = "Maria Elena";
        String UP_LAST_NAME = "Lopez Torres";

        Vet newVet = new Vet();
        newVet.setFirstName(FIRST_NAME);
        newVet.setLastName(LAST_NAME);

        // CREATE
        ResultActions mvcActions = mockMvc.perform(post("/vets")
                        .content(om.writeValueAsString(newVet))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        // UPDATE
        Vet upVet = new Vet();
        upVet.setId(id);
        upVet.setFirstName(UP_FIRST_NAME);
        upVet.setLastName(UP_LAST_NAME);

        mockMvc.perform(put("/vets/" + id)
                        .content(om.writeValueAsString(upVet))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // FIND - Verificar la actualización
        mockMvc.perform(get("/vets/" + id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.firstName", is(UP_FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(UP_LAST_NAME)));

        // DELETE - Limpiar
        mockMvc.perform(delete("/vets/" + id))
                .andExpect(status().isOk());
    }
}