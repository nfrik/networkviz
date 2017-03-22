package core;

import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.*;

/**
 * Created by NF on 2/13/2017.
 */
public class Graph {

    private static double COS30 = Math.cos(Math.PI / 6);
    private static double SIN30 = Math.sin(Math.PI / 6);
    private static double EPS = 0.0001;
    private Map<Vertex, ArrayList<Edge>> adjMapList;
    private XformWorld world;
    private Random rn;
    private PhongMaterial edgeDefaultMaterial = new PhongMaterial();
    private PhongMaterial vertexDefaultMaterial = new PhongMaterial();
    private Point3D yAxis = new Point3D(0, 1, 0);
    private Point3D xAxis = new Point3D(1, 0, 0);
    private Point3D zAxis = new Point3D(0, 0, 1);
    private Point3D origin = new Point3D(0, 0, 0);
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

    public Graph() {
        adjMapList = new HashMap<Vertex, ArrayList<Edge>>();
        rn = new Random();


        edgeDefaultMaterial.setDiffuseColor(Color.YELLOW);
        edgeDefaultMaterial.setSpecularColor(Color.LIGHTBLUE);

//        setEdgeDefaultMaterial(edgeDefaultMaterial);
    }

    public XformWorld getWorld() {
        return world;
    }

    public void setWorld(XformWorld world) {
        this.world = world;
    }


    /**
     * Get the number of vertices (road intersections) in the graph
     *
     * @return The number of vertices in the graph.
     */
    public int getNumVertices() {
        return adjMapList.size();
    }

    /**
     * Return the intersections, which are the vertices in this graph.
     *
     * @return The vertices in this graph as GeographicPoints
     */
    public Set<Vertex> getVertices() {
        Set<Vertex> intersections = new HashSet<Vertex>();

        for (Vertex gp : adjMapList.keySet()) {
            intersections.add(gp);
        }

        return intersections;
    }

    /**
     * Get the number of road segments in the graph
     *
     * @return The number of edges in the graph.
     */
    public int getNumEdges() {
        int ne = 0;

        for (Vertex gp : adjMapList.keySet()) {
            for (Edge gpe : adjMapList.get(gp)) {
                ne++;
            }
        }


        return ne;
    }

    public List<Edge> getEdges() {

        List<Edge> edges = new ArrayList<Edge>();

        for (Vertex gp : adjMapList.keySet()) {
            for (Edge gpe : adjMapList.get(gp)) {
                edges.add(gpe);
            }
        }

        return edges;
    }


    public boolean addVertex(Vertex point) {
        if (!adjMapList.containsKey(point)) {
            adjMapList.put(point, new ArrayList<Edge>());
            return true;
        }
        return false;
    }

    public boolean updateVertex(Vertex point) {
        if (adjMapList.containsKey(point)) {
            ArrayList<Edge> tempList = adjMapList.get(point);
            adjMapList.put(point, tempList);
            return true;
        }
        return false;
    }

    public void addEdge(Vertex from, Vertex to, PhongMaterial material, Object uniqueProperties) throws IllegalArgumentException {

//        if(!adjMapList.containsKey(from) || !adjMapList.containsKey(to)) {
//            throw new IllegalArgumentException();
//        }else{

        if (!adjMapList.containsKey(from)) {
            addVertex(from);
        }

        if (!adjMapList.containsKey(to)) {
            addVertex(to);
        }

        ArrayList<Edge> tempList = adjMapList.get(from);
        Edge se = createEdge(from, to);

        //TODO find better way to assign material
        se.setMaterial(material);

        se.setUniqueProperties(uniqueProperties);

        tempList.add(se);

        adjMapList.put(from, tempList);
//        }
    }

    public void addEdge(Vertex from, Vertex to) throws IllegalArgumentException {
        addEdge(from, to, getEdgeDefaultMaterial(), null);
    }

