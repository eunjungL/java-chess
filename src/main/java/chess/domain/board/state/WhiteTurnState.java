package chess.domain.board.state;

import chess.domain.piece.CampType;
import chess.domain.piece.Piece;

public class WhiteTurnState implements GameProgressState {

    private static final String GAME_NOT_OVER_EXCEPTION = "게임이 아직 종료되지 않았습니다.";

    @Override
    public GameProgressState nextTurnState() {
        return new BlackTurnState();
    }

    @Override
    public boolean checkMovable(Piece source, Piece destination) {
        return source.isWhite() && (destination.isBlack() || !destination.isNotEmpty());
    }

    @Override
    public GameProgressState makeGameOver() {
        return new GameOverState(CampType.WHITE);
    }

    @Override
    public CampType findWinner() {
        throw new UnsupportedOperationException(GAME_NOT_OVER_EXCEPTION);
    }

    @Override
    public StateName getSateName() {
        return StateName.WHITE_TURN;
    }
}
