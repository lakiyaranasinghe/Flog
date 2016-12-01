/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsbm.service;

import com.google.gson.Gson;
import static com.nsbm.common.CommonData.FINAL_SCORE;
import static com.nsbm.common.CommonData.GET_SPECIAL_POINTS;
import static com.nsbm.common.CommonData.POINT_CLASS;
import static com.nsbm.common.CommonData.POST;
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
public class PointServiceHandler {

    public static String getSpecialPoints(String username) {
        String output = null;
        try {
            HttpURLConnection connection = new FactoryServiceHandler().getServiceConnection(POINT_CLASS, GET_SPECIAL_POINTS, POST);
            sendOutput(username, connection);
            output = getInput(connection);
        } catch (Exception e) {
            System.out.println(e);
        }
        return output;
    }
    
    public static String [] getFinalScore(String username) {
        String output = null;
        String [] scores = null;
        try {
            HttpURLConnection connection = new FactoryServiceHandler().getServiceConnection(POINT_CLASS, FINAL_SCORE, POST);
            sendOutput(username, connection);
            output = getInput(connection);
            Gson parser = new Gson();
            scores = parser.fromJson(output, String[].class);
        } catch (Exception e) {
            System.out.println(e);
        }
        return scores;
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
            Logger.getLogger(PointServiceHandler.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(PointServiceHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return output;
    }
}
