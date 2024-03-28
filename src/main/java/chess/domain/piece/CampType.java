package chess.domain.piece;

import java.util.Arrays;

public enum CampType {

    BLACK,
    WHITE,
    EMPTY,
    ;

    public static CampType findByName(String name) {
        return Arrays.stream(CampType.values())
                .filter(campType ->  campType.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 진영 타입입니다."));
    }
}
