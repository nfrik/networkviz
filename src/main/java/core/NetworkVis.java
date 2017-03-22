package core;

/**
 * Created by NF on 2/9/2017.
 */


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.util.*;

public class NetworkVis extends Application {
    final Group root = new Group();
    final XformWorld world = new XformWorld();
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final Xform cameraXform = new Xform();
    private static final double CAMERA_INITIAL_DISTANCE = -1000;
    private static final double CAMERA_INITIAL_X_ANGLE = 70.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 320.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 100000.0;
    private static final double AXIS_LENGTH = 250.0;
    private static final double CONTROL_MULTIPLIER = 0.1;
    private static final double SHIFT_MULTIPLIER = 10.0;
    private static final double MOUSE_SPEED = 1;
    private static final double ROTATION_SPEED = 2.0;
    private static final double TRACK_SPEED = 0.3;

    Graph mapGraph = null;

    double mousePosX, mousePosY, mouseOldX, mouseOldY, mouseDeltaX, mouseDeltaY;
    double mouseFactorX, mouseFactorY;
    private Group axisGroup = new Group();

    @Override
    public void start(Stage primaryStage) {
        root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);
        buildCamera();
        buildAxes();
//        buildBodySystem();
        Scene scene = new Scene(root, 800, 600, true);
        scene.setFill(Color.BLACK);
        handleMouse(scene);
        handleKeyboard(scene);
        primaryStage.setTitle("NetworkVis");
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.setCamera(camera);
        mouseFactorX = 180.0 / scene.getWidth();
        mouseFactorY = 180.0 / scene.getHeight();

