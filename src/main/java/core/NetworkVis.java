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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    private Timeline loop = null;

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

        UtilityMenu.getInstance().createUtilityWindow(primaryStage,this);
    }

    private void buildCamera() {
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(camera);
        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
    }

    protected void build2DHexBodySystem(double dx, double dy, double a, int copies) {
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



//        world.getChildren().addAll(mapGraph.getEdges());
        world.getChildren().addAll(mapGraph.getVertices());

        System.out.println("Total edges: " + mapGraph.getEdges().size());
        System.out.println("Total vertices: " + mapGraph.getVertices().size());

        setAndUpdatePositionsLoop(250,a, copies);

    }

    protected void build3DHexBodySystem(double dx, double dy, double dz, double a, double c, int copies) {

        mapGraph = new Graph();
        mapGraph.getVertexDefaultMaterial().setDiffuseColor(Color.AQUA);
        mapGraph.getEdgeDefaultMaterial().setDiffuseColor(Color.YELLOW);
        mapGraph.setVertexDefaultRadius(20);

        mapGraph.generateHexagonalLattice3D(dx, dy, dz, a, c, null, true);

//        world.getChildren().addAll(mapGraph.getEdges());
        world.getChildren().addAll(mapGraph.getVertices());

        System.out.println("Total edges: " + mapGraph.getEdges().size());
        System.out.println("Total vertices: " + mapGraph.getVertices().size());

        setAndUpdatePositionsLoop(250,a, copies);

    }

    private void setAndUpdatePositionsLoop(long millis, double bound, int copies){

        if(getLoop()!=null){
            getLoop().stop();
        }

        Random rn = new Random();
        PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

//        Sphere sphere = new Sphere(20);
//        sphere.setMaterial(redMaterial);

        List<Sphere> spheres = new ArrayList<>();

        for(int i=0;i<copies;i++){
            spheres.add(new Sphere(20));
            spheres.get(i).setMaterial(redMaterial);
        }

        final Point3D vacancy = new Point3D(0, 0, 0);

        world.getChildren().addAll(spheres);


        final Vertex nextVacancyPos = mapGraph.getCentralVertexWithinBoundary(bound);//TODO replace with actual side length a

        final List<Vertex> vertices = new ArrayList<Vertex>();


        for(Sphere sphere : spheres) {
            vertices.add(nextVacancyPos);
            if (nextVacancyPos != null) {
                sphere.setTranslateX(nextVacancyPos.getPoint3D().getX());
                sphere.setTranslateY(nextVacancyPos.getPoint3D().getY());
                sphere.setTranslateZ(nextVacancyPos.getPoint3D().getZ());
            }
        }



        final Point3D startPoint = nextVacancyPos.getPoint3D();



        setLoop(new Timeline(new KeyFrame(Duration.millis(millis), new EventHandler<ActionEvent>() {

            int steps = 1;


            @Override
            public void handle(ActionEvent event) {
                //Here we simply update position by searching neighbors of given vertex
                double rmsd = 0;

                //Get new positions
                for(Vertex v : vertices){


                }

                for(int i=0;i<vertices.size();i++){
                    List<Vertex> nbs = mapGraph.getAllNeighbors(vertices.get(i));
                    if(nbs!=null){
                        vertices.set(i,nbs.get(rn.nextInt(nbs.size())));
                    }
                    spheres.get(i).setTranslateX(vertices.get(i).getPoint3D().getX());
                    spheres.get(i).setTranslateY(vertices.get(i).getPoint3D().getY());
                    spheres.get(i).setTranslateZ(vertices.get(i).getPoint3D().getZ());
                }

                for(int i=0; i<vertices.size() ; i++){
                    double r=vertices.get(i).getPoint3D().distance(startPoint);
                    rmsd+=r*r/vertices.size();
                }

//                List<Vertex> nbs = mapGraph.getOutNeighbors(nextVacancyPos[0]);

                //If there are neighbors choose random element and assign new position


//                if(nextVacancyPos[0] !=null){
//                    sphere.setTranslateX(nextVacancyPos[0].getPoint3D().getX());
//                    sphere.setTranslateY(nextVacancyPos[0].getPoint3D().getY());
//                    sphere.setTranslateZ(nextVacancyPos[0].getPoint3D().getZ());
//                }


//                R.add(startPoint.distance(nextVacancyPos[0].getPoint3D()));

//                double Rn = startPoint.distance(nextVacancyPos[0].getPoint3D());

                UtilityMenu.getInstance().getRmsdLabel().setText(String.format("Rmsd: %2.2f Steps: %d", Math.sqrt(rmsd),steps));

                steps++;

            }
        })));

        getLoop().setCycleCount(Timeline.INDEFINITE);
        getLoop().play();
    }

    public Timeline getLoop() {
        return loop;
    }

    public void setLoop(Timeline loop) {
        this.loop = loop;
    }

    protected void buildAxes() {
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
