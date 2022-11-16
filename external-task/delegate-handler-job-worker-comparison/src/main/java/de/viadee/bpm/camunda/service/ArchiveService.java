package de.viadee.bpm.camunda.service;

import model.Archived;
import model.Document;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ArchiveService {

  public Archived archive(final Document document) {
    var archived = new Archived();
    archived.setId(document.getId());
    archived.setArchivedAt(LocalDateTime.now());
    archived.setArchiveId("ARC_" + UUID.randomUUID().toString().split("-")[0]);
    return archived;
  }

}
