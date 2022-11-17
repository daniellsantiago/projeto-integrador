package com.grupo6.projetointegrador.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class DueDateControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    void setup() {

    }

    @Test
    void findItemBatchBySection_throwsUnprocessedEntity_whenDaysIsNegative() throws Exception {
        // Given
        Long sectionId = 1L;
        int days = -10;
        // When / Then
        mockMvc.perform(get("/api/due-date?sectionId=" + sectionId + "&days=" + days)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void findItemBatchBySection_throwsInternalError_whenRequiredFieldIsNotProvided() throws Exception {
        // Given
        Long sectionId = 1L;
        // When / Then
        mockMvc.perform(get("/api/due-date?sectionId=" + sectionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findItemBatchByCategory_throwsUnprocessedEntity_whenCategoryDoeNotExists() throws Exception {
        // Given
        Long sectionId = 1L;
        int days = 10;
        String category = "INVALID_CATEGORY";
        // When / Then
        mockMvc.perform(get("/api/due-date/list?sectionId=" + sectionId + "&days=" + days + "&category=" + category)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void findItemBatchByCategory_throwsInternalError_whenRequiredFieldIsNotProvided() throws Exception {
        // Given
        Long sectionId = 1L;
        // When / Then
        mockMvc.perform(get("/api/due-date/list?sectionId=" + sectionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
