package com.thogakade.model;

import javafx.beans.property.*;

public class Item {
    private StringProperty code;
    private StringProperty description;
    private StringProperty packSize;
    private DoubleProperty unitPrice;
    private IntegerProperty qtyOnHand;

    public Item() {
        this.code = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.packSize = new SimpleStringProperty();
        this.unitPrice = new SimpleDoubleProperty();
        this.qtyOnHand = new SimpleIntegerProperty();
    }

    public Item(String code, String description, String packSize, double unitPrice, int qtyOnHand) {
        this();
        setCode(code);
        setDescription(description);
        setPackSize(packSize);
        setUnitPrice(unitPrice);
        setQtyOnHand(qtyOnHand);
    }

    // Getters and Setters
    public String getCode() { return code.get(); }
    public void setCode(String code) { this.code.set(code); }
    public StringProperty codeProperty() { return code; }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }
    public StringProperty descriptionProperty() { return description; }

    public String getPackSize() { return packSize.get(); }
    public void setPackSize(String packSize) { this.packSize.set(packSize); }
    public StringProperty packSizeProperty() { return packSize; }

    public double getUnitPrice() { return unitPrice.get(); }
    public void setUnitPrice(double unitPrice) { this.unitPrice.set(unitPrice); }
    public DoubleProperty unitPriceProperty() { return unitPrice; }

    public int getQtyOnHand() { return qtyOnHand.get(); }
    public void setQtyOnHand(int qtyOnHand) { this.qtyOnHand.set(qtyOnHand); }
    public IntegerProperty qtyOnHandProperty() { return qtyOnHand; }
}