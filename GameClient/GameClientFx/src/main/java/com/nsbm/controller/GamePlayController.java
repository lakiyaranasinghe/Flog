/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsbm.controller;

import com.nsbm.common.CommonData;
import com.nsbm.common.Mouse;
import static com.nsbm.common.ResponseResult.ADALA_NA;
import static com.nsbm.common.ResponseResult.SUCCESS;
import com.nsbm.common.Validator;
import static com.nsbm.service.WordServiceHandler.addWord;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Muthu
 */
public class GamePlayController implements Initializable {
    private Mouse mouse = new Mouse();
    private Timer timer = new Timer();
    private Validator validate = new Validator();
    private static int counter = 100;
    private static double progressCount = 0.00;
    private String letters,initial;
    @FXML
    private AnchorPane letterPane;
    @FXML
    private TextField wordField;
    @FXML
    private Button submitButton;
    @FXML
    private Pane gamePlayPane;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label currentRound;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Alert alert;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userNameLabel.setText(CommonData.username);
        letters = CommonData.letters;
        initial = CommonData.initialLetters;
        CommonData.currentRound++;
        currentRound.setText("Round " + String.valueOf(CommonData.currentRound));
        int count = 0;
        for (Node node : letterPane.getChildren()) {
            if (count == letters.length()) {
                break;
            }
            if (node instanceof TextField) {
                if(count == 0 || count == 1){
                    ((TextField) node).setText(String.valueOf(initial.charAt(count)).toUpperCase());
                    node.setDisable(true);
                }
                else{
                    ((TextField) node).setText(String.valueOf(letters.charAt(count)).toUpperCase());
                    //if(letters.charAt(count) == CommonData.initialLetters.charAt(0) || letters.charAt(count) == CommonData.initialLetters.charAt(1)){
                    //node.setDisable(true);
                }              
            }
            count++;
        }

        timer = new Timer();
            counter = 100;
            progressCount = 0.00;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        if (counter == 0) {                         
                            sendWord();                            
                        } else {         
                            progressBar.setProgress(progressCount);
                            progressCount=progressCount+0.01;
                            counter--;
                        }
                    });
                }
            }, 0, 1000);
        
        gamePlayPane.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                mouse.setX(event.getX());
                mouse.setY(event.getY());
            }
        
        });
        gamePlayPane.setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                gamePlayPane.getScene().getWindow().setX(event.getScreenX() - mouse.getX() - 14);
                gamePlayPane.getScene().getWindow().setY(event.getScreenY() - mouse.getY() - 14);
            }
        
        });
    }

    public void sendWord() {
        timer.cancel();
        String word;
        Stage stage = new Stage();
        if(wordField.getText()== null || wordField.getText().length() == 0 ){
            word = "noword";
        }
        else{
            word = wordField.getText();
        }
        String response = addWord(word);
        if (response.equals(SUCCESS)) {
            JOptionPane.showMessageDialog(null, "Correct Word");
        } else if (response.equals(ADALA_NA)) {
                JOptionPane.showMessageDialog(null, "You Can Only Used Given Letters");
        } else {
                JOptionPane.showMessageDialog(null, "Incorrect Word"); 
        }
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/RoundComplete.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(GameWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
            
    }
}
