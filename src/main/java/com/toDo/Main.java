package com.toDo;
import java.sql.SQLException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.sql.Connection;

import com.toDo.gui.TodoAppGUI;
import com.toDo.util.DatabaseConnection;
public class Main {
    public static void main(String arg[]){
        
        try{
            Connection cn = DatabaseConnection.getDBConnection();
            System.out.println("Connection ok dei super da");
        }
        catch(SQLException e){
            System.out.println("connection failed da");
        }
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e){
            System.out.println();
        }
        SwingUtilities.invokeLater(
            ()->{
                try {
                    new TodoAppGUI().setVisible(true);
                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println("error starting");
                }
            }
        );
    }
}
