package core;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Created by nfrik on 7/23/17.
 */
public class CircuitUtil{

    private static final int LAT = 50;

    public static Graph generateRandomCircuit(int size, int dim, double sparceness) {
        Graph G = new Graph();

        G.getVertexDefaultMaterial().setDiffuseColor(Color.BLUE);
        G.getEdgeDefaultMaterial().setDiffuseColor(Color.WHITE);

        Random random = new Random();

        if(dim == 2){
            int n = (int) Math.round(Math.sqrt(size * 1.0));
            G.generateSquareLattice2D(n*LAT,n*LAT,LAT,null,true);
        }else{
            int n = (int) Math.round(Math.pow(size * 1.0,1.0/3));
            G.generateCubicLattice3D(n*LAT,n*LAT,n*LAT,LAT,LAT,LAT,null,true);
        }

        for(Edge e : G.getEdges()){
            if(random.nextDouble() > (1.0-sparceness)){
                G.removeEdge(G.getRandomEdge(),null);
            }
        }

        return G;
    }

    /**
     * Export Graph To Symphony File
     * @param g - graph
     * @param path - path to file
     * @return
     */
    public static boolean exportGraphToCSFile(Graph g, String path, CircuitTypes type) {
        normalizeGraphNodePositionsForCS(g);

        Random random = new Random();
        try(PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter(path)))){
            pr.println(String.format("$ %d %1.1E %f %d %1.1f %d",0,5.0E-6,1.03,50,5.0,50));
            for(Edge e : g.getEdges()){
                int x1 = (int)Math.ceil(e.getStartPoint().getPoint3D().getX());
                int y1 = (int)Math.ceil(e.getStartPoint().getPoint3D().getY());
                int x2 = (int)Math.ceil(e.getEndPoint().getPoint3D().getX());
                int y2 = (int)Math.ceil(e.getEndPoint().getPoint3D().getY());

                if(random.nextDouble()>0.5){
                    pr.println(String.format("w %d %d %d %d %d",x1,y1,x2,y2,0));
                }else{
                    pr.println(String.format("r %d %d %d %d %d %1.1f",x1,y1,x2,y2,0, 100.0));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * Rounds up positions of all edges and nodes to provide compatibility with circuitsymphony
     * @param g
     */
    public static void normalizeGraphNodePositionsForCS(Graph g){
        for(Vertex vi : g.getVertices()){
            Point3D vloc = vi.getPoint3D();
            double x = Math.ceil(vloc.getX());
            double y = Math.ceil(vloc.getY());
            double z = Math.ceil(vloc.getZ());
            g.transformVertex(vi,new Point3D(x,y,z));
        }
    }
}
