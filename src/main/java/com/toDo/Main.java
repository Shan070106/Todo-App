package com.toDo;
import java.sql.SQLException;
import java.sql.Connection;

import com.toDo.util.DatabaseConnection;
public class Main {
    public static void main(String arg[]){
        DatabaseConnection dbConnection = new DatabaseConnection();
        try{
            Connection cn = dbConnection.getDBConnection();
            System.out.println("Connection ok dei super da");
        }
        catch(SQLException e){
            System.out.println("connection failed da");
        }
    }
}
