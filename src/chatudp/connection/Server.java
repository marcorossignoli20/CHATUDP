package chatudp.connection;

import chatudp.userdata.DBManager;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    DatagramSocket serverSocket;
    DBManager db = new DBManager();
    ArrayList<ClientInfo> ar = new ArrayList<ClientInfo>();

    public Server(int port) {
        try {
            start(port);
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void start(int port) throws SocketException, IOException {
        DatagramPacket receivePacket;
        serverSocket = new DatagramSocket(port);

        while (true) {
            byte[] receiveData = new byte[1024];
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String sentence = new String(receivePacket.getData());
            check(sentence.trim(), receivePacket);
        }
    }

    public void check(String sentence, DatagramPacket receivePacket) throws IOException {
        System.out.println("RECEIVED: " + sentence);
        if (sentence.charAt(0) == '*') {
            service(sentence, receivePacket);
        } else if (sentence.charAt(0) == '$') {
            message(sentence.substring(1), receivePacket);
        }
    }

    public void service(String sentence, DatagramPacket receivePacket) throws IOException {
        sentence = sentence.substring(1);
        String[] userpass = new String[2];
        userpass = sentence.split("\\|");
        String username = userpass[0];
        String password = userpass[1];
        if (db.checkUser(username)) {
            if (db.checkPass(username, password)) {
                ar.add(new ClientInfo(username, receivePacket.getAddress(), receivePacket.getPort() + ""));
                byte[] data = "ok".getBytes();
                DatagramPacket sendPacket = new DatagramPacket(data, data.length, receivePacket.getAddress(), receivePacket.getPort());
                serverSocket.send(sendPacket);
            } else {
                byte[] data = "wrong".getBytes();
                DatagramPacket sendPacket = new DatagramPacket(data, data.length, receivePacket.getAddress(), receivePacket.getPort());
                serverSocket.send(sendPacket);
            }
        } else {
            db.insertUser(username, password);
            ar.add(new ClientInfo(username, receivePacket.getAddress(), receivePacket.getPort() + ""));
            byte[] data = "ok".getBytes();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, receivePacket.getAddress(), receivePacket.getPort());
            serverSocket.send(sendPacket);
        }
    }

    public void message(String sentence, DatagramPacket receivedPacket) throws IOException {
        int k = 0;
        String nome = "";
        while (sentence.charAt(k) != '|') {
            nome += sentence.charAt(k);
            k++;
        }
        for (int i = 0; i < ar.size(); i++) {
            if (!ar.get(i).getNome().equals(nome)) {
                byte[] data = (sentence).getBytes();
                DatagramPacket sendPacket = new DatagramPacket(data, data.length, ar.get(i).getIp(), Integer.parseInt(ar.get(i).getPort()));
                serverSocket.send(sendPacket);
            }
        }
    }
}
