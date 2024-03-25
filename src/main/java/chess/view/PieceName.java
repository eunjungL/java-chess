package chess.view;

import chess.domain.piece.Piece;

import java.util.Arrays;

public enum PieceName {

    KING("K", "k"),
    QUEEN("Q", "q"),
    ROOK("R", "r"),
    BISHOP("B", "b"),
    KNIGHT("N", "n"),
    PAWN("P", "p"),
    EMPTY(".", "."),
    ;

    private final String blackName;
    private final String whiteName;

    PieceName(String blackName, String whiteName) {
        this.blackName = blackName;
        this.whiteName = whiteName;
    }

    public static String findName(Piece piece) {
        PieceName pieceName = Arrays.stream(PieceName.values())
                .filter(p -> piece.getPieceType().name().equals(p.name()))
                .findFirst()
                .orElseThrow();

        return selectByColor(piece, pieceName);
    }

    private static String selectByColor(Piece piece, PieceName pieceName) {
        if (piece.isBlack()) {
            return pieceName.blackName;
        }

        return pieceName.whiteName;
    }
}
