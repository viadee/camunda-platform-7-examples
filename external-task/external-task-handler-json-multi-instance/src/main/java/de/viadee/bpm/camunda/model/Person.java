package de.viadee.bpm.camunda.model;

public class Person {

    private String name;
    private String quote;
    private String token;

    public Person() {
        // nop
    }

    public Person(final String name, final String quote) {
        this.name = name;
        this.quote = quote;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(final String quote) {
        this.quote = quote;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }
}
