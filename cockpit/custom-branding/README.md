# cockpit-custom-branding

This example demonstrates how the Camunda Webapps might be extended to be 
customizable using application-properties, so that it can be configured differently in
every environment.

### Login screen
![login][1]

### Camunda Cockpit
![cockpit][2]

## How to start

1. Check out project
2. Run `ProcessApplication`
3. Visit `http://localhost:8080` (demo / demo)
4. 1 Process-instance is started automatically after 5 seconds


## How it works
An additional `Filter` intercepts the requests, and every time the requested resources `/styles/user-styles.css` or `/custom-logo.png`
are recognized, they are replaced by previously manipulated resources where the custom branding information
have been injected at the application's startup.

Detailed mechanism, see here: [CustomBrandingResourceFilter.java](/src/main/java/de/viadee/bpm/camunda/branding/CustomBrandingResourceFilter.java)


[1]: docs/login.png
[2]: docs/cockpit.png
