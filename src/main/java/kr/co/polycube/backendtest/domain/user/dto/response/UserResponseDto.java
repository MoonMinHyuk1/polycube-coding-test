package kr.co.polycube.backendtest.domain.user.dto.response;

import kr.co.polycube.backendtest.domain.user.entity.User;
import lombok.Builder;

@Builder
public record UserResponseDto(
        Long id,
        String name
) {

    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
