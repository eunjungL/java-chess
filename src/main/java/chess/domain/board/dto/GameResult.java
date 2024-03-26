package chess.domain.board.dto;

import chess.domain.piece.CampType;

public record GameResult(CampType winnerCamp, double whiteScore, double blackScore) {
}