    public void addEdge(Edge edge, PhongMaterial material, Object uniqueProperties) throws IllegalArgumentException {


        if (!adjMapList.containsKey(edge.getStartPoint())) {
            addVertex(edge.getStartPoint());
        }

        if (!adjMapList.containsKey(edge.getEndPoint())) {
            addVertex(edge.getEndPoint());
        }

        ArrayList<Edge> tempList = adjMapList.get(edge.getStartPoint());
//        Edge se = createEdge(edge.getStartPoint(),edge.getEndPoint());

        //TODO find better way to assign material
        edge.setMaterial(material);
        edge.setUniqueProperties(uniqueProperties);

        tempList.add(edge);

        adjMapList.put(edge.getStartPoint(), tempList);

    }

    public Edge getEdgeBetweenVertexes(Vertex v1, Vertex v2) {
        List<Edge> edges = getOutNeigborEdges(v1);
        edges.addAll(getInNeighborsEdges(v1));
        for (Edge e : edges) {
            if (e.getStartPoint().equals(v2) || e.getEndPoint().equals(v2)) {
                return e;
            }
        }

        return null;
    }

    public List<Vertex> getOutNeighbors(Vertex of) {
        List<Vertex> outNbs = new ArrayList<Vertex>();
        if (!adjMapList.containsKey(of)) {
            return null;
        }

        for (Edge se : adjMapList.get(of)) {
            outNbs.add(se.getEndPoint());
        }
        return outNbs;
    }

    public List<Vertex> getInNeighbors(Vertex of) {
        List<Vertex> inNbs = new ArrayList<Vertex>();

        if (!adjMapList.containsKey(of)) {
            return null;
        }

        for (Vertex start : adjMapList.keySet()) {
            for (Edge se : adjMapList.get(start)) {
                if (se.getEndPoint().equals(of)) {
                    inNbs.add(se.getStartPoint());
                }
            }
        }

        return inNbs;
    }

    public List<Vertex> getAllNeighbors2(Vertex of){
        List<Vertex> nbs = new ArrayList<Vertex>();

        if(!adjMapList.containsKey(of)){
            return null;
        }

        for(Vertex v : adjMapList.keySet()){
            for(Edge se: adjMapList.get(v)){
                if (se.getEndPoint().equals(of)) {
                    nbs.add(se.getStartPoint());
                }
            }
        }

        return nbs;
    }

    public List<Vertex> getAllNeighbors(Vertex of){
        List<Vertex> inNbs = getInNeighbors(of);
        List<Vertex> outNbs = getOutNeighbors(of);

        if(outNbs!=null){
            inNbs.addAll(outNbs);
        }

        return inNbs;
    }

    public List<Edge> getOutNeigborEdges(Vertex of) {
        List<Edge> outNbs = new ArrayList<Edge>();
        if (!adjMapList.containsKey(of)) {
            return null;
        }

        for (Edge se : adjMapList.get(of)) {
            outNbs.add(se);
        }
        return outNbs;
    }


    public List<Edge> getInNeighborsEdges(Vertex of) {
        List<Edge> inNbs = new ArrayList<Edge>();

        if (!adjMapList.containsKey(of)) {
            return null;
        }

        for (Vertex start : adjMapList.keySet()) {
            if (adjMapList.get(start) != null) {
                for (Edge se : adjMapList.get(start)) {
                    if (se.getEndPoint().equals(of)) {
                        inNbs.add(se);
                    }
                }
            }
        }

        return inNbs;
    }


    public double getAverageGeodesicDisntance(Map<Vertex, ArrayList<Edge>> graph) {
        //TODO implement
        return 0;
    }

    public double getClusteringCoefficient(Map<Vertex, ArrayList<Edge>> graph) {
        //TODO implement
        return 0;
    }


    /**
     * @param n    - The number of nodes
     * @param k    - Each node is connected to k nearest neighbors in ring topology
     * @param p    - The probability of rewiring each edge
     * @param seed - Seed for random number generator (default=None)
     * @return
     */
    public Map<Vertex, ArrayList<Edge>> generateSmallWorldGraph(int n, int k, double p, int seed) {
        //TODO impllement

        return null;
    }

