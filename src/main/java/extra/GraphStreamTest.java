package extra;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.layout.Layout;
import org.graphstream.ui.layout.springbox.implementations.SpringBox;


public class GraphStreamTest {

    public static void main(String args[]) {
        Graph graph = new SingleGraph("Tutorial 1");
        Layout layout = new SpringBox(false);
        graph.addSink(layout);
        layout.addAttributeSink(graph);
        graph.addNode("A" );
        graph.addNode("B" );
        graph.addNode("C" );
        graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");

        // iterate the compute() method a number of times
        while(layout.getStabilization() < 0.9){
            layout.compute();
        }
        graph.display();
    }
}
