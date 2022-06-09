package com.example.projektto1.controller;

import com.example.projektto1.dto.EmployeeDto;
import com.example.projektto1.factory.PopupFactory;
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

public class EditEmployeeController implements Initializable {

    private final EmployeeRestClient employeeRestClient;
    private final PopupFactory popupFactory;

    private Long idEmployee;

    public EditEmployeeController() {
        this.employeeRestClient = new EmployeeRestClient();
        this.popupFactory = new PopupFactory();
    }

    @FXML
    public BorderPane editEmployeeBorderPane;

    @FXML
    public Button editButton;

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
        initializeEditButton();
        initializeCancelButton();
    }

    private void initializeEditButton() {
        editButton.setOnAction(event -> {
            Stage waitingPopup = popupFactory.createWaitingPopup("Connecting to the server...");
            waitingPopup.show();
            Thread thread = new Thread(() -> {
                EmployeeDto dto = createEmployeeDto();
                employeeRestClient.saveEmployee(dto, () -> {
                    Platform.runLater(() -> {
                        waitingPopup.close();
                        Stage infoPopup = popupFactory.createInfoPopup("Employee has been updated", () -> {
                            getStage().close();
                        });
                        infoPopup.show();
                    });
                });
            });
            thread.start();
        });
    }

    private EmployeeDto createEmployeeDto() {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String salary = salaryTextField.getText();
        EmployeeDto dto = new EmployeeDto();
        dto.setIdEmployee(idEmployee);
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setSalary(salary);
        return dto;
    }

    public void loadEmployeeData(Long idEmployee, EmployeeLoadedHandler handler){
        Thread thread = new Thread(() -> {
            EmployeeDto dto = employeeRestClient.getEmployee(idEmployee);
            Platform.runLater(() -> {
                this.idEmployee = idEmployee;
                firstNameTextField.setText(dto.getFirstName());
                lastNameTextField.setText(dto.getLastName());
                salaryTextField.setText(dto.getSalary());
                handler.handle();
            });
        });
        thread.start();
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction(event -> {
            getStage().close();
        });
    }

    private Stage getStage(){
        return (Stage) editEmployeeBorderPane.getScene().getWindow();
    }

}
