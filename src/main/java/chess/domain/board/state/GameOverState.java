package chess.domain.board.state;

import chess.domain.piece.CampType;
import chess.domain.piece.Piece;

public class GameOverState implements GameProgressState {

    private static final String ALREADY_OVER_EXCEPTION = "이미 종료된 게임입니다.";

    private final CampType winnerCamp;

    public GameOverState(CampType winnerCamp) {
        this.winnerCamp = winnerCamp;
    }

    @Override
    public GameProgressState nextTurnState() {
        throw new UnsupportedOperationException(ALREADY_OVER_EXCEPTION);
    }

    @Override
    public boolean checkMovable(Piece source, Piece destination) {
        throw new UnsupportedOperationException(ALREADY_OVER_EXCEPTION);
    }

    @Override
    public GameProgressState makeGameOver() {
        throw new UnsupportedOperationException(ALREADY_OVER_EXCEPTION);
    }

    @Override
    public CampType findWinner() {
        return winnerCamp;
    }

    @Override
    public StateName getSateName() {
        return StateName.GAME_OVER;
    }
}
