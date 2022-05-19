package sample.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.Models.NasaGalleryModel;

public class NasaGalleryItemController {

    @FXML
    Label title;
    @FXML
    Label description;
    @FXML
    ImageView imageView;

    public void setData(NasaGalleryModel model){

        title.setText(model.getTitle());
        description.setText(model.getDescription());
        if(model.getImageUrl()!=null) {
            imageView.setImage(new Image(model.getImageUrl()));
        }

    }


}
