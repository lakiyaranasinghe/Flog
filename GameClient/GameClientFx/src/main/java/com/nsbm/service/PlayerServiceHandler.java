/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsbm.service;

import com.google.gson.Gson;
import static com.nsbm.common.CommonUtil.setPlayerJoinModelData;
import com.nsbm.common.PlayerStatus;
import com.nsbm.common.CommonData;
import static com.nsbm.common.CommonData.ADD_PLAYER;
import static com.nsbm.common.CommonData.BROADCAST;
import static com.nsbm.common.CommonData.CHECK_TERMINATION;
import static com.nsbm.common.CommonData.GET;
import static com.nsbm.common.CommonData.GET_PLAYERS;
import static com.nsbm.common.CommonData.GET_ROUND_COMPLETED_PLAYERS;
import static com.nsbm.common.CommonData.PLAYER_CLASS;
import static com.nsbm.common.CommonData.PLAYER_JOIN_BROADCAST;
import static com.nsbm.common.CommonData.PLAYER_JOIN_LISTEN;
import static com.nsbm.common.CommonData.POST;
import static com.nsbm.common.CommonData.REGISTER_PLAYER;
import static com.nsbm.common.CommonData.REMOVE_PLAYER;
import static com.nsbm.common.CommonData.ROUND_COMPLETION_BROADCAST;
import static com.nsbm.common.CommonData.ROUND_COMPLETION_LISTEN;
import static com.nsbm.common.CommonData.TERMINATE_PLAYER;
import static com.nsbm.common.CommonData.username;
import static com.nsbm.common.CommonUtil.addObservableListData;
import static com.nsbm.common.ResponseResult.SUCCESS;
import com.nsbm.entity.Player;
import com.nsbm.entity.PlayerStatistic;
import static com.nsbm.service.PointServiceHandler.getSpecialPoints;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;

/**
 *
 * @author Lakshitha
 */
public class PlayerServiceHandler {

    private static ObservableList<String> model = null;
    private static ObservableList<PlayerStatistic> playerScore = null;
    private static Label label;
    private static int counter = 10;
    private static Timer timer = new Timer();
    private static Stage stage = null;
    private static Label specialPointsLabel;

    public static void setModelReference(ObservableList<String> model) {
        PlayerServiceHandler.model = model;
    }

    public static void setLabelReference(Label label) {
        PlayerServiceHandler.label = label;
    }

    public static void setFrameReference(Stage stage) {
        PlayerServiceHandler.stage = stage;
    }

    public static Label getSpecialPointsLabel() {
        return specialPointsLabel;
    }

    public static void setSpecialPointsLabel(Label specialPointsLabel) {
        PlayerServiceHandler.specialPointsLabel = specialPointsLabel;
    }

    public static void setPlayerScore(ObservableList<PlayerStatistic> playerScore) {
        PlayerServiceHandler.playerScore = playerScore;
    }
    
    public static String RegisterPlayer(String playerName, String playerPassword, String email) {
        String output = null;
        try {
            HttpURLConnection connection = new FactoryServiceHandler().getServiceConnection(PLAYER_CLASS, REGISTER_PLAYER + "/"+email+"/"+playerName+"/"+playerPassword, POST);
            sendOutput(null, connection);
            output = getInput(connection);
        } catch (Exception e) {
            System.out.println(e);
        }
        return output;
    }

    public static String LoginPlayer(String playerName, String playerPassword) {
        String output = null;
        try {
            HttpURLConnection connection = new FactoryServiceHandler().getServiceConnection(PLAYER_CLASS, ADD_PLAYER+"/"+playerName+"/"+playerPassword, POST);
            sendOutput(playerName, connection);

            output = getInput(connection);
        } catch (Exception e) {
            System.out.println(e);
        }
        return output;
    }

    public static Player[] getAllPlayers() {
        Player[] playerList = null;
        try {
            HttpURLConnection connection = new FactoryServiceHandler().getServiceConnection(PLAYER_CLASS, GET_PLAYERS, GET);
            String output = getInput(connection);
            Gson parser = new Gson();
            playerList = parser.fromJson(output, Player[].class);

        } catch (IOException e) {
            System.out.println(e);
        }
        return playerList;
    }

    public static String[] getRoundCompletedPlayers() {
        String[] playerList = null;
        try {
            HttpURLConnection connection = new FactoryServiceHandler().getServiceConnection(PLAYER_CLASS, GET_ROUND_COMPLETED_PLAYERS, GET);
            String output = getInput(connection);
            Gson parser = new Gson();
            playerList = parser.fromJson(output, String[].class);
        } catch (IOException e) {
            System.out.println(e);
        }
        return playerList;
    }

    public static void notifyPlayerJoin() {
        String output = null;
        try {
            HttpURLConnection connection = new FactoryServiceHandler().getServiceConnection(BROADCAST, PLAYER_JOIN_BROADCAST, POST);
            String input = CommonData.username;
            OutputStream os = connection.getOutputStream();
            os.write(input.getBytes());
            os.flush();
            output = getInput(connection);
            System.out.println(output);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void listenToJoinEvent() {
        Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
        WebTarget target = client.target(CommonData.IP + BROADCAST + PLAYER_JOIN_LISTEN);

        EventInput eventInput = target.request().get(EventInput.class);
        while (!eventInput.isClosed()) {
            final InboundEvent inboundEvent = eventInput.read();
            if (CommonData.playerStatus == PlayerStatus.PLAYING) {
                break;
            }
            if (inboundEvent == null) {
                break;
            }
            Platform.runLater(new Runnable() {
                public void run() {
                    setPlayerJoinModelData(inboundEvent.readData(String.class), model);
                }
            });

        }
        System.out.println("Done");
    }

    public static void notifyRoundCompletion() {
        String output = null;
        try {
            System.out.println(username + " notifing completion");
            HttpURLConnection connection = new FactoryServiceHandler().getServiceConnection(BROADCAST, ROUND_COMPLETION_BROADCAST, POST);
            sendOutput(username, connection);
            output = getInput(connection);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void terminatePlayer() {
        String output = null;
        try {
            HttpURLConnection connection = new FactoryServiceHandler().getServiceConnection(PLAYER_CLASS, TERMINATE_PLAYER, POST);
            sendOutput(username, connection);
            output = getInput(connection);
            System.out.println(output);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void listenToRoundCompletionEvent() {
        Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
        WebTarget target = client.target(CommonData.IP + BROADCAST + ROUND_COMPLETION_LISTEN);

        EventInput eventInput = target.request().get(EventInput.class);
        while (!eventInput.isClosed()) {
            final InboundEvent inboundEvent = eventInput.read();
            if (inboundEvent == null) {
                break;
            }
            System.out.println(username + " > " + inboundEvent.readData(String.class));

            counter = 10;
            timer = new Timer();
            if (inboundEvent.readData(String.class).equals("roundEnd")) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        String sp = getSpecialPoints(username);
                        CommonData.specialPoints = Integer.parseInt(sp);
                        specialPointsLabel.setText(sp);
                    }
                });
                timer.schedule(new TimerTask() {
                    public void run() {
                        Platform.runLater(new Runnable() {
                            public void run() {
                                if (counter == 0) {
                                    timer.cancel();
                                    Stage stage = (Stage) label.getScene().getWindow();
                                    stage.close();
                                    Parent root = null;
                                    try {
                                        root = FXMLLoader.load(getClass().getResource("/fxml/GameWindow.fxml"));
                                    } catch (IOException ex) {
                                        Logger.getLogger(PlayerServiceHandler.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    Scene scene = new Scene(root);
                                    scene.getStylesheets().add("/styles/Styles.css");
                                    stage.setResizable(false);
                                    stage.setScene(scene);
                                    stage.show();
                                } else {
                                    label.setText(String.valueOf(counter));
                                    counter--;
                                }
                            }
                        });
                    }
                }, 0, 1000);
                System.out.println(username + " stopped listening");
                break;
            } else if (inboundEvent.readData(String.class).equals("gameComplete")) {
                Platform.runLater(() -> {
                    Stage scoringMenu = new Stage();
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(new Object().getClass().getResource("/fxml/ScoringMenu.fxml"));
                    } catch (IOException ex) {
                        Logger.getLogger(PlayerServiceHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Scene scene = new Scene(root);
                    scene.getStylesheets().add("/styles/Styles.css");
                    scoringMenu.setResizable(false);
                    scoringMenu.initStyle(StageStyle.UNDECORATED);
                    scoringMenu.setScene(scene);
                    scoringMenu.show();
                    scoringMenu = (Stage) label.getScene().getWindow();
                    scoringMenu.close();

                });
                break;
            } else {
                Platform.runLater(new Runnable() {
                    public void run() {
                        addObservableListData(inboundEvent.readData(String.class), playerScore);
                    }
                });

            }
        }
    }

    public static String removePlayer(String username) {
        String output = null;
        try {
            HttpURLConnection connection = new FactoryServiceHandler().getServiceConnection(PLAYER_CLASS, REMOVE_PLAYER, GET);
            sendOutput(username, connection);
            output = getInput(connection);
        } catch (IOException e) {
            System.out.println(e);
        }
        return output;
    }
    
    public static String checkTermination(){
        String output = null;
        try {
            HttpURLConnection connection = new FactoryServiceHandler().getServiceConnection(PLAYER_CLASS, CHECK_TERMINATION, POST);
            sendOutput(username, connection);
            output = getInput(connection);
        } catch (Exception e) {
            System.out.println(e);
        }
        return output;
    }

    private static void sendOutput(String username, HttpURLConnection connection) {
        try {
            Player player = new Player();
            player.setUsername(username);
            String input = new Gson().toJson(player);
            OutputStream os = connection.getOutputStream();
            os.write(input.getBytes());
            os.flush();
        } catch (IOException ex) {
            Logger.getLogger(WordServiceHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String getInput(HttpURLConnection connection) {
        BufferedReader br = null;
        String output = null;
        try {
            br = new BufferedReader(new InputStreamReader(
                    (connection.getInputStream())));
            output = br.readLine();
            return output;
        } catch (IOException ex) {
            Logger.getLogger(WordServiceHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(WordServiceHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return output;
    }
}
