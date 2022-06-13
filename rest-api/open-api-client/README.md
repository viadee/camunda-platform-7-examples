# Camunda Rest-Api Client

This example demonstrates how Open-Api-Spec of Camunda can be utilized to generate api-clients and make use of the Camunda Rest-Api

## How to start

1. Check out project
2. Run `ProcessApplication`
3. Process-instance is started automatically after 10 seconds **using Rest-Api**
4. Other calls to Rest-Api are executed every 15 seconds using the `VersionApi` in `ApiService.java`


## How it works
The `openapi-generator-maven-plugin` is used to generate the resources specified in the `camunda-engine-rest-openapi` dependency.
#### pom.xml
```xml
<plugin>
  <groupId>org.openapitools</groupId>
  <artifactId>openapi-generator-maven-plugin</artifactId>
  <version>${version.openapi-generator}</version>
  <executions>
    <execution>
      <goals>
        <goal>generate</goal>
      </goals>
      <configuration>
        <inputSpec>/openapi.json</inputSpec>
        <apiPackage>de.viadee.bpm.camunda.rest.api.client</apiPackage>
        <modelPackage>de.viadee.bpm.camunda.rest.api.model</modelPackage>
        <generateModelTests>false</generateModelTests>
        <generateApiTests>false</generateApiTests>
        <generatorName>java</generatorName>
        <library>resttemplate</library>
        <additionalProperties>
          <additionalProperty>io.swagger.parser.util.RemoteUrl.trustAll=true</additionalProperty>
        </additionalProperties>
        <configOptions>
          <openApiNullable>false</openApiNullable>
          <dateLibrary>java8</dateLibrary>
          <sourceFolder>src/gen/java/main</sourceFolder>
        </configOptions>
      </configuration>
    </execution>
  </executions>
  <dependencies>
    <dependency>
      <groupId>org.camunda.bpm</groupId>
      <artifactId>camunda-engine-rest-openapi</artifactId>
      <version>${version.camunda}</version>
    </dependency>
  </dependencies>
</plugin>
```
