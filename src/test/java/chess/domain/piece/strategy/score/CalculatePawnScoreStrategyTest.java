package chess.domain.piece.strategy.score;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("폰 점수 계산 전략")
class CalculatePawnScoreStrategyTest {

    private static final double PAWN_SCORE = 1;

    CalculateScoreStrategy calculateScoreStrategy;

    @BeforeEach
    void setUp() {
        calculateScoreStrategy = new CalculatePawnScoreStrategy(PAWN_SCORE);
    }

    @DisplayName("폰 점수 계산 전략은 같은 파일에 폰이 여러 개면 패널티 점수를 반환한다.")
    @Test
    void calculateWhenDuplicated() {
        // given
        long duplicatedPAwnCount = 2;

        // when
        double actual = calculateScoreStrategy.calculate(duplicatedPAwnCount);

        // then
        assertThat(actual).isEqualTo(0.5);
    }

    @DisplayName("폰 점수 계산 전략은 같은 파일에 폰이 한개면 기본 점수를 반환한다.")
    @Test
    void calculateWhenNotDuplicated() {
        // given
        long duplicatedPAwnCount = 1;

        // when
        double actual = calculateScoreStrategy.calculate(duplicatedPAwnCount);

        // then
        assertThat(actual).isEqualTo(1);
    }
}
