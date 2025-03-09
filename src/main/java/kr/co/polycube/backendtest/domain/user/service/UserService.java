package kr.co.polycube.backendtest.domain.user.service;

import kr.co.polycube.backendtest.domain.user.dto.request.UserRequestDto;
import kr.co.polycube.backendtest.domain.user.dto.response.UserResponseDto;
import kr.co.polycube.backendtest.domain.user.dto.response.UserSaveResponseDto;
import kr.co.polycube.backendtest.domain.user.entity.User;
import kr.co.polycube.backendtest.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserSaveResponseDto saveUser(UserRequestDto userRequestDto) {
        User user = UserRequestDto.toEntity(userRequestDto);
        userRepository.save(user);
        return UserSaveResponseDto.from(user);
    }

    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회언입니다."));
        return UserResponseDto.from(user);
    }

    @Transactional
    public UserResponseDto updateUser(Long userId, UserRequestDto userRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회언입니다."));
        user.updateName(userRequestDto.name());
        return UserResponseDto.from(user);
    }
}
