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
    public List<Todo> getAllTodos() throws SQLException {
        List<Todo> todos = new ArrayList<Todo>();

        try( Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos ORDER BY created_at DESC;");
            ResultSet res = stmt.executeQuery();
        ){
            while(res.next()){
                Todo todo = new Todo();

                todo.setId(res.getInt("id"));
                todo.setTitle(res.getString("title"));
                todo.setDescription(res.getString("description"));
                todo.setCompleted(res.getBoolean("completed"));
                
                // LocalDateTime createdAt = res.getTimestamp("created_at").toLocalDateTime();
                todo.setCreated_at(res.getTimestamp("created_at").toLocalDateTime());
                todo.setUpdeated_at(res.getTimestamp("updated_at").toLocalDateTime());
                todos.add(todo);
            }
        }
        return todos;
    }
}
