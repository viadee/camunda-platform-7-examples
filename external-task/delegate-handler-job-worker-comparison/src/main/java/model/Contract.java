package model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Contract implements JsonDataType {

  private String id;
  private String policyHolder;
  private String tarif;
  private LocalDate begin;

}
