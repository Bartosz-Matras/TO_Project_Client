package com.example.projektto1.controller;

import com.example.projektto1.Main;
import com.example.projektto1.dto.ItemDto;
import com.example.projektto1.dto.WarehouseDto;
import com.example.projektto1.dto.WarehouseModuleDto;
import com.example.projektto1.rest.ItemRestClient;
import com.example.projektto1.rest.WarehouseRestClient;
import com.example.projektto1.table.ItemTableModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class WarehouseController implements Initializable {

    private static final String ADD_ITEM_FXML = "add-item.fxml";
    private static final String VIEW_ITEM_FXML = "view-item.fxml";
    private static final String EDIT_ITEM_FXML = "edit-item.fxml";
    private static final String DELETE_ITEM_FXML = "delete-item.fxml";

    @FXML
    public BorderPane warehouseBorderPane;

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

    @FXML
    public TableView<ItemTableModel> warehouseTableView;

    @FXML
    public ComboBox<WarehouseDto> warehouseComboBox;

    private final ItemRestClient itemRestClient;

    private final WarehouseRestClient warehouseRestClient;

    private ObservableList<ItemTableModel> data;

    public WarehouseController() {
        itemRestClient = new ItemRestClient();
        data = FXCollections.observableArrayList();
        warehouseRestClient = new WarehouseRestClient();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableView();
        initializeComboBox();
        initializeAddItemButton();
        initializeViewItemButton();
        initializeEditItemButton();
        initializeDeleteItemButton();
        initializeRefreshButton();
    }

    private void initializeRefreshButton() {
        refreshButton.setOnAction(event -> {
            loadItemData();
        });
    }

    private void initializeDeleteItemButton() {
        deleteButton.setOnAction(event -> {
            ItemTableModel selectedItem = warehouseTableView.getSelectionModel().getSelectedItem();
            if(selectedItem == null){
                return;
            }else{
                try{
                    Stage stage = createItemCrudStage();
                    FXMLLoader loader = new FXMLLoader(Main.class.getResource(DELETE_ITEM_FXML));
                    Scene scene = new Scene(loader.load(), 400, 200);
                    stage.setScene(scene);
                    DeleteItemController controller = loader.getController();
                    controller.loadItem(selectedItem);
                    stage.show();
                }catch (IOException e){
                    throw new RuntimeException("Can't load fxml file: " + DELETE_ITEM_FXML);
                }
            }
        });
    }

    private void initializeEditItemButton() {
        editButton.setOnAction(event -> {
            ItemTableModel selectedItem = warehouseTableView.getSelectionModel().getSelectedItem();
            if(selectedItem == null){
                return;
            }else{
                try{
                    Stage stage = createItemCrudStage();
                    FXMLLoader loader = new FXMLLoader(Main.class.getResource(EDIT_ITEM_FXML));
                    Scene scene = new Scene(loader.load(), 500, 400);
                    stage.setScene(scene);
                    EditItemController controller = loader.getController();
                    controller.loadItemData(selectedItem.getIdItem());
                    stage.show();
                }catch (IOException e){
                    throw new RuntimeException("Can't load fxml file: " + EDIT_ITEM_FXML);
                }
            }
        });
    }

    private void initializeViewItemButton() {
        viewButton.setOnAction(event -> {
            ItemTableModel selectedItem = warehouseTableView.getSelectionModel().getSelectedItem();
            if(selectedItem == null){
                return;
            }else{
                try{
                    Stage stage = createItemCrudStage();
                    FXMLLoader loader = new FXMLLoader(Main.class.getResource(VIEW_ITEM_FXML));
                    Scene scene = new Scene(loader.load(), 500, 400);
                    stage.setScene(scene);
                    ViewItemController controller = loader.getController();
                    controller.loadItemData(selectedItem.getIdItem());
                    stage.show();

                }catch (IOException e){
                    throw new RuntimeException("Can't load fxml file: " + VIEW_ITEM_FXML);
                }
            }
        });
    }

    private void initializeAddItemButton() {
        addButton.setOnAction(x -> {
            try {
                Stage stage = createItemCrudStage();
                FXMLLoader loader = new FXMLLoader(Main.class.getResource(ADD_ITEM_FXML));
                Scene scene = new Scene(loader.load(),500, 400 );
                stage.setScene(scene);
                AddItemController controller = loader.getController();
                WarehouseDto selectedWarehouseDto = warehouseComboBox.getSelectionModel().getSelectedItem();
                controller.setWarehouseDto(selectedWarehouseDto);
                controller.loadQuantityTypes();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private Stage createItemCrudStage() {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        return stage;
    }

    private void initializeComboBox() {
        warehouseComboBox.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == null){
                return;
            }
            if(!newValue.equals(oldValue) && oldValue != null){
                WarehouseDto warehouseDto =  warehouseComboBox.getSelectionModel().getSelectedItem();
                loadItemData(warehouseDto);
            }
        }));
    }

    private void initializeTableView() {
        // If I add next column to table, all columns will be the same size
        warehouseTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn nameColumn = new TableColumn("Name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<ItemTableModel, String>("name"));

        TableColumn quantityColumn = new TableColumn("Quantity");
        quantityColumn.setMinWidth(100);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<ItemTableModel, Double>("quantity"));

        TableColumn quantityTypeColumn = new TableColumn("Quantity Type");
        quantityTypeColumn.setMinWidth(100);
        quantityTypeColumn.setCellValueFactory(new PropertyValueFactory<ItemTableModel, String>("quantityType"));

        warehouseTableView.getColumns().addAll(nameColumn, quantityColumn, quantityTypeColumn);

        warehouseTableView.setItems(data);

        loadItemData();
    }

    private void loadItemData(){
        Thread thread = new Thread(() -> {
            WarehouseModuleDto warehouseModuleDto = warehouseRestClient.getWarehouseModuleData();
            data.clear();
            setWarehouseComboBoxItems(warehouseModuleDto);
            data.addAll(warehouseModuleDto.getItemDtoList().stream().map(ItemTableModel::of).collect(Collectors.toList()));
        });
        thread.start();
    }


    private void loadItemData(WarehouseDto warehouseDto){
        Thread thread = new Thread(() -> {
            WarehouseModuleDto warehouseModuleDto = warehouseRestClient.getWarehouseModuleData(warehouseDto.getIdWarehouse());
            data.clear();
            setWarehouseComboBoxItems(warehouseModuleDto);
            data.addAll(warehouseModuleDto.getItemDtoList().stream().map(ItemTableModel::of).collect(Collectors.toList()));
        });
        thread.start();
    }

    private void setWarehouseComboBoxItems(WarehouseModuleDto warehouseModuleDto) {
        List<WarehouseDto> warehouseDtoList = warehouseModuleDto.getWarehouseDtoList();
        ObservableList<WarehouseDto> warehouseComboBoxItems = FXCollections.observableArrayList();

        Platform.runLater(() -> {
            warehouseComboBoxItems.addAll(warehouseDtoList);
            warehouseComboBox.setItems(warehouseComboBoxItems);
            warehouseComboBox.getSelectionModel().select(warehouseDtoList.indexOf(warehouseModuleDto.getSelectedWarehouse()));
        });
    }

}
