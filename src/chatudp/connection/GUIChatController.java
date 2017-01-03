/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatudp.connection;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 *
 * @author Marco
 */
public class GUIChatController implements Initializable{
    @FXML
    private JFXTextArea txtOutput;

    @FXML
    private JFXTextArea txtInput;

    @FXML
    private JFXButton btnSend;
    
    
    @FXML
    void onClickSend(ActionEvent event) throws IOException {
        Client.getInstance().sendMessage(txtInput.getText());
        addMessage("io|"+txtInput.getText());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
            Task read = new Task<Void>(){
            @Override public Void call() {
                while(true){
                    try {          
                            addMessage(Client.getInstance().readMessage().trim());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        
        new Thread(read).start();
    }
    
    public void addMessage(String message){
        int i = 0;
        String nome = "";
        while(message.charAt(i) != '|'){
            nome += message.charAt(i);
            i++;
        }
        i++;
        message = "\n" + nome + ": " + message.substring(i);
        txtOutput.setText(txtOutput.getText() + message);
    }
    
    
}
