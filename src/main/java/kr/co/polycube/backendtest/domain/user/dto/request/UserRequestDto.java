package kr.co.polycube.backendtest.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import kr.co.polycube.backendtest.domain.user.entity.User;
import lombok.Builder;

@Builder
public record UserSaveRequestDto(
        @NotBlank(message = "이름은 공백이 될 수 없습니다.")
        String name
) {

    public static User toEntity(UserSaveRequestDto userSaveRequestDto) {
        return User.builder()
                .name(userSaveRequestDto.name())
                .build();
    }
}
