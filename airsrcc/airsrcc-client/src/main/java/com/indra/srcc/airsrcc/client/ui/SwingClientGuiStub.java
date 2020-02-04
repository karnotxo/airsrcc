package com.indra.srcc.airsrcc.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
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

import org.apache.batik.transcoder.TranscoderException;
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
import com.indra.srcc.airsrcc.client.util.IconTool;

import bibliothek.extension.gui.dock.theme.EclipseTheme;
import bibliothek.extension.gui.dock.theme.eclipse.stack.tab.RectGradientPainter;
import bibliothek.gui.DockController;
import bibliothek.gui.DockTheme;
import bibliothek.gui.Dockable;
import bibliothek.gui.Orientation;
import bibliothek.gui.dock.ExpandableToolbarItemStrategy;
import bibliothek.gui.dock.ScreenDockStation;
import bibliothek.gui.dock.ToolbarContainerDockStation;
import bibliothek.gui.dock.ToolbarDockStation;
import bibliothek.gui.dock.ToolbarGroupDockStation;
import bibliothek.gui.dock.ToolbarItemDockable;
import bibliothek.gui.dock.action.actions.SimpleButtonAction;
import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.grouping.PlaceholderGrouping;
import bibliothek.gui.dock.common.perspective.CGridPerspective;
import bibliothek.gui.dock.common.perspective.CPerspective;
import bibliothek.gui.dock.common.theme.ThemeMap;
import bibliothek.gui.dock.facile.lookandfeel.DockableCollector;
import bibliothek.gui.dock.station.toolbar.group.ToolbarGroupProperty;
import bibliothek.gui.dock.support.lookandfeel.ComponentCollector;
import bibliothek.gui.dock.support.lookandfeel.LookAndFeelList;
import bibliothek.gui.dock.themes.basic.BasicSpanFactory;
import bibliothek.gui.dock.toolbar.expand.DefaultExpandableToolbarItemStrategy;
import bibliothek.gui.dock.toolbar.expand.ExpandedState;
import bibliothek.util.Path;
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
	private CControl dockingFramesControl;

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

		this.rootframe.setLayout(new BorderLayout());

		dockingFramesControl = new CControl(this.rootframe);

		// final DockController controller = new DockController();
		final DockController controller = dockingFramesControl.intern().getController();

		// controller.getProperties().set( DockTheme.SPAN_FACTORY, new BasicSpanFactory(
		// 500, 250 ) );
		controller.setTheme(new EclipseTheme());
		controller.getProperties().set(EclipseTheme.TAB_PAINTER, RectGradientPainter.FACTORY);

		LookAndFeelList laflist = LookAndFeelList.getDefaultList();

		ComponentCollector collector = new DockableCollector(dockingFramesControl.intern());
		laflist.addComponentCollector(collector);

		ThemeMap themes = dockingFramesControl.getThemes();

		themes.select(ThemeMap.KEY_FLAT_THEME);

		final ScreenDockStation screen = new ScreenDockStation(this.rootframe);
		controller.add(screen);

		final ToolbarContainerDockStation north = new ToolbarContainerDockStation(Orientation.HORIZONTAL, 3);

		controller.add(north);

		final ToolbarGroupDockStation group = new ToolbarGroupDockStation();

		this.rootframe.add(north.getComponent(), BorderLayout.NORTH);

		ToolbarDockStation toolbar = new ToolbarDockStation();

		SimpleButtonAction actionPower = new SimpleButtonAction();
		try {
			actionPower.setIcon(IconTool.getIconMultiResolution(16, "icons/svg/lnr-power-switch.svg", Color.ORANGE));
		} catch (IOException | TranscoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			actionPower.setText("Power");
		}
		toolbar.drop(new ToolbarItemDockable(actionPower));

		SimpleButtonAction actionUser = new SimpleButtonAction();
		try {
			actionUser.setIcon(IconTool.getIconMultiResolution(16, "icons/svg/lnr-user.svg", Color.BLUE));
		} catch (IOException | TranscoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			actionUser.setText("User");
		}

		toolbar.drop(new ToolbarItemDockable(actionUser));

		group.drop(toolbar, new ToolbarGroupProperty(0, 0, null));

		ToolbarDockStation toolbar2 = new ToolbarDockStation();

		SimpleButtonAction actionConfig = new SimpleButtonAction();
		try {
			actionConfig.setIcon(IconTool.getIconMultiResolution(16, "icons/svg/lnr-cog.svg", Color.GREEN));
		} catch (IOException | TranscoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			actionConfig.setText("Config");
		}

		toolbar2.drop(new ToolbarItemDockable(actionConfig));

		group.drop(toolbar2, new ToolbarGroupProperty(1, 0, null));

		
		ToolbarDockStation toolbar3 = new ToolbarDockStation();

		SimpleButtonAction actionHelp = new SimpleButtonAction();
		try {
			actionHelp.setIcon(IconTool.getIconMultiResolution(16, "icons/svg/lnr-question-circle.svg", Color.CYAN));
		} catch (IOException | TranscoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			actionHelp.setText("Help");
		}

		toolbar3.drop(new ToolbarItemDockable(actionHelp));

		group.drop(toolbar3, new ToolbarGroupProperty(2, 0, null));
		
		controller.getProperties().set(ExpandableToolbarItemStrategy.STRATEGY,
				new DefaultExpandableToolbarItemStrategy() {
					@Override
					public boolean isEnabled(Dockable item, ExpandedState state) {
						return false;
					}
				});

		north.drop(group);

		this.rootframe.add(dockingFramesControl.getContentArea(), BorderLayout.CENTER);
		// control.setTheme(ThemeMap.KEY_FLAT_THEME);

		this.displayTree = createDisplayTree();
		this.messageLog = createMessageLog();
		this.alertsDisplay = createAlertsDisplay();
		this.siteControlForm = createSiteControlForm();
		this.sitesSelectionTable = createSiteSelectionTable();
		// IMPORTANT: CruiseControl must be the last one to be in front of the others!
		this.ppimap = createPPIMap();

		DefaultSingleCDockable displaytreedock = new DefaultSingleCDockable("displayTree", "Display Tree",
				new JScrollPane(this.displayTree));
		DefaultSingleCDockable messagelogdock = new DefaultSingleCDockable("messageLog", "Message Log",
				new JScrollPane(this.messageLog));
		DefaultSingleCDockable alertsdisplaydock = new DefaultSingleCDockable("alertsdisplay", "Alerts Display",
				new JScrollPane(this.alertsDisplay));
		DefaultSingleCDockable siteselectiontabledock = new DefaultSingleCDockable("siteselectiontable",
				"Site Selection", new JScrollPane(this.sitesSelectionTable));
		DefaultSingleCDockable sitecontrolformdock = new DefaultSingleCDockable("sitecontrolform", "Control",
				new JScrollPane(this.siteControlForm));
		DefaultSingleCDockable ppimapdock = new DefaultSingleCDockable("ppimap", "Map View", this.ppimap);

		initMap();

		/*
		 * After setting up the JComponents, we mark some locations with placeholders.
		 */
		initialLayout(dockingFramesControl);

		/*
		 * We define the group of "dockable". It will now show up at the location where
		 * "placeholder" was inserted into the layout, or at the location of other
		 * dockables that have the same group.
		 */
		ppimapdock.setGrouping(new PlaceholderGrouping(dockingFramesControl, new Path("workspace", "ppi")));
		ppimapdock.setCloseable(false);

		// CContentAreaCenterLocation normal = CLocation.base().normal();
		// ppimapdock.setLocation(normal);
		// ppimapdock.setSticky(false);

		// TreeLocationRoot south = CLocation.base().normalSouth(0.4);

		// alertsdisplaydock.setLocation(south);
		// messagelogdock.setLocation(south.stack());
		alertsdisplaydock.setGrouping(new PlaceholderGrouping(dockingFramesControl, new Path("workspace", "alerts")));
		alertsdisplaydock.setCloseable(false);
		messagelogdock.setGrouping(new PlaceholderGrouping(dockingFramesControl, new Path("workspace", "messaging")));
		messagelogdock.setCloseable(false);

		// TreeLocationRoot west = CLocation.base().normalWest(0.4);

		// displaytreedock.setLocation(west);
		displaytreedock.setGrouping(new PlaceholderGrouping(dockingFramesControl, new Path("workspace", "filtering")));
		displaytreedock.setCloseable(false);

		// TreeLocationRoot east = CLocation.base().normalEast(0.4);

		// siteselectiontabledock.setLocation(east);
		// sitecontrolformdock.setLocation(east.stack());
		siteselectiontabledock
				.setGrouping(new PlaceholderGrouping(dockingFramesControl, new Path("workspace", "sites")));
		siteselectiontabledock.setCloseable(false);
		sitecontrolformdock.setGrouping(new PlaceholderGrouping(dockingFramesControl, new Path("workspace", "detail")));
		sitecontrolformdock.setCloseable(false);

		dockingFramesControl.addDockable(displaytreedock);
		dockingFramesControl.addDockable(messagelogdock);
		dockingFramesControl.addDockable(alertsdisplaydock);
		dockingFramesControl.addDockable(siteselectiontabledock);
		dockingFramesControl.addDockable(sitecontrolformdock);
		dockingFramesControl.addDockable(ppimapdock);

		displaytreedock.setVisible(true);
		messagelogdock.setVisible(true);
		alertsdisplaydock.setVisible(true);
		siteselectiontabledock.setVisible(true);
		sitecontrolformdock.setVisible(true);
		ppimapdock.setVisible(true);

		this.rootframe.pack();
		this.rootframe.setBounds(50, 50, 1000, 700);// TODO: get better size calcs
		this.rootframe.setVisible(true);
		screen.setShowing(true);
	}

	private static void initialLayout(CControl control) {
		CPerspective layout = control.getPerspectives().createEmptyPerspective();

		/*
		 * Adding a placeholder called "workspace.detail" to the right "minimized area"
		 * of the frame
		 */
		layout.getContentArea().getEast().addPlaceholder(new Path("workspace", "detail"));

		/*
		 * One of the many locations of the "external area" is associated with a
		 * placeholder.
		 */
		layout.getScreenStation().addPlaceholder(new Path("workspace", "detail"), 80, 80, 500, 200);

		/*
		 * Adding a placeholder called "workspace.messaging" to the bottom
		 * "minimized area" of the frame
		 */
		layout.getContentArea().getSouth().addPlaceholder(new Path("workspace", "messaging"));

		/*
		 * One of the many locations of the "external area" is associated with a
		 * placeholder.
		 */
		layout.getScreenStation().addPlaceholder(new Path("workspace", "messaging"), 200, 200, 600, 100);

		/* Building a grid (3 rows, 2 columns) of placeholder. */
		CGridPerspective center = layout.getContentArea().getCenter();
		center.gridPlaceholder(0, 0, 1, 3, new Path("workspace", "filtering"));
		center.gridPlaceholder(1, 0, 3, 3, new Path("workspace", "ppi"));
		center.gridPlaceholder(4, 0, 1, 2, new Path("workspace", "sites"));
		center.gridPlaceholder(4, 2, 1, 2, new Path("workspace", "detail"));
		center.gridPlaceholder(0, 4, 2, 1, new Path("workspace", "messaging"));
		center.gridPlaceholder(2, 4, 2, 1, new Path("workspace", "alerts"));

		/*
		 * Finally we tell the CControl that the perspective we just set up should be
		 * displayed.
		 */
		control.getPerspectives().setPerspective(layout, true);
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
