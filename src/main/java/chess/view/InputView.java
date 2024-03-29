package chess.view;

import chess.view.dto.Command;

import java.util.List;
import java.util.Scanner;

public class InputView {

    private static final String INVALID_COMMAND_EXCEPTION = "잘못된 명령어 입력입니다. 'end' 혹은 'move source 위치 target 위치'로 입력해주세요.";
    private static final String INVALID_GAME_COMMAND_EXCEPTION = "잘못된 명령어 입력입니다. 'enter' 혹은 'create gameId'로 입력해주세요.";
    private static final String COMMAND_SEPARATOR = " ";
    public static final String START_COMMAND = "start";
    private static final String END_COMMAND = "end";
    private static final String MOVE_COMMAND = "move";
    private static final String STATUS_COMMAND = "status";
    private static final String GAME_CREATE_COMMAND = "create";
    private static final String GAME_ENTER_COMMAND = "enter";
    private static final String CHESS_GAME_TITLE = String.format("> 체스 게임을 시작합니다.\n" +
            "> 게임 시작 : %s\n" +
            "> 게임 종료 : %s\n" +
            "> 게임 이동 : %s source위치 target위치 - 예. %s b2 b3", START_COMMAND, END_COMMAND, MOVE_COMMAND, MOVE_COMMAND);
    private static final String COMMAND_EXCEPTION_MESSAGE = String.format("%s 또는 %s만 입력할 수 있습니다. 다시 입력하세요.", START_COMMAND, END_COMMAND);
    public static final String GAME_COMMAND_TITLE = "> 새로운 게임 시작하기 : create - 예. create %n" +
            "> 게임 이어서 시작하기 : enter (입장 가능한 game: %s) - 예. enter 1 %n";

    private final Scanner scanner;

    public InputView() {
        this.scanner = new Scanner(System.in);
    }

    public Command readProgressCommand() {
        System.out.println(CHESS_GAME_TITLE);

        String input = scanner.nextLine();
        validateProgressCommand(input);

        return new Command(input);
    }

    private void validateProgressCommand(String command) {
        if (!List.of(START_COMMAND, END_COMMAND).contains(command)) {
            throw new IllegalArgumentException(COMMAND_EXCEPTION_MESSAGE);
        }
    }

    public Command readGameCommand(List<String> gameIds) {
        System.out.printf(GAME_COMMAND_TITLE, String.join(" ", gameIds));

        List<String> input = List.of(scanner.nextLine().split(COMMAND_SEPARATOR));
        String command = input.get(0);
        validateGameCommand(command);

        if (input.size() == 1) {
            return new Command(command);
        }

        return new Command(command, input.get(1));
    }

    private void validateGameCommand(String command) {
        if (!List.of(GAME_CREATE_COMMAND, GAME_ENTER_COMMAND).contains(command)) {
            throw new IllegalArgumentException(INVALID_GAME_COMMAND_EXCEPTION);
        }
    }

    public Command readCommand() {
        List<String> input = List.of(scanner.nextLine().split(COMMAND_SEPARATOR));

        String command = input.get(0);
        validateCommand(command);

        if (input.size() == 1) {
            return new Command(command);
        }

        return new Command(command, input.get(1), input.get(2));
    }

    private static void validateCommand(String command) {
        if (!List.of(MOVE_COMMAND, END_COMMAND, STATUS_COMMAND,
                GAME_CREATE_COMMAND, GAME_ENTER_COMMAND).contains(command)) {
            throw new IllegalArgumentException(INVALID_COMMAND_EXCEPTION);
        }
    }
}
