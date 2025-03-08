package kr.co.polycube.backendtest.domain.user.controller;

import jakarta.validation.Valid;
import kr.co.polycube.backendtest.domain.user.dto.request.UserRequestDto;
import kr.co.polycube.backendtest.domain.user.dto.response.UserResponseDto;
import kr.co.polycube.backendtest.domain.user.dto.response.UserSaveResponseDto;
import kr.co.polycube.backendtest.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserSaveResponseDto> saveUser(
            @Valid @RequestBody UserRequestDto userRequestDto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.saveUser(userRequestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(
            @PathVariable(name = "id") Long userId
    ) {
        return ResponseEntity
                .ok(userService.getUser(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable(name = "id") Long userId,
            @Valid @RequestBody UserRequestDto userRequestDto
    ) {
        return ResponseEntity
                .ok(userService.updateUser(userId, userRequestDto));
    }
}
