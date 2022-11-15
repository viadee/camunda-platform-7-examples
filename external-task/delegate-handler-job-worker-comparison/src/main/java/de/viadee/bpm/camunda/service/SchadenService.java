package de.viadee.bpm.camunda.service;

import model.Schadenmeldung;
import model.Vertrag;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SchadenService {

  public String schadenAnlegen(final Schadenmeldung schaden, final Vertrag vertrag) {
    try {
      Thread.sleep(new Random().nextInt(5000) + 1000);
    } catch (InterruptedException ignored) {
    }
    return
      "VN" + vertrag.getId()
        + "_" + vertrag.getPolicyHolder() + "_SN"
        + String.valueOf(schaden.hashCode()).replace("-", "");
  }

}
