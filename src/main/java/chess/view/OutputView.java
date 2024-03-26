package chess.view;

import chess.domain.board.dto.GameResult;

import java.util.List;

public class OutputView {

    public void writeBoard(List<String> board) {
        System.out.println(String.join("\n", board));
    }

    public void writeGameResult(GameResult gameResult) {
        System.out.printf("흰색 말: %f, 검은 말: %f, 우승자: %s%n" ,
                gameResult.whiteScore(), gameResult.blackScore(), gameResult.winnerCamp().name());
    }
}
