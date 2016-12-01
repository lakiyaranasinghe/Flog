/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsbm.controller;

import com.nsbm.common.CommonData;
import com.nsbm.common.Mouse;
import static com.nsbm.service.PointServiceHandler.getFinalScore;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Muthu
 */
public class ScoringMenuController implements Initializable {

    private Mouse mouse = new Mouse();
    private String[] scores;

    @FXML
    private Button backButton;
    @FXML
    private Pane scorePane;
    @FXML
    private Label playerName;
    @FXML
    private Label playerScore;
    @FXML
    private ListView scoreList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scores = getFinalScore(CommonData.username);
        ObservableList<String> items = FXCollections.observableArrayList();
        
        int yourScore = 0;
        int highestScore = 0;

        for (String score : scores) {
            String[] scoreParts = score.split("@");
            if(Integer.parseInt(scoreParts[1]) > highestScore){
                highestScore = Integer.parseInt(scoreParts[1]);

            }
            if(scoreParts[0].equals(CommonData.username)){
                yourScore = Integer.parseInt(scoreParts[1]);
            }
            items.add(scoreParts[0] + "     " + scoreParts[1]);
        }
        
        if(yourScore == highestScore){
            playerName.setText("You Won");
        }
        scoreList.setItems(items);
        scorePane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouse.setX(event.getX());
                mouse.setY(event.getY());
            }

        });
        scorePane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                scorePane.getScene().getWindow().setX(event.getScreenX() - mouse.getX() - 14);
                scorePane.getScene().getWindow().setY(event.getScreenY() - mouse.getY() - 14);
            }
        });
    }

    @FXML
    private void backAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}
