package com.example.projektto1.controller;

import com.example.projektto1.dto.ItemEditViewDto;
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

public class EditItemController implements Initializable {

    @FXML
    public BorderPane editItemBorderPane;

    @FXML
    public TextField nameTextField;

    @FXML
    public TextField quantityTextField;

    @FXML
    public ComboBox<QuantityTypeDto> quantityTypeComboBox;

    @FXML
    public Button editButton;

    @FXML
    public Button cancelButton;


    private final ItemRestClient itemRestClient;

    private Long idItemToEdit;

    public EditItemController() {
        itemRestClient = new ItemRestClient();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeEditButton();
        initializeCancelButton();
    }

    private void initializeEditButton() {
        editButton.setOnAction(event -> {
            String name = nameTextField.getText();
            double quantity;
            try{
                quantity = Double.parseDouble(quantityTextField.getText());
            }catch (NumberFormatException ex){
                quantity = 0.0;
            }
            Long idQuantityType = quantityTypeComboBox.getSelectionModel().getSelectedItem().getIdQuantityType();

            ItemSaveDto dto = new ItemSaveDto();
            dto.setIdItem(idItemToEdit);
            dto.setName(name);
            dto.setQuantity(quantity);
            dto.setIdQuantityType(idQuantityType);
            itemRestClient.saveItem(dto, () -> {
                getStage().close();
            });

        });
    }

    public void loadItemData(Long idItem){
        Thread thread = new Thread(() -> {
            ItemEditViewDto dto = itemRestClient.getEditItemData(idItem);
            Platform.runLater(() -> {
                idItemToEdit = dto.getIdItem();
                nameTextField.setText(dto.getName());
                quantityTextField.setText(dto.getQuantity().toString());
                quantityTypeComboBox.setItems(FXCollections.observableArrayList(dto.getQuantityTypeDtoList()));
                for(int i = 0; quantityTypeComboBox.getItems().size() > i; i++){
                    QuantityTypeDto quantityTypeDto = quantityTypeComboBox.getItems().get(i);
                    if (quantityTypeDto.getIdQuantityType().equals(dto.getIdQuantityType())){
                        quantityTypeComboBox.getSelectionModel().select(i);
                    }
                }
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
        return (Stage) editItemBorderPane.getScene().getWindow();
    }
}
