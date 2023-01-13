# incident-notification-plugin

This example shows how to implement process-engine-plugins that publish notifications about incident to other systems.

## How to start
* Simply start the `ProcessApplication` and follow anything in the log, or may use the debugger.
* You can also start all process-definitions using the Camunda Tasklist

## How it works

* The [IncidentNotificationHandler](src/main/java/de/viadee/bpm/camunda/engine/plugin/IncidentNotificationHandler.java) extends the default behaviour of Camunda
in order to notify individual [NotificationService](src/main/java/de/viadee/bpm/camunda/service/NotificationService.java)-Beans after an incident is created 
* There are two types of `IncidentNotificationHandler` in order to _register_ on 'failed jobs' and 'failed external-tasks' in the same manner

## How it looks

The `JavaDelegate` and the `External-Task-Handler`are executed multiple times, they produce an error each time, but only the 
execution causing the incident triggers `Notification-Handlers`

```
09:54:07.630  INFO - process.Activities  : execute external-task
09:54:17.630  INFO - process.Activities  : execute external-task
09:54:17.635 ERROR - NotificationHandler : 💣 Incident: Hallo External Error!
09:54:17.638  INFO - NotificationServices: ✉️ Notification by mail...
09:54:17.644  INFO - NotificationServices: 🎫 Notification to ticket-system...

09:56:59.291  INFO - process.Activities  : execute java-delegate
09:57:59.291  INFO - process.Activities  : execute java-delegate
09:57:59.299 ERROR - NotificationHandler : 💣 Incident: Hallo Delegate Error!
09:57:59.299  INFO - NotificationServices: ✉️ Notification by mail...
09:57:59.299  INFO - NotificationServices: 🎫 Notification to ticket-system...
```
