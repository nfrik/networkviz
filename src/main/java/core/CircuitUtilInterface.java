package core;

/**
 * Created by nfrik on 7/23/17.
 */
public interface CircuitUtilInterface {

    public Graph generateRandomCircuit(String type, int size, int dim);

    public boolean exportGraphToFile(Graph g,String path);

}
