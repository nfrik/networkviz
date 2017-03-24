package core;

import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by NF on 3/24/2017.
 */
public class Problem4 {

    private Timeline loop = null;

    PltXYSeries msdxy = new PltXYSeries("MSD planar");
    PltXYSeries msdz = new PltXYSeries("MSD vertical");

    Graph mapGraph = null;

    public static void main(String[] args){
        System.out.println("Test");
    }


    protected void build3DRectBodySystem(double dx, double dy, double dz, double a, double c, int copies) {

        mapGraph = new Graph();
        mapGraph.setVertexDefaultRadius(10);
        mapGraph.getVertexDefaultMaterial().setDiffuseColor(Color.AQUA);
        mapGraph.getEdgeDefaultMaterial().setDiffuseColor(Color.YELLOW);
        
        mapGraph.generateCubicLattice3D(dx,dy,dz,a,a,c,null,true);

//        world.getChildren().addAll(mapGraph.getEdges());
//        world.getChildren().addAll(mapGraph.getVertices());

        System.out.println("Total edges: " + mapGraph.getEdges().size());
        System.out.println("Total vertices: " + mapGraph.getVertices().size());

        setAndUpdatePositionsDirectExchangeLoop(250,a, copies);

    }


    private void setAndUpdatePositionsDirectExchangeLoop(long millis, double bound, int copies){

        if(loop!=null){
            loop.stop();
            msdxy.getSeries().clear();
            msdz.getSeries().clear();
        }

        Random rn = new Random();

        PhongMaterial atomAMaterial = new PhongMaterial();
        atomAMaterial.setDiffuseColor(Color.YELLOW);

        PhongMaterial atomBMaterial = new PhongMaterial();
        atomBMaterial.setDiffuseColor(Color.BLUE);


//        List<Sphere> spheres = new ArrayList<>();
//
//        for(int i=0;i<copies;i++){
//            spheres.add(new Sphere(20));
//            spheres.get(i).setMaterial(atomMaterial);
//        }




//        world.getChildren().addAll(spheres);


        final Vertex nextVacancyPos = mapGraph.getCentralVertexWithinBoundary(bound);//TODO replace with actual side length a

        final List<Vertex> vertices = new ArrayList<Vertex>();


        for(Sphere sphere : spheres) {
            vertices.add(nextVacancyPos);
            if (nextVacancyPos != null) {
                sphere.setTranslateX(nextVacancyPos.getPoint3D().getX());
                sphere.setTranslateY(nextVacancyPos.getPoint3D().getY());
                sphere.setTranslateZ(nextVacancyPos.getPoint3D().getZ());
            }
        }



        final Point3D startPoint = nextVacancyPos.getPoint3D();



        setLoop(new Timeline(new KeyFrame(Duration.millis(millis), new EventHandler<ActionEvent>() {

            int steps = 1;


            @Override
            public void handle(ActionEvent event) {
                //Here we simply update position by searching neighbors of given vertex
                double rmsdxy = 0;
                double rmsdz = 0;
                double rmsd = 0;


                for(int i=0;i<vertices.size();i++){
                    List<Vertex> nbs = mapGraph.getAllNeighbors(vertices.get(i));
                    if(nbs!=null){
                        vertices.set(i,nbs.get(rn.nextInt(nbs.size())));
                    }
                    spheres.get(i).setTranslateX(vertices.get(i).getPoint3D().getX());
                    spheres.get(i).setTranslateY(vertices.get(i).getPoint3D().getY());
                    spheres.get(i).setTranslateZ(vertices.get(i).getPoint3D().getZ());
                }

                for(int i=0; i<vertices.size() ; i++){
                    double rr = vertices.get(i).getPoint3D().distance(startPoint);
                    double rrz=vertices.get(i).getPoint3D().getZ()-startPoint.getZ();
                    rr*=rr;
                    rrz*=rrz;
                    double rrxy=rr-rrz;

                    rmsd+=rr/vertices.size();
                    rmsdxy+=rrxy/vertices.size();
                    rmsdz+=rrz/vertices.size();
                }

                msdxy.getSeries().add(steps,rmsdxy);
                msdz.getSeries().add(steps,rmsdz);

                steps++;

            }
        })));

        getLoop().setCycleCount(Timeline.INDEFINITE);
        getLoop().play();
    }

}
