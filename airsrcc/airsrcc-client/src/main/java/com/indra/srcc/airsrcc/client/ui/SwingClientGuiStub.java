package com.indra.srcc.airsrcc.client.ui;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
import com.indra.srcc.airsrcc.client.control.SwingClientController;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CLocation;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.SingleCDockable;
import bibliothek.gui.dock.common.location.CContentAreaCenterLocation;
import bibliothek.gui.dock.common.location.TreeLocationRoot;
import bibliothek.gui.dock.common.theme.ThemeMap;
import de.sciss.treetable.j.TreeTable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SwingClientGuiStub {

	private JFrame rootframe;

	@Autowired
	SwingClientController _swingClientController;

	private javax.swing.JMenuItem aboutMenuItem;
	private javax.swing.JMenuItem contentMenuItem;
	private javax.swing.JMenuItem exitMenuItem;
	private javax.swing.JMenu fileMenu;
	private javax.swing.JMenu helpMenu;
	private javax.swing.JMenuBar menuBar;
	private javax.swing.JMenuItem openMenuItem;

	private JComponent displayTree;
	private JComponent messageLog;
	private JComponent alertsDisplay;
	private JComponent sitesSelectionTable;
	private JComponent ppimap;

	private JComponent siteControlForm;

	/**
	 * CONSTRUCTOR
	 */
	public SwingClientGuiStub() {

	}

	public void launchGUI() {

		this.rootframe = new JFrame("AirSRCC");
		this.rootframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.menuBar = new javax.swing.JMenuBar();
		this.fileMenu = new javax.swing.JMenu();
		this.openMenuItem = new javax.swing.JMenuItem();
		this.exitMenuItem = new javax.swing.JMenuItem();
		this.helpMenu = new javax.swing.JMenu();
		this.contentMenuItem = new javax.swing.JMenuItem();
		this.aboutMenuItem = new javax.swing.JMenuItem();

		fileMenu.setMnemonic('f');
		fileMenu.setText("File");

		openMenuItem.setMnemonic('m');
		openMenuItem.setText("Send Message");
		openMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(openMenuItem);

		exitMenuItem.setMnemonic('x');
		exitMenuItem.setText("Exit");
		exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exitMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(exitMenuItem);

		menuBar.add(fileMenu);

		helpMenu.setMnemonic('h');
		helpMenu.setText("Help");

		contentMenuItem.setMnemonic('c');
		contentMenuItem.setText("Contents");
		helpMenu.add(contentMenuItem);

		aboutMenuItem.setMnemonic('a');
		aboutMenuItem.setText("About");
		helpMenu.add(aboutMenuItem);

		menuBar.add(helpMenu);

		this.rootframe.setJMenuBar(menuBar);

		CControl control = new CControl(this.rootframe);
		this.rootframe.setLayout(new BorderLayout());
		this.rootframe.add(control.getContentArea(), BorderLayout.CENTER);
		control.setTheme(ThemeMap.KEY_FLAT_THEME);

		this.displayTree = createDisplayTree();
		this.messageLog = createMessageLog();
		this.alertsDisplay = createAlertsDisplay();
		this.siteControlForm = createSiteControlForm();
		this.sitesSelectionTable = createSiteSelectionTable();
		// IMPORTANT: CruiseControl must be the last one to be in front of the others!
		this.ppimap = createPPIMap();

		SingleCDockable displaytreedock = new DefaultSingleCDockable("displayTree", "Display Tree",
				new JScrollPane(this.displayTree));
		SingleCDockable messagelogdock = new DefaultSingleCDockable("messageLog", "Message Log",
				new JScrollPane(this.messageLog));
		SingleCDockable alertsdisplaydock = new DefaultSingleCDockable("alertsdisplay", "Alerts Display",
				new JScrollPane(this.alertsDisplay));
		SingleCDockable siteselectiontabledock = new DefaultSingleCDockable("siteselectiontable", "Site Selection",
				new JScrollPane(this.sitesSelectionTable));
		SingleCDockable sitecontrolformdock = new DefaultSingleCDockable("sitecontrolform", "Control",
				new JScrollPane(this.siteControlForm));
		SingleCDockable ppimapdock = new DefaultSingleCDockable("ppimap", "Map View", new JScrollPane(this.ppimap));

		control.addDockable(displaytreedock);
		control.addDockable(messagelogdock);
		control.addDockable(alertsdisplaydock);
		control.addDockable(siteselectiontabledock);
		control.addDockable(sitecontrolformdock);
		control.addDockable(ppimapdock);

		CContentAreaCenterLocation normal = CLocation.base().normal();

		ppimapdock.setLocation(normal);

		TreeLocationRoot south = CLocation.base().normalSouth(0.4);

		alertsdisplaydock.setLocation(south);
		messagelogdock.setLocation(south.stack());

		TreeLocationRoot west = CLocation.base().normalWest(0.4);

		displaytreedock.setLocation(west);

		TreeLocationRoot east = CLocation.base().normalEast(0.4);

		siteselectiontabledock.setLocation(east);
		sitecontrolformdock.setLocation(east.stack());

		displaytreedock.setVisible(true);
		messagelogdock.setVisible(true);
		alertsdisplaydock.setVisible(true);
		siteselectiontabledock.setVisible(true);
		ppimapdock.setVisible(true);
		
		initMap();

		this.rootframe.pack();
		this.rootframe.setBounds(50, 50, 1000, 700);// TODO: get better size calcs
		this.rootframe.setVisible(true);
	}

	private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_exitMenuItemActionPerformed
		System.exit(0);
	}

	private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_openMenuItemActionPerformed
		// pessoaUI.setVisible(true);
		_swingClientController.sendMessage();
		// _grpcClientController.printMessage("Jesus");
	}

	private JComponent createDisplayTree() {
		return new JTree();
	}

	private JComponent createMessageLog() {
		return new JTextArea();
	}

	private JComponent createAlertsDisplay() {
		return new JTable();
	}

	private JComponent createSiteSelectionTable() {
		return new TreeTable();
	}

	private JComponent createPPIMap() {

		return new BasicMapPanel();
	}

	private JComponent createSiteControlForm() {
		return new JPanel();
	}

	private void initMap() {
		try {
			// Get the default MapHandler the BasicMapPanel created.
			MapHandler mapHandler = ((BasicMapPanel) this.ppimap).getMapHandler();

			// Add MouseDelegator, which handles mouse modes (managing mouse
			// events)
			mapHandler.add(new MouseDelegator());
			// Add OMMouseMode, which handles how the map reacts to mouse
			// movements
			mapHandler.add(new OMMouseMode());

			// Set the map's center
			((BasicMapPanel) this.ppimap).getMapBean().setCenter(new LatLonPoint.Double(40.416775, -3.703790));
			// Set the map's scale 1:12 million
			((BasicMapPanel) this.ppimap).getMapBean().setScale(12000000f);
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
				this.rootframe.revalidate();
			});

			// Add the map to the frame
			//mapHandler.add(this.rootframe);
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
			p.load(new StringReader("shapePolitical.prettyName=Political Solid\n"
					+ "shapePolitical.lineColor=000000\n" + "shapePolitical.fillColor=BDDE83\n"
					+ "shapePolitical.shapeFile=map/shape/europe.shp\n"));
					//+ "shapePolitical.spatialIndex=map/shape/europe.prj\n"));
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
