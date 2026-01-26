package io.github.panjung99.panapi.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OpenApiGeneratorTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void generateOpenApiJson() throws Exception {
        MvcResult result = mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        Files.createDirectories(Paths.get("../../docs"));
        Files.write(Paths.get("../../docs/openapi.json"), content.getBytes());
        System.out.println("OpenAPI JSON 已成功导出到 docs/openapi.json");
    }
}