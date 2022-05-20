package de.viadee.bpm.camunda.model;

public class Customer implements JsonDataType {

    private String name;
    private Address address;

    public Customer(final String name, final Address address) {
        this.name = name;
        this.address = address;
    }

    public Customer() {
        // nop
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }
}
