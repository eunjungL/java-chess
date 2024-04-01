package chess.domain.board.service;

import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.board.dao.BoardRepository;
import chess.domain.board.dto.MoveCommand;
import chess.domain.board.state.GameProgressState;
import chess.domain.board.state.StateName;
import chess.domain.game.dao.GameRepository;
import chess.domain.piece.Piece;
import chess.domain.square.File;
import chess.domain.square.Rank;
import chess.domain.square.Square;
import chess.view.PieceName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoardService {

    private final BoardRepository boardRepository;
    private final GameRepository gameRepository;

    public BoardService(BoardRepository boardRepository, GameRepository gameRepository) {
        this.boardRepository = boardRepository;
        this.gameRepository = gameRepository;
    }

    public Board createBoard(int gameId) {
        Map<Square, Piece> board = boardRepository.findByGameId(gameId)
                .orElseGet(() -> createNewBoard(gameId));

        return new Board(board, gameRepository.findStateById(gameId));
    }

    private Map<Square, Piece> createNewBoard(int gameId) {
        Map<Square, Piece> newBoard = new BoardFactory().create();
        boardRepository.saveAll(gameId, newBoard);

        return newBoard;
    }

    public void move(int gameId, Board board, MoveCommand moveCommand) {
        Square source = moveCommand.source();
        Square destination = moveCommand.destination();

        Piece destinationPiece = board.move(source, destination);
        boardRepository.update(gameId, source, destinationPiece);
        boardRepository.update(gameId, destination, board.findPieceBySquare(destination));

        GameProgressState gameProgressState = board.getBoardState();
        gameRepository.update(gameId, gameProgressState.getSateName());
        if (gameProgressState.getSateName().equals(StateName.GAME_OVER)) {
            gameRepository.update(gameId, gameProgressState.findWinner());
        }
    }

    public List<String> makeBoardOutput(Board board) {
        Map<Square, Piece> boardOutput = board.toBoardOutput().board();
        List<String> output = new ArrayList<>();

        for (Rank rank : Rank.reverse()) {
            output.add(makeRankOutput(rank, boardOutput));
        }

        return output;
    }

    private String makeRankOutput(Rank rank, Map<Square, Piece> boardOutput) {
        StringBuilder stringBuilder = new StringBuilder();

        for (File file : File.values()) {
            Square square = Square.of(file, rank);
            Piece piece = boardOutput.get(square);

            String pieceName = PieceName.findName(piece);
            stringBuilder.append(pieceName);
        }

        return stringBuilder.toString();
    }
}
