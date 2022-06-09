package com.example.projektto1.controller;

import com.example.projektto1.Main;
import com.example.projektto1.dto.EmployeeDto;
import com.example.projektto1.factory.PopupFactory;
import com.example.projektto1.rest.EmployeeRestClient;
import com.example.projektto1.table.EmployeeTableModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EmployeeController implements Initializable {

    private static final String ADD_EMPLOYEE_FXML = "add-employee.fxml";
    private static final String VIEW_EMPLOYEE_FXML = "view-employee.fxml";
    private static final String EDIT_EMPLOYEE_FXML = "edit-employee.fxml";
    private static final String DELETE_EMPLOYEE_FXML = "delete-employee.fxml";

    private final EmployeeRestClient employeeRestClient;

    private PopupFactory popupFactory;

    private ObservableList<EmployeeTableModel> data;

    public EmployeeController(){
        employeeRestClient = new EmployeeRestClient();
        data = FXCollections.observableArrayList();
        popupFactory = new PopupFactory();
    }

    @FXML
    public TableView<EmployeeTableModel> employeeTableView;

    @FXML
    public Button addButton;

    @FXML
    public Button viewButton;

    @FXML
    public Button editButton;

    @FXML
    public Button deleteButton;

    @FXML
    public Button refreshButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeAddEmployeeButton();
        initializeViewButton();
        initializeEditButton();
        initializeDeleteButton();
        initializeRefreshButton();
        initializeTableView();
    }

    private void initializeDeleteButton() {
        deleteButton.setOnAction(event -> {
            EmployeeTableModel selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
            if(selectedEmployee != null){
                Stage deleteEmployeeStage = createEmployeeCrudStage();
                FXMLLoader loader = new FXMLLoader(Main.class.getResource(DELETE_EMPLOYEE_FXML));
                Scene scene = null;

                try {
                    scene = new Scene(loader.load(), 400, 200);
                } catch (IOException e) {
                    throw new RuntimeException("Can't load fxml file: " + EDIT_EMPLOYEE_FXML);
                }

                deleteEmployeeStage.setScene(scene);

                DeleteEmployeeController controller = loader.getController();
                controller.loadEmployeeData(selectedEmployee);
                deleteEmployeeStage.show();

            }
        });
    }

    private void initializeEditButton() {
        editButton.setOnAction(event -> {
            EmployeeTableModel selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
            if (selectedEmployee != null){
                Stage waitingPopup = popupFactory.createWaitingPopup("Loading employee data...");
                waitingPopup.show();
                Stage editEmployeeStage = createEmployeeCrudStage();
                FXMLLoader loader = new FXMLLoader(Main.class.getResource(EDIT_EMPLOYEE_FXML));
                Scene scene = null;

                try {
                    scene = new Scene(loader.load(), 500, 400);
                } catch (IOException e) {
                    throw new RuntimeException("Can't load fxml file: " + EDIT_EMPLOYEE_FXML);
                }

                editEmployeeStage.setScene(scene);
                editEmployeeStage.show();

                EditEmployeeController controller = loader.getController();
                controller.loadEmployeeData(selectedEmployee.getIdEmployee(), () ->{
                    waitingPopup.close();
                    editEmployeeStage.show();
                });
            }
        });
    }


    private Stage createEmployeeCrudStage() {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);

        return stage;
    }

    private void initializeViewButton() {
        viewButton.setOnAction(event -> {
            EmployeeTableModel employee = employeeTableView.getSelectionModel().getSelectedItem();
            if (employee == null){
                return;
            }else{
                Stage waitingPopup = popupFactory.createWaitingPopup("Loading employee data...");
                waitingPopup.show();

                Stage viewEmployeeStage = new Stage();
                viewEmployeeStage.initStyle(StageStyle.UNDECORATED);
                viewEmployeeStage.initModality(Modality.APPLICATION_MODAL);

                FXMLLoader loader = new FXMLLoader(Main.class.getResource(VIEW_EMPLOYEE_FXML));
                Scene scene = null;
                try {
                    scene = new Scene(loader.load(), 500, 400);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                viewEmployeeStage.setScene(scene);
                viewEmployeeStage.show();
                ViewEmployeeController controller = loader.getController();
                controller.loadEmployeeData(employee.getIdEmployee(), () -> {
                    waitingPopup.close();
                    viewEmployeeStage.show();
                });
            }
        });
    }

    private void initializeRefreshButton() {
        refreshButton.setOnAction(event -> {
            loadEmployeeData();
        });
    }

    private void initializeAddEmployeeButton() {
        addButton.setOnAction(event -> {
            Stage appStage = new Stage();
            Scene scene = null;

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(ADD_EMPLOYEE_FXML));
            try {
                scene = new Scene(fxmlLoader.load(), 500, 400);
            } catch (IOException e) {
                e.printStackTrace();
            }

            appStage.setScene(scene);
            appStage.initStyle(StageStyle.UNDECORATED);
            appStage.initModality(Modality.APPLICATION_MODAL);
            appStage.show();
        });
    }

    private void initializeTableView() {
        // If I add next column to table, all columns will be the same size
        employeeTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn firstNameColumn = new TableColumn("First Name");
        firstNameColumn.setMinWidth(100);
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<EmployeeTableModel, String>("firstName"));


        TableColumn lastNameColumn = new TableColumn("Last Name");
        lastNameColumn.setMinWidth(100);
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<EmployeeTableModel, String>("lastName"));


        TableColumn salaryColumn = new TableColumn("Salary");
        salaryColumn.setMinWidth(100);
        salaryColumn.setCellValueFactory(new PropertyValueFactory<EmployeeTableModel, String>("salary"));


        employeeTableView.getColumns().addAll(firstNameColumn, lastNameColumn, salaryColumn);

        loadEmployeeData();

        employeeTableView.setItems(data);
    }

    private void loadEmployeeData() {
        Thread thread = new Thread(() -> {
            List<EmployeeDto> employees = employeeRestClient.getEmployees();
            data.clear();
            data.addAll(employees.stream().map(EmployeeTableModel::of).collect(Collectors.toList()));
        });
        thread.start();
    }
}
