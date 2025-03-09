package kr.co.polycube.backendtest.domain.winner.entity;

import jakarta.persistence.*;
import kr.co.polycube.backendtest.domain.lotto.entity.Lotto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "winner")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Winner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "rank", nullable = false)
    private Integer rank;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lotto_id", nullable = false)
    private Lotto lotto;

    @Builder
    public Winner(Integer rank, Lotto lotto) {
        this.rank = rank;
        this.lotto = lotto;
    }
}
