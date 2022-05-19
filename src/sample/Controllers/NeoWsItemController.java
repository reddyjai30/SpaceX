package sample.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sample.Models.NeoWsModel;

public class NeoWsItemController {

    @FXML
    Label title;
    @FXML
    Label newsDate;
    @FXML
    Label hazardous;
    @FXML
    Label absMag;
    @FXML
    Label minDia;
    @FXML
    Label maxDia;
    @FXML
    Label approachDate;
    @FXML
    Label apVelocity;
    @FXML
    Label missDistance;
    @FXML
    Label orbitingBody;

    public void setData(NeoWsModel model){

       title.setText(model.getName());
       newsDate.setText(model.getNewsDate());
       if(model.getPotentiallyHazardous()) {
           hazardous.setText("Yes");
       } else{
           hazardous.setText("No");
       }

       absMag.setText(""+model.getAbsoluteMagnitude());
       minDia.setText(""+model.getMinDiameter());
       maxDia.setText(""+model.getMaxDiameter());
       approachDate.setText(model.getApproachDate());
       missDistance.setText(model.getMissDistance());
       orbitingBody.setText(model.getOrbitingBody());

    }


}
