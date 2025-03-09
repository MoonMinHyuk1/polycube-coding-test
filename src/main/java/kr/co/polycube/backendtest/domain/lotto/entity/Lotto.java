package kr.co.polycube.backendtest.domain.lotto.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Getter
@Entity
@Table(name = "lotto")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "number_1", nullable = false)
    private Integer number1;

    @Column(name = "number_2", nullable = false)
    private Integer number2;

    @Column(name = "number_3", nullable = false)
    private Integer number3;

    @Column(name = "number_4", nullable = false)
    private Integer number4;

    @Column(name = "number_5", nullable = false)
    private Integer number5;

    @Column(name = "number_6", nullable = false)
    private Integer number6;

    @Builder
    public Lotto(List<Integer> numbers) {
        this.number1 = numbers.get(0);
        this.number2 = numbers.get(1);
        this.number3 = numbers.get(2);
        this.number4 = numbers.get(3);
        this.number5 = numbers.get(4);
        this.number6 = numbers.get(5);
    }

    public List<Integer> getNumbers() {
        return List.of(
                number1, number2, number3, number4, number5, number6
        );
    }
}
