package csc221.project;

import csc221.project.database.Database;
import csc221.project.employees.Employee;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    Database db;
    ObservableList<Employee> list;

    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane root = new StackPane();

        /** User and Password Scene */
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        Scene userandpassScene = new Scene(gridPane, 250, 100);
        Scene mainScene = new Scene(root, 900, 800);

        // Username text field
        TextField username = new TextField();
        username.setPromptText("Enter your username:");
        username.setPrefColumnCount(10);
        username.getText();
        gridPane.setConstraints(username, 0, 0);
        gridPane.getChildren().add(username);

        // Password text field
        TextField password = new TextField();
        password.setPromptText("Enter your password:");
        gridPane.setConstraints(password, 0, 1);
        gridPane.getChildren().add(password);

        // Submit Button
        Button submit = new Button();
        submit.setText("Submit");
        gridPane.setConstraints(submit, 1, 0);
        gridPane.getChildren().add(submit);

        // Clear Button
        Button clear = new Button();
        clear.setText("Clear");
        gridPane.setConstraints(clear, 1, 1);
        gridPane.getChildren().add(clear);

        Label sorry = new Label();
        gridPane.setConstraints(sorry, 0, 2);
        gridPane.getChildren().add(sorry);

        // Adds action
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String checkUser = username.getText().toString();
                String checkPass = password.getText().toString();
                if(checkUser.equals(Database.getUsername()) && checkPass.equals(Database.getPassword())) {
                    primaryStage.setScene(mainScene);
                    db = new Database();

                    // Grid pane for the second scene
                    GridPane grid = new GridPane();
                    grid.setHgap(5);
                    grid.setVgap(5);
                    grid.setPadding(new Insets(10, 5, 10, 5));

                    // Table
                    TableView<Employee> table = createTable();
                    grid.setConstraints(table, 0, 0);
                    grid.getChildren().add(table);

                    // Text Field ssn
                    TextField ssn = new TextField();
                    ssn.setPromptText("SSN:");
                    grid.setConstraints(ssn, 0, 1);
                    grid.getChildren().add(ssn);

                    // First Name
                    TextField fn = new TextField();
                    fn.setPromptText("First Name: ");
                    grid.setConstraints(fn, 0, 2);
                    grid.getChildren().add(fn);

                    // Last Name
                    TextField ln = new TextField();
                    ln.setPromptText("Last Name: ");
                    grid.setConstraints(ln, 0, 3);
                    grid.getChildren().add(ln);

                    // Birthday
                    TextField bday = new TextField();
                    bday.setPromptText("Birthday: ");
                    grid.setConstraints(bday, 0, 4);
                    grid.getChildren().add(bday);

                    // Employee Type
                    TextField type = new TextField();
                    type.setPromptText("Employee Type: ");
                    grid.setConstraints(type, 0, 5);
                    grid.getChildren().add(type);

                    // Department Name
                    TextField dep = new TextField();
                    dep.setPromptText("Department: ");
                    grid.setConstraints(dep, 0, 6);
                    grid.getChildren().add(dep);

                    // Insert Button
                    Button insertButton = new Button("Insert");
                    grid.setConstraints(insertButton, 1, 1);
                    grid.getChildren().add(insertButton);

                    // Search First Name
                    Button searchFirstNameButton = new Button("Search First Name");
                    grid.setConstraints(searchFirstNameButton, 1, 2);
                    grid.getChildren().add(searchFirstNameButton);

                    // Search First Name
                    Button searchSalesButton = new Button("Search Sales");
                    grid.setConstraints(searchSalesButton, 1, 3);
                    grid.getChildren().add(searchSalesButton);

                    // Action
                    insertButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            list = db.insert(ssn.getText().toString(), fn.getText().toString(), ln.getText().toString(), bday.getText().toString(), type.getText().toString(), dep.getText().toString());
                            table.setItems(list);
                        }
                    });

                    searchFirstNameButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            list = db.searchFirstName(fn.getText().toString());
                            table.setItems(list);
                        }
                    });

                    searchSalesButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            list = db.searchSales("SALES");
                            table.setItems(list);
                        }
                    });

                    db.selectAllEmployees();
                    root.getChildren().add(grid);
                } else {
                    sorry.setText("Sorry, but that is the wrong username or password!!");
                }
            }
        });

        clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                username.clear();
                password.clear();
                sorry.setText("");
            }
        });

        primaryStage.setTitle("Employees Database");
        primaryStage.setScene(userandpassScene);
        primaryStage.show();
    }

    public TableView<Employee> createTable() {
        TableView<Employee> res = new TableView<Employee>();

        TableColumn<Employee, String> SSNCol = new TableColumn<Employee, String>("Social Security Number");
        TableColumn<Employee, String> fullNameCol = new TableColumn<Employee, String>("Full Name");
        TableColumn<Employee, String> FNCol = new TableColumn<Employee, String>("First Name");
        TableColumn<Employee, String> LNCol = new TableColumn<Employee, String>("Last Name");
        TableColumn<Employee, String> BDayCol = new TableColumn<Employee, String>("Birthday");
        TableColumn<Employee, String> ETypeCol = new TableColumn<Employee, String>("Employee Type");
        TableColumn<Employee, String> DepTypeCol = new TableColumn<Employee, String>("Department Type");
        fullNameCol.getColumns().addAll(FNCol, LNCol);
        list = db.selectAllEmployees();
        res.setItems(list);

        SSNCol.setCellValueFactory(new PropertyValueFactory<>("socialSecurityNumber"));
        FNCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        LNCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        BDayCol.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        ETypeCol.setCellValueFactory(new PropertyValueFactory<>("employeeType"));
        DepTypeCol.setCellValueFactory(new PropertyValueFactory<>("departmentType"));

        res.getColumns().addAll(SSNCol, fullNameCol, BDayCol, ETypeCol, DepTypeCol);

        return res;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
