package model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class DamageReport implements JsonDataType {

  private DamageData damageData;
  private String vsnr;
  private String inputChannel;
  private List<Document> documents = new ArrayList<>();

}
