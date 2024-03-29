package chess.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("데이터베이스")
public class DBConnectionTest {

    @DisplayName("데이터베이스가 제대로 연결됐는지 확인한다.")
    @Test
    void connection() {
        // when & then
        assertThatCode(DBConnection::getConnection).doesNotThrowAnyException();
    }
}
