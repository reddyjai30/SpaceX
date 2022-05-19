package sample.Controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import sample.Models.NasaGalleryModel;
import sample.Models.NeoWsModel;
import sample.Query.NasaGalleryQueryUtils;
import sample.Query.NeoWsQueryUtils;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NasaGalleryController implements Initializable {

    private Scene scene;
    private Stage stage;
    private Parent root;

    @FXML
    GridPane grid;

    @FXML
    ProgressBar progressBar;

    @FXML
    TextField searchTxt;

    ArrayList<NasaGalleryModel> modelArrayList = new ArrayList<>();

    public static int progressStatus = 0;
    private static String SAMPLE_JSON_REQ_LINK = "https://images-api.nasa.gov/search?q=apollo+11";
    private String tempLink = "https://images-api.nasa.gov/search?q=apollo+11";

    public void multiNasaSearch(ActionEvent event){

        String startLink = "https://images-api.nasa.gov/search?q=";

        startLink += searchTxt.getText();

        SAMPLE_JSON_REQ_LINK = startLink;
        System.out.println(SAMPLE_JSON_REQ_LINK);

        modelArrayList.clear();
        doWorkAfter task = new doWorkAfter();
        progressBar.progressProperty().bind(task.progressProperty());
        modelArrayList.clear();
        new Thread(task).start();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        doWorkAfter d = new doWorkAfter();
         progressBar.progressProperty().bind(d.progressProperty());
        new Thread(d).start();

    }

    public void onBackPress(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../Fxml/main.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        SAMPLE_JSON_REQ_LINK = tempLink;

    }

    private class doWorkAfter extends Task<Integer> {

        @Override
        protected Integer call() throws Exception {

            modelArrayList.clear();
            ArrayList<NasaGalleryModel> response = NasaGalleryQueryUtils.excutePost(SAMPLE_JSON_REQ_LINK);
            modelArrayList.addAll(response);
            int size = modelArrayList.size();

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    int column = 0;
                    int row = 1;
                    try {
                        for (int i = 0; i < modelArrayList.size(); i++) {
                            updateProgress(i,size);
                            FXMLLoader fxmlLoader = new FXMLLoader();
                            fxmlLoader.setLocation(getClass().getResource("../Fxml/nasaGalleryItem.fxml"));
                            AnchorPane anchorPane = fxmlLoader.load();

                            NasaGalleryItemController itemController = fxmlLoader.getController();
                            itemController.setData(modelArrayList.get(i));

                            if (column == 2) {
                                column = 0;
                                row++;
                            }

                            grid.add(anchorPane, column++, row); //(child,column,row)
                            //set grid width
                            grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                            grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                            grid.setMaxWidth(Region.USE_PREF_SIZE);

                            //set grid height
                            grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                            grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                            grid.setMaxHeight(Region.USE_PREF_SIZE);

                            GridPane.setMargin(anchorPane, new Insets(10));
                        }
                        updateProgress(size,size);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            return 1;
        }

        @Override
        protected void updateProgress(long l, long l1) {
            updateMessage("Done");
            super.updateProgress(l, l1);
        }

    }


}
