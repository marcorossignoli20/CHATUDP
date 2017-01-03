package chatudp.connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Client {
    String username;
    String password;
    InetAddress ipAddress;
    int port;
    DatagramSocket clientSocket;
    byte[] sendData;
    byte[] receiveData;
    private final static Client instance = new Client();
    public String error = "";
    
    public void startConnection(String username, String password, String ipAddress, String port) throws SocketException, UnknownHostException, IOException{
        this.username = username;
        this.password = password;
        this.ipAddress = InetAddress.getByName(ipAddress);
        this.port = Integer.parseInt(port);
        clientSocket = new DatagramSocket();
        checkServer();
    }

    public static Client getInstance() {
        return instance;
    }
    
    
    public void checkServer() throws SocketException, IOException{
        String message = "*" + username + "|" + password;
        byte[] data = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, ipAddress, port);
        clientSocket.send(sendPacket);
        data = new byte[100];
        DatagramPacket receivedPacket = new DatagramPacket(data, data.length, ipAddress, port);
        clientSocket.receive(receivedPacket);
        if(new String(receivedPacket.getData()).trim().equals("ok")){
            error = "";
            start();
        } else {
            error = "Password errata";
        }
    }
    
    public void start(){
        try{

            FXMLLoader load = new FXMLLoader(getClass().getResource("GUIChat.fxml"));
            Stage secondaryStage = new Stage();
            AnchorPane layout2 = (AnchorPane)load.load();


            Scene scene2 = new Scene(layout2);
            secondaryStage.setTitle("ChatUDP");
            secondaryStage.setResizable(false);
            secondaryStage.setScene(scene2);

            secondaryStage.show();
        }catch(Exception e){System.out.println(e.getMessage());}
    }
    
    public void sendMessage(String input) throws IOException{
        String message = "$"+username+"|" + input;
        byte[] data = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, ipAddress, port);
        clientSocket.send(sendPacket);
        
    }
    
    
	public String readMessage() throws IOException{
            byte[] data = new byte[100];
            DatagramPacket packet = new DatagramPacket(data, data.length,ipAddress, port);
            clientSocket.receive(packet);
            return new String(packet.getData());
	}

    public String getError() {
        return error;
    }
        
    
    
}
