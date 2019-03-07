package com.example.junsta.common;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class ProfilesControllerTest extends BaseControllerTest {

    @Test
    public void  getProfile() throws Exception {
        ResultActions perform = mockMvc.perform(get("/profiles"));

        String response = perform.andReturn().getResponse().getContentAsString();

        assertThat(response).isEqualTo("test");
    }
}