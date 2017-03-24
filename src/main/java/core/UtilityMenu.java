package core;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * Created by NF on 3/23/2017.
 */
public class UtilityMenu {


    private static UtilityMenu utilityMenu = null;

    protected UtilityMenu(){
        // Exists only to defeat instantiation
    }

    public static UtilityMenu getInstance(){
        if(utilityMenu==null){
            utilityMenu = new UtilityMenu();
        }
        return utilityMenu;
    }

    public Label getRmsdLabel() {
        return rmsdLabel;
    }

    public void setRmsdLabel(Label rmsdLabel) {
        this.rmsdLabel = rmsdLabel;
    }

    private static Label rmsdLabel;



    public void createUtilityWindow(Stage stage, NetworkVis parent) {
        final Stage reportingStage = new Stage();
        reportingStage.setTitle("Control Panel");
        reportingStage.initStyle(StageStyle.UTILITY);
        reportingStage.setX(stage.getX() + stage.getWidth());
        reportingStage.setY(stage.getY());


        //Setup the VBox Container and BorderPane
        BorderPane root = new BorderPane();
        VBox topContainer = new VBox();

        //Setup the Main Menu bar and the ToolBar
        MenuBar mainMenu = new MenuBar();
        ToolBar toolBar = new ToolBar();
        ToolBar toolBar2 = new ToolBar();
        VBox utilityLayout = new VBox(10);
        utilityLayout.setStyle("-fx-padding:10; -fx-background-color: linear-gradient(to bottom, lightblue, derive(lightblue, 20%));");

        //Create SubMenu File.
        Menu file = new Menu("File");
        MenuItem openFile = new MenuItem("Open File");
        MenuItem exitApp = new MenuItem("Exit");
        file.getItems().addAll(openFile,exitApp);

        //Create SubMenu Edit.
        Menu edit = new Menu("Edit");
        MenuItem properties = new MenuItem("Properties");
        edit.getItems().add(properties);

        //Create SubMenu Help.
        Menu help = new Menu("Help");
        MenuItem visitWebsite = new MenuItem("Visit Website");
        help.getItems().add(visitWebsite);

        mainMenu.getMenus().addAll(file, edit, help);

        //Create some toolbar buttons
        Button openFileBtn = new Button("Hellew");
        Button openFileBtn2 = new Button("Hellew2");
        Button printBtn = new Button();
        Button snapshotBtn = new Button();

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        //Coordinates input
        final TextField deltax = new TextField();
        deltax.setPromptText("Enter dX");
        deltax.setPrefColumnCount(10);
        deltax.getText();
        deltax.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    deltax.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        GridPane.setConstraints(deltax, 0, 0);
        grid.getChildren().add(deltax);

        final TextField deltay = new TextField();
        deltay.setPromptText("Enter dY");
        deltay.setPrefColumnCount(10);
        deltay.getText();
        deltay.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    deltay.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        GridPane.setConstraints(deltay, 0, 1);
        grid.getChildren().add(deltay);

        final TextField deltaz = new TextField();
        deltaz.setPromptText("Enter dY");
        deltaz.setPrefColumnCount(10);
        deltaz.getText();
        deltaz.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    deltaz.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        GridPane.setConstraints(deltaz, 0, 2);
        grid.getChildren().add(deltaz);


        final TextField sideLength = new TextField();
        sideLength.setPromptText("Enter side length a");
        sideLength.setPrefColumnCount(10);
        sideLength.getText();
        sideLength.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    sideLength.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        GridPane.setConstraints(sideLength, 1, 0);
        grid.getChildren().add(sideLength);

        final TextField verticalLength = new TextField();
        verticalLength.setPromptText("Enter vertical length c");
        verticalLength.setPrefColumnCount(10);
        verticalLength.getText();
        verticalLength.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    verticalLength.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        GridPane.setConstraints(verticalLength, 1, 1);
        grid.getChildren().add(verticalLength);

        final TextField deltaT = new TextField();
        deltaT.setPromptText("Enter Delta T in millis");
        deltaT.setPrefColumnCount(10);
        deltaT.getText();
        deltaT.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    deltaT.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        GridPane.setConstraints(deltaT, 1, 2);
        grid.getChildren().add(deltaT);

        final TextField nCopies = new TextField();
        nCopies.setPromptText("Number of copies");
        nCopies.setPrefColumnCount(10);
        nCopies.setText("10");
        nCopies.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    nCopies.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        GridPane.setConstraints(nCopies, 1, 3);
        grid.getChildren().add(nCopies);

        //Defining generate Hex2D Vacancy grid button
        Button hex2dVac = new Button("Generate Hex2D Vac");
        GridPane.setConstraints(hex2dVac, 0, 5);
        hex2dVac.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                parent.world.getChildren().clear();
                parent.buildAxes();
                parent.build2DHexBodySystem(new Integer(deltax.getText()),new Integer(deltay.getText()),new Double(sideLength.getText()),new Integer(nCopies.getText()));
            }
        });
        grid.getChildren().add(hex2dVac);

        setRmsdLabel(new Label());
        GridPane.setConstraints(getRmsdLabel(),1,6);
        grid.getChildren().add(getRmsdLabel());

        //Defining generate Hex3D Vacancy grid button
        Button hex3dVac = new Button("Generate Hex3D Vac");
        hex3dVac.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                parent.getLoop().stop();
                parent.world.getChildren().clear();
                parent.buildAxes();
                parent.build3DHexBodySystem(new Integer(deltax.getText()),new Integer(deltay.getText()),new Integer(deltaz.getText()),new Double(sideLength.getText()),new Double(verticalLength.getText()),new Integer(nCopies.getText()));
            }
        });
        GridPane.setConstraints(hex3dVac, 0, 6);
        grid.getChildren().add(hex3dVac);


        //Defining generate HCP 3D direct exchange button
        Button hcp3dExchange = new Button("Generate HCP Exchange");
        GridPane.setConstraints(hcp3dExchange, 0, 7);
        grid.getChildren().add(hcp3dExchange);

        //Defining Start button
        Button start = new Button("Start");
        GridPane.setConstraints(start, 0, 8);
        grid.getChildren().add(start);

        //Defining Start button
        Button stop = new Button("Stop");
        GridPane.setConstraints(stop, 1, 8);
        grid.getChildren().add(stop);

        //Add the ToolBar and Main Meu to the VBox
        topContainer.getChildren().add(mainMenu);
        topContainer.getChildren().add(grid);
        topContainer.getChildren().add(utilityLayout);

        //Apply the VBox to the Top Border
        root.setTop(topContainer);
        Scene scene = new Scene(root, 300, 250);

        // layout the utility pane.
        utilityLayout.setStyle("-fx-padding:10; -fx-background-color: linear-gradient(to bottom, lightblue, derive(lightblue, 20%));");
        utilityLayout.setPrefHeight(530);
        reportingStage.setWidth(500);
        reportingStage.setHeight(800);
        reportingStage.setScene(scene);
        reportingStage.show();
//
        // ensure the utility window closes when the main app window closes.
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                reportingStage.close();
            }
        });

    }
}
