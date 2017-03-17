package core;

/**
 * Created by NF on 2/9/2017.
 */


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
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

    double mousePosX, mousePosY, mouseOldX, mouseOldY, mouseDeltaX, mouseDeltaY;
    double mouseFactorX, mouseFactorY;
    private Group axisGroup = new Group();

    @Override
    public void start(Stage primaryStage) {
        root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);
        buildCamera();
        buildAxes();
        buildBodySystem();
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

    }

    private void buildCamera() {
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(camera);
        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
    }

    private void buildBodySystem() {
        PhongMaterial whiteMaterial = new PhongMaterial();
        whiteMaterial.setDiffuseColor(Color.YELLOW);
        whiteMaterial.setSpecularColor(Color.LIGHTBLUE);
        Box box = new Box(400, 200, 100);
        box.setMaterial(whiteMaterial);
        PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);
        Sphere sphere = new Sphere(5);
        sphere.setMaterial(redMaterial);
        sphere.setTranslateX(10.0);
        sphere.setTranslateY(-100.0);
        sphere.setTranslateZ(-50.0);
//        world.getChildren().addAll(box);


        Graph mapGraph = new Graph();
        mapGraph.getVertexDefaultMaterial().setDiffuseColor(Color.AQUA);
        mapGraph.getEdgeDefaultMaterial().setDiffuseColor(Color.WHITE);
        mapGraph.setVertexDefaultRadius(20);
//
//        Vertex v1=new Vertex(0,0,0);
//        Vertex v2=new Vertex(100,100,50);
//        Vertex v3=new Vertex(70,700,100);
//        Vertex v4=new Vertex(-80,100,-50);
//        Vertex v5=new Vertex(0,0,500);
//
//
//        mapGraph.addEdge(v1,v2, whiteMaterial,new Object());
//        mapGraph.addEdge(v1,v3,whiteMaterial,new Object());
//        mapGraph.addEdge(v1,v4,whiteMaterial,new Object());
//        mapGraph.addEdge(v2,v5,whiteMaterial,new Object());
//        mapGraph.addEdge(v3,v5,whiteMaterial,new Object());
//        mapGraph.addEdge(v4,v5,whiteMaterial,new Object());

//        mapGraph.generateRandomGraph(40,0.96,whiteMaterial);
          mapGraph.generateSquareLattice2D(5000,5000,50,null,true);



//        Vertex v1 = new Vertex(0,0,0);
//        Vertex v2 = new Vertex(0,100,0);
//        Vertex v3 = new Vertex(0,0,100);
//        Vertex v4 = new Vertex(0,200,90);
//
//        mapGraph.addEdge(v1,v2);
//        mapGraph.addEdge(v2,v3);
//        mapGraph.addEdge(v3,v1);
//        mapGraph.addEdge(v3,v4);


//        Edge edge1 = mapGraph.createEdge(new Vertex(-100,-100,-100),new Vertex(100,100,100));
////        mapGraph.addEdge(edge1,whiteMaterial,new Object());
//        Edge edge2 = mapGraph.createEdge(edge1.getEndPoint(),new Vertex(200,600,900));
//        Edge edge3 = mapGraph.createEdge(edge2.getEndPoint(),edge1.getStartPoint());

//        world.getChildren().addAll(edge1);
//        world.getChildren().addAll(edge1.getStartPoint(),edge1.getEndPoint());

        world.getChildren().addAll(mapGraph.getEdges());
        world.getChildren().addAll(mapGraph.getVertices());

        System.out.println("Total edges: "+mapGraph.getEdges().size());
        System.out.println("Total vertices: "+mapGraph.getVertices().size());

        Random rn = new Random();
        Layout layout = new Layout();

        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {
            Point3D startP = new Point3D(0,0,0);
            double alpha = 0;
            double betta = 0;
            @Override
            public void handle(ActionEvent event) {

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
