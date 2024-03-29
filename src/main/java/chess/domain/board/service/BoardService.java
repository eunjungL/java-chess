package chess.domain.board.service;

import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.board.dao.BoardRepository;
import chess.domain.board.dto.MoveCommand;
import chess.domain.board.state.BoardState;
import chess.domain.board.state.StateName;
import chess.domain.game.dao.GameRepository;
import chess.domain.piece.Piece;
import chess.domain.square.Square;

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

        BoardState boardState = board.getBoardState();
        gameRepository.update(gameId, boardState.getSateName());
        if (boardState.getSateName().equals(StateName.GAME_OVER)) {
            gameRepository.update(gameId, boardState.findWinner());
        }
    }
}
