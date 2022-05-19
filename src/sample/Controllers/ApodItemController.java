package sample.Controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sample.Models.ApodModel;

import javax.swing.text.LabelView;


public class ApodItemController {


    @FXML
    Label copyRight;
    @FXML
    Label date;
    @FXML
    Label explanation;
    @FXML
    ImageView apodImageView;

    @FXML
    Label title;


    public void setData(ApodModel model){
        System.out.println("Setting item details");
        title.setText(model.getTitle());
        copyRight.setText(model.getCopyRight());
        date.setText(model.getDate());
        explanation.setText(model.getExplanation());

       // apodImageView.setImage(new Image(model.getUrl()));


        // image view

    }

}
