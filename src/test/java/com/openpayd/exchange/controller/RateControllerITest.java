package com.openpayd.exchange.controller;

import com.openpayd.exchange.model.Currency;
import com.openpayd.exchange.utils.ResponseUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RateControllerITest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void getRate() throws Exception {
        when(restTemplate.getForEntity(ArgumentMatchers.contains("latest"),
                ArgumentMatchers.<Class<Currency>>any())).thenReturn(ResponseUtils.getMockResponseForRate());

        this.mockMvc.perform(get("/api/rates?source=USD&target=TRY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate").value(9.72))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void getRateWithApiException() throws Exception {
        when(restTemplate.getForEntity(ArgumentMatchers.contains("latest"),
                ArgumentMatchers.<Class<Currency>>any())).thenThrow(ResourceAccessException.class);

        this.mockMvc.perform(get("/api/rates?source=USD&target=TRY"))
                .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().is4xxClientError());
    }

}
