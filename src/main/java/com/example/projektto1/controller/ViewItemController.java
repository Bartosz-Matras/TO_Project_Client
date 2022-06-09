package com.example.projektto1.controller;

import com.example.projektto1.dto.ItemDto;
import com.example.projektto1.dto.ItemSaveDto;
import com.example.projektto1.dto.QuantityTypeDto;
import com.example.projektto1.rest.ItemRestClient;
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
import java.util.ResourceBundle;

public class ViewItemController implements Initializable {

    private final ItemRestClient itemRestClient;

    @FXML
    public BorderPane viewItemBorderPane;

    @FXML
    public TextField nameTextField;

    @FXML
    public TextField quantityTextField;

    @FXML
    public ComboBox<String> quantityTypeComboBox;

    @FXML
    public Button okButton;

    public ViewItemController() {
        itemRestClient = new ItemRestClient();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeControls();
        initializeOkButton();
    }

    private void initializeControls() {
        nameTextField.setEditable(false);
        quantityTextField.setEditable(false);
        quantityTypeComboBox.setEditable(false);
    }

    private void initializeOkButton() {
        okButton.setOnAction(x -> {
            getStage().close();
        });
    }

    public void loadItemData(Long idItem){
        Thread thread = new Thread(() ->{
            ItemDto itemDto = itemRestClient.getItem(idItem);
            Platform.runLater(() -> {
                nameTextField.setText(itemDto.getName());
                quantityTextField.setText(itemDto.getQuantity().toString());
                quantityTypeComboBox.setItems(FXCollections.observableArrayList(itemDto.getQuantityType()));
                quantityTypeComboBox.getSelectionModel().select(0);
            });
        });
        thread.start();
    }

    private Stage getStage(){
        return (Stage) viewItemBorderPane.getScene().getWindow();
    }
}
