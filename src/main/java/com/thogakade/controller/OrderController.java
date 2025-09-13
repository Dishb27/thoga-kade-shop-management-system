package com.thogakade.controller;

import com.thogakade.dao.CustomerDAO;
import com.thogakade.dao.OrderDAO;
import com.thogakade.model.Customer;
import com.thogakade.model.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class OrderController implements Initializable {

    @FXML private TextField txtOrderId;
    @FXML private DatePicker dpOrderDate;
    @FXML private ComboBox<String> cmbCustomerId;
    @FXML private TextField txtOrderTotal;

    @FXML private TableView<Order> tblOrders;
    @FXML private TableColumn<Order, String> colOrderId;
    @FXML private TableColumn<Order, LocalDate> colOrderDate;
    @FXML private TableColumn<Order, String> colCustomerId;
    @FXML private TableColumn<Order, Double> colOrderTotal;

    private OrderDAO orderDAO;
    private CustomerDAO customerDAO;
    private ObservableList<Order> orders;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        orderDAO = new OrderDAO();
        customerDAO = new CustomerDAO();

        initializeTable();
        loadCustomers();
        loadOrders();

        tblOrders.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        selectOrder(newValue);
                    }
                }
        );
    }

    private void initializeTable() {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colOrderTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    private void loadCustomers() {
        ObservableList<Customer> customers = customerDAO.getAllCustomers();
        ObservableList<String> customerIds = FXCollections.observableArrayList();

        for (Customer customer : customers) {
            customerIds.add(customer.getId());
        }

        cmbCustomerId.setItems(customerIds);
    }

    private void loadOrders() {
        orders = orderDAO.getAllOrders();
        tblOrders.setItems(orders);
    }

    private void selectOrder(Order order) {
        txtOrderId.setText(order.getOrderId());
        dpOrderDate.setValue(order.getOrderDate());
        cmbCustomerId.setValue(order.getCustomerId());
        txtOrderTotal.setText(String.valueOf(order.getTotal()));
    }

    @FXML
    private void addOrder() {
        if (validateInput()) {
            Order order = new Order(
                    txtOrderId.getText(),
                    dpOrderDate.getValue(),
                    cmbCustomerId.getValue(),
                    Double.parseDouble(txtOrderTotal.getText())
            );

            if (orderDAO.addOrder(order)) {
                orders.add(order);
                clearFields();
                showAlert("Success", "Order added successfully!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "Failed to add order!", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void updateOrder() {
        Order selectedOrder = tblOrders.getSelectionModel().getSelectedItem();
        if (selectedOrder != null && validateInput()) {
            selectedOrder.setOrderDate(dpOrderDate.getValue());
            selectedOrder.setCustomerId(cmbCustomerId.getValue());
            selectedOrder.setTotal(Double.parseDouble(txtOrderTotal.getText()));

            if (orderDAO.updateOrder(selectedOrder)) {
                tblOrders.refresh();
                clearFields();
                showAlert("Success", "Order updated successfully!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "Failed to update order!", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Warning", "Please select an order to update!", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void deleteOrder() {
        Order selectedOrder = tblOrders.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Delete Order");
            alert.setContentText("Are you sure you want to delete this order?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
                orderDetailDAO.deleteAllOrderDetails(selectedOrder.getOrderId());

                if (orderDAO.deleteOrder(selectedOrder.getOrderId())) {
                    orders.remove(selectedOrder);
                    clearFields();
                    showAlert("Success", "Order deleted successfully!", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Error", "Failed to delete order!", Alert.AlertType.ERROR);
                }
            }
        } else {
            showAlert("Warning", "Please select an order to delete!", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clearFields() {
        txtOrderId.clear();
        dpOrderDate.setValue(null);
        cmbCustomerId.setValue(null);
        txtOrderTotal.clear();
        tblOrders.getSelectionModel().clearSelection();
    }

    private boolean validateInput() {
        if (txtOrderId.getText().isEmpty() || dpOrderDate.getValue() == null ||
                cmbCustomerId.getValue() == null || txtOrderTotal.getText().isEmpty()) {
            showAlert("Warning", "Please fill all fields!", Alert.AlertType.WARNING);
            return false;
        }

        try {
            Double.parseDouble(txtOrderTotal.getText());
        } catch (NumberFormatException e) {
            showAlert("Warning", "Total must be a valid number!", Alert.AlertType.WARNING);
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