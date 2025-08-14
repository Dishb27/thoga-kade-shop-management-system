package com.thogakade.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Customer {
    private StringProperty id;
    private StringProperty title;
    private StringProperty name;
    private ObjectProperty<LocalDate> dob;
    private DoubleProperty salary;
    private StringProperty address;
    private StringProperty city;
    private StringProperty province;
    private StringProperty postalCode;

    public Customer() {
        this.id = new SimpleStringProperty();
        this.title = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.dob = new SimpleObjectProperty<>();
        this.salary = new SimpleDoubleProperty();
        this.address = new SimpleStringProperty();
        this.city = new SimpleStringProperty();
        this.province = new SimpleStringProperty();
        this.postalCode = new SimpleStringProperty();
    }

    public Customer(String id, String title, String name, LocalDate dob, double salary,
                    String address, String city, String province, String postalCode) {
        this();
        setId(id);
        setTitle(title);
        setName(name);
        setDob(dob);
        setSalary(salary);
        setAddress(address);
        setCity(city);
        setProvince(province);
        setPostalCode(postalCode);
    }

    // Getters and Setters
    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }
    public StringProperty idProperty() { return id; }

    public String getTitle() { return title.get(); }
    public void setTitle(String title) { this.title.set(title); }
    public StringProperty titleProperty() { return title; }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }
    public StringProperty nameProperty() { return name; }

    public LocalDate getDob() { return dob.get(); }
    public void setDob(LocalDate dob) { this.dob.set(dob); }
    public ObjectProperty<LocalDate> dobProperty() { return dob; }

    public double getSalary() { return salary.get(); }
    public void setSalary(double salary) { this.salary.set(salary); }
    public DoubleProperty salaryProperty() { return salary; }

    public String getAddress() { return address.get(); }
    public void setAddress(String address) { this.address.set(address); }
    public StringProperty addressProperty() { return address; }

    public String getCity() { return city.get(); }
    public void setCity(String city) { this.city.set(city); }
    public StringProperty cityProperty() { return city; }

    public String getProvince() { return province.get(); }
    public void setProvince(String province) { this.province.set(province); }
    public StringProperty provinceProperty() { return province; }

    public String getPostalCode() { return postalCode.get(); }
    public void setPostalCode(String postalCode) { this.postalCode.set(postalCode); }
    public StringProperty postalCodeProperty() { return postalCode; }
}