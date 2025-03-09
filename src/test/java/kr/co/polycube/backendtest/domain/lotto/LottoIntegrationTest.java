package kr.co.polycube.backendtest.domain.lotto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class LottoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("로또 번호 발급 통합 테스트")
    public void saveLottoTest() throws Exception {
        //given & when
        ResultActions resultActions = mockMvc.perform(post("/lottos"));

        //then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numbers").isArray())
                .andExpect(jsonPath("$.numbers", hasSize(6)))
                .andExpect(jsonPath("$.numbers[0]").isNumber())
                .andExpect(jsonPath("$.numbers[1]").isNumber())
                .andExpect(jsonPath("$.numbers[2]").isNumber())
                .andExpect(jsonPath("$.numbers[3]").isNumber())
                .andExpect(jsonPath("$.numbers[4]").isNumber())
                .andExpect(jsonPath("$.numbers[5]").isNumber());
    }
}
