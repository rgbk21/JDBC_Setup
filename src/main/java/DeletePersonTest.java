import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DeletePersonTest {

    public static void main(String[] args) {

        Connection connection = null;

        try {
            connection = JDBCUtil.getConnection();
            // Delete the person with person_id = 101
            deletePerson(connection, 3);
        } catch (SQLException e) {
            e.printStackTrace();
            JDBCUtil.rollback(connection);
        } finally {
            JDBCUtil.closeConnection(connection);
        }

    }

    private static void deletePerson(Connection connection, int personId) throws SQLException {

        String query = "DELETE FROM person " +
                "WHERE person_id = " + personId;

        Statement statement = null;

        try {
            statement = connection.createStatement();
            int numOfRowsDeleted = statement.executeUpdate(query);
            System.out.println("Num of rows deleted: " + numOfRowsDeleted);
        } finally {
            JDBCUtil.closeStatement(statement);
        }
    }
}
