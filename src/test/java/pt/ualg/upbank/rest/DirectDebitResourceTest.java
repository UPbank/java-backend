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


public class DirectDebitResourceTest extends BaseIT {

    @Test
    @Sql("/data/directDebitData.sql")
    public void getAllDirectDebits_success() throws Exception {
        mockMvc.perform(get("/api/directDebits")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.content[0].id").value(((long)1200)));
    }

    @Test
    @Sql("/data/directDebitData.sql")
    public void getDirectDebit_success() throws Exception {
        mockMvc.perform(get("/api/directDebits/1200")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    public void getDirectDebit_notFound() throws Exception {
        mockMvc.perform(get("/api/directDebits/1866")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.exception").value("ResponseStatusException"));
    }

    @Test
    public void createDirectDebit_success() throws Exception {
        mockMvc.perform(post("/api/directDebits")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/directDebitDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1, directDebitRepository.count());
    }

    @Test
    public void createDirectDebit_missingField() throws Exception {
        mockMvc.perform(post("/api/directDebits")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/directDebitDTORequest_missingField.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("active"));
    }

    @Test
    @Sql("/data/directDebitData.sql")
    public void updateDirectDebit_success() throws Exception {
        mockMvc.perform(put("/api/directDebits/1200")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/directDebitDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(true, directDebitRepository.findById(((long)1200)).get().getActive());
        assertEquals(1, directDebitRepository.count());
    }

    @Test
    @Sql("/data/directDebitData.sql")
    public void deleteDirectDebit_success() throws Exception {
        mockMvc.perform(delete("/api/directDebits/1200")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertEquals(0, directDebitRepository.count());
    }

}
