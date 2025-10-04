package com.toDo.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.toDo.model.Todo;
import com.toDo.util.DatabaseConnection;

public class TodoAppDAO {

    private static final String SELECT_ALL_TODO = "SELECT * FROM todo ORDER BY created_at DESC";
    private static final String INSERT_TODO = "INSERT INTO todo (title,description,completed, created_at,updated_at) VALUES(?,?,?,?,?)";
    private static final String SELECT_TODO_BY_ID = "SELECT * FROM todo WHERE id=?";
    private static final String UPDATE_TODO = "UPDATE todo SET title = ?, description = ?, completed = ?,updated_at = ? WHERE id = ?";
    private static final String DELETE_TODO = "DELETE FROM todo WHERE id = ?";
    private static final String FILTER_TODO = "SELECT * FROM todo WHERE completed = ?";

    private Todo getTodo(ResultSet res)throws SQLException{
        int id = res.getInt("id");
        String title = res.getString("title");
        String description = res.getString("description");
        Boolean completed = res.getBoolean("completed");
        Timestamp updatedTs = res.getTimestamp("updated_at");
        Timestamp createdTs = res.getTimestamp("created_at");

    LocalDateTime updated_at = updatedTs != null ? updatedTs.toLocalDateTime() : null;
    LocalDateTime created_at = createdTs != null ? createdTs.toLocalDateTime() : null;
        
        Todo todo =  new Todo(id,title,description,completed,created_at,updated_at);
        return todo;
    }

    public List<Todo> getAllTodos() throws SQLException {
        List<Todo> todos = new ArrayList<>();

        try( Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_TODO);
            ResultSet res = stmt.executeQuery();
        ){
            while(res.next()){
                
                todos.add(getTodo(res));
                
            }
        }
        return todos;
    }

    public int createTodo(Todo todo) throws SQLException{
        if (todo.getCreated_at() == null) todo.setCreated_at(LocalDateTime.now());
        if (todo.getUpdated_at() == null) todo.setUpdated_at(todo.getCreated_at());

        try(
            Connection cn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = cn.prepareStatement(INSERT_TODO, Statement.RETURN_GENERATED_KEYS);
        ) {
            stmt.setString(1,todo.getTitle());
            stmt.setString(2,todo.getDescription());
            stmt.setBoolean(3, todo.isCompleted());
            stmt.setTimestamp(4,Timestamp.valueOf(todo.getCreated_at()));
            stmt.setTimestamp(5, Timestamp.valueOf(todo.getUpdated_at()));

            int rowsAffected = stmt.executeUpdate();

            if(rowsAffected == 0){
                throw new SQLException("Inserting a row failed, no ID obtained");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    todo.setId(id);
                    return id;
                }    
                else{
                    throw new SQLException("Creating failed, no ID obtained.");
                }            
            }
        }
        
    }

    public Todo getTodoById(int todoId) throws SQLException{
        try(
            Connection cn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = cn.prepareStatement(SELECT_TODO_BY_ID);
        ){
            stmt.setInt(1, todoId);
            ResultSet res = stmt.executeQuery();
            if(res.next()){
                return getTodo(res);
            }
        }
        return null;
    }
    
    public boolean updateTodo(Todo todo) throws SQLException {
        try(
            Connection cn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = cn.prepareStatement(UPDATE_TODO);
        )
        {
            stmt.setString(1, todo.getTitle());
            stmt.setString(2,todo.getDescription());
            stmt.setBoolean(3, todo.isCompleted());
            stmt.setTimestamp(4,Timestamp.valueOf(todo.getUpdated_at()));
            stmt.setInt(5,todo.getId());

            int rowsAffected = stmt.executeUpdate();
            return (rowsAffected>0);
        }
    }

    public boolean deleteTodo(int tododId) throws SQLException{
        try (
            Connection cn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = cn.prepareStatement(DELETE_TODO);
        ) {
            stmt.setInt(1, tododId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected>0;

        } 
    }

    public List<Todo> todoFilter(boolean  selectedQuery) throws SQLException{
        List<Todo> todos = new ArrayList<>();
        try (
                Connection cn = DatabaseConnection.getDBConnection();
                PreparedStatement stmt = cn.prepareStatement(FILTER_TODO); //final attribute in upper case
            ) {
                stmt.setBoolean(1, selectedQuery);
                ResultSet res = stmt.executeQuery();

                while(res.next()){
                    todos.add(getTodo(res));
                }
                return todos;
            } 
    }
}
