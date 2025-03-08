package kr.co.polycube.backendtest.domain.user.dto.response;

import kr.co.polycube.backendtest.domain.user.entity.User;
import lombok.Builder;

@Builder
public record UserSaveResponseDto(
        Long id
) {

    public static UserSaveResponseDto from(User user) {
        return UserSaveResponseDto.builder()
                .id(user.getId())
                .build();
    }
}
