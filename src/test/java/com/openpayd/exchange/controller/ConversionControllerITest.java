package com.openpayd.exchange.controller;

import com.openpayd.exchange.model.AmountConversion;
import com.openpayd.exchange.utils.RequestUtils;
import com.openpayd.exchange.utils.ResponseUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ConversionControllerITest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void getConversionsWithId() throws Exception {
        this.mockMvc.perform(get("/api/conversions?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void getConversionsWithDate() throws Exception {
        this.mockMvc.perform(get("/api/conversions?date=2021-01-02"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(8)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void getConversionsWithDateAndPagination() throws Exception {
        this.mockMvc.perform(get("/api/conversions?date=2021-01-02&page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void convertAmount() throws Exception {
        when(restTemplate.getForEntity(ArgumentMatchers.contains("convert"),
                ArgumentMatchers.<Class<AmountConversion>>any())).thenReturn(ResponseUtils.getMockResponseForConversion());
        mockMvc.perform(post("/api/conversions")
                .content(RequestUtils.getConversionPost())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void convertAmountWithException() throws Exception {
        when(restTemplate.getForEntity(ArgumentMatchers.contains("convert"),
                ArgumentMatchers.<Class<AmountConversion>>any())).thenThrow(ResourceAccessException.class);

        mockMvc.perform(post("/api/conversions")
                .content(RequestUtils.getConversionPost())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}
