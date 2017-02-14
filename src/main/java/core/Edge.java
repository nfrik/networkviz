package core;

import javafx.geometry.Point3D;
import javafx.scene.shape.Cylinder;

/**
 * Created by NF on 2/13/2017.
 */
class Edge extends Cylinder {

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