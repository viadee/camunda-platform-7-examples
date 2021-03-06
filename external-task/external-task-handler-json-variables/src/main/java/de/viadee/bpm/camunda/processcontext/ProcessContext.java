package de.viadee.bpm.camunda.processcontext;

import de.viadee.bpm.camunda.model.Customer;

import java.util.List;
import java.util.Map;

public class ProcessContext extends AbsProcessContext {

    public static final String SELECTED_CUSTOMER_KEY = "selected_customer";
    public static final String ALL_CUSTOMERS_LIST_KEY = "all_customers_list";
    public static final String ALL_CUSTOMERS_JSON_KEY = "all_customer_json";


    public ProcessContext(final Map<String, Object> variables) {
        super(variables);
    }

    public Customer getSelectedCustomer() {
        return super.readObject(SELECTED_CUSTOMER_KEY, Customer.class);
    }

    public List<Customer> getAllCustomers() {
        return super.readList(ALL_CUSTOMERS_LIST_KEY, Customer.class);
    }

}
