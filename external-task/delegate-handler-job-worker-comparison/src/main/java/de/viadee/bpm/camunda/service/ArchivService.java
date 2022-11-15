package de.viadee.bpm.camunda.service;

import model.Archived;
import model.Dokument;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ArchivService {

  public Archived archive(final Dokument dokument) {
    var archived = new Archived();
    archived.setId(dokument.getId());
    archived.setArchivedAt(LocalDateTime.now());
    archived.setArchiveId("ARC_" + UUID.randomUUID().toString().split("-")[0]);
    return archived;
  }

}
