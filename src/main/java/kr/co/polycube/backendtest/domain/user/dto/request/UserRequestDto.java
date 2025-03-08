package kr.co.polycube.backendtest.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import kr.co.polycube.backendtest.domain.user.entity.User;
import lombok.Builder;

@Builder
public record UserRequestDto(
        @NotBlank(message = "이름은 공백이 될 수 없습니다.")
        String name
) {

    public static User toEntity(UserRequestDto userRequestDto) {
        return User.builder()
                .name(userRequestDto.name())
                .build();
    }
}
