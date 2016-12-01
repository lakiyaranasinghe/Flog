/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsbm.common;

import javafx.scene.control.TextField;

/**
 *
 * @author Muthu Devendra
 */
public class Validator {
    public boolean isInputEmpty(TextField textField) {
        Boolean b= false;
        if (!(textField.getText() == null || textField.getText().length() == 0)) 
            b = true;
        return b;
    }
    public boolean isNumeric(String string){
        for (char c : string.toCharArray())
        {
            if (!Character.isDigit(c)) 
                return false;
        }
        return true;        
    }
}
