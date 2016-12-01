/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsbm.service;

import com.nsbm.common.CommonData;
import static com.nsbm.common.CommonData.BROADCAST;
import static com.nsbm.common.CommonData.NEXT_ROUND_BROADCAST;
import static com.nsbm.common.CommonData.POST;
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
public class RoundServiceHandler {

    public static void notifyNextRoundStart() {
        String output = null;
        try {
            HttpURLConnection connection = new FactoryServiceHandler().getServiceConnection(BROADCAST, NEXT_ROUND_BROADCAST, POST);
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
