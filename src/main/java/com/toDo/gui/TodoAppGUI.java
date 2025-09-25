package com.toDo.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.toDo.dao.TodoAppDAO;
import com.toDo.model.Todo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class TodoAppGUI extends JFrame {
    private TodoAppDAO todoDAO;
    private JTable todoTable;
    private DefaultTableModel tableModel;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JCheckBox completedCheckBox;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JComboBox<String> filterComboBox;

    public TodoAppGUI() {
        this.todoDAO = new TodoAppDAO();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        loadTodo();
    }

    private void initializeComponents() {
        setTitle("Todo Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Title", "Description", "Completed", "Created At", "Updated At"};
        tableModel = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        todoTable = new JTable(tableModel);
        todoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        todoTable.getSelectionModel().addListSelectionListener(
            e -> {
                if(!e.getValueIsAdjusting()){
                    loadSelectedTodo();
                }
            }
        );

        titleField = new JTextField(20);
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        completedCheckBox = new JCheckBox("Completed");
        addButton = new JButton("Add Todo");
        updateButton = new JButton("Update Todo"); 
        deleteButton = new JButton("Delete Todo");
        refreshButton = new JButton("Refresh Todo");
        
        String[] filterOptions = {"All", "Completed", "Pending"};
        filterComboBox = new JComboBox<>(filterOptions);
        filterComboBox.addActionListener(e -> {
            filterTodos();
        });
    }
    private void setupLayout(){
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5,5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(new JLabel("Title"),gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(titleField,gbc);

        add(inputPanel,BorderLayout.WEST);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("description"),gbc);
        gbc.gridx = 1;
        inputPanel.add(new JScrollPane(descriptionArea) ,gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        inputPanel.add(completedCheckBox,gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter"));
        filterPanel.add(filterComboBox);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(inputPanel,BorderLayout.CENTER);
        northPanel.add(buttonPanel,BorderLayout.SOUTH);
        northPanel.add(filterPanel,BorderLayout.NORTH);

        add(northPanel,BorderLayout.NORTH);
        add(new JScrollPane(todoTable),BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("Status with app"));
        add(statusPanel,BorderLayout.SOUTH);
    }

    private void setupEventListeners(){
        addButton.addActionListener((e)->{
            addTodo();
        });

        updateButton.addActionListener((e)->{
            updateTodo();
        });

        deleteButton.addActionListener((e)->{
            deleteTodo();
        });

        refreshButton.addActionListener((e)->{
            refreshTodo();
        });
    }

    private void addTodo(){
        String titleString = titleField.getText().trim();
        String descriptionString = descriptionArea.getText().trim(); 
        boolean completed = completedCheckBox.isSelected();
        
        if(titleString.isEmpty()){
            JOptionPane.showMessageDialog(this, "Enter Title", "Validation error", JOptionPane.WARNING_MESSAGE);
            return ;
        }

        try{
            Todo todo = new Todo(titleString,descriptionString);
            todo.setCompleted(completed);
            todoDAO.createTodo(todo);

            JOptionPane.showMessageDialog(this,"Row inserted successfully","Success",JOptionPane.INFORMATION_MESSAGE);
            clearSelection();
            loadTodo();
        }
        catch(SQLException e){
            JOptionPane.showConfirmDialog(this, "Error adding todo" + e.getMessage(),"fail",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateTodo(){
        int row = todoTable.getSelectedRow();

        if(row==-1){
            JOptionPane.showMessageDialog(this, "please select row","Validation error" , JOptionPane.WARNING_MESSAGE);
            return ;
        }

        String title = titleField.getText().trim();
        if(title.isEmpty()){
            JOptionPane.showMessageDialog(this,"please select row" , "Validation error", JOptionPane.WARNING_MESSAGE);
        }

        int id = (int)todoTable.getValueAt(row, 0);
        try{
            Todo todo = todoDAO.getTodoById(id);
            
            if(todo != null){
                todo.setTitle(title);
                todo.setDescription(descriptionArea.getText().trim());
                todo.setCompleted(completedCheckBox.isSelected());
            }
            
            if(todoDAO.updateTodo(todo)){
                JOptionPane.showMessageDialog(this,"todo updated ok", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearSelection();
                loadTodo();
            }
            else{
                JOptionPane.showMessageDialog(this,"todo update fail","Update Error",JOptionPane.ERROR_MESSAGE);    
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(this,"todo update fail","Update Error",JOptionPane.ERROR_MESSAGE);
        }

    }

    private void deleteTodo(){
        int row = todoTable.getSelectedRow();

        if(row == -1){
            JOptionPane.showMessageDialog(this, "please select row","Validation Error" , JOptionPane.WARNING_MESSAGE);
            return ;
        }

        int id = (int)todoTable.getValueAt(row,0);
        try{
            Todo todo = todoDAO.getTodoById(id);
            if(todo != null){
                todoDAO.deleteTodo(todo);
                refreshTodo();
                JOptionPane.showMessageDialog(this, "Deleted todo successfully", "Deletion Successful", JOptionPane.INFORMATION_MESSAGE);
                clearSelection();
                loadTodo();
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(this, "todo deletion failed","Error in deletion",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTodo(){
        filterComboBox.setSelectedIndex(0);
        clearSelection();
        loadTodo();
    }

    private void clearSelection(){
        titleField.setText("");
        descriptionArea.setText("");
        completedCheckBox.setSelected(false);
        filterComboBox.setSelectedIndex(0);
    }

    private void loadTodo(){
        try {
            List<Todo> todos = todoDAO.getAllTodos();
            updateTable(todos);  
    
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load","Error",JOptionPane.ERROR_MESSAGE);
        }
             
    }

    private void updateTable(List<Todo> todos){
        tableModel.setRowCount(0);
        for(Todo t : todos){
        
            Object[] row = {
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.isCompleted(),
                t.getCreated_at(),
                t.getUpdated_at()
            };
            tableModel.addRow(row);
        }
    }

    private void loadSelectedTodo() {
        int row = todoTable.getSelectedRow();

        if(row != 1){
            String title = (String)tableModel.getValueAt(row,1);
            String descsription = (String)tableModel.getValueAt(row, 2);
            Boolean completed  = (boolean) tableModel.getValueAt(row, 3);

            titleField.setText(title);
            descriptionArea.setText(descsription);
            completedCheckBox.setSelected(completed);

        }
    } 
    
    private void filterTodos(){
        String selectedQuery = (String)filterComboBox.getSelectedItem();
        
        try {
            if(selectedQuery == "All"){
                refreshTodo();
            }
            else{
                List<Todo> todos = todoDAO.todoFilter(selectedQuery);
                updateTable(todos);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "An error has occurred in filtration","Filteration failed",JOptionPane.ERROR_MESSAGE);
        }
    }
}