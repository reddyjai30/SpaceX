package sample.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Scene scene;
    private Stage stage;
    private Parent root;

    @FXML
    MediaView mediaView;
    File file;

    public void onApodSClick(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("../Fxml/apods.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }


    public void onNeoWsClick(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("../Fxml/neows.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        
    }

    public void onNasaGalleryClick(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("../Fxml/nasaGallery.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /*file = new File("C:\\Users\\Admin\\Downloads\\Pexels Videos 1851190.mp4");
        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.play();
        mediaView.setPreserveRatio(false);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);*/


    }


}
