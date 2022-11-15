package model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Schadenmeldung implements JsonDataType {

  private Schaden schaden;
  private String vsnr;
  private String eingangskanal;
  private List<Dokument> dokumente = new ArrayList<>();

}
