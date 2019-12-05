package Controller;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import Common.Common;
import Common.DBHelper;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	@FXML
    private TextField txtUserName;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnExit;
    
    private Common com = new Common();
    private DBHelper dh = new DBHelper();

    @FXML
    void onExit(ActionEvent event) {
    	currentStage(event).close();
    }
    
    private Stage currentStage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}

    @FXML
    void onLogin(ActionEvent event) throws IOException {
    	String userName = txtUserName.getText();
    	String password = txtPassword.getText();
    	
    	if(userName.isEmpty() || password.isEmpty()) {
    		com.NotificationBox("Notification", "Please Enter Value in UserName or Password");
    	}else {
    		ResultSet rs = dh.executeQuery("select password from `adesai35_users` where username = '"+userName+"'");
    		String pass = "";
    		try {
				while(rs.next()) {
					pass = rs.getString(1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		if(password.equals(pass)) {
    			currentStage(event).close();
    			initHomeScreen();
    			
    		}else {
    			com.NotificationBox("Notification", "User Name or Password is worng");
    		}
    	}
    	
    }
    
    private void initHomeScreen() throws IOException {
    	Stage window = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/HomeView.fxml"));
		Parent root = (Parent) loader.load();
		window.setTitle("Billing System");

		Scene scene = new Scene(root);
		window.setScene(scene);
		window.show();
    }

}
