package core;

import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.*;

/**
 * Created by NF on 2/13/2017.
 */
public class Graph {

    private Map<Vertex, ArrayList<Edge>> adjMapList;

    private XformWorld world;

    private Random rn;

    private PhongMaterial edgeDefaultMaterial = new PhongMaterial();

    private PhongMaterial vertexDefaultMaterial = new PhongMaterial();

    private Point3D yAxis = new Point3D(0,1,0);

    private Point3D xAxis = new Point3D(1,0,0);

    private Point3D zAxis = new Point3D(0,0,1);

    private Point3D origin = new Point3D(0,0,0);

    private double vertexDefaultRadius = 100;

    public double getVertexDefaultRadius() {
        return vertexDefaultRadius;
    }

    public void setVertexDefaultRadius(double defaultRadius) {
        this.vertexDefaultRadius = defaultRadius;
    }

    public PhongMaterial getVertexDefaultMaterial() {
        return vertexDefaultMaterial;
    }

    public void setVertexDefaultMaterial(PhongMaterial vertexDefaultMaterial) {
        this.vertexDefaultMaterial = vertexDefaultMaterial;
    }

    public PhongMaterial getEdgeDefaultMaterial() {
        return edgeDefaultMaterial;
    }

    public void setEdgeDefaultMaterial(PhongMaterial edgeDefaultMaterial) {
        this.edgeDefaultMaterial = edgeDefaultMaterial;
    }

    public Map<Vertex, ArrayList<Edge>> getAdjMapList() {
        return adjMapList;
    }

    public void setAdjMapList(Map<Vertex, ArrayList<Edge>> adjMapList) {
        this.adjMapList = adjMapList;
    }

    public Graph(){
        adjMapList = new HashMap<Vertex,ArrayList<Edge>>();
        rn = new Random();
    }

    public XformWorld getWorld() {
        return world;
    }

