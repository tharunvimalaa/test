package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;
import databasePart1.DatabaseHelper;

/**
 * AdminHomePage class represents the admin interface where admins can manage user roles.
 */
public class AdminHomePage {
    private final DatabaseHelper databaseHelper;

    public AdminHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the admin page with user role management.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param adminUser The currently logged-in admin username.
     */
    public void show(Stage primaryStage, String adminUser) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label adminLabel = new Label("Admin Dashboard");
        adminLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ComboBox<String> userDropdown = new ComboBox<>();
        ComboBox<String> roleDropdown = new ComboBox<>();
        roleDropdown.getItems().addAll("admin", "user");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        // Fetch all users from the database
        try {
            userDropdown.getItems().addAll(databaseHelper.getAllUsers());
        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Error loading users.");
        }

        Button updateRoleButton = new Button("Update Role");

        updateRoleButton.setOnAction(e -> {
            String selectedUser = userDropdown.getValue();
            String selectedRole = roleDropdown.getValue();

            if (selectedUser != null && selectedRole != null) {
                try {
                    if (databaseHelper.updateUserRole(adminUser, selectedUser, selectedRole)) {
                        messageLabel.setText("Role updated successfully.");
                    } else {
                        messageLabel.setText("Operation not allowed.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    messageLabel.setText("Database error.");
                }
            }
        });

        layout.getChildren().addAll(adminLabel, userDropdown, roleDropdown, updateRoleButton, messageLabel);
        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.show();
    }
}
