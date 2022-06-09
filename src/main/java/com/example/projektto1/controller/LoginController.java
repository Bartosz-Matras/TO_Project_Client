package com.example.projektto1.controller;


import com.example.projektto1.Main;
import com.example.projektto1.dto.OperatorCredentialsDto;
import com.example.projektto1.factory.PopupFactory;
import com.example.projektto1.rest.Authenticator;
import com.example.projektto1.rest.AuthenticatorImpl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    private static final String APP_FXML = "app.fxml";
    private static final String TITLE = "ERP System";

    private PopupFactory popupFactory;
    private Authenticator authenticator;

    @FXML
    public AnchorPane loginAnchorPane;
    @FXML
    public Button exitButton;
    @FXML
    public Button loginButton;
    @FXML
    public TextField loginTextField;
    @FXML
    public PasswordField passwordTextField;

    public LoginController(){
        popupFactory = new PopupFactory();
        authenticator = new AuthenticatorImpl();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeExitButton();
        initializeLoginButton();
    }

    private void initializeLoginButton() {
        loginButton.setOnAction((x) -> {
            performAuthentication();
        });
    }

    private void performAuthentication() {
        Stage waitingPopup = popupFactory.createWaitingPopup("Connecting to the server....");
        waitingPopup.show();

        String login = loginTextField.getText();
        String password = passwordTextField.getText();

        OperatorCredentialsDto dto = new OperatorCredentialsDto();
        dto.setLogin(login);
        dto.setPassword(password);

        authenticator.authenticate(dto, (authenticationResult) ->{
            Platform.runLater(() -> {
                waitingPopup.close();
                if(authenticationResult.isAuthenticated()){
                    openAppAndCloseLoginStage();
                }else{
                    showIncorrectCredentialsMessage();
                }
            });
        });
    }

    private void openAppAndCloseLoginStage() {
        Stage appStage = new Stage();
        Scene scene = null;

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(APP_FXML));
        try {
            scene = new Scene(fxmlLoader.load(), 1024, 768);
        } catch (IOException e) {
            e.printStackTrace();
        }

        appStage.setTitle(TITLE);
        appStage.setScene(scene);
        appStage.show();
        getStage().close();
    }

    private void showIncorrectCredentialsMessage() {
        //TODO
        System.out.println("Incorrect credentials");
    }

    private void initializeExitButton() {
        exitButton.setOnAction((x) -> {
            getStage().close();
        });
    }


    private Stage getStage(){
        return (Stage) loginAnchorPane.getScene().getWindow();
    }

}
