package core;

import javafx.geometry.Point3D;
import javafx.scene.paint.Material;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.*;

/**
 * Created by NF on 2/13/2017.
 */
public class Graph {

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

    public List<Edge> getEdges(){

        List<Edge> edges = new ArrayList<Edge>();

        for(Point3D gp : adjMapList.keySet()){
            for(Edge gpe : adjMapList.get(gp)){
                edges.add(gpe);
            }
        }

        return edges;
    }


    public boolean addVertex(Point3D point){
        if(!adjMapList.containsKey(point)){
            adjMapList.put(point,new ArrayList<Edge>());
            return true;
        }
        return false;
    }

    public boolean updateVertex(Point3D point){
        if(adjMapList.containsKey(point)){
            ArrayList<Edge> tempList = adjMapList.get(point);
            adjMapList.put(point,tempList);
            return true;
        }
        return false;
    }

    public void addEdge(Point3D from, Point3D to, Material material, Object uniqueProperties) throws IllegalArgumentException {

//        if(!adjMapList.containsKey(from) || !adjMapList.containsKey(to)) {
//            throw new IllegalArgumentException();
//        }else{

            if(!adjMapList.containsKey(from)){
                addVertex(from);
            }

            if(!adjMapList.containsKey(to)){
                addVertex(to);
            }

            ArrayList<Edge> tempList = adjMapList.get(from);
            Edge se = createEdge(from,to);

            se.setMaterial(material);
            se.setUniqueProperties(uniqueProperties);

            tempList.add(se);

            adjMapList.put(from,tempList);
//        }

    }

    public List<Point3D> outNeighbors(Point3D of){
        List<Point3D> outNbs = new ArrayList<Point3D>();
        if(!adjMapList.containsKey(of)){
            return null;
        }

        for(Edge se : adjMapList.get(of)){
            outNbs.add(se.getEndPoint());
        }
        return outNbs;
    }

    public List<Edge> neighborEdgesOf(Point3D of){
        List<Edge> outNbs = new ArrayList<Edge>();
        if(!adjMapList.containsKey(of)){
            return null;
        }

        for(Edge se : adjMapList.get(of)){
            outNbs.add(se);
        }
        return outNbs;
    }

    public List<Point3D> inNeighbors(Point3D of){
        List<Point3D> inNbs = new ArrayList<Point3D>();

        if(!adjMapList.containsKey(of)){
            return null;
        }

        for(Point3D start : adjMapList.keySet()){
            for(Edge se : adjMapList.get(start)){
                if(se.getEndPoint().equals(of)) {
                    inNbs.add(se.getEndPoint());
                }
            }
        }

        return inNbs;
    }

    public List<Edge> inNeighborsEdges(Point3D of){
        List<Edge> inNbs = new ArrayList<Edge>();

        if(!adjMapList.containsKey(of)){
            return null;
        }

        for(Point3D start : adjMapList.keySet()){
            for(Edge se : adjMapList.get(start)){
                if(se.getEndPoint().equals(of)) {
                    inNbs.add(se);
                }
            }
        }

        return inNbs;
    }

    public Edge createEdge(Point3D point1, Point3D point2) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = point2.subtract(point1);
        double height = diff.magnitude();

        Point3D mid = point2.midpoint(point1);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Edge line = new Edge(1, height, point1, point2);

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        return line;
    }

    public void transformEdge(Edge edge, Point3D point1, Point3D point2){
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = point2.subtract(point1);
        double height = diff.magnitude();

        Point3D mid = point2.midpoint(point1);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Edge line = new Edge(1, height, point1, point2);

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);
    }

}