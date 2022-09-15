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


public class StandingOrderResourceTest extends BaseIT {

    @Test
    @Sql("/data/standingOrderData.sql")
    public void getAllStandingOrders_success() throws Exception {
        mockMvc.perform(get("/api/standingOrders")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.content[0].id").value(((long)1300)));
    }

    @Test
    @Sql("/data/standingOrderData.sql")
    public void getStandingOrder_success() throws Exception {
        mockMvc.perform(get("/api/standingOrders/1300")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(((long)95)));
    }

    @Test
    public void getStandingOrder_notFound() throws Exception {
        mockMvc.perform(get("/api/standingOrders/1966")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.exception").value("ResponseStatusException"));
    }

    @Test
    public void createStandingOrder_success() throws Exception {
        mockMvc.perform(post("/api/standingOrders")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/standingOrderDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1, standingOrderRepository.count());
    }

    @Test
    public void createStandingOrder_missingField() throws Exception {
        mockMvc.perform(post("/api/standingOrders")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/standingOrderDTORequest_missingField.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("amount"));
    }

    @Test
    @Sql("/data/standingOrderData.sql")
    public void updateStandingOrder_success() throws Exception {
        mockMvc.perform(put("/api/standingOrders/1300")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/standingOrderDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(((long)94), standingOrderRepository.findById(((long)1300)).get().getAmount());
        assertEquals(1, standingOrderRepository.count());
    }

    @Test
    @Sql("/data/standingOrderData.sql")
    public void deleteStandingOrder_success() throws Exception {
        mockMvc.perform(delete("/api/standingOrders/1300")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertEquals(0, standingOrderRepository.count());
    }

}