    public void setWorld(XformWorld world) {
        this.world = world;
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
    public Set<Vertex> getVertices(){
        Set<Vertex> intersections = new HashSet<Vertex>();

        for(Vertex gp : adjMapList.keySet()){
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

        for(Vertex gp : adjMapList.keySet()){
            for(Edge gpe : adjMapList.get(gp)){
                ne++;
            }
        }


        return ne;
    }

    public List<Edge> getEdges(){

        List<Edge> edges = new ArrayList<Edge>();

        for(Vertex gp : adjMapList.keySet()){
            for(Edge gpe : adjMapList.get(gp)){
                edges.add(gpe);
            }
        }

        return edges;
    }


    public boolean addVertex(Vertex point){
        if(!adjMapList.containsKey(point)){
            adjMapList.put(point,new ArrayList<Edge>());
            return true;
        }
        return false;
    }

    public boolean updateVertex(Vertex point){
        if(adjMapList.containsKey(point)){
            ArrayList<Edge> tempList = adjMapList.get(point);
            adjMapList.put(point,tempList);
            return true;
        }
        return false;
    }

    public void addEdge(Vertex from, Vertex to, PhongMaterial material, Object uniqueProperties) throws IllegalArgumentException {

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

            //TODO find better way to assign material
            se.setMaterial(material);

            se.setUniqueProperties(uniqueProperties);

            tempList.add(se);

            adjMapList.put(from,tempList);
//        }

    }

    public void addEdge(Edge edge, PhongMaterial material, Object uniqueProperties) throws IllegalArgumentException {


        if(!adjMapList.containsKey(edge.getStartPoint())){
            addVertex(edge.getStartPoint());
        }

        if(!adjMapList.containsKey(edge.getEndPoint())){
            addVertex(edge.getEndPoint());
        }

        ArrayList<Edge> tempList = adjMapList.get(edge.getStartPoint());
//        Edge se = createEdge(edge.getStartPoint(),edge.getEndPoint());

        //TODO find better way to assign material
        edge.setMaterial(material);
        edge.setUniqueProperties(uniqueProperties);

        tempList.add(edge);

        adjMapList.put(edge.getStartPoint(),tempList);

    }

    public List<Vertex> outNeighbors(Vertex of){
        List<Vertex> outNbs = new ArrayList<Vertex>();
        if(!adjMapList.containsKey(of)){
            return null;
        }

        for(Edge se : adjMapList.get(of)){
            outNbs.add(se.getEndPoint());
        }
        return outNbs;
    }

    public List<Edge> outNeigborEdges(Vertex of){
        List<Edge> outNbs = new ArrayList<Edge>();
        if(!adjMapList.containsKey(of)){
            return null;
        }

        for(Edge se : adjMapList.get(of)){
            outNbs.add(se);
        }
        return outNbs;
    }

    public List<Vertex> inNeighbors(Vertex of){
        List<Vertex> inNbs = new ArrayList<Vertex>();

        if(!adjMapList.containsKey(of)){
            return null;
        }

        for(Vertex start : adjMapList.keySet()){
            for(Edge se : adjMapList.get(start)){
                if(se.getEndPoint().equals(of)) {
                    inNbs.add(se.getEndPoint());
                }
            }
        }

        return inNbs;
    }

    public List<Edge> inNeighborsEdges(Vertex of){
        List<Edge> inNbs = new ArrayList<Edge>();

        if(!adjMapList.containsKey(of)){
            return null;
        }

        for(Vertex start : adjMapList.keySet()){
            if(adjMapList.get(start) != null) {
                for (Edge se : adjMapList.get(start)) {
                    if (se.getEndPoint().equals(of)) {
                        inNbs.add(se);
                    }
                }
            }
        }

        return inNbs;
    }

    public Edge createEdge(Vertex point1, Vertex point2) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = point2.getPoint3D().subtract(point1.getPoint3D());// subtract(point1);
        double height = diff.magnitude();

        Point3D mid = point2.getPoint3D().midpoint(point1.getPoint3D());
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation =  diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Edge line = new Edge(2, height, point1, point2);

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        return line;
    }

    public void translateEdge(Edge edge, Point3D point1, Point3D point2){
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = point2.subtract(point1);

        Point3D mid = point2.midpoint(point1);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation =  diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));

        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), mid.getX(),mid.getY(),mid.getZ(), axisOfRotation);

        removeAllTransforms(edge);

        edge.setHeight(diff.magnitude());
        edge.getTransforms().addAll(rotateAroundCenter,moveToMidpoint);

        removeAllTransforms(edge.getStartPoint());
        removeAllTransforms(edge.getEndPoint());
        edge.getStartPoint().setTranslateX(point1.getX());
        edge.getStartPoint().setTranslateY(point1.getY());
        edge.getStartPoint().setTranslateZ(point1.getZ());

        edge.getEndPoint().setTranslateX(point2.getX());
        edge.getEndPoint().setTranslateY(point2.getY());
        edge.getEndPoint().setTranslateZ(point2.getZ());
    }

    public void rotateEdgeAroundCenter(Edge edge, double theta, double phi){

        double r  = edge.getHeight()/2;
        Point3D normdir = new Point3D(Math.sin(theta)*Math.cos(phi),Math.sin(theta)*Math.sin(phi),Math.cos(theta));

        Point3D mid = edge.getEndPoint().getPoint3D().midpoint(edge.getStartPoint().getPoint3D());

        edge.getStartPoint().setTranslateX(mid.getX()+r*normdir.getX());
        edge.getStartPoint().setTranslateY(mid.getY()+r*normdir.getY());
        edge.getStartPoint().setTranslateZ(mid.getZ()+r*normdir.getZ());

        edge.getEndPoint().setTranslateX(mid.getX()-r*normdir.getX());
        edge.getEndPoint().setTranslateY(mid.getY()-r*normdir.getY());
        edge.getEndPoint().setTranslateZ(mid.getZ()-r*normdir.getZ());

        translateEdge(edge,edge.getStartPoint().getPoint3D(),edge.getEndPoint().getPoint3D());
    }

    public void removeAllTransforms(Node node){
        while(node.getTransforms().size()>0){
            node.getTransforms().remove(0);
        }
    }

    public void transformVertex(Vertex vertex, Point3D destination){
        List<Edge> edges = outNeigborEdges(vertex);

        List<Edge> inEdges = inNeighborsEdges(vertex);

        if(inEdges!=null) {
            edges.addAll(inEdges);
        }


        if(edges!=null && edges.size()>0){
            for(Edge ed: edges){
                if(ed.getEndPoint().equals(vertex)){//Change end point
                    translateEdge(ed,ed.getStartPoint().getPoint3D(),destination);
                }else {//Change start point
                    translateEdge(ed,destination,ed.getEndPoint().getPoint3D());
                }
            }

        }

    }

    public void transformVertexRandomDelta(Vertex vertex, double delta){
        transformVertex(vertex,new Point3D(vertex.getPoint3D().getX()+(rn.nextDouble()-0.5)*delta,vertex.getPoint3D().getY()+(rn.nextDouble()-0.5)*delta,vertex.getPoint3D().getZ()+(rn.nextDouble()-0.5)*delta));
    }

    public void transformVertexRandomDelta(List<Vertex> vertexList, double delta){
        for(Vertex vertex : vertexList) {
            transformVertex(vertex, new Point3D(vertex.getPoint3D().getX() + (rn.nextDouble() - 0.5) * delta, vertex.getPoint3D().getY() + (rn.nextDouble() - 0.5) * delta, vertex.getPoint3D().getZ() + (rn.nextDouble() - 0.5) * delta));
        }
    }

    public void generateRandomGraph(int n, double sparcity, PhongMaterial material){
        if(n>0) {

            double r = Math.pow(n,1.4);
            for(int i=0;i<n;i++){
                rn.nextGaussian();
                addVertex(new Vertex(rn.nextGaussian()*r,rn.nextGaussian()*r,rn.nextGaussian()*r,getVertexDefaultRadius(),getVertexDefaultMaterial()));
            }
            for(Vertex vertex1 : getVertices()){
                for(Vertex vertex2 : getVertices()){
                    if((vertex1!=vertex2) && (rn.nextDouble()>sparcity)){
                        addEdge(vertex1,vertex2,material,null);
                    }
                }
            }
        }
    }

}