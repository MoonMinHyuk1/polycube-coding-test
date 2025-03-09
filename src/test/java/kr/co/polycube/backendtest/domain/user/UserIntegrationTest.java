package kr.co.polycube.backendtest.domain.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.polycube.backendtest.domain.user.dto.request.UserRequestDto;
import kr.co.polycube.backendtest.domain.user.entity.User;
import kr.co.polycube.backendtest.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자 생성 통합 테스트")
    public void saveUserTest() throws Exception {
        //given
        UserRequestDto userRequestDto = UserRequestDto.builder().name("name").build();
        String content = objectMapper.writeValueAsString(userRequestDto);

        //when
        ResultActions resultActions = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        //then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("사용자 조회 통합 테스트")
    public void getUserTest() throws Exception {
        //given
        String name = "name";
        User user = User.builder().name(name).build();
        userRepository.save(user);

        //when
        ResultActions resultActions = mockMvc.perform(get("/users/{id}", user.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()));
    }

    @Test
    @DisplayName("사용자 수정 통합 테스트")
    public void updateUserTest() throws Exception {
        //given
        String name = "name";
        User user = User.builder().name(name).build();
        userRepository.save(user);

        String newName = "newName";
        UserRequestDto userRequestDto = UserRequestDto.builder().name(newName).build();
        String content = objectMapper.writeValueAsString(userRequestDto);

        //when
        ResultActions resultActions = mockMvc.perform(put("/users/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(newName));
    }

    @Test
    @DisplayName("예외 상황 - 사용자 생성 시 공백 값 요청 테스트")
    public void saveUserTest_exception() throws Exception {
        //given
        UserRequestDto userRequestDto = UserRequestDto.builder().name("").build();
        String content = objectMapper.writeValueAsString(userRequestDto);

        //when
        ResultActions resultActions = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reason").value("이름은 공백이 될 수 없습니다."));
    }
}
