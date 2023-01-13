package de.viadee.bpm.camunda.service;

public interface NotificationService {

    void publish(final Notification notification);

    class Notification {

        private String processDefinition;
        private String processInstanceId;
        private String activityName;
        private String message;

        public String getProcessDefinition() {
            return processDefinition;
        }

        public void setProcessDefinition(final String processDefinition) {
            this.processDefinition = processDefinition;
        }

        public String getProcessInstanceId() {
            return processInstanceId;
        }

        public void setProcessInstanceId(final String processInstanceId) {
            this.processInstanceId = processInstanceId;
        }

        public String getActivityName() {
            return activityName;
        }

        public void setActivityName(final String activityName) {
            this.activityName = activityName;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(final String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "Notification{" +
                    "processDefinition='" + processDefinition + '\'' +
                    ", processInstanceId='" + processInstanceId + '\'' +
                    ", activityName='" + activityName + '\'' +
                    ", message='" + message + '\'' +
                    '}';
        }
    }
}
