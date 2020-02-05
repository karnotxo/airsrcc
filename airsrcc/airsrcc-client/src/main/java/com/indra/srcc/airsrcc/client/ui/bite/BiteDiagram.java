package com.indra.srcc.airsrcc.client.ui.bite;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.orthogonal.mxOrthogonalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;
import com.mxgraph.view.mxStylesheet;

public class BiteDiagram {

	private mxGraphComponent graphComponent = null;

	public BiteDiagram() {
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {
			Object v1 = graph.insertVertex(parent, null, "R1", 20, 20, 90, 40, 
					"fillColor=#ff0000;gradientColor=#ff0f0f;shadow=1");
			Object v2 = graph.insertVertex(parent, null, "PSR", 60, 120, 80, 30, 
					"fillColor=#ff0000;gradientColor=#0fff0f;shadow=1");
			graph.insertEdge(parent, null, null, v1, v2, "edgeStyle=elbowEdgeStyle;elbow=vertical;orthogonal=1;");
			Object v3 = graph.insertVertex(parent, null, "SSR", 150, 100, 80, 30, 
					"fillColor=#ff0000;gradientColor=#ff0f0f;shadow=1");
			graph.insertEdge(parent, null, null, v1, v3, "edgeStyle=elbowEdgeStyle;elbow=verical;orthogonal=1;");
		} finally {
			graph.getModel().endUpdate();
		}
		graph.setCellsMovable(false);
		graph.setCellsDisconnectable(false);
		graph.setCellsEditable(false);
		graph.setCellsLocked(true);
		graph.setAllowDanglingEdges(false);
		
	    Map<String, Object> edge = new HashMap<String, Object>();
	    edge.put(mxConstants.STYLE_ROUNDED, true);
	    edge.put(mxConstants.STYLE_ORTHOGONAL, false);
	    edge.put(mxConstants.STYLE_EDGE, "elbowEdgeStyle");
	    edge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
	    edge.put(mxConstants.STYLE_ENDARROW, mxConstants.NONE/*ARROW_CLASSIC*/);
	    edge.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
	    edge.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
	    edge.put(mxConstants.STYLE_STROKECOLOR, "#000000"); // default is #6482B9
	    edge.put(mxConstants.STYLE_FONTCOLOR, "#446299");

	    mxStylesheet edgeStyle = new mxStylesheet();
	    
	    edgeStyle.setDefaultEdgeStyle(edge);
	    graph.setStylesheet(edgeStyle);
		
		graphComponent = new mxGraphComponent(graph);
		graphComponent.getGraphControl().addMouseWheelListener(new MouseWheelListener() {			
			
		@Override
			public void mouseWheelMoved(MouseWheelEvent e){

				  if (e.getWheelRotation() < 0){					  
					  graphComponent.zoomIn();				      
				 } else {
					  graphComponent.zoomOut();
				 }
				 e.consume();
				}		
		});
		mxGraphView view = graphComponent.getGraph().getView();
		/*int compLen = graphComponent.getWidth();
		int viewLen = (int)view.getGraphBounds().getWidth();
		view.setScale((double)compLen/viewLen * view.getScale());*/
		new mxOrthogonalLayout(graph).execute(graph.getDefaultParent());
		mxGraphLayout layout = new mxHierarchicalLayout(graph); 
        layout.setUseBoundingBox(false);
        layout.execute(graph.getDefaultParent());
		//new mxParallelEdgeLayout(graph).execute(graph.getDefaultParent());
	}

	public JComponent getComponent() {
		return graphComponent;
	}

}
