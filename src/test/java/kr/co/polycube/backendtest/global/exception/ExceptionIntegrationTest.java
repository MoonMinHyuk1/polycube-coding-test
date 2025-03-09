package kr.co.polycube.backendtest.global.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class ExceptionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("등록되지 않은 API 요청 테스트")
    public void notFoundExceptionTest() throws Exception {
        //given & when
        ResultActions resultActions = mockMvc.perform(get("/test"));

        //then
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.reason").exists());
    }

    @Test
    @DisplayName("허용되지 않은 특수문자를 포함한 URL 요청 테스트")
    public void filterTest() throws Exception {
        //given & when
        ResultActions resultActions = mockMvc.perform(get("/users/{id}?name=test!!", 1));

        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reason").value("허용되지 않은 특수문자가 포함되어 있습니다."));
    }
}
