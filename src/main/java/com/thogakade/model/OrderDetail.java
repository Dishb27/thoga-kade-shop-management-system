package com.thogakade.model;

import javafx.beans.property.*;

public class OrderDetail {
    private StringProperty orderId;
    private StringProperty itemCode;
    private IntegerProperty qty;
    private DoubleProperty unitPrice;
    private DoubleProperty discount;
    private DoubleProperty total;

    public OrderDetail() {
        this.orderId = new SimpleStringProperty();
        this.itemCode = new SimpleStringProperty();
        this.qty = new SimpleIntegerProperty();
        this.unitPrice = new SimpleDoubleProperty();
        this.discount = new SimpleDoubleProperty();
        this.total = new SimpleDoubleProperty();
    }

    public OrderDetail(String orderId, String itemCode, int qty, double unitPrice, double discount, double total) {
        this();
        setOrderId(orderId);
        setItemCode(itemCode);
        setQty(qty);
        setUnitPrice(unitPrice);
        setDiscount(discount);
        setTotal(total);
    }

    public String getOrderId() { return orderId.get(); }
    public void setOrderId(String orderId) { this.orderId.set(orderId); }
    public StringProperty orderIdProperty() { return orderId; }

    public String getItemCode() { return itemCode.get(); }
    public void setItemCode(String itemCode) { this.itemCode.set(itemCode); }
    public StringProperty itemCodeProperty() { return itemCode; }

    public int getQty() { return qty.get(); }
    public void setQty(int qty) { this.qty.set(qty); }
    public IntegerProperty qtyProperty() { return qty; }

    public double getUnitPrice() { return unitPrice.get(); }
    public void setUnitPrice(double unitPrice) { this.unitPrice.set(unitPrice); }
    public DoubleProperty unitPriceProperty() { return unitPrice; }

    public double getDiscount() { return discount.get(); }
    public void setDiscount(double discount) { this.discount.set(discount); }
    public DoubleProperty discountProperty() { return discount; }

    public double getTotal() { return total.get(); }
    public void setTotal(double total) { this.total.set(total); }
    public DoubleProperty totalProperty() { return total; }
}