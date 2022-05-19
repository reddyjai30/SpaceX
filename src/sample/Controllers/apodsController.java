package sample.Controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.Models.ApodModel;
import sample.Query.ApodQueryUtils;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class apodsController implements Initializable {

    @FXML
    AnchorPane curPane;

    @FXML
    ListView<AnchorPane> resListView;
    @FXML
    ProgressBar progressBar;

    @FXML
    TextField startDay , startMonth,startYear;
    @FXML
    TextField endDay , endMonth,endYear;

    private static String SAMPLE_JSON_REQ_LINK = "https://api.nasa.gov/planetary/apod?api_key="
            + "bjL4RAgTbl6XOoLX9fRa8VvEOeKl3pWgFrP5tU3C&start_date=2021-11-02&end_date=2021-11-16";
    private String tempLink = "https://api.nasa.gov/planetary/apod?api_key="
            + "bjL4RAgTbl6XOoLX9fRa8VvEOeKl3pWgFrP5tU3C&start_date=2021-11-02&end_date=2021-11-16";

    private Scene scene;
    private Stage stage;
    private Parent root;

    private List<ApodModel> modelList = new ArrayList<>();
    // private MyListener myListener;

    public void multiApodSearch(ActionEvent event){

        String startLink = "https://api.nasa.gov/planetary/apod?api_key=bjL4RAgTbl6XOoLX9fRa8VvEOeKl3pWgFrP5tU3C&start_date=";
        String startDate = startYear.getText() + "-" + startMonth.getText() + "-" + startDay.getText() + "&end_date=";
        startLink += startDate;
        String endDate = endYear.getText() + "-" + endMonth.getText() + "-" + endDay.getText() + "";
        startLink += endDate;

        SAMPLE_JSON_REQ_LINK = startLink;

        doWorkAfter task = new doWorkAfter();
        progressBar.progressProperty().bind(task.progressProperty());
        resListView.getItems().clear();
        new Thread(task).start();

    }

    public void onBackPress(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../Fxml/main.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        SAMPLE_JSON_REQ_LINK = tempLink;

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        doWorkAfter d = new doWorkAfter();
        progressBar.progressProperty().bind(d.progressProperty());
        new Thread(d).start();

        resListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AnchorPane>() {
            @Override
            public void changed(ObservableValue<? extends AnchorPane> observableValue, AnchorPane anchorPane, AnchorPane t1) {

               int ind = resListView.getSelectionModel().getSelectedIndex();
                System.out.println("wksjfsdjfjadsklfj;sdjfl;ds:    "+modelList.get(ind).getExplanation());

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("../Fxml/ApodDetails.fxml"));

                try {
                    root = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ApodDetailsController itemController1 = fxmlLoader.getController();
                itemController1.setData(modelList.get(ind));
                scene = new Scene(root);
                stage = (Stage) (curPane.getScene().getWindow());
                stage.setScene(scene);
                stage.show();

            }
        });

    }

    private class doWorkAfter extends  Task<Integer> {

        @Override
        protected Integer call() throws Exception {

            System.out.println("Task running");
            //updateProgress(5,30);
            ArrayList<ApodModel> response = ApodQueryUtils.excutePost(SAMPLE_JSON_REQ_LINK);
            int size = response.size() + 5;
            updateProgress(5,size);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Loading");
                   // updateProgress(16,30);
                    modelList.clear();
                    modelList.addAll(response);

                    resListView.getItems().clear();
                    try {
                        for (int i = 0; i < modelList.size(); i++) {
                            updateProgress(5+i,size);
                            FXMLLoader fxmlLoader = new FXMLLoader();
                            fxmlLoader.setLocation(getClass().getResource("../Fxml/ApodItem.fxml"));
                            AnchorPane anchorPane = fxmlLoader.load();
                            //updateProgress(i +16,30);

                            ApodItemController itemController1 = fxmlLoader.getController();
                            itemController1.setData(modelList.get(i));

                            resListView.getItems().add(anchorPane);

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
