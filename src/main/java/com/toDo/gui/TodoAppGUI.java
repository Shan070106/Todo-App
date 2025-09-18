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
                    // loadSelectedTodo();
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
            // filterTodos();
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

        gbc.gridx = 1;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Completed"),gbc);

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

    }

    private void updateTodo(){
        
    }

    private void deleteTodo(){
        
    }

    private void refreshTodo(){
        loadTodo();
    }

    private void loadTodo(){
        try {
             List<Todo> todos = todoDAO.getAllTodos();
            updateTable(todos);  
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load","Error",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
             
    }

    private void updateTable(List<Todo> todos){
        tableModel.setRowCount(0);
        for(Todo t : todos){
            Object[] row = {
                t.getId(),
                t.getTitle(),
                t.getCreated_at(),
                t.getDescription(),
                t.getUpdated_at(),
                t.isCompleted()
            };
            tableModel.addRow(row);
        }
    }
}