/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsbm.service;

import com.google.gson.Gson;
import static com.nsbm.common.CommonData.GAME_CLASS;
import static com.nsbm.common.CommonData.POST;
import static com.nsbm.common.CommonData.START_GAME;
import static com.nsbm.common.CommonData.username;
import com.nsbm.entity.Player;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lakshitha
 */
public class GameServiceHandler {

    public static String startGame() {
        String output = null;
        try {
            HttpURLConnection connection = new FactoryServiceHandler().getServiceConnection(GAME_CLASS, START_GAME, POST);
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
