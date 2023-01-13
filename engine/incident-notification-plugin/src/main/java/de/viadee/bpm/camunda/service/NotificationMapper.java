package de.viadee.bpm.camunda.service;

import org.camunda.bpm.engine.runtime.Incident;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "processDefinition", source = "processDefinitionId")
    @Mapping(target = "processInstanceId", source = "processInstanceId")
    @Mapping(target = "activityName", source = "failedActivityId")
    @Mapping(target = "message", source = "incidentMessage")
    NotificationService.Notification map(Incident incident);
}