        createUtilityWindow(primaryStage);
    }

    private void buildCamera() {
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(camera);
        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
    }

    private void build2DHexBodySystem(double dx, double dy, double a) {
//        PhongMaterial whiteMaterial = new PhongMaterial();
//        whiteMaterial.setDiffuseColor(Color.YELLOW);
//        whiteMaterial.setSpecularColor(Color.LIGHTBLUE);
//        Box box = new Box(400, 200, 100);
//        box.setMaterial(whiteMaterial);
//        PhongMaterial redMaterial = new PhongMaterial();
//        redMaterial.setDiffuseColor(Color.DARKRED);
//        redMaterial.setSpecularColor(Color.RED);
//        Sphere sphere = new Sphere(5);
//        sphere.setMaterial(redMaterial);
//        sphere.setTranslateX(10.0);
//        sphere.setTranslateY(-100.0);
//        sphere.setTranslateZ(-50.0);
//        world.getChildren().addAll(box);

        mapGraph = new Graph();
        mapGraph.getVertexDefaultMaterial().setDiffuseColor(Color.AQUA);
        mapGraph.getEdgeDefaultMaterial().setDiffuseColor(Color.TURQUOISE);
        mapGraph.setVertexDefaultRadius(20);

        mapGraph.generateHexagonalLattice2D(dx, dy, a, null, true);



        world.getChildren().addAll(mapGraph.getEdges());
        world.getChildren().addAll(mapGraph.getVertices());

        System.out.println("Total edges: " + mapGraph.getEdges().size());
        System.out.println("Total vertices: " + mapGraph.getVertices().size());

        setAndUpdatePositionsLoop(500,a);

    }

    private void build3DHexBodySystem(double dx, double dy, double dz, double a, double c) {

        mapGraph = new Graph();
        mapGraph.getVertexDefaultMaterial().setDiffuseColor(Color.AQUA);
        mapGraph.getEdgeDefaultMaterial().setDiffuseColor(Color.YELLOW);
        mapGraph.setVertexDefaultRadius(20);

        mapGraph.generateHexagonalLattice3D(dx, dy, dz, a, c, null, true);

        world.getChildren().addAll(mapGraph.getEdges());
        world.getChildren().addAll(mapGraph.getVertices());

        System.out.println("Total edges: " + mapGraph.getEdges().size());
        System.out.println("Total vertices: " + mapGraph.getVertices().size());

        setAndUpdatePositionsLoop(50,a);

    }

    private void setAndUpdatePositionsLoop(long millis, double bound){

        PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);
        Sphere sphere = new Sphere(20);
        sphere.setMaterial(redMaterial);
        sphere.setTranslateX(10.0);
        sphere.setTranslateY(-100.0);
        sphere.setTranslateZ(-50.0);

        final Point3D vacancy = new Point3D(0, 0, 0);

        world.getChildren().addAll(sphere);

        final Vertex[] nextVacancyPos = {mapGraph.getCentralVertexWithinBoundary(bound)};//TODO replace with actual side length a
        if(nextVacancyPos[0] !=null){
            sphere.setTranslateX(nextVacancyPos[0].getPoint3D().getX());
            sphere.setTranslateY(nextVacancyPos[0].getPoint3D().getY());
            sphere.setTranslateZ(nextVacancyPos[0].getPoint3D().getZ());
        }


        Random rn = new Random();
        Layout layout = new Layout();
        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(millis), new EventHandler<ActionEvent>() {
//            Point3D startP = new Point3D(0, 0, 0);
//            double alpha = 0;
//            double betta = 0;


            @Override
            public void handle(ActionEvent event) {
                //Here we simply update position by searching neighbors of given vertex

                List<Vertex> nbs = mapGraph.getAllNeighbors(nextVacancyPos[0]);
//                List<Vertex> nbs = mapGraph.getOutNeighbors(nextVacancyPos[0]);

                //If there are neighbors choose random element and assign new position
                if(nbs!=null){
                    nextVacancyPos[0] = nbs.get(rn.nextInt(nbs.size()));
                }

                if(nextVacancyPos[0] !=null){
                    sphere.setTranslateX(nextVacancyPos[0].getPoint3D().getX());
                    sphere.setTranslateY(nextVacancyPos[0].getPoint3D().getY());
                    sphere.setTranslateZ(nextVacancyPos[0].getPoint3D().getZ());
                }

//                Vertex v = (Vertex) mapGraph.getVertices().toArray()[rn.nextInt(mapGraph.getNumVertices()-1)];
//                Edge e = (Edge) mapGraph.getEdges().toArray()[rn.nextInt(mapGraph.getNumVertices()-1)];
//                PhongMaterial pm = new PhongMaterial();
//                pm.setDiffuseColor(new Color(rn.nextDouble(),rn.nextDouble(),rn.nextDouble(),1));
//                v.setMaterial(pm);
//                e.setMaterial(pm);
//
////                mapGraph.rotateEdgeAroundCenter(edge1,.4,betta);
////                alpha+=0.01;
////                betta+=0.01;
//
//                for(Vertex vertex: mapGraph.getVertices()){
//                    mapGraph.transformVertexRandomDelta(vertex,1);
//                }

//                layout.runSpring(mapGraph,mapGraph.getVertices().iterator().next());
            }
        }));

        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();
    }

    private void createUtilityWindow(Stage stage) {
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


        //Defining generate Hex2D Vacancy grid button
        Button hex2dVac = new Button("Generate Hex2D Vac");
        GridPane.setConstraints(hex2dVac, 0, 3);
        hex2dVac.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                world.getChildren().clear();
                buildAxes();
                build2DHexBodySystem(new Integer(deltax.getText()),new Integer(deltay.getText()),new Double(sideLength.getText()));
            }
        });
        grid.getChildren().add(hex2dVac);

        //Defining generate Hex3D Vacancy grid button
        Button hex3dVac = new Button("Generate Hex3D Vac");
        hex3dVac.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                world.getChildren().clear();
                buildAxes();
                build3DHexBodySystem(new Integer(deltax.getText()),new Integer(deltay.getText()),new Integer(deltaz.getText()),new Double(sideLength.getText()),new Double(verticalLength.getText()));
            }
        });
        GridPane.setConstraints(hex3dVac, 0, 4);
        grid.getChildren().add(hex3dVac);


        //Defining generate HCP 3D direct exchange button
        Button hcp3dExchange = new Button("Generate HCP Exchange");
        GridPane.setConstraints(hcp3dExchange, 0, 5);
        grid.getChildren().add(hcp3dExchange);

        //Defining Start button
        Button start = new Button("Start");
        GridPane.setConstraints(start, 0, 7);
        grid.getChildren().add(start);

        //Defining Start button
        Button stop = new Button("Stop");
        GridPane.setConstraints(stop, 1, 7);
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

    private void buildAxes() {
        System.out.println("buildAxes()");
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);
        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);
        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);
        final Box xAxis = new Box(AXIS_LENGTH, 1, 1);
        final Box yAxis = new Box(1, AXIS_LENGTH, 1);
        final Box zAxis = new Box(1, 1, AXIS_LENGTH);
        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);
        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        axisGroup.setVisible(true);
        world.getChildren().addAll(axisGroup);
    }


    private void handleMouse(Scene scene) {
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseOldX = me.getSceneX();
                mouseOldY = me.getSceneY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseDeltaX = (mousePosX - mouseOldX);
                mouseDeltaY = (mousePosY - mouseOldY);

                double modifier = 1.0;

                if (me.isControlDown()) {
                    modifier = CONTROL_MULTIPLIER;
                }
                if (me.isShiftDown()) {
                    modifier = SHIFT_MULTIPLIER;
                }
                if (me.isPrimaryButtonDown()) {
                    cameraXform.ry.setAngle(cameraXform.ry.getAngle() - mouseDeltaX * MOUSE_SPEED * modifier * ROTATION_SPEED);
                    cameraXform.rx.setAngle(cameraXform.rx.getAngle() + mouseDeltaY * MOUSE_SPEED * modifier * ROTATION_SPEED);
                } else if (me.isSecondaryButtonDown()) {
                    double z = camera.getTranslateZ();
                    double newZ = z + mouseDeltaX * MOUSE_SPEED * modifier;
                    camera.setTranslateZ(newZ);
                } else if (me.isMiddleButtonDown()) {
                    cameraXform.t.setX(cameraXform.t.getX() + mouseDeltaX * MOUSE_SPEED * modifier * TRACK_SPEED);
                    cameraXform.t.setY(cameraXform.t.getY() + mouseDeltaY * MOUSE_SPEED * modifier * TRACK_SPEED);
                }
            }
        });
    }

        private void handleKeyboard(Scene scene) {
            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    switch (event.getCode()) {
                        case Z:
                            cameraXform.t.setX(0.0);
                            cameraXform.t.setY(0.0);
                            camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
                            cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
                            cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
                            break;
                        case X:
                            axisGroup.setVisible(!axisGroup.isVisible());
                            break;
                        case V:
                            world.setVisible(!world.isVisible());
                            break;
                    }
                }
           });
        };

    public static void main(String[] args) {
        launch(args);
    }

}


class XformCamera extends Group {
    Point3D px = new Point3D(1.0, 0.0, 0.0);
    Point3D py = new Point3D(0.0, 1.0, 0.0);
    Rotate r;
    Transform t = new Rotate();

    public XformCamera() {
        super();
    }

    public void rx(double angle) {
        r = new Rotate(angle, px);
        this.t = t.createConcatenation(r);
        this.getTransforms().clear();
        this.getTransforms().addAll(t);
    }

    public void ry(double angle) {
        r = new Rotate(angle, py);
        this.t = t.createConcatenation(r);
        this.getTransforms().clear();
        this.getTransforms().addAll(t);
    }

}
