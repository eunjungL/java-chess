package chess.domain.board.state;

import chess.domain.piece.CampType;

public enum StateName {

    WHITE_TURN,
    BLACK_TURN,
    GAME_OVER,
    ;

    private static final String STATE_NOT_FOUND = "존재하지 않는 상태입니다.";

    public static GameProgressState findByStateName(String stateName) {
        if (stateName.equals(WHITE_TURN.name())) {
            return new WhiteTurnState();
        }

        if (stateName.equals(BLACK_TURN.name())) {
            return new BlackTurnState();
        }

        throw new IllegalArgumentException(STATE_NOT_FOUND);
    }

    public static GameProgressState findByStateName(String stateName, String winnerCampName) {
        if (stateName.equals(GAME_OVER.name())) {
            CampType winnerCamp = CampType.findByName(winnerCampName);
            return new GameOverState(winnerCamp);
        }

        throw new IllegalArgumentException(STATE_NOT_FOUND);
    }
}
