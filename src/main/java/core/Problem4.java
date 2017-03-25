package core;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;

import java.util.*;

/**
 * Created by NF on 3/24/2017.
 */
public class Problem4 {

    private Timeline loop = null;

    PltXYSeries msdAplt = new PltXYSeries("MSD A");
    PltXYSeries msdBplt = new PltXYSeries("MSD B");

    Graph mapGraph = null;

    TreeMap<Double,Vertex> tm = new TreeMap<>();

    Random rn = new Random();

    public static void main(String[] args){

        System.out.println("Test");

        Problem4 pb4 = new Problem4();

        int steps = 60;
        int repetitions = 10;

        pb4.build3DRectBodySystemAndRun(500,500,500,30,50,steps,repetitions);

    }


    protected void build3DRectBodySystemAndRun(double dx, double dy, double dz, double a, double c, int steps, int repetitions) {

        mapGraph = new Graph();
        mapGraph.setVertexDefaultRadius(10);
        mapGraph.getVertexDefaultMaterial().setDiffuseColor(Color.AQUA);
        mapGraph.getEdgeDefaultMaterial().setDiffuseColor(Color.YELLOW);
        
        mapGraph.generateCubicLattice3D(dx,dy,dz,a,a,c,null,true);

//        world.getChildren().addAll(mapGraph.getEdges());
//        world.getChildren().addAll(mapGraph.getVertices());

        System.out.println("Total edges: " + mapGraph.getEdges().size());
        System.out.println("Total vertices: " + mapGraph.getVertices().size());

        setAndUpdatePositionsDirectExchangeLoop(250,a, steps,repetitions);

    }


    private void setAndUpdatePositionsDirectExchangeLoop(long millis, double bound, int steps, int rep){

        if(loop!=null){
            loop.stop();
            msdAplt.getSeries().clear();
            msdBplt.getSeries().clear();
        }


        PhongMaterial atomAMaterial = new PhongMaterial();
        atomAMaterial.setDiffuseColor(Color.YELLOW);

        PhongMaterial atomBMaterial = new PhongMaterial();
        atomBMaterial.setDiffuseColor(Color.BLUE);


        Set<Vertex> vertices = mapGraph.getVertices();

        //Populate supercell with A/B atoms randomly 50/50
        for (Vertex v : mapGraph.getVertices()){
            v.setLuggage((rn.nextBoolean()?1:2));
        }

        Vertex initialA = null;
        Vertex initialB = null;

        initialA = mapGraph.getCentralVertexWithinBoundaryAndType(bound*2,1);
        initialB = mapGraph.getCentralVertexWithinBoundaryAndType(bound*2,2);

        //The atoms we track have these labels
        initialA.setLuggage(3);
        initialB.setLuggage(4);

        Vertex lastA = initialA;
        Vertex lastB = initialB;

        System.out.println(initialA+" "+initialB);

        double[][] rAm = new double[rep][steps];
        double[][] rBm = new double[rep][steps];
//        Map<Integer, Map<Integer,Double>> rAm = new HashMap<>();
//        Map<Integer, Map<Integer,Double>> rBm = new HashMap<>();

        //Here we run direct exchange code
        for(int i=0;i<rep;i++) {
            for (int j = 0; j < steps; j++) {
                for (Vertex v : vertices) {
                    Vertex newPos = getWeightedRandomNeighborVertex(v);

                    //Wee need to update lastA/B
                    if (v.getLuggage() == 3) {
                        lastA = newPos;
                    }

                    if (v.getLuggage() == 4) {
                        lastB = newPos;
                    }

                    exchengeLuggage(v, newPos);
                }

                //Save new positions
                rAm[i][j]=lastA.getPoint3D().distance(initialA.getPoint3D());
                rBm[i][j]=lastB.getPoint3D().distance(initialB.getPoint3D());

            }

            System.out.println("Rep: " + i + " A moved: "+rAm[i][steps-1]+ " B moved: "+rBm[i][steps-1]);
        }

        for(int j=0; j<steps; j++){

            double rrA=0;
            double rrB=0;

            for(int i=0; i<rep; i++){
                rrA+=rAm[i][j]*rAm[i][j]/rep;
                rrB+=rBm[i][j]*rBm[i][j]/rep;
            }


            msdAplt.getSeries().add(j,rrA);
            msdBplt.getSeries().add(j,rrB);

        }

    }

    private void exchengeLuggage(Vertex A, Vertex B){
        int oldA = A.getLuggage();
        A.setLuggage(B.getLuggage());
        B.setLuggage(oldA);
    }

    private Vertex getWeightedRandomNeighborVertex(Vertex of){

            Vertex out = null;
            tm.clear();
            double count=0;
            double norm=0;
            List<Vertex> nbs = mapGraph.getAllNeighbors(of);

            for(Vertex n: nbs){

                if(of.getLuggage()==1) {
                    if(n.getLuggage()==1) {
                        count+=0.2;
                    }else if(n.getLuggage()==2) {
                        count+=0.3;
                    }
                }else {
                    if(n.getLuggage()==1) {
                        count+=0.3;
                    }else if(n.getLuggage()==2) {
                        count+=0.5;
                    }
                }
                tm.put(count,n);
            }

        double rand = tm.higherKey(rn.nextDouble() * count);
        try {

            out = tm.get(rand);
        }catch(Exception e){
            System.out.println("Oops");
        }

        return out;

    }


}
