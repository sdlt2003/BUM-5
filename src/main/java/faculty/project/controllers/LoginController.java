package faculty.project.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import faculty.project.businessLogic.BlFacade;
import faculty.project.ui.MainGUI;
import faculty.project.uicontrollers.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements Controller {

        private BlFacade bl;

        public LoginController(BlFacade bl) {
                this.bl = bl;
        }

        @FXML
        private TextField login;

        @FXML
        private PasswordField password;


        @FXML
        private ResourceBundle resources;

        @FXML
        private URL location;
        private MainGUI mainGUI;

        @FXML
        void onClick(ActionEvent event) {
                System.out.println(login.getText());
                System.out.println(password.getText());
        }

        @FXML
        void initialize() {

        }

        @Override
        public void setMainApp(MainGUI mainGUI) {
                this.mainGUI = mainGUI;
        }
}
