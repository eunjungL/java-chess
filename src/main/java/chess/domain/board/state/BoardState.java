package chess.domain.board.state;

import chess.domain.piece.CampType;
import chess.domain.piece.Piece;

public interface BoardState {

    BoardState nextTurnState();
    boolean checkMovable(Piece source, Piece destination);
    BoardState makeGameOver();
    CampType findWinner();
}
