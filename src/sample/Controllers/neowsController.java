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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import sample.Models.ApodModel;
import sample.Models.NeoWsModel;
import sample.Query.ApodQueryUtils;
import sample.Query.NeoWsQueryUtils;

import javax.swing.*;
import java.awt.image.SampleModel;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class neowsController implements Initializable {

    private Scene scene;
    private Stage stage;
    private Parent root;

    @FXML
    GridPane grid;

    @FXML
    ProgressBar progressBar;

    @FXML
    TextField startDay , startMonth,startYear;
    @FXML
    TextField endDay , endMonth,endYear;

    ArrayList<NeoWsModel> modelArrayList = new ArrayList<>();
    ArrayList<LocalDate> dates = new ArrayList<>();

    private static String SAMPLE_JSON_REQ_LINK = "https://api.nasa.gov/neo/rest" +
            "/v1/feed?start_date=2021-11-07&end_date=2021-11-08&api_key=bjL4RAgTbl6XOoLX9fRa8VvEOeKl3pWgFrP5tU3C";
    private String tempLink = "https://api.nasa.gov/neo/rest" +
            "/v1/feed?start_date=2021-11-07&end_date=2021-11-08&api_key=bjL4RAgTbl6XOoLX9fRa8VvEOeKl3pWgFrP5tU3C";

    public void multiNeoWsSearch(ActionEvent event){

        String startLink = "https://api.nasa.gov/neo/rest/v1/feed?api_key=bjL4RAgTbl6XOoLX9fRa8VvEOeKl3pWgFrP5tU3C&start_date=";
        if(Integer.parseInt(endDay.getText())-Integer.parseInt(startDay.getText())>3||
                Integer.parseInt(startMonth.getText())!=Integer.parseInt(endMonth.getText())||
                Integer.parseInt(startYear.getText())!=Integer.parseInt(endYear.getText())){
            JOptionPane.showMessageDialog(null,"At max you can get 4 Days Results");
            System.out.println("Error");
            return;
        }
        String startDate = startYear.getText() + "-" + startMonth.getText() + "-" + startDay.getText();
        startLink += startDate + "&end_date=";
        String endDate = endYear.getText() + "-" + endMonth.getText() + "-" + endDay.getText();
        startLink += endDate;

        // FUNCTION for converting the dates in between ranges if the date range can lie between years and month also 10 days
        String s = startDate;
        String e = endDate;
        LocalDate start = LocalDate.parse(s);
        LocalDate end = LocalDate.parse(e);
        List<LocalDate> totalDates = new ArrayList<>();
        while (!start.isAfter(end)) {
            totalDates.add(start);
            start = start.plusDays(1);
        }
        System.out.println(totalDates);

        dates.clear();
        dates.addAll(totalDates);

        SAMPLE_JSON_REQ_LINK = startLink;
        System.out.println(SAMPLE_JSON_REQ_LINK);

        doWorkAfter task = new doWorkAfter();
        progressBar.progressProperty().bind(task.progressProperty());
        modelArrayList.clear();
        new Thread(task).start();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        dates.clear();
        String s = "2021-11-07";
        String e = "2021-11-08";
        LocalDate start = LocalDate.parse(s);
        LocalDate end = LocalDate.parse(e);
        dates.add(start);
        dates.add(end);
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
            ArrayList<NeoWsModel> response = NeoWsQueryUtils.excutePost(SAMPLE_JSON_REQ_LINK,dates);
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
                            fxmlLoader.setLocation(getClass().getResource("../Fxml/NeoWsItem.fxml"));
                            AnchorPane anchorPane = fxmlLoader.load();

                            NeoWsItemController itemController = fxmlLoader.getController();
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
