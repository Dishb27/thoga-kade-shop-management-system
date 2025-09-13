package com.thogakade.controller;

import com.thogakade.dao.ItemDAO;
import com.thogakade.dao.OrderDAO;
import com.thogakade.dao.OrderDetailDAO;
import com.thogakade.model.Item;
import com.thogakade.model.OrderDetail;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class OrderDetailController implements Initializable {

    @FXML private TextField txtOrderId;
    @FXML private ComboBox<String> cmbItemCode;
    @FXML private TextField txtQty;
    @FXML private TextField txtUnitPrice;
    @FXML private TextField txtDiscount;
    @FXML private TextField txtTotal;

    @FXML private TableView<OrderDetail> tblOrderDetails;
    @FXML private TableColumn<OrderDetail, String> colOrderId;
    @FXML private TableColumn<OrderDetail, String> colItemCode;
    @FXML private TableColumn<OrderDetail, Integer> colQty;
    @FXML private TableColumn<OrderDetail, Double> colUnitPrice;
    @FXML private TableColumn<OrderDetail, Double> colDiscount;
    @FXML private TableColumn<OrderDetail, Double> colDetailTotal;

    private OrderDetailDAO orderDetailDAO;
    private ItemDAO itemDAO;
    private ObservableList<OrderDetail> orderDetails;
    private String currentOrderId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        orderDetailDAO = new OrderDetailDAO();
        itemDAO = new ItemDAO();

        initializeTable();
        loadItems();

        tblOrderDetails.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        selectOrderDetail(newValue);
                    }
                }
        );
    }

    public void setOrderId(String orderId) {
        this.currentOrderId = orderId;
        txtOrderId.setText(orderId);
        loadOrderDetails();
    }

    private void initializeTable() {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colDetailTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    private void loadItems() {
        ObservableList<Item> items = itemDAO.getAllItems();
        ObservableList<String> itemCodes = FXCollections.observableArrayList();

        for (Item item : items) {
            itemCodes.add(item.getCode());
        }

        cmbItemCode.setItems(itemCodes);

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        Item selectedItem = itemDAO.getAllItems().stream()
                                .filter(item -> item.getCode().equals(newValue))
                                .findFirst()
                                .orElse(null);
                        if (selectedItem != null) {
                            txtUnitPrice.setText(String.valueOf(selectedItem.getUnitPrice()));
                            calculateTotal();
                        }
                    }
                }
        );
    }

    private void loadOrderDetails() {
        if (currentOrderId != null) {
            orderDetails = orderDetailDAO.getOrderDetailsByOrderId(currentOrderId);
            tblOrderDetails.setItems(orderDetails);
        }
    }

    private void selectOrderDetail(OrderDetail orderDetail) {
        cmbItemCode.setValue(orderDetail.getItemCode());
        txtQty.setText(String.valueOf(orderDetail.getQty()));
        txtUnitPrice.setText(String.valueOf(orderDetail.getUnitPrice()));
        txtDiscount.setText(String.valueOf(orderDetail.getDiscount()));
        txtTotal.setText(String.valueOf(orderDetail.getTotal()));
    }

    @FXML
    private void calculateTotal() {
        if (!txtQty.getText().isEmpty() && !txtUnitPrice.getText().isEmpty()) {
            try {
                int qty = Integer.parseInt(txtQty.getText());
                double unitPrice = Double.parseDouble(txtUnitPrice.getText());
                double discount = txtDiscount.getText().isEmpty() ? 0 : Double.parseDouble(txtDiscount.getText());

                double total = (qty * unitPrice) * (1 - discount / 100);
                txtTotal.setText(String.format("%.2f", total));
            } catch (NumberFormatException e) {
                // Ignore invalid input
            }
        }
    }

    @FXML
    private void addOrderDetail() {
        if (validateInput()) {
            OrderDetail orderDetail = new OrderDetail(
                    currentOrderId,
                    cmbItemCode.getValue(),
                    Integer.parseInt(txtQty.getText()),
                    Double.parseDouble(txtUnitPrice.getText()),
                    Double.parseDouble(txtDiscount.getText()),
                    Double.parseDouble(txtTotal.getText())
            );

            if (orderDetailDAO.addOrderDetail(orderDetail)) {
                orderDetails.add(orderDetail);
                updateOrderTotal();
                clearFields();
                showAlert("Success", "Order detail added successfully!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "Failed to add order detail!", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void updateOrderDetail() {
        OrderDetail selectedOrderDetail = tblOrderDetails.getSelectionModel().getSelectedItem();
        if (selectedOrderDetail != null && validateInput()) {
            selectedOrderDetail.setItemCode(cmbItemCode.getValue());
            selectedOrderDetail.setQty(Integer.parseInt(txtQty.getText()));
            selectedOrderDetail.setUnitPrice(Double.parseDouble(txtUnitPrice.getText()));
            selectedOrderDetail.setDiscount(Double.parseDouble(txtDiscount.getText()));
            selectedOrderDetail.setTotal(Double.parseDouble(txtTotal.getText()));

            if (orderDetailDAO.updateOrderDetail(selectedOrderDetail)) {
                tblOrderDetails.refresh();
                updateOrderTotal();
                clearFields();
                showAlert("Success", "Order detail updated successfully!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "Failed to update order detail!", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Warning", "Please select an order detail to update!", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void deleteOrderDetail() {
        OrderDetail selectedOrderDetail = tblOrderDetails.getSelectionModel().getSelectedItem();
        if (selectedOrderDetail != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Delete Order Detail");
            alert.setContentText("Are you sure you want to delete this order detail?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                if (orderDetailDAO.deleteOrderDetail(
                        selectedOrderDetail.getOrderId(),
                        selectedOrderDetail.getItemCode())) {

                    orderDetails.remove(selectedOrderDetail);
                    updateOrderTotal();
                    clearFields();
                    showAlert("Success", "Order detail deleted successfully!", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Error", "Failed to delete order detail!", Alert.AlertType.ERROR);
                }
            }
        } else {
            showAlert("Warning", "Please select an order detail to delete!", Alert.AlertType.WARNING);
        }
    }

    private void updateOrderTotal() {
        double orderTotal = orderDetails.stream()
                .mapToDouble(OrderDetail::getTotal)
                .sum();

        OrderDAO orderDAO = new OrderDAO();
        com.thogakade.model.Order order = orderDAO.getOrderById(currentOrderId);
        if (order != null) {
            order.setTotal(orderTotal);
            orderDAO.updateOrder(order);
        }
    }

    @FXML
    private void clearFields() {
        cmbItemCode.setValue(null);
        txtQty.clear();
        txtUnitPrice.clear();
        txtDiscount.clear();
        txtTotal.clear();
        tblOrderDetails.getSelectionModel().clearSelection();
    }

    private boolean validateInput() {
        if (cmbItemCode.getValue() == null || txtQty.getText().isEmpty() ||
                txtUnitPrice.getText().isEmpty() || txtDiscount.getText().isEmpty()) {
            showAlert("Warning", "Please fill all fields!", Alert.AlertType.WARNING);
            return false;
        }

        try {
            Integer.parseInt(txtQty.getText());
            Double.parseDouble(txtUnitPrice.getText());
            Double.parseDouble(txtDiscount.getText());
        } catch (NumberFormatException e) {
            showAlert("Warning", "Please enter valid numbers!", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}