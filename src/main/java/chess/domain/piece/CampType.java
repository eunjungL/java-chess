package chess.domain.piece;

import java.util.Arrays;

public enum CampType {

    BLACK,
    WHITE,
    EMPTY,
    ;

    private static final String CAMP_TYPE_NOT_FOUND = "존재하지 않는 진영 타입입니다.";

    public static CampType findByName(String name) {
        return Arrays.stream(CampType.values())
                .filter(campType ->  campType.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(CAMP_TYPE_NOT_FOUND));
    }
}
