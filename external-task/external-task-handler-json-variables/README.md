# external-task-handler-json-variables

This example demonstrates how an External-Task-Handlers (Java Client provided by Camunda)
might store Pojos as `Json` with own de-/serialization capabilities.

The process mainly contains three steps, all of them implemented as **External-Task**:

1. Create a list of _customers_ and store them as  `java.util.ArrayList<String>`,
   with one customer per string is represented as **Json-String** (escaped)
2. Read the list of _customers_, select one randomly, and store the selected one
   separately as (true) `Json`
3. Read the selected _customer_ demonstrating the deserialization capabilities

![cockpit][1]
![variable][2]
![variable-list][3]

## How to start

1. Check out project
2. Run `ProcessApplication`
3. Visit `http://localhost:8080` (demo / demo)
4. 1 Process-instance is started automatically after 5 seconds
5. Alternately, start more instances using tasklist or rest-api
6. Have look into the process-context
7. Observe logs, they reveal some details about what happens:

```
AllCustomersToJsonList : Create some data
AllCustomersToJsonList : Serialize 2 objects of type <Customer> as json
AllCustomersToJsonList : Data serialized: [{"name":"Alice","address":{"street":"Madison Square Garden"}}, {"name":"Eve","address":null}]
AllCustomersToJsonList : Store as 'native' collection: 'class java.util.ArrayList', key: 'all_customers_list'
AllCustomersToJsonList : Complete task

SelectOneCustomer : Instantiate process-context
SelectOneCustomer : Access data using method
SelectOneCustomer : Hello Alice and Eve!
SelectOneCustomer : Selected customer: 'Alice'
SelectOneCustomer : Selected customer as json: {"name":"Alice","address":{"street":"Madison Square Garden"}}
SelectOneCustomer : Store as json-value, key: 'selected_customer'
SelectOneCustomer : Complete task

ReadSelectedCustomer : Instantiate process-context
ReadSelectedCustomer : Access data using method
ReadSelectedCustomer : Hello Alice, you're selected and this is you as json: {"name":"Alice","address":{"street":"Madison Square Garden"}}
ReadSelectedCustomer : Complete task
```

## How it works

### Write data

The **Java-Interface** provides a **default** method to serialize an object to Json

```java
public interface JsonDataType {
    default String toJson() {
        // responsible to serialize 'this' to json
        // implementation: see sources
    }
}

public class Customer implements JsonDataType {
    // pojo holding attributes and nested objects, 
    // including getter/setter methods
    // implementation: see sources
}


public class ExternalTaskHandler() {
    public void execute(ExternalTask task, ExternalTaskService taskService) {
        // some pojo
        var customer = new Customer();

        // complete external-task with variable as json
        taskService.complete(task,
            ClientValues.createVariables()
                        .putValue("customer", ClientValues.jsonValue(customer.toJson())));
    }
}
```

For lists, there is a method to serialize a list of objects implementing the `JsonDataType` each,
which returns a list of strings each holding a json-string. And there is a method to create a 'true' json: 

```java
public interface JsonDataType {
   static List<String> toJsonList(final List<? extends JsonDataType> objects) {
      return objects.stream()
                    .filter(Objects::nonNull)
                    .map(JsonDataType::toJson)
                    .collect(Collectors.toList());
   }

   static String toJsonString(final List<? extends JsonDataType> objects) {
      return "[" + String.join(",", toJsonList(objects)) + "]";
   }
}
```

### Why `ArrayList<String>` instead of Json?
If you want to use a collection as source of a bpmn-multi-instantiation, the engine can't iterate a json, 
but it can iterate a list of strings. ;-) (Prerequisite: `java.util.*`-classes are available at the classpath)


### Read data

The `AbsProcessContext` holds helper methods to 1. load a given variable from the process-context
and 2. deserialize the data against a given `type`, so that the business-codes stays clean from any
type casting:

```java
public abstract class AbsProcessContext {

    /* Read a single JsonDataType-object */
   <T extends JsonDataType> T readObject(final String variableName, final Class<T> type) {
      String json = this.readVariable(variableName); // see sources
      if (nonNull(json)) return JsonDataType.from(json, type);
      else return null; // don't create any data, if there is no data
   }


    /* Read a list of JsonDataType-objects */
   <T extends JsonDataType> List<T> readList(final String variableName, final Class<T> type) {
      List<String> jsons = this.readVariable(variableName); // see sources
      if (nonNull(jsons)) return JsonDataType.fromList(jsons, type);
      else return null; // don't create empty lists, if there is no data at all!
   }
}
```

[1]: docs/cockpit.png
[2]: docs/variable.png
[3]: docs/variable-list.png
