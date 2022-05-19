package sample.Controllers;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import sample.Models.ApodModel;

import javax.swing.plaf.LabelUI;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ApodDetailsController {

    @FXML
    ImageView imageView;
    @FXML
    Label title;

    ApodModel apodModel;
    private Scene scene;
    private Stage stage;
    private Parent root;

    public void setData(ApodModel model){

        
        apodModel = model;
        loadImage load = new loadImage();
        new Thread(load).start();
        title.setText(model.getTitle());
        System.out.println(model.getUrl());
       // indicator.setVisible(false);

    }

    private class loadImage extends Task<Integer> {
        @Override
        protected Integer call() throws Exception {
            imageView.setImage(new Image(apodModel.getUrl()));
            return null;
        }

    }

    public void onBackPress(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../Fxml/apods.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}
