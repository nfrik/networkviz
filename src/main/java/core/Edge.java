package core;

import javafx.scene.shape.Cylinder;

/**
 * Created by NF on 2/13/2017.
 */
class Edge extends Cylinder {

    private String name;
    private Double resistance;
    private Object uniqueProperties;
    private Vertex startPoint;
    private Vertex endPoint;

    public Edge(double radius, double height) {
        super(radius, height);
    }

    public Edge(double radius, double height, Vertex point1, Vertex point2) {
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

    public Vertex getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Vertex startPoint) {
        this.startPoint = startPoint;
    }

    public Vertex getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Vertex endPoint) {
        this.endPoint = endPoint;
    }
}