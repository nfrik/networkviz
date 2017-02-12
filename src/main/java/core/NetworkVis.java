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
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class NetworkVis extends Application {
    final Group root = new Group();
    final XformWorld world = new XformWorld();
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final XformCamera cameraXform = new XformCamera();
    private static final double CAMERA_INITIAL_DISTANCE = -1000;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    private static final double AXIS_LENGTH = 250.0;
    double mousePosX, mousePosY, mouseOldX, mouseOldY, mouseDeltaX, mouseDeltaY;
    double mouseFactorX, mouseFactorY;
    private Group axisGroup = new Group();

    @Override
    public void start(Stage primaryStage) {
        root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);
        buildCamera();
        buildBodySystem();
        buildAxes();
        Scene scene = new Scene(root, 800, 600, true);
        scene.setFill(Color.GREY);
        handleMouse(scene);
        primaryStage.setTitle("TrafoTest");
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
        whiteMaterial.setDiffuseColor(Color.WHITE);
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
//        Edge edge1 = mapGraph.createEdge(new Point3D(0,0,0),new Point3D(100,100,100),1);
//        Edge edge2 = mapGraph.createEdge(new Point3D(100,100,100),new Point3D(200,600,900),1);
//        Edge edge3 = mapGraph.createEdge(new Point3D(200,600,900),new Point3D(700,900,1200),1);

//        world.getChildren().addAll(edge1,edge2,edge3);
        world.getChildren().addAll(sphere);


        Random rn = new Random();
        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
            Point3D startP = new Point3D(0,0,0);
            @Override
            public void handle(ActionEvent event) {
                Point3D newPoint = startP.add((rn.nextDouble()-0.5)*100,(rn.nextDouble()-0.5)*100,(rn.nextDouble()-0.5)*100);
                world.getChildren().addAll(mapGraph.createEdge(startP,newPoint,1));
                startP = newPoint;
//                for(Node node : world.getChildren()) {
//                    node.getTransforms().add(new Rotate(0.5, 0, 0, 0, Rotate.X_AXIS));
//
////                    node.setTranslateX(node.getTranslateX()+10);
////                    node.setRotationAxis(Rotate.X_AXIS);
////                    matrixRotateNode(node, 0, Math.PI / 4, 0);
//                }
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



    private void matrixRotateNode(Node n, double alf, double bet, double gam){
        double A11=Math.cos(alf)*Math.cos(gam);
        double A12=Math.cos(bet)*Math.sin(alf)+Math.cos(alf)*Math.sin(bet)*Math.sin(gam);
        double A13=Math.sin(alf)*Math.sin(bet)-Math.cos(alf)*Math.cos(bet)*Math.sin(gam);
        double A21=-Math.cos(gam)*Math.sin(alf);
        double A22=Math.cos(alf)*Math.cos(bet)-Math.sin(alf)*Math.sin(bet)*Math.sin(gam);
        double A23=Math.cos(alf)*Math.sin(bet)+Math.cos(bet)*Math.sin(alf)*Math.sin(gam);
        double A31=Math.sin(gam);
        double A32=-Math.cos(gam)*Math.sin(bet);
        double A33=Math.cos(bet)*Math.cos(gam);

        double d = Math.acos((A11+A22+A33-1d)/2d);
        if(d!=0d){
            double den=2d*Math.sin(d);
            Point3D p= new Point3D((A32-A23)/den,(A13-A31)/den,(A21-A12)/den);
            n.setRotationAxis(p);
            n.setRotate(Math.toDegrees(d));
        }
    }

    private void handleMouse(Scene scene) {
        scene.setOnMousePressed((MouseEvent me) -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        scene.setOnMouseDragged((MouseEvent me) -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);
            if (me.isPrimaryButtonDown()) {
                cameraXform.ry(mouseDeltaX * 180.0 / scene.getWidth());
                cameraXform.rx(-mouseDeltaY * 180.0 / scene.getHeight());
            } else if (me.isSecondaryButtonDown()) {
                camera.setTranslateZ(camera.getTranslateZ() + mouseDeltaY);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}

class Graph {

    private Map<Point3D, ArrayList<Edge>> adjMapList;

    public Graph(){
        adjMapList = new HashMap<Point3D,ArrayList<Edge>>();
    }

    /**
     * Get the number of vertices (road intersections) in the graph
     * @return The number of vertices in the graph.
     */
    public int getNumVertices(){
        return adjMapList.size();
    }

    /**
     * Return the intersections, which are the vertices in this graph.
     * @return The vertices in this graph as GeographicPoints
     */
    public Set<Point3D> getVertices(){
        Set<Point3D> intersections = new HashSet<Point3D>();

        for(Point3D gp : adjMapList.keySet()){
            intersections.add(gp);
        }

        return intersections;
    }

    /**
     * Get the number of road segments in the graph
     * @return The number of edges in the graph.
     */
    public int getNumEdges(){
        int ne=0;

        for(Point3D gp : adjMapList.keySet()){
            for(Edge gpe : adjMapList.get(gp)){
                ne++;
            }
        }

        return ne;
    }

    public Edge createEdge(Point3D point1, Point3D point2, double radius) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = point2.subtract(point1);
        double height = diff.magnitude();

        Point3D mid = point2.midpoint(point1);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Edge line = new Edge(radius, height, point1, point2);

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        return line;
    }

    private Cylinder getCylinder(Point3D point1, Point3D point2, double radius, PhongMaterial material){
        double height = point2.distance(point1);

        Cylinder cyl = new Cylinder(radius,height);

        //Get translation vector
        Point3D b = point2.subtract(point1);
        Point3D k = point2.add(b.getX()/2*Math.PI,b.getY()/2*Math.PI,b.getZ()/2*Math.PI);

        //Apply translation
        cyl.setTranslateX(cyl.getTranslateX()+k.getX());
        cyl.setTranslateY(cyl.getTranslateY()+k.getY());
        cyl.setTranslateZ(cyl.getTranslateZ()+k.getZ());

        //Get rotation components
        Point3D diff = point2.subtract(point1);
        double az=Math.atan2(diff.getX(),diff.getY());
        double ax=Math.atan2(diff.getZ(),diff.getY());
        double ay=Math.atan2(diff.getX(),diff.getZ());

        //Apply rotation
        cyl.getTransforms().add(new Rotate(az, 0, 0, 0, Rotate.Z_AXIS));
        cyl.getTransforms().add(new Rotate(ax, 0, 0, 0, Rotate.X_AXIS));
        cyl.getTransforms().add(new Rotate(ay, 0, 0, 0, Rotate.Y_AXIS));

        cyl.setMaterial(material);
        //cyl.getTransforms().add(new Rotate());

        return cyl;
    }
}

class Edge extends Cylinder{

    private String name;
    private Double resistance;
    private Object uniqueProperties;
    private Point3D startPoint;
    private Point3D endPoint;

    public Edge(double radius, double height) {
        super(radius, height);
    }

    public Edge(double radius, double height, Point3D point1, Point3D point2) {
        super(radius, height);
        setStartPoint(point1);
        setEndPoint(point2);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getResistance() {
        return resistance;
    }

    public void setResistance(Double resistance) {
        this.resistance = resistance;
    }

    public Object getUniqueProperties() {
        return uniqueProperties;
    }

    public void setUniqueProperties(Object uniqueProperties) {
        this.uniqueProperties = uniqueProperties;
    }

    public Point3D getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point3D startPoint) {
        this.startPoint = startPoint;
    }

    public Point3D getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point3D endPoint) {
        this.endPoint = endPoint;
    }
}

class XformWorld extends Group {
    final Translate t = new Translate(0.0, 0.0, 0.0);
    final Rotate rx = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
    final Rotate ry = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
    final Rotate rz = new Rotate(0, 0, 0, 0, Rotate.Z_AXIS);

    public XformWorld() {
        super();
        this.getTransforms().addAll(t, rx, ry, rz);
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
