package com.example.projektto1.controller;

import com.example.projektto1.dto.ItemSaveDto;
import com.example.projektto1.dto.QuantityTypeDto;
import com.example.projektto1.dto.WarehouseDto;
import com.example.projektto1.handler.ProcessFinishedHandler;
import com.example.projektto1.rest.ItemRestClient;
import com.example.projektto1.rest.QuantityTypeRestClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddItemController implements Initializable {

    private WarehouseDto selectedWarehouseDto;

    private final ItemRestClient itemRestClient;

    private final QuantityTypeRestClient quantityTypeRestClient;

    public AddItemController(){
        itemRestClient = new ItemRestClient();
        quantityTypeRestClient = new QuantityTypeRestClient();
    }

    @FXML
    public BorderPane addItemBorderPane;

    @FXML
    public TextField nameTextField;

    @FXML
    public TextField quantityTextField;

    @FXML
    public ComboBox<QuantityTypeDto> quantityTypeComboBox;

    @FXML
    public Button saveButton;

    @FXML
    public Button cancelButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeSaveButton();
        initializeCancelButton();
    }

    private void initializeSaveButton() {
        saveButton.setOnAction(event -> {
            String name = nameTextField.getText();
            Double quantity;
            try{
                quantity = Double.parseDouble(quantityTextField.getText());
            }catch (NumberFormatException exception){
                quantity = 0.0;
            }
            Long idQuantityType = quantityTypeComboBox.getSelectionModel().getSelectedItem().getIdQuantityType();
            Long idWarehouse = selectedWarehouseDto.getIdWarehouse();

            ItemSaveDto dto = new ItemSaveDto(null, name, quantity, idQuantityType, idWarehouse);

            Thread thread = new Thread(() -> {
                itemRestClient.saveItem(dto,() -> {
                    Platform.runLater(() -> {
                        getStage().close();
                    });
                });
            });
            thread.start();
        });
    }

    public void loadQuantityTypes(){
        Thread thread = new Thread(() -> {
            List<QuantityTypeDto> quantityTypes = quantityTypeRestClient.getQuantityTypes();
            Platform.runLater(() -> {
                quantityTypeComboBox.setItems(FXCollections.observableArrayList(quantityTypes));
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
        return (Stage) addItemBorderPane.getScene().getWindow();
    }

    public void setWarehouseDto(WarehouseDto selectedWarehouseDto) {
        this.selectedWarehouseDto = selectedWarehouseDto;
    }
}
