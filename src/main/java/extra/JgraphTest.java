package extra;


import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxOrganicLayout;
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


            mxCell v1 =(mxCell)graph.insertVertex(parent, "id1", "Hello", 0, 0,
                    0, 0);
            mxCell v2 = (mxCell)graph.insertVertex(parent, "id2", "World!", 10, 0,
                    0, 0);

            mxCell v3 = (mxCell)graph.insertVertex(parent, "id3", "World!", 10, 10,
                    0, 0);

            mxCell v4 = (mxCell)graph.insertVertex(parent, "id4", "World!", 0, 10,
                    0, 0);
            Object e1 = graph.insertEdge(parent, "e1", "Edge", v1, v2);
            Object e2 = graph.insertEdge(parent, "e2", "Edge", v2, v3);
            Object e3 = graph.insertEdge(parent, "e3", "Edge", v3, v4);
            Object e4 = graph.insertEdge(parent, "e4", "Edge", v1, v4);
            graph.getModel().endUpdate();

        mxIGraphLayout layout = new mxFastOrganicLayout(graph);
        layout.execute(graph.getDefaultParent());
        ((mxGraphModel)graph.getModel()).getCell("Hello");

        mxGraphComponent graphComponent = new mxGraphComponent(graph);


        getContentPane().add(graphComponent);
    }

    public static void main(String[] args)
    {
        JgraphTest frame = new JgraphTest();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 320);
        frame.setVisible(true);


//        mxGraph graph = new mxGraph();
//        Object parent = graph.getDefaultParent();
//
//        graph.getModel().beginUpdate();
//
//        mxCell v1 =(mxCell)graph.insertVertex(parent, "id1", "Hello", 20, 20, 80,
//                    30);
//        mxCell v2 = (mxCell)graph.insertVertex(parent, "id2", "World!", 240, 150,
//                    80, 130);
//
//        mxCell v3 = (mxCell)graph.insertVertex(parent, "id3", "World!", 10, 100,
//                80, 130);
//
//        mxCell v4 = (mxCell)graph.insertVertex(parent, "id4", "World!", 30, 50,
//                80, 130);
//            graph.insertEdge(parent, null, "Edge", v1, v2);
//            graph.insertEdge(parent, null, "Edge", v2, v4);
//            graph.insertEdge(parent, null, "Edge", v3, v4);
//
//            graph.getModel().endUpdate();
//
////        mxIGraphLayout layout = new mxFastOrganicLayout(graph);
//        mxFastOrganicLayout layout = new mxFastOrganicLayout(graph);
//        layout.setForceConstant(40);
//        layout.setDisableEdgeStyle(false);
//        layout.setMaxIterations(200);
//        layout.setMaxDistanceLimit(1000);
//        layout.execute(graph.getDefaultParent());
//        mxCell myCell = (mxCell)((mxGraphModel)graph.getModel()).getCell("id1");
//        System.out.println(mxCell.class);

    }
}
