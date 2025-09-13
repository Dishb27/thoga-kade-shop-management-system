package com.thogakade.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Order {
    private StringProperty orderId;
    private ObjectProperty<LocalDate> orderDate;
    private StringProperty customerId;
    private DoubleProperty total;

    public Order() {
        this.orderId = new SimpleStringProperty();
        this.orderDate = new SimpleObjectProperty<>();
        this.customerId = new SimpleStringProperty();
        this.total = new SimpleDoubleProperty();
    }

    public Order(String orderId, LocalDate orderDate, String customerId, double total) {
        this();
        setOrderId(orderId);
        setOrderDate(orderDate);
        setCustomerId(customerId);
        setTotal(total);
    }

    public String getOrderId() { return orderId.get(); }
    public void setOrderId(String orderId) { this.orderId.set(orderId); }
    public StringProperty orderIdProperty() { return orderId; }

    public LocalDate getOrderDate() { return orderDate.get(); }
    public void setOrderDate(LocalDate orderDate) { this.orderDate.set(orderDate); }
    public ObjectProperty<LocalDate> orderDateProperty() { return orderDate; }

    public String getCustomerId() { return customerId.get(); }
    public void setCustomerId(String customerId) { this.customerId.set(customerId); }
    public StringProperty customerIdProperty() { return customerId; }

    public double getTotal() { return total.get(); }
    public void setTotal(double total) { this.total.set(total); }
    public DoubleProperty totalProperty() { return total; }
}