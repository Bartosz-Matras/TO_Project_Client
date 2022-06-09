package com.example.projektto1.controller;

import com.example.projektto1.dto.EmployeeDto;
import com.example.projektto1.handler.EmployeeLoadedHandler;
import com.example.projektto1.rest.EmployeeRestClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewEmployeeController implements Initializable
{
    private final EmployeeRestClient employeeRestClient;


    public ViewEmployeeController(){
        employeeRestClient = new EmployeeRestClient();
    }

    @FXML
    public BorderPane viewEmployeeBorderPane;

    @FXML
    public Button okButton;

    @FXML
    public TextField firstNameTextField;

    @FXML
    public TextField lastNameTextField;

    @FXML
    public TextField salaryTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        firstNameTextField.setEditable(false);
        lastNameTextField.setEditable(false);
        salaryTextField.setEditable(false);
        initializeOkButton();
    }

    public void loadEmployeeData(Long idEmployee, EmployeeLoadedHandler handler){
        Thread thread = new Thread(() -> {
            EmployeeDto dto = employeeRestClient.getEmployee(idEmployee);
            Platform.runLater(() -> {
                firstNameTextField.setText(dto.getFirstName());
                lastNameTextField.setText(dto.getLastName());
                salaryTextField.setText(dto.getSalary());
                handler.handle();
            });
        });
        thread.start();
    }

    private void initializeOkButton() {
        okButton.setOnAction(x -> {
            getStage().close();
        });
    }

    private Stage getStage(){
        return (Stage) viewEmployeeBorderPane.getScene().getWindow();
    }
}
