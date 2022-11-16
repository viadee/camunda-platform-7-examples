package model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class DamageData {

  private BigDecimal value;
  private String type;
  private LocalDate damageDate;
  private String description;

}
