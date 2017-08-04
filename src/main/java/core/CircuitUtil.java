package core;

import javafx.scene.paint.Color;

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


    public static boolean exportGraphToFile(Graph g, String path) {

        return false;
    }
}
