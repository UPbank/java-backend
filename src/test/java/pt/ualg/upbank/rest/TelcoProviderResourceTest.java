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


public class TelcoProviderResourceTest extends BaseIT {

    @Test
    @Sql("/data/telcoProviderData.sql")
    public void getAllTelcoProviders_success() throws Exception {
        mockMvc.perform(get("/api/telcoProviders")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(((long)1600)));
    }

    @Test
    public void getAllTelcoProviders_unauthorized() throws Exception {
        mockMvc.perform(get("/api/telcoProviders")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.exception").value("AccessDeniedException"));
    }

    @Test
    @Sql("/data/telcoProviderData.sql")
    public void getTelcoProvider_success() throws Exception {
        mockMvc.perform(get("/api/telcoProviders/1600")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cras sed interdum..."));
    }

    @Test
    public void getTelcoProvider_notFound() throws Exception {
        mockMvc.perform(get("/api/telcoProviders/2266")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.exception").value("ResponseStatusException"));
    }

    @Test
    public void createTelcoProvider_success() throws Exception {
        mockMvc.perform(post("/api/telcoProviders")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/telcoProviderDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1, telcoProviderRepository.count());
    }

    @Test
    public void createTelcoProvider_missingField() throws Exception {
        mockMvc.perform(post("/api/telcoProviders")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/telcoProviderDTORequest_missingField.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("name"));
    }

    @Test
    @Sql("/data/telcoProviderData.sql")
    public void updateTelcoProvider_success() throws Exception {
        mockMvc.perform(put("/api/telcoProviders/1600")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/telcoProviderDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals("Donec ac nibh...", telcoProviderRepository.findById(((long)1600)).get().getName());
        assertEquals(1, telcoProviderRepository.count());
    }

    @Test
    @Sql("/data/telcoProviderData.sql")
    public void deleteTelcoProvider_success() throws Exception {
        mockMvc.perform(delete("/api/telcoProviders/1600")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertEquals(0, telcoProviderRepository.count());
    }

}
