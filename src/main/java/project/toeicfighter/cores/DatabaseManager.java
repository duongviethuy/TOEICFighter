package project.toeicfighter.cores;

import project.toeicfighter.models.Account;
import project.toeicfighter.models.PracticeHistory;
import project.toeicfighter.models.Question;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DatabaseManager {
    private static final String dbURL = "jdbc:mysql://localhost:3306/toeic_fighter";
    private static final String dbUSER = "root";
    private static final String dbPASSWORD = "12102001";

    private static Connection connection;

    public static void getConnection() throws SQLException {
        if(connection == null || connection.isClosed()) {
            System.out.println("Successfully connected to database");
            connection = DriverManager.getConnection(dbURL, dbUSER, dbPASSWORD);
        }
    }

    public static void closeConnection() throws SQLException {
        connection.close();
    }

    //NOTE: Hàm để lấy tất cả tài khoản đã được lưu trong database
    public static ArrayList<Account> getAccountList() throws SQLException {
        ArrayList<Account> accountsList = new ArrayList<>();

        String allAccountQuery = "SELECT * FROM account";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(allAccountQuery);

        if (!resultSet.isBeforeFirst()) {
            // NOTE: If no account exists in the database, create a default one.
            String insertDefaultAccount = "INSERT INTO account (accountID, username, password, fullname, role) VALUES " +
                    "('admin001', 'admin', '123', 'Default', 'Teacher') ";
            statement.executeUpdate(insertDefaultAccount);
            accountsList.add(new Account("admin001", "admin", "123", "Default","Teacher"));
            return accountsList;
        }

        while (resultSet.next()) {
            String accountID = resultSet.getString("accountID");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            String fullname = resultSet.getString("fullname");
            String role = resultSet.getString("role");
            LocalDateTime createDate = resultSet.getTimestamp("createDate").toLocalDateTime();
            LocalDateTime lastLogin;
            try {
                lastLogin = resultSet.getTimestamp("lastLogin").toLocalDateTime();
            }
            catch (Exception e) {
                lastLogin = null;
            }
            accountsList.add(new Account(accountID, username, password, fullname, role, createDate, lastLogin));
        }

        return accountsList;
    }

    public static void changePassword(String username, String newPassword) throws SQLException {
        String query = "UPDATE account SET password = ? WHERE username = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, newPassword);
        ps.setString(2, username);
        ps.executeUpdate();
    }

    public static void updateLoginStatus(String username) throws SQLException {
        String  query = "UPDATE account SET lastLogin = ? WHERE username = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(2, username);
        ps.executeUpdate();
    }

    public static void createAccount(Account account) throws SQLException {
        String query = "INSERT INTO account (username, password, fullname, role, createDate) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, account.getUsername());
        ps.setString(2, account.getPassword());
        ps.setString(3, account.getFullname());
        ps.setString(4, account.getRole());
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        ps.executeUpdate();
    }

    public static void deleteAccount(String accountID) throws SQLException {
        String query = "DELETE FROM account WHERE accountID = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, accountID);
        ps.executeUpdate();
    }

    // hàm để lấy tất cả câu hỏi trong database và trả lại 1 ArrayList
    public static ArrayList<Question> getAllQuestion() throws SQLException {
        ArrayList<Question> questionList = new ArrayList<>();

        String allQuestionQuery = "SELECT * FROM question";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(allQuestionQuery);

        while (resultSet.next()) {
            int id = resultSet.getInt("questionID");
            String question = resultSet.getString("Content");
            String answerA = resultSet.getString("answerA");
            String answerB = resultSet.getString("answerB");
            String answerC = resultSet.getString("answerC");
            String answerD = resultSet.getString("answerD");
            String correctAnswer = resultSet.getString("correctAnswer");
            String hint = resultSet.getString("hint");
            String addbyID = resultSet.getString("addByID");
            questionList.add(new Question(id, question,answerA, answerB, answerC,answerD,correctAnswer, hint, addbyID));
        }
        return  questionList;
    }

    // hàm để lấy ngẫu nhiên n câu hỏi từ database
    public static ArrayList<Question> getRandomQuestions(int numberOfQuestion) throws SQLException {
        ArrayList<Question> questionList = new ArrayList<>();

        // SQL lấy ngẫu nhiên numberOfQuestion câu hỏi
        String randomQuestionQuery = "SELECT * FROM question ORDER BY RAND() LIMIT ?";
        PreparedStatement preparedStatement = connection.prepareStatement(randomQuestionQuery);
        preparedStatement.setInt(1, numberOfQuestion); // gán số lượng câu hỏi

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("questionID");
            String question = resultSet.getString("Content");
            String answerA = resultSet.getString("answerA");
            String answerB = resultSet.getString("answerB");
            String answerC = resultSet.getString("answerC");
            String answerD = resultSet.getString("answerD");
            String correctAnswer = resultSet.getString("correctAnswer");
            String hint = resultSet.getString("hint");
            String addbyID = resultSet.getString("addByID");
            questionList.add(new Question(id, question, answerA, answerB, answerC, answerD, correctAnswer, hint, addbyID));
        }
        return questionList;
    }

    public static void saveToHistory(String userID, int numberOfQuestion, int numberOfCorrectQuestion, LocalDateTime start_time, LocalDateTime end_time) throws SQLException {
        String query = "INSERT INTO practice_history (userID, numberOfQuestion, numberOfCorrectQuestion, start_time, end_time) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, userID);
        ps.setInt(2, numberOfQuestion);
        ps.setInt(3, numberOfCorrectQuestion);
        ps.setTimestamp(4, Timestamp.valueOf(start_time));
        ps.setTimestamp(5, Timestamp.valueOf(end_time));
        ps.executeUpdate();
    }

    public static ArrayList<PracticeHistory> getPracticeHistory() throws SQLException {
        ArrayList<PracticeHistory> practiceHistory = new ArrayList<>();
        String query = "SELECT * from practice_history";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            int testID = rs.getInt("test_history_id");
            String userID = rs.getString("userID");
            int numberOfQuestion = rs.getInt("numberOfQuestion");
            int numberOfCorrectQuestion = rs.getInt("numberOfCorrectQuestion");
            LocalDateTime start_time = rs.getTimestamp("start_time").toLocalDateTime();
            LocalDateTime end_time = rs.getTimestamp("end_time").toLocalDateTime();
            practiceHistory.add(new PracticeHistory(testID, userID, numberOfQuestion, numberOfCorrectQuestion, start_time, end_time));
        }
        return practiceHistory;
    }

    public static String getFullNameByUserID(String userID) throws SQLException {
        String fullname;
        String query = "SELECT fullname FROM account where accountID = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, userID);
        ResultSet resultSet = ps.executeQuery();
        if(resultSet.next()) {
            fullname = resultSet.getString("fullname");
            return fullname;
        }
        else return "null";
    }

    public static void addAQuestion(Question question) throws SQLException {
        String query = "INSERT INTO question (Content, answerA, answerB, answerC, answerD, correctAnswer, hint, addByID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, question.getQuestion());
        ps.setString(2, question.getAnswerA());
        ps.setString(3, question.getAnswerB());
        ps.setString(4, question.getAnswerC());
        ps.setString(5, question.getAnswerD());
        ps.setString(6, question.getCorrectAnswer());
        ps.setString(7, question.getHint());
        ps.setString(8, question.getAddbyID());
        ps.executeUpdate();
    }

    public static void deleteAQuestion(int id) throws SQLException {
        String query = "DELETE FROM question WHERE questionID = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    public static void updateAQuestion(int questionID, Question question) throws SQLException {
        String query = "UPDATE question SET Content = ?, answerA = ?, answerB = ?, answerC = ?, answerD = ?, correctAnswer = ?, hint = ?, addByID = ? WHERE questionID = ?";

        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, question.getQuestion());
        ps.setString(2, question.getAnswerA());
        ps.setString(3, question.getAnswerB());
        ps.setString(4, question.getAnswerC());
        ps.setString(5, question.getAnswerD());
        ps.setString(6, question.getCorrectAnswer());
        ps.setString(7, question.getHint());
        ps.setString(8, question.getAddbyID());
        ps.setInt(9, questionID); // Gán id của câu hỏi cần cập nhật
        ps.executeUpdate();
    }





}
