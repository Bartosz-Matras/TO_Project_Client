package com.example.projektto1.controller;

import com.example.projektto1.factory.PopupFactory;
import com.example.projektto1.rest.EmployeeRestClient;
import com.example.projektto1.table.EmployeeTableModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteEmployeeController implements Initializable {

    private final EmployeeRestClient employeeRestClient;
    private final PopupFactory popupFactory;

    private Long idEmployee;

    public DeleteEmployeeController() {
        employeeRestClient = new EmployeeRestClient();
        popupFactory = new PopupFactory();
    }

    @FXML
    public BorderPane deleteEmployeeBorderPane;

    @FXML
    public Label firstNameLabel;

    @FXML
    public Label lastNameLabel;

    @FXML
    public Button deleteButton;

    @FXML
    public Button cancelButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeDeleteButton();
        initializeCancelButton();
    }

    public void loadEmployeeData(EmployeeTableModel employee){
            this.idEmployee = employee.getIdEmployee();
            firstNameLabel.setText(employee.getFirstName());
            lastNameLabel.setText(employee.getLastName());
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction((x) -> {
            getStage().close();
        });
    }

    private void initializeDeleteButton() {
        deleteButton.setOnAction((x) -> {
            Stage waitingPopup = popupFactory.createWaitingPopup("Deleting employee...");
            waitingPopup.show();

            Thread thread = new Thread(() -> {
                Platform.runLater(() -> {
                    employeeRestClient.deleteEmployee(idEmployee, () -> {
                        waitingPopup.close();
                        Stage infoPopup = popupFactory.createInfoPopup("Employee has been deleted", () -> {
                            getStage().close();
                        });
                        infoPopup.show();
                    });
                });
            });
            thread.start();
        });
    }


    private Stage getStage(){
        return (Stage) deleteEmployeeBorderPane.getScene().getWindow();
    }

}
