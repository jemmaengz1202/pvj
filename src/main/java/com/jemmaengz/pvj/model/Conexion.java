package com.jemmaengz.pvj.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jemmaengz.pvj.Utilities;

public class Conexion {
    private final static String user = Utilities.dotenv.get("DB_USER");
    private final static String password = Utilities.dotenv.get("DB_PASSWORD");
    private final static String url = Utilities.dotenv.get("DB_URL");
    
    public static Connection getConexion() {
        Connection con = null;
        
        try {
            
            con = DriverManager.getConnection(url, user, password);
            
            if(con != null) {
                System.out.println("Conectado");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return con;
    }
    
}
