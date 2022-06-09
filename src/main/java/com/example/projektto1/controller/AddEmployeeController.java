package com.example.projektto1.controller;

import com.example.projektto1.dto.EmployeeDto;
import com.example.projektto1.factory.PopupFactory;
import com.example.projektto1.rest.EmployeeRestClient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddEmployeeController implements Initializable {


    private final PopupFactory popupFactory;
    private final EmployeeRestClient employeeRestClient;

    public AddEmployeeController() {
        popupFactory = new PopupFactory();
        employeeRestClient = new EmployeeRestClient();
    }

    @FXML
    public BorderPane addEmployeeBorderPane;

    @FXML
    public Button saveButton;

    @FXML
    public Button cancelButton;

    @FXML
    public TextField firstNameTextField;

    @FXML
    public TextField lastNameTextField;

    @FXML
    public TextField salaryTextField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeCancelButton();
        initializeSaveButton();
    }

    private void initializeSaveButton() {
        saveButton.setOnAction(event -> {
            EmployeeDto dto = createEmployeeDto();
            Stage waitingPopup = popupFactory.createWaitingPopup("Connecting to the server...");
            waitingPopup.show();
            employeeRestClient.saveEmployee(dto, () -> {
                waitingPopup.close();
                Stage info = popupFactory.createInfoPopup("Employee has been saved", () -> {
                    getStage().close();
                });
                info.show();
            });
        });
    }

    private EmployeeDto createEmployeeDto() {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String salary = salaryTextField.getText();
        EmployeeDto dto = new EmployeeDto();
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setSalary(salary);
        return dto;
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction(event -> {
            getStage().close();
        });
    }

    private Stage getStage(){
        return (Stage) addEmployeeBorderPane.getScene().getWindow();
    }

}
