package kr.co.polycube.backendtest.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LottoNumberGenerator {

    public static List<Integer> generateLottoNumbers() {
        Set<Integer> numbers = new HashSet<>();

        while (numbers.size() < 6) {
            numbers.add(ThreadLocalRandom.current().nextInt(1, 46));
        }

        return numbers.stream()
                .sorted()
                .collect(Collectors.toList());
    }
}
