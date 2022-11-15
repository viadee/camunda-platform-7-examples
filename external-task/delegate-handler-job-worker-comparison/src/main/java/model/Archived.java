package model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Archived implements JsonDataType {

  private String id;
  private String archiveId;
  private LocalDateTime archivedAt;

}