    /**
     * @param point1
     * @param point2
     * @return
     */
    public Edge createEdge(Vertex point1, Vertex point2) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = point2.getPoint3D().subtract(point1.getPoint3D());// subtract(point1);
        double height = diff.magnitude();

        Point3D mid = point2.getPoint3D().midpoint(point1.getPoint3D());
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Edge line = new Edge(2, height, point1, point2);

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        return line;
    }

    public void translateEdge(Edge edge, Point3D point1, Point3D point2) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = point2.subtract(point1);

        Point3D mid = point2.midpoint(point1);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));

        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), mid.getX(), mid.getY(), mid.getZ(), axisOfRotation);

        removeAllTransforms(edge);

        edge.setHeight(diff.magnitude());
        edge.getTransforms().addAll(rotateAroundCenter, moveToMidpoint);

        removeAllTransforms(edge.getStartPoint());
        removeAllTransforms(edge.getEndPoint());
        edge.getStartPoint().setTranslateX(point1.getX());
        edge.getStartPoint().setTranslateY(point1.getY());
        edge.getStartPoint().setTranslateZ(point1.getZ());

        edge.getEndPoint().setTranslateX(point2.getX());
        edge.getEndPoint().setTranslateY(point2.getY());
        edge.getEndPoint().setTranslateZ(point2.getZ());
    }

    public void rotateEdgeAroundCenter(Edge edge, double theta, double phi) {

        double r = edge.getHeight() / 2;
        Point3D normdir = new Point3D(Math.sin(theta) * Math.cos(phi), Math.sin(theta) * Math.sin(phi), Math.cos(theta));

        Point3D mid = edge.getEndPoint().getPoint3D().midpoint(edge.getStartPoint().getPoint3D());

        edge.getStartPoint().setTranslateX(mid.getX() + r * normdir.getX());
        edge.getStartPoint().setTranslateY(mid.getY() + r * normdir.getY());
        edge.getStartPoint().setTranslateZ(mid.getZ() + r * normdir.getZ());

        edge.getEndPoint().setTranslateX(mid.getX() - r * normdir.getX());
        edge.getEndPoint().setTranslateY(mid.getY() - r * normdir.getY());
        edge.getEndPoint().setTranslateZ(mid.getZ() - r * normdir.getZ());

        translateEdge(edge, edge.getStartPoint().getPoint3D(), edge.getEndPoint().getPoint3D());
    }

    public void removeAllTransforms(Node node) {
        while (node.getTransforms().size() > 0) {
            node.getTransforms().remove(0);
        }
    }

    public void transformVertex(Vertex vertex, Point3D destination) {
        List<Edge> edges = getOutNeigborEdges(vertex);

        List<Edge> inEdges = getInNeighborsEdges(vertex);

        if (inEdges != null) {
            edges.addAll(inEdges);
        }


        if (edges != null && edges.size() > 0) {
            for (Edge ed : edges) {
                if (ed.getEndPoint().equals(vertex)) {//Change end point
                    translateEdge(ed, ed.getStartPoint().getPoint3D(), destination);
                } else {//Change start point
                    translateEdge(ed, destination, ed.getEndPoint().getPoint3D());
                }
            }

        }

    }

    public Vertex getCentralVertexWithinBoundary(double boundary){
        Vertex centralVertex = null;

        //Boundaries of the graph in 3D
        double dx=0;
        double dy=0;
        double dz=0;

        for(Vertex v : getVertices()){
            if(v.getPoint3D().getX()>dx){
                dx=v.getPoint3D().getX();
            }
            if(v.getPoint3D().getY()>dy){
                dy=v.getPoint3D().getY();
            }
            if(v.getPoint3D().getZ()>dz){
                dz=v.getPoint3D().getZ();
            }
        }

        //Find approximate location of middle point
        dx=dx/2;
        dy=dy/2;
        dz=dz/2;

        Point3D mPoint = new Point3D(dx,dy,dz);

        for(Vertex v : getVertices()){
            if(mPoint.distance(v.getPoint3D()) <= boundary){
                centralVertex = v;
            }
        }

        return centralVertex;
    }

    public void transformVertexRandomDelta(Vertex vertex, double delta) {
        transformVertex(vertex, new Point3D(vertex.getPoint3D().getX() + (rn.nextDouble() - 0.5) * delta, vertex.getPoint3D().getY() + (rn.nextDouble() - 0.5) * delta, vertex.getPoint3D().getZ() + (rn.nextDouble() - 0.5) * delta));
    }

    public void transformVertexRandomDelta(List<Vertex> vertexList, double delta) {
        for (Vertex vertex : vertexList) {
            transformVertex(vertex, new Point3D(vertex.getPoint3D().getX() + (rn.nextDouble() - 0.5) * delta, vertex.getPoint3D().getY() + (rn.nextDouble() - 0.5) * delta, vertex.getPoint3D().getZ() + (rn.nextDouble() - 0.5) * delta));
        }
    }

    public void generateRandomGraph(int n, double sparcity, PhongMaterial material) {
        if (
                n > 0) {

            double r = Math.pow(n, 1.4);
            for (int i = 0; i < n; i++) {
                rn.nextGaussian();
                addVertex(new Vertex(rn.nextGaussian() * r, rn.nextGaussian() * r, rn.nextGaussian() * r, getVertexDefaultRadius(), getVertexDefaultMaterial()));
            }
            for (Vertex vertex1 : getVertices()) {
                for (Vertex vertex2 : getVertices()) {
                    if ((vertex1 != vertex2) && (rn.nextDouble() > sparcity)) {
                        addEdge(vertex1, vertex2, material, null);
                    }
                }
            }
        }
    }

    public void generateSquareLattice2DSlow(double dx, double dy, double spacing, String type, boolean periodic) {

        int lim;
        int xmax = (int) Math.ceil(dx / spacing);
        int ymax = (int) Math.ceil(dy / spacing);
        Vertex vertex;

        for (int j = 0; j < xmax; j++) {
            for (int i = 0; i < ymax; i++) {
                addVertex(new Vertex(i * spacing, j * spacing, 0));
            }
        }

        for (Vertex vertex1 : getVertices()) {
            lim = 0;
            for (Vertex vertex2 : getVertices()) {
                if ((vertex1 != vertex2) && (vertex1.getPoint3D().distance(vertex2.getPoint3D()) <= spacing)) {
                    addEdge(vertex1, vertex2, getVertexDefaultMaterial(), null);
                    lim++;
                }
                if (lim == 4) {
                    break;
                }
            }
        }
        System.out.println("Total vertexes: " + getNumVertices());
    }

    public void generateSquareLattice2D(double dx, double dy, double spacing, String type, boolean periodic) {

        int lim;
        int xmax = (int) Math.ceil(dx / spacing);
        int ymax = (int) Math.ceil(dy / spacing);

        Vertex[][] vx = new Vertex[ymax][xmax];

        for(int i = 0; i< ymax; i++) {
            for (int j = 0; j < xmax; j++) {
                vx[i][j] = new Vertex(j*spacing,i*spacing,0);
                addVertex(vx[i][j]);
            }
        }


        for(int i = 0; i< ymax; i++) {
            for (int j = 0; j < xmax-1; j++) {
                addEdge(vx[i][j], vx[i][j+1]);
            }
        }

        for(int j = 0; j < xmax; j++){
            for(int i = 0; i < ymax-1; i++){
                addEdge(vx[i][j], vx[i+1][j]);
            }
        }

    }

    public void generateCubicLattice3DSlow(double dx, double dy, double dz, double a, double b, double c, String type, boolean periodic) {

        int lim;
        int nx = (int) Math.ceil(dx / a);
        int ny = (int) Math.ceil(dy / b);
        int nz = (int) Math.ceil(dz / c);
        double dist = 0;

        for (int k = 0; k < nz; k++) {
            for (int j = 0; j < ny; j++) {
                for (int i = 0; i < nx; i++) {
                    addVertex(new Vertex(i * a, j * b, k * c));
                }
            }
        }


        for (Vertex vertex1 : getVertices()) {
            lim = 0;
            for (Vertex vertex2 : getVertices()) {
                dist = vertex1.getPoint3D().distance(vertex2.getPoint3D());
                if ((vertex1 != vertex2) && ((dist - a) < EPS || (dist - b) < EPS || (dist - c) < EPS)) {
                    addEdge(vertex1, vertex2, getVertexDefaultMaterial(), null);
                    lim++;
                }
                if (lim == 6) {
                    break;
                }
            }
        }
        System.out.println("Total vertexes: " + getNumVertices());
    }

    public void generateCubicLattice3D(double dx, double dy, double dz, double a, double b, double c, String type, boolean periodic) {

        int lim;
        int nx = (int) Math.ceil(dx / a);
        int ny = (int) Math.ceil(dy / b);
        int nz = (int) Math.ceil(dz / c);
        double dist = 0;

        Vertex[][][] vx = new Vertex[nx][ny][nz];

        for (int k = 0; k < nz; k++) {
            for (int i = 0; i < ny; i++) {
                for (int j = 0; j < nx; j++) {
                    vx[i][j][k] = new Vertex(i * a, j * b, k * c);
                    addVertex(vx[i][j][k]);
                }
            }
        }




        for(int k = 0; k < nz; k++) {
            for (int i = 0; i < ny; i++) {
                for (int j = 0; j < nx - 1; j++) {
                    addEdge(vx[i][j][k], vx[i][j + 1][k]);
                }
            }

            for (int j = 0; j < nx; j++) {
                for (int i = 0; i < ny - 1; i++) {
                    addEdge(vx[i][j][k], vx[i + 1][j][k]);
                }
            }
        }

        for(int k= 0; k<nz-1;k++){
            for (int i = 0; i < ny; i++) {
                for (int j = 0; j < nx ; j++) {
                    addEdge(vx[i][j][k],vx[i][j][k+1]);
                }
            }
        }


    }

    public void generateHexagonalLattice2DSlow(double dx, double dy, double spacing, String type, boolean periodic) {
        int lim = 3;
        int xmax = (int) Math.ceil(dx / spacing);
        int ymax = (int) Math.ceil(dy / spacing);
        Vertex vertex;

        for (int i = 0; i < xmax; i++) {
            for (int j = 0; j < ymax; j++) {
                addVertex(new Vertex(j * spacing, i * spacing, 0));
            }
        }


        for (Vertex vertex1 : getVertices()) {
            lim = 0;
            for (Vertex vertex2 : getVertices()) {
                if ((vertex1 != vertex2) && (vertex1.getPoint3D().distance(vertex2.getPoint3D()) <= spacing)) {
                    addEdge(vertex1, vertex2, getVertexDefaultMaterial(), null);
                    lim++;
                }
                if (lim == 3) {
                    break;
                }
            }
        }
        System.out.println("Total vertexes: " + getNumVertices());
    }



    /**
     * // build 2d lattice with _/- shape insertion in x direction and zigzag in y direction
     *
     * @param dx
     * @param dy
     * @param a
     * @param type
     * @param periodic
     */
    public void generateHexagonalLattice2D(double dx, double dy, double a, String type, boolean periodic) {
        int lim = 3;
        double xunit = a * COS30;
        double yunit = a * (SIN30 + 1);

        int numx = (int) Math.ceil(dx / xunit);
        int numy = (int) Math.ceil(dy / yunit);

        Vertex[][] vx = new Vertex[numy][numx];

//        grid[0][0] = new Hexagon(10,15,0,0);
//
        for (int i = 0; i < numy; i++){
            for (int j = 0; j < numx; j++) {
                vx[i][j] = new Vertex(-1 * ((j + i) % 2) * a * SIN30 + yunit * i, j * a * COS30, 0);
                addVertex(vx[i][j]);
            }
        }

        //Connect zigzag vertices
        for(int i = 0; i< numy; i++) {
            for (int j = 0; j < numx-1; j++) {
                addEdge(vx[i][j], vx[i][j+1]);
            }
        }

        //Connect odd vertical vertices
        for(int i=0; i< numy-1; i+=2){
            for(int j=0; j<numx; j+=2){
                addEdge(vx[i][j],vx[i+1][j]);
            }
        }

        //Connect even vertical vertices
        for(int i=1; i< numy-1; i+=2){
            for(int j=1; j<numx; j+=2){
                addEdge(vx[i][j],vx[i+1][j]);
            }
        }

    }


    /**
     * // build 2d lattice with _/- shape insertion in x direction and zigzag in y direction
     *
     * @param dx
     * @param dy
     * @param a
     * @param type
     * @param periodic
     */
    public void generateHexagonalLattice3D(double dx, double dy, double dz, double a, double c, String type, boolean periodic) {
        int lim = 3;
        double xunit = a * COS30;
        double yunit = a * (SIN30 + 1);

        int numx = (int) Math.ceil(dx / xunit);
        int numy = (int) Math.ceil(dy / yunit);
        int numz = (int) Math.ceil(dz / c);

        Vertex[][][] vx = new Vertex[numy][numx][numz];

//        grid[0][0] = new Hexagon(10,15,0,0);
//
        for(int k = 0; k < numz ; k++) {
            for (int i = 0; i < numy; i++) {
                for (int j = 0; j < numx; j++) {
                    vx[i][j][k] = new Vertex(-1 * ((j + i) % 2) * a * SIN30 + yunit * i, j * a * COS30, k*c);
                    addVertex(vx[i][j][k]);
                }
            }

            //Connect zigzag vertices
            for (int i = 0; i < numy; i++) {
                for (int j = 0; j < numx - 1; j++) {
                    addEdge(vx[i][j][k], vx[i][j + 1][k]);
                }
            }

            //Connect odd vertical vertices
            for (int i = 0; i < numy - 1; i += 2) {
                for (int j = 0; j < numx; j += 2) {
                    addEdge(vx[i][j][k], vx[i + 1][j][k]);
                }
            }

            //Connect even vertical vertices
            for (int i = 1; i < numy - 1; i += 2) {
                for (int j = 1; j < numx; j += 2) {
                    addEdge(vx[i][j][k], vx[i + 1][j][k]);
                }
            }
        }

        for(int k = 0; k < numz-1; k++) {
            for(int i=0; i < numy; i++){
                for(int j=0; j < numx; j++){
                    addEdge(vx[i][j][k], vx[i][j][k+1]);
                }
            }
        }

    }

    public void generateHexagonalLattice3DSlow(double dx, double dy, double dz, double a, double c, String type, boolean periodic) {

        int limPlane;
        double xunit = (2 + SIN30) * a;
        double yunit = 2 * COS30 * a;

        int numx = (int) Math.ceil(dx / xunit);
        int numy = (int) Math.ceil(dy / yunit);
        int numz = (int) Math.ceil(dz / c);

        for (int k = 0; k < numz; k++) {
            for (int j = 0; j < numy; j++) {
                for (int i = 0; i < numx; i++) {
                    addVertex(new Vertex(i * (xunit + SIN30 * a), j * yunit, k * c));
                    addVertex(new Vertex(i * (xunit + SIN30 * a) + a, j * yunit, k * c));
                    addVertex(new Vertex(i * (xunit + SIN30 * a) + (1 + SIN30) * a, j * yunit + COS30 * a, k * c));
                    addVertex(new Vertex(i * (xunit + SIN30 * a) + (2 + SIN30) * a, j * yunit + COS30 * a, k * c));
                }
            }
        }


        for (Vertex vertex1 : getVertices()) {
            limPlane = 0;
            for (Vertex vertex2 : getVertices()) {
                if ((vertex1 != vertex2) && ((vertex1.getPoint3D().distance(vertex2.getPoint3D()) <= a + EPS) || (Math.abs(vertex1.getPoint3D().distance(vertex2.getPoint3D()) - c) < EPS))) {
                    addEdge(vertex1, vertex2, getVertexDefaultMaterial(), null);
                    limPlane++;
                }
                if (limPlane == 5) {
                    break;
                }
            }
        }


        System.out.println("Total vertexes: " + getNumVertices());

    }

}