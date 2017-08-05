package core;

import javafx.geometry.Point3D;

import java.util.List;

/**
 * Created by friknik on 2/28/2017.
 */
public class Layout {

    //mass
    double alpha = 1.0;

    //charge
    double beta = 2e6;

    //spring const
    double k = 1.;

    //potential cutoff
    double eps = 10.0;

    //damping
    double eta = .9;

    //time
    double delta_t = .01;

    double springl = 100;

    private Point3D coulombForce(Vertex p1, Vertex p2){
        Point3D dr = p2.getPoint3D().subtract(p1.getPoint3D());

        double mag = dr.magnitude();

        if(mag < eps){
            return dr.multiply(0);
        }else{
            return dr.multiply(-beta/(mag*mag*mag));
        }
    }

    private Point3D hookeForce(Vertex p1, Vertex p2, Edge e){
        Point3D dr = p2.getPoint3D().subtract(p1.getPoint3D());
//      Point3D dr = e.getEndPoint().getPoint3D().subtract(e.getStartPoint().getPoint3D());

        double mag = dr.magnitude();

        double dl =  mag - springl;

        return dr.multiply(k*dl/mag);
    }

    public void runSpring(Graph graph, Vertex stat){
        Point3D F;
        double Ekin = 0;
        List<Vertex> viNeighbors = null;
        Edge edgevivj = null;

        for(Vertex vi : graph.getVertices()){

            F = new Point3D(0,0,0);
            viNeighbors = graph.getInNeighbors(vi);
            viNeighbors.addAll(graph.getOutNeighbors(vi));

            for(Vertex vj : graph.getVertices()){
                if(vj!=stat && vi!=vj) {
                    edgevivj = graph.getEdgeBetweenVertexes(vi, vj);
                    if (edgevivj != null) {
                        //calculate hookes'
                        F = F.add(hookeForce(vi,vj,edgevivj));

                    } else {
                        //calculate columbs
                        F = F.add(coulombForce(vi, vj));
                    }
                    //F=F.add(coulombForce(vi, vj));
                }
            }

            vi.setVelocity(vi.getVelocity().add(F.multiply(delta_t*alpha)).multiply(eta));

            Ekin+=vi.getVelocity().dotProduct(vi.getVelocity())*alpha;

//            System.out.println("Kinetic energy: "+Ekin);
        }

        stat.setVelocity(new Point3D(0,0,0));
        for(Vertex vi : graph.getVertices()){
            graph.transformVertex(vi,vi.getPoint3D().add(vi.getVelocity().multiply(delta_t)));
        }
    }

}
