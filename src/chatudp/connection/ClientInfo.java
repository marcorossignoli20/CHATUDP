/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatudp.connection;

import java.net.InetAddress;

/**
 *
 * @author Marco
 */
public class ClientInfo {
    private String nome;
    private InetAddress ip;
    private String port;

    
    public ClientInfo(String nome, InetAddress ip, String port){
        this.nome = nome;
        this.ip = ip;
        this.port = port;
    }

    public String getNome() {
        return nome;
    }

    public InetAddress getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }
    
    
}
