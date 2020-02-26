package com.indra.srcc.airsrcc.openmap;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.pf4j.Extension;

import com.bbn.openmap.LayerHandler;
import com.bbn.openmap.MapHandler;
import com.bbn.openmap.MouseDelegator;
import com.bbn.openmap.MultipleSoloMapComponentException;
import com.bbn.openmap.PropertyHandler;
import com.bbn.openmap.event.OMMouseMode;
import com.bbn.openmap.gui.BasicMapPanel;
import com.bbn.openmap.layer.GraticuleLayer;
import com.bbn.openmap.layer.shape.ShapeLayer;
import com.bbn.openmap.proj.coords.LatLonPoint;
import com.indra.srcc.airsrcc.lib.PpiMapProvider;

import lombok.extern.slf4j.Slf4j;


/**
 * @author jluzonm
 *
 */
@Slf4j
@Extension
public class OpenMapPpiProvider implements PpiMapProvider {

	@Override
	public JComponent createPpiMapComponent() {
		return new BasicMapPanel();
	}

	@Override
	public void initPpiMap(JFrame rootframe, JComponent ppimap) {
		try {
			// Get the default MapHandler the BasicMapPanel created.
			MapHandler mapHandler = ((BasicMapPanel) ppimap).getMapHandler();

			// Add MouseDelegator, which handles mouse modes (managing mouse
			// events)
			mapHandler.add(new MouseDelegator());
			// Add OMMouseMode, which handles how the map reacts to mouse
			// movements
			mapHandler.add(new OMMouseMode());

			// Set the map's center
			((BasicMapPanel) ppimap).getMapBean().setCenter(new LatLonPoint.Double(40.416775, -3.703790));
			// Set the map's scale 1:12 million
			((BasicMapPanel) ppimap).getMapBean().setScale(12000000f);
			/*
			 * Create and add a LayerHandler to the MapHandler. The LayerHandler manages
			 * Layers, whether they are part of the map or not. layer.setVisible(true) will
			 * add it to the map. The LayerHandler has methods to do this, too. The
			 * LayerHandler will find the MapBean in the MapHandler.
			 */
			mapHandler.add(new LayerHandler());
			CompletableFuture.supplyAsync(() -> getShapeLayer()).thenAcceptAsync(shapeLayer -> {
				// Add the political layer to the map
				mapHandler.add(shapeLayer);
				mapHandler.add(new GraticuleLayer());
				rootframe.revalidate();
			});

			// Add the map to the frame
			// mapHandler.add(this.rootframe);
		} catch (MultipleSoloMapComponentException msmce) {
			// The MapHandler is only allowed to have one of certain
			// items. These items implement the SoloMapComponent
			// interface. The MapHandler can have a policy that
			// determines what to do when duplicate instances of the
			// same type of object are added - replace or ignore.

			// In this example, this will never happen, since we are
			// controlling that one MapBean, LayerHandler,
			// MouseDelegator, etc is being added to the MapHandler.
		}
	}

	private ShapeLayer getShapeLayer() {
		PropertyHandler propertyHandler = null;
		final Properties p = new Properties();
		try {
			p.load(new StringReader("shapePolitical.prettyName=Political Solid\n" + "shapePolitical.lineColor=000000\n"
					+ "shapePolitical.fillColor=BDDE83\n" + "shapePolitical.shapeFile=map/shape/europe.shp\n"));
			// + "shapePolitical.spatialIndex=map/shape/europe.prj\n"));
			propertyHandler = new PropertyHandler.Builder().setProperties(p).setPropertyPrefix("shapePolitical")
					.build();
		} catch (IOException ex) {
			log.error(null, ex);
		}
		// ShapeLayer:
		ShapeLayer shapeLayer = new ShapeLayer();
		if (propertyHandler != null) {
			shapeLayer.setProperties(propertyHandler.getPropertyPrefix(),
					propertyHandler.getProperties(propertyHandler.getPropertyPrefix()));
		}
		return shapeLayer;
	}

}
