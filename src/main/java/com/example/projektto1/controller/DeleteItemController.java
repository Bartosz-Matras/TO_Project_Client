package com.example.projektto1.controller;

import com.example.projektto1.rest.ItemRestClient;
import com.example.projektto1.table.ItemTableModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteItemController implements Initializable {

    @FXML
    public BorderPane deleteItemBorderPane;

    @FXML
    public Label nameLabel;

    @FXML
    public Label quantityLabel;

    @FXML
    public Button deleteButton;

    @FXML
    public Button cancelButton;

    private final ItemRestClient itemRestClient;

    private Long idItemToDelete;
    
    public DeleteItemController() {
        itemRestClient = new ItemRestClient();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeCancelButton();
        initializeDeleteButton();
    }

    public void loadItem(ItemTableModel itemTableModel){
        nameLabel.setText(itemTableModel.getName());
        quantityLabel.setText(itemTableModel.getQuantity() + " " + itemTableModel.getQuantityType());
        this.idItemToDelete = itemTableModel.getIdItem();
    }

    private void initializeDeleteButton() {
        deleteButton.setOnAction(x -> {
            Thread thread = new Thread(() -> {
                itemRestClient.deleteItem(idItemToDelete);
                Platform.runLater(() -> {
                    getStage().close();
                });
            });
            thread.start();
        });
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction((x) -> {
            getStage().close();
        });
    }

    private Stage getStage(){
        return (Stage) deleteItemBorderPane.getScene().getWindow();
    }

}
