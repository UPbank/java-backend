package pt.ualg.upbank.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import pt.ualg.upbank.config.BaseIT;


public class CardResourceTest extends BaseIT {

    @Test
    @Sql("/data/cardData.sql")
    public void getAllCards_success() throws Exception {
        mockMvc.perform(get("/api/cards")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(((long)1400)));
    }

    @Test
    @Sql("/data/cardData.sql")
    public void getCard_success() throws Exception {
        mockMvc.perform(get("/api/cards/1400")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cras sed interdum..."));
    }

    @Test
    public void getCard_notFound() throws Exception {
        mockMvc.perform(get("/api/cards/2066")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.exception").value("ResponseStatusException"));
    }

    @Test
    public void createCard_success() throws Exception {
        mockMvc.perform(post("/api/cards")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/cardDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1, cardRepository.count());
    }

    @Test
    public void createCard_missingField() throws Exception {
        mockMvc.perform(post("/api/cards")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/cardDTORequest_missingField.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("expirationDate"));
    }

    @Test
    @Sql("/data/cardData.sql")
    public void updateCard_success() throws Exception {
        mockMvc.perform(put("/api/cards/1400")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/cardDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals("Donec ac nibh...", cardRepository.findById(((long)1400)).get().getName());
        assertEquals(1, cardRepository.count());
    }

    @Test
    @Sql("/data/cardData.sql")
    public void deleteCard_success() throws Exception {
        mockMvc.perform(delete("/api/cards/1400")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertEquals(0, cardRepository.count());
    }

}
