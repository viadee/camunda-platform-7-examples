package model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Schaden {

  private BigDecimal value;
  private String typ;
  private LocalDate schadenDatum;
  private String beschreibung;

}
