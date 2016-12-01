/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsbm.service;

import com.google.gson.Gson;
import static com.nsbm.common.CommonData.CHANGE_LETTER;
import static com.nsbm.common.CommonData.INITIAL_LETTERS;
import static com.nsbm.common.CommonData.LETTERS;
import static com.nsbm.common.CommonData.POST;
import static com.nsbm.common.CommonData.SUBMIT_WORD;
import static com.nsbm.common.CommonData.WORD_CLASS;
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
public class WordServiceHandler {

    public static String getInitialLetters() {
        String output = null;
        try {
            HttpURLConnection connection = new FactoryServiceHandler().getServiceConnection(WORD_CLASS, INITIAL_LETTERS, POST);
            sendOutput(username, connection);
            output = getInput(connection);
        } catch (Exception e) {
            System.out.println(e);
        }
        return output;
    }

    public static String getLetters(int vowelsRequired, int consonantsRequired) {
        String output = null;
        try {
            HttpURLConnection connection = new FactoryServiceHandler().getServiceConnection(WORD_CLASS, LETTERS+"/"+vowelsRequired+"/"+consonantsRequired, POST);
            sendOutput(username, connection);
            output = getInput(connection);
        } catch (Exception e) {
            System.out.println(e);
        }
        return output;
    }
    
    public static String changeLetter(String currentLetter) {
        String output = null;
        try {
            HttpURLConnection connection = new FactoryServiceHandler().getServiceConnection(WORD_CLASS, CHANGE_LETTER+"/"+currentLetter, POST);
            sendOutput(username, connection);
            output = getInput(connection);
        } catch (Exception e) {
            System.out.println(e);
        }
        return output;
    }

    public static String addWord(String word) {
        String output = null;
        try {
            HttpURLConnection connection = new FactoryServiceHandler().getServiceConnection(WORD_CLASS, SUBMIT_WORD+"/"+word, POST);
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
