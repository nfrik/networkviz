package extra;


import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;

public class JgraphTest extends JFrame{
    /**
     *
     */
    private static final long serialVersionUID = -2707712944901661771L;

    public JgraphTest()
    {
        super("Hello, World!");

        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        try
        {
            Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 80,
                    30);
            Object v2 = graph.insertVertex(parent, null, "World!", 240, 150,
                    80, 30);
            graph.insertEdge(parent, null, "Edge", v1, v2);
        }
        finally
        {
            graph.getModel().endUpdate();
        }

        mxIGraphLayout layout = new mxFastOrganicLayout(graph);
        layout.execute(graph.getDefaultParent());
        ((mxGraphModel)graph.getModel()).getCell("Hello");

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);
    }

    public static void main(String[] args)
    {
//        JgraphTest frame = new JgraphTest();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(400, 320);
//        frame.setVisible(true);


        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        try
        {
            Object v1 = graph.insertVertex(parent, "id1", "Hello", 20, 20, 80,
                    30);
            Object v2 = graph.insertVertex(parent, "id2", "World!", 240, 150,
                    80, 30);
            graph.insertEdge(parent, null, "Edge", v1, v2);
        }
        finally
        {
            graph.getModel().endUpdate();
        }

        mxIGraphLayout layout = new mxFastOrganicLayout(graph);
//        layout.execute(graph.getDefaultParent());
        mxCell myCell = (mxCell)((mxGraphModel)graph.getModel()).getCell("id1");
        System.out.println(mxCell.class);

    }
}
