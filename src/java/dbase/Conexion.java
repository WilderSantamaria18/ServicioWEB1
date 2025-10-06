/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author USER
 */
public class Conexion {
    public static Connection MySQL() throws ClassNotFoundException, SQLException {
        try {
      
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
  
            Class.forName("com.mysql.jdbc.Driver");
        }

        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/iphone_db?user=root&password=123456");

        return c;
    }
}
