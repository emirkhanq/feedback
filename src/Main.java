import java.sql.*;
import java.util.Scanner;

public class Main {

    // Метод для подключения к базе данных
    private static Connection getConnection() throws SQLException {
        String URL = "jdbc:postgresql://localhost:5432/final_project_db";
        String USERNAME = "postgres";
        String PASSWORD = "postgres";
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    // Метод для добавления отзыва
    private static void addFeedback(String username, String email, String message) {
        String sql = "INSERT INTO feedback (username, email, message) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, message);
            preparedStatement.executeUpdate();

            System.out.println("The feedback has been successfully added!");
        } catch (SQLException e) {
            System.err.println("Error was adding a feedback: " + e.getMessage());
        }
    }

    // Метод для получения всех отзывов
    private static void viewAllFeedbacks() {
        String sql = "SELECT * FROM feedback";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id"));
                System.out.println("Username: " + resultSet.getString("username"));
                System.out.println("Email: " + resultSet.getString("email"));
                System.out.println("Feedback: " + resultSet.getString("message"));
                System.out.println("Submitted at: " + resultSet.getTimestamp("submitted_at"));
                System.out.println("------------------------------------");
            }
        } catch (SQLException e) {
            System.err.println("Error was adding a feedback:" + e.getMessage());
        }
    }

    // Метод для обновления отзыва
    private static void updateFeedback(int id, String newMessage) {
        String sql = "UPDATE feedback SET message = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, newMessage);
            preparedStatement.setInt(2, id);
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Feedback successfully updated!");
            } else {
                System.out.println("Feedback with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            System.err.println("Error while updating the feedback: " + e.getMessage());
        }
    }

    // Метод для удаления отзыва
    private static void deleteFeedback(int id) {
        String sql = "DELETE FROM feedback WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Feedback successfully deleted!");
            } else {
                System.out.println("Feedback with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            System.err.println("Error while deleting the feedback: " + e.getMessage());
        }
    }

    // Основное меню для взаимодействия
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int userChoice;

        do {
            System.out.println("\n=== Feedback Management System ===");
            System.out.println("1. Add feedback");
            System.out.println("2. Show all feedbacks");
            System.out.println("3. Update feedback");
            System.out.println("4. Delete feedback");
            System.out.println("5. Exit from program");
            System.out.print("Enter your choice: ");

            userChoice = scanner.nextInt();
            scanner.nextLine(); // Очищаем буфер после считывания числа

            switch (userChoice) {
                case 1:
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter feedback: ");
                    String message = scanner.nextLine();
                    addFeedback(username, email, message);
                    break;
                case 2:
                    viewAllFeedbacks();
                    break;
                case 3:
                    System.out.print("Enter ID of feedback to update: ");
                    int idToUpdate = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new feedback message: ");
                    String newMessage = scanner.nextLine();
                    updateFeedback(idToUpdate, newMessage);
                    break;
                case 4:
                    System.out.print("Enter ID of feedback to delete: ");
                    int idToDelete = scanner.nextInt();
                    deleteFeedback(idToDelete);
                    break;
                case 5:
                    System.out.println("Exiting from program...");
                    break;
                default:
                    System.out.println("Wrong input! Try again...");
            }
        } while (userChoice != 5);

        scanner.close();
    }
}