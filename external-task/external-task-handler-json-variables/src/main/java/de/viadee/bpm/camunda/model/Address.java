package de.viadee.bpm.camunda.model;

public class Address {

    private String street;

    public Address(final String street) {
        this.street = street;
    }

    public Address() {
        // nop
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(final String street) {
        this.street = street;
    }
}
