/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatudp.userdata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marco
 */
public class DBManager {
    Statement stmt = null;
    
    public DBManager(){
    String path = "./db/users.db";
    Connection c = null;
    
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + path);
            stmt = c.createStatement();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }
    
    public boolean checkUser(String username){
        String query = "select * from userData;";
        ResultSet rs = null;
        
        try {
            rs = stmt.executeQuery(query);
            while(rs.next()){
                if(rs.getString("username").equals(username)) return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean checkPass(String username, String password){
        String query = "select * from userData where username = '"+ username +"'";
        ResultSet rs = null;
        
        try {
            rs = stmt.executeQuery(query);
            while(rs.next()){
                if(rs.getString("password").equals(password)) return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public void insertUser(String username, String password){
        String insert = "insert into userData (username, password) values ('"+username.trim()+"','"+password.trim()+"');";

        
        try {
            stmt.executeUpdate(insert);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
