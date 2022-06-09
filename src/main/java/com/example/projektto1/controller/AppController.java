package com.example.projektto1.controller;

import com.example.projektto1.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    private final static String EMPLOYEE_MODULE_VIEW = "employee.fxml";
    private final static String WAREHOUSE_MODULE_VIEW = "warehouse.fxml";
    private final static String LOGIN_VIEW = "login.fxml";

    @FXML
    public BorderPane appBorderPane;

    @FXML
    public Pane appPane;

    @FXML
    public MenuItem employeeModuleMenuItem;

    @FXML
    public MenuItem warehouseModuleMenuItem;

    @FXML
    public MenuItem logoutMenuItem;

    @FXML
    public MenuItem exitMenuItem;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDefaultView();
        initializeMenuItems();
    }

    private void initializeMenuItems() {
        warehouseModuleMenuItem.setOnAction(x -> {
            loadModuleView(WAREHOUSE_MODULE_VIEW);
        });
        employeeModuleMenuItem.setOnAction(x -> {
            loadModuleView(EMPLOYEE_MODULE_VIEW);
        });
        logoutMenuItem.setOnAction(x -> logout());
        exitMenuItem.setOnAction(x -> {
            getStage().close();
        });
    }

    private void logout() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(Main.class.getResource(LOGIN_VIEW));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root, Main.WIDTH, Main.HEIGHT));
            stage.show();
            getStage().close();
        } catch (IOException e) {
            throw new RuntimeException("Can't load fxml file: " + LOGIN_VIEW);
        }
    }

    private void loadDefaultView() {
        loadModuleView(EMPLOYEE_MODULE_VIEW);
    }

    private void loadModuleView(String viewPath){
        appPane.getChildren().clear();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(viewPath));
            BorderPane borderPane = fxmlLoader.load();
            appPane.getChildren().add(borderPane);
        } catch (IOException e) {
            throw new RuntimeException("Can't load fxml file: " + viewPath);
        }
    }

    private Stage getStage(){
        return (Stage) appBorderPane.getScene().getWindow();
    }


}
