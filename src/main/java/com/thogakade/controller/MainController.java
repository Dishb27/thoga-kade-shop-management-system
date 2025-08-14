package com.thogakade.controller;

import com.thogakade.dao.CustomerDAO;
import com.thogakade.dao.ItemDAO;
import com.thogakade.database.DatabaseManager;
import com.thogakade.model.Customer;
import com.thogakade.model.Item;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    // Customer Tab Controls
    @FXML private TextField txtCustomerId;
    @FXML private ComboBox<String> cmbCustomerTitle;
    @FXML private TextField txtCustomerName;
    @FXML private DatePicker dpCustomerDOB;
    @FXML private TextField txtCustomerSalary;
    @FXML private TextField txtCustomerAddress;
    @FXML private TextField txtCustomerCity;
    @FXML private TextField txtCustomerProvince;
    @FXML private TextField txtCustomerPostal;
    @FXML private TableView<Customer> tblCustomers;
    @FXML private TableColumn<Customer, String> colCustomerId;
    @FXML private TableColumn<Customer, String> colCustomerTitle;
    @FXML private TableColumn<Customer, String> colCustomerName;
    @FXML private TableColumn<Customer, LocalDate> colCustomerDOB;
    @FXML private TableColumn<Customer, Double> colCustomerSalary;
    @FXML private TableColumn<Customer, String> colCustomerAddress;
    @FXML private TableColumn<Customer, String> colCustomerCity;
    @FXML private TableColumn<Customer, String> colCustomerProvince;
    @FXML private TableColumn<Customer, String> colCustomerPostal;

    // Item Tab Controls
    @FXML private TextField txtItemCode;
    @FXML private TextField txtItemDescription;
    @FXML private TextField txtItemPackSize;
    @FXML private TextField txtItemUnitPrice;
    @FXML private TextField txtItemQtyOnHand;
    @FXML private TableView<Item> tblItems;
    @FXML private TableColumn<Item, String> colItemCode;
    @FXML private TableColumn<Item, String> colItemDescription;
    @FXML private TableColumn<Item, String> colItemPackSize;
    @FXML private TableColumn<Item, Double> colItemUnitPrice;
    @FXML private TableColumn<Item, Integer> colItemQtyOnHand;

    private CustomerDAO customerDAO;
    private ItemDAO itemDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DatabaseManager.initializeDatabase();
        customerDAO = new CustomerDAO();
        itemDAO = new ItemDAO();

        initializeCustomerComponents();
        initializeItemComponents();
        loadCustomers();
        loadItems();
    }

    private void initializeCustomerComponents() {
        // Initialize customer title combo box
        cmbCustomerTitle.getItems().addAll("Mr.", "Mrs.", "Miss", "Dr.", "Prof.");

        // Initialize customer table columns
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomerTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCustomerDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colCustomerSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colCustomerAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCustomerCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colCustomerProvince.setCellValueFactory(new PropertyValueFactory<>("province"));
        colCustomerPostal.setCellValueFactory(new PropertyValueFactory<>("postalCode"));

        // Add table selection listener
        tblCustomers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateCustomerFields(newSelection);
            }
        });
    }

    private void initializeItemComponents() {
        // Initialize item table columns
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colItemDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colItemPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        colItemUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colItemQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));

        // Add table selection listener
        tblItems.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateItemFields(newSelection);
            }
        });
    }

    // Customer CRUD Operations
    @FXML
    private void addCustomer() {
        if (validateCustomerInput()) {
            Customer customer = createCustomerFromInput();
            if (customerDAO.addCustomer(customer)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer added successfully!");
                clearCustomerFields();
                loadCustomers();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add customer!");
            }
        }
    }

    @FXML
    private void updateCustomer() {
        Customer selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a customer to update!");
            return;
        }

        if (validateCustomerInput()) {
            Customer customer = createCustomerFromInput();
            if (customerDAO.updateCustomer(customer)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer updated successfully!");
                clearCustomerFields();
                loadCustomers();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update customer!");
            }
        }
    }

    @FXML
    private void deleteCustomer() {
        Customer selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a customer to delete!");
            return;
        }

        Optional<ButtonType> result = showConfirmationAlert("Confirm Delete",
                "Are you sure you want to delete customer " + selectedCustomer.getName() + "?");

        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (customerDAO.deleteCustomer(selectedCustomer.getId())) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer deleted successfully!");
                clearCustomerFields();
                loadCustomers();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete customer!");
            }
        }
    }

    @FXML
    private void clearCustomerFields() {
        txtCustomerId.clear();
        cmbCustomerTitle.setValue(null);
        txtCustomerName.clear();
        dpCustomerDOB.setValue(null);
        txtCustomerSalary.clear();
        txtCustomerAddress.clear();
        txtCustomerCity.clear();
        txtCustomerProvince.clear();
        txtCustomerPostal.clear();
        tblCustomers.getSelectionModel().clearSelection();
    }

    // Item CRUD Operations
    @FXML
    private void addItem() {
        if (validateItemInput()) {
            Item item = createItemFromInput();
            if (itemDAO.addItem(item)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Item added successfully!");
                clearItemFields();
                loadItems();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add item!");
            }
        }
    }

    @FXML
    private void updateItem() {
        Item selectedItem = tblItems.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select an item to update!");
            return;
        }

        if (validateItemInput()) {
            Item item = createItemFromInput();
            if (itemDAO.updateItem(item)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Item updated successfully!");
                clearItemFields();
                loadItems();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update item!");
            }
        }
    }

    @FXML
    private void deleteItem() {
        Item selectedItem = tblItems.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select an item to delete!");
            return;
        }

        Optional<ButtonType> result = showConfirmationAlert("Confirm Delete",
                "Are you sure you want to delete item " + selectedItem.getDescription() + "?");

        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (itemDAO.deleteItem(selectedItem.getCode())) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Item deleted successfully!");
                clearItemFields();
                loadItems();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete item!");
            }
        }
    }

    @FXML
    private void clearItemFields() {
        txtItemCode.clear();
        txtItemDescription.clear();
        txtItemPackSize.clear();
        txtItemUnitPrice.clear();
        txtItemQtyOnHand.clear();
        tblItems.getSelectionModel().clearSelection();
    }

    // Helper Methods
    private void loadCustomers() {
        ObservableList<Customer> customers = customerDAO.getAllCustomers();
        tblCustomers.setItems(customers);
    }

    private void loadItems() {
        ObservableList<Item> items = itemDAO.getAllItems();
        tblItems.setItems(items);
    }

    private void populateCustomerFields(Customer customer) {
        txtCustomerId.setText(customer.getId());
        cmbCustomerTitle.setValue(customer.getTitle());
        txtCustomerName.setText(customer.getName());
        dpCustomerDOB.setValue(customer.getDob());
        txtCustomerSalary.setText(String.valueOf(customer.getSalary()));
        txtCustomerAddress.setText(customer.getAddress());
        txtCustomerCity.setText(customer.getCity());
        txtCustomerProvince.setText(customer.getProvince());
        txtCustomerPostal.setText(customer.getPostalCode());
    }

    private void populateItemFields(Item item) {
        txtItemCode.setText(item.getCode());
        txtItemDescription.setText(item.getDescription());
        txtItemPackSize.setText(item.getPackSize());
        txtItemUnitPrice.setText(String.valueOf(item.getUnitPrice()));
        txtItemQtyOnHand.setText(String.valueOf(item.getQtyOnHand()));
    }

    private boolean validateCustomerInput() {
        if (txtCustomerId.getText().isEmpty() || cmbCustomerTitle.getValue() == null ||
                txtCustomerName.getText().isEmpty() || dpCustomerDOB.getValue() == null ||
                txtCustomerSalary.getText().isEmpty() || txtCustomerAddress.getText().isEmpty() ||
                txtCustomerCity.getText().isEmpty() || txtCustomerProvince.getText().isEmpty() ||
                txtCustomerPostal.getText().isEmpty()) {

            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill all fields!");
            return false;
        }

        try {
            Double.parseDouble(txtCustomerSalary.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Salary must be a valid number!");
            return false;
        }

        return true;
    }

    private boolean validateItemInput() {
        if (txtItemCode.getText().isEmpty() || txtItemDescription.getText().isEmpty() ||
                txtItemPackSize.getText().isEmpty() || txtItemUnitPrice.getText().isEmpty() ||
                txtItemQtyOnHand.getText().isEmpty()) {

            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill all fields!");
            return false;
        }

        try {
            Double.parseDouble(txtItemUnitPrice.getText());
            Integer.parseInt(txtItemQtyOnHand.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Unit Price and Quantity must be valid numbers!");
            return false;
        }

        return true;
    }

    private Customer createCustomerFromInput() {
        return new Customer(
                txtCustomerId.getText(),
                cmbCustomerTitle.getValue(),
                txtCustomerName.getText(),
                dpCustomerDOB.getValue(),
                Double.parseDouble(txtCustomerSalary.getText()),
                txtCustomerAddress.getText(),
                txtCustomerCity.getText(),
                txtCustomerProvince.getText(),
                txtCustomerPostal.getText()
        );
    }

    private Item createItemFromInput() {
        return new Item(
                txtItemCode.getText(),
                txtItemDescription.getText(),
                txtItemPackSize.getText(),
                Double.parseDouble(txtItemUnitPrice.getText()),
                Integer.parseInt(txtItemQtyOnHand.getText())
        );
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Optional<ButtonType> showConfirmationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait();
    }
}