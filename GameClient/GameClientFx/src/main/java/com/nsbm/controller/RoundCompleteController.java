/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsbm.controller;

import com.nsbm.common.CommonData;
import static com.nsbm.common.CommonUtil.setTableColumns;
import com.nsbm.common.Mouse;
import com.nsbm.entity.PlayerStatistic;
import static com.nsbm.service.PlayerServiceHandler.getRoundCompletedPlayers;
import static com.nsbm.service.PlayerServiceHandler.listenToRoundCompletionEvent;
import static com.nsbm.service.PlayerServiceHandler.notifyRoundCompletion;
import static com.nsbm.service.PlayerServiceHandler.removePlayer;
import static com.nsbm.service.PlayerServiceHandler.setLabelReference;
import static com.nsbm.service.PlayerServiceHandler.setPlayerScore;
import static com.nsbm.service.PlayerServiceHandler.setSpecialPointsLabel;
import static com.nsbm.service.PlayerServiceHandler.terminatePlayer;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

/**
 *
 * @author Lakshitha
 */
public class RoundCompleteController implements Initializable {

    private String[] allCompletedPlayer;
    final ObservableList<String> model = FXCollections.observableArrayList();
    final ObservableList<PlayerStatistic> playerStatistics = FXCollections.observableArrayList();
    private Mouse mouse = new Mouse();
    @FXML
    private Label nextRoundTime;
    @FXML
    private Label specialPoints;
    @FXML
    private Label roundNumber;
    @FXML
    private Button terminatePerk;
    @FXML
    private TableView<PlayerStatistic> scoreTable;
    @FXML
    private Pane completePane;
    @FXML
    private Label userNameLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        roundNumber.setText(String.valueOf(CommonData.currentRound));
        userNameLabel.setText(CommonData.username);
        allCompletedPlayer = getRoundCompletedPlayers();
        setSpecialPointsLabel(specialPoints);
        setLabelReference(nextRoundTime);
        setPlayerScore(playerStatistics);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setTableColumns(scoreTable, playerStatistics, allCompletedPlayer);
                listenToRoundCompletionEvent();
            }
        };
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        t.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ScoringMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        notifyRoundCompletion();
        completePane.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                mouse.setX(event.getX());
                mouse.setY(event.getY());
            }
        
        });
        completePane.setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                completePane.getScene().getWindow().setX(event.getScreenX() - mouse.getX() - 14);
                completePane.getScene().getWindow().setY(event.getScreenY() - mouse.getY() - 14);
            }
        
        });
    }

    @FXML
    private void terminateAction(ActionEvent event) {
        if(CommonData.specialPoints < 1){
            JOptionPane.showMessageDialog(null,"Need " + (20 - CommonData.specialPoints) + " Special Points");
        }
        else{
            System.out.println("Terminate Perk Activated");
            terminatePlayer();
        }
    }
}
