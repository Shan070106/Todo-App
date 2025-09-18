package com.toDo.dao;
import com.toDo.model.Todo;
import java.sql.Connection;
import java.sql.PreparedStatement;

import com.toDo.util.DatabaseConnection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

public class TodoAppDAO {
    private Todo getTodo(ResultSet res)throws SQLException{
        int id = res.getInt("id");
        String title = res.getString("title");
        String description = res.getString("description");
        Boolean completed = res.getBoolean("completed");
        LocalDateTime updated_at = res.getTimestamp("updated_at").toLocalDateTime();
        LocalDateTime created_at = res.getTimestamp("created_at").toLocalDateTime();
        
        return new Todo(id,title,description,completed,created_at,updated_at);
    }
    public List<Todo> getAllTodos() throws SQLException {
        List<Todo> todos = new ArrayList<>();

        try( Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todo ORDER BY created_at DESC;");
            ResultSet res = stmt.executeQuery();
        ){
            while(res.next()){
                todos.add(getTodo(res));
            }
        }
        return todos;
    }
}
