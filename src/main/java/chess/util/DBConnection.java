package chess.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String SERVER = "localhost:13306";
    private static final String DATABASE = "chess";
    private static final String OPTION = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    public static final String DB_CONNECTION_EXCEPTION = "DB 연결 중 오류가 발생했습니다.";

    private static Connection connection;

    private DBConnection() {
        connection = null;
    }

    public static Connection getConnection() {
        if (connection == null) {
            connect();
        }

        return connection;
    }

    private static void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + SERVER + "/" + DATABASE + OPTION,
                                                        USERNAME,
                                                        PASSWORD);
        } catch (SQLException exception) {
            throw new IllegalStateException(DB_CONNECTION_EXCEPTION);
        }
    }
}
