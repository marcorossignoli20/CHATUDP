/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatudp;

import chatudp.connection.Client;
import chatudp.connection.Server;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;

/**
 *
 * @author marco.rossignoli
 */
public class GUIStartController implements Initializable {

    Server srv;
    Client clt;
    @FXML
    private JFXTextField txtUsername;

    @FXML
    private JFXTextField txtPassword;

    @FXML
    private JFXTextField txtIP;

    @FXML
    private JFXTextField txtPort;

    @FXML
    private JFXButton btnConnect;

    @FXML
    private JFXButton btnStartServer;

    @FXML
    public void onClickConnect(ActionEvent event) throws IOException {
        if (check()) {
            if (Client.getInstance().getError().equals("")) {
                try {
                    Client.getInstance().startConnection(txtUsername.getText(), txtPassword.getText(), txtIP.getText(), txtPort.getText());
                } catch (SocketException ex) {
                    Logger.getLogger(GUIStartController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(GUIStartController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Look, a Warning Dialog");
                alert.setContentText(Client.getInstance().getError());

                alert.showAndWait();
            }
        }
    }

    @FXML
    void onClickStartServer(ActionEvent event) {
        if (!txtPort.getText().trim().equals("")) {
            srv = new Server(Integer.parseInt(txtPort.getText()));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public boolean check() {
        if (!txtUsername.getText().trim().equals("")) {
            if (!txtPassword.getText().trim().equals("")) {
                if (!txtIP.getText().trim().equals("")) {
                    if (!txtPort.getText().trim().equals("")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
