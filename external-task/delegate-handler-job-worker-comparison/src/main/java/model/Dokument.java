package model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Dokument implements JsonDataType {

  private String id;
  private String content;
  private String type;

}
