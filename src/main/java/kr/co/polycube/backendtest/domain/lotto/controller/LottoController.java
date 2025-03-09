package kr.co.polycube.backendtest.domain.lotto.controller;

import kr.co.polycube.backendtest.domain.lotto.dto.response.LottoResponseDto;
import kr.co.polycube.backendtest.domain.lotto.service.LottoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lottos")
public class LottoController {

    private final LottoService lottoService;

    @PostMapping
    public ResponseEntity<LottoResponseDto> saveLotto() {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(lottoService.saveLotto());
    }
}
