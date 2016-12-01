/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsbm.service;

import com.nsbm.common.CommonData;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Lakshitha
 */
public class FactoryServiceHandler {

    public HttpURLConnection getServiceConnection(String service, String action, String method) throws MalformedURLException, IOException {
        URL url = new URL(CommonData.IP + service + action);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON);
        return connection;
    }
}
