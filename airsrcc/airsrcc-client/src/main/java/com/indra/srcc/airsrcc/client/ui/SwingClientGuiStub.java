package com.indra.srcc.airsrcc.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JTree;

import org.apache.batik.transcoder.TranscoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.icons.FlatInternalFrameIconifyIcon;
import com.formdev.flatlaf.icons.FlatInternalFrameMaximizeIcon;
import com.formdev.flatlaf.icons.FlatInternalFrameMinimizeIcon;
import com.indra.srcc.airsrcc.client.control.SwingClientController;
import com.indra.srcc.airsrcc.client.ui.bite.BiteDiagram;
import com.indra.srcc.airsrcc.client.ui.forms.FlatInternalFrameExternalizeIcon;
import com.indra.srcc.airsrcc.client.ui.forms.FlatInternalFrameUnexternalizeIcon;
import com.indra.srcc.airsrcc.client.util.IconTool;
import com.indra.srcc.airsrcc.lib.PpiMapProvider;
import com.l2fprod.common.demo.PropertySheetMain;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.grouping.PlaceholderGrouping;
import bibliothek.gui.dock.common.perspective.CGridPerspective;
import bibliothek.gui.dock.common.perspective.CPerspective;
import bibliothek.gui.dock.common.theme.ThemeMap;
import bibliothek.gui.dock.facile.lookandfeel.DockableCollector;
import bibliothek.gui.dock.support.lookandfeel.ComponentCollector;
import bibliothek.gui.dock.support.lookandfeel.LookAndFeelList;
import bibliothek.util.Path;
import de.sciss.treetable.j.TreeTable;

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
	private JComponent bitediagram;
	private JComponent ppimap;

	private JComponent siteControlForm;
	private CControl dockingFramesControl;
	
	private PpiMapProvider ppiMapProvider;

	/**
	 * CONSTRUCTOR
	 */
	public SwingClientGuiStub() {

	}

	public void launchGUI(PpiMapProvider ppiMapProvider) {
		
		this.ppiMapProvider = ppiMapProvider;
		
		FlatDarkLaf.install();

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
		
		/*
		 * locationmanager.normalize = normalize.png 
		 * locationmanager.maximize = maximize.png 
		 * locationmanager.minimize = minimize.png
		 * locationmanager.externalize = externalize.png 
		 * locationmanager.unexternalize = unexternalize.png 
		 * locationmanager.unmaximize_externalized = normalize.png
		 * 
		 * rename = rename.png 
		 * replace = replace.png
		 */
		

		dockingFramesControl.getController().getIcons().setIconClient("locationmanager.normalize", 
				new FlatInternalFrameMinimizeIcon());		
		dockingFramesControl.getController().getIcons().setIconClient("locationmanager.minimize", 
				new FlatInternalFrameIconifyIcon());
		dockingFramesControl.getController().getIcons().setIconClient("locationmanager.maximize", 
				new FlatInternalFrameMaximizeIcon());

		dockingFramesControl.getController().getIcons().setIconClient("locationmanager.externalize", 
				new FlatInternalFrameExternalizeIcon());

		dockingFramesControl.getController().getIcons().setIconClient("locationmanager.unexternalize", 
				new FlatInternalFrameUnexternalizeIcon());

		// final DockController controller = new DockController();
		//final DockController controller = dockingFramesControl.intern().getController();

		// controller.getProperties().set( DockTheme.SPAN_FACTORY, new BasicSpanFactory(
		// 500, 250 ) );
		dockingFramesControl.setTheme(ThemeMap.KEY_FLAT_THEME);
		
		LookAndFeelList laflist = LookAndFeelList.getDefaultList();

		ComponentCollector collector = new DockableCollector(dockingFramesControl.intern());
		laflist.addComponentCollector(collector);

		ThemeMap themes = dockingFramesControl.getThemes();

		themes.select(ThemeMap.KEY_FLAT_THEME);

		JToolBar toolbar = new JToolBar();
		
		
		

		Action actionPower = new AbstractAction("Power") {
			public void actionPerformed(ActionEvent e) {
				
			};		
		};
		try {
			actionPower.putValue(Action.SMALL_ICON, 
					IconTool.getIconMultiResolution(16, "icons/svg/lnr-power-switch.svg", Color.CYAN));
		} catch (IOException | TranscoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			actionPower.putValue(Action.SHORT_DESCRIPTION, "Power");
		}
		
		toolbar.add(actionPower);
		

		Action actionUser = new AbstractAction("User") {
			public void actionPerformed(ActionEvent e) {
				
			};		
		};
		try {
			actionUser.putValue(Action.SMALL_ICON, 
					IconTool.getIconMultiResolution(16, "icons/svg/lnr-user.svg", Color.PINK));
		} catch (IOException | TranscoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			actionUser.putValue(Action.SHORT_DESCRIPTION,"User");
		}

		toolbar.add(actionUser);


		Action actionConfig = new AbstractAction("Config") {
			public void actionPerformed(ActionEvent e) {
				
			};		
		};
		try {
			actionConfig.putValue(Action.SMALL_ICON, 
					IconTool.getIconMultiResolution(16, "icons/svg/lnr-cog.svg", Color.WHITE));
		} catch (IOException | TranscoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			actionConfig.putValue(Action.SHORT_DESCRIPTION,"Config");
		}

		toolbar.add(actionConfig);
		
		Action actionHelp = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				
			};		
		};
		try {
			actionHelp.putValue(Action.SMALL_ICON, 
					IconTool.getIconMultiResolution(16, "icons/svg/lnr-question-circle.svg", Color.CYAN));
		} catch (IOException | TranscoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			actionHelp.putValue(Action.SHORT_DESCRIPTION,"Help");
		}

		toolbar.add(actionHelp);		

		this.rootframe.add(toolbar, BorderLayout.NORTH);

		this.rootframe.add(dockingFramesControl.getContentArea(), BorderLayout.CENTER);
		// control.setTheme(ThemeMap.KEY_FLAT_THEME);

		this.displayTree = createDisplayTree();
		this.messageLog = createMessageLog();
		this.alertsDisplay = createAlertsDisplay();
		this.siteControlForm = createSiteControlForm();
		this.sitesSelectionTable = createSiteSelectionTable();
		this.bitediagram = createBiteDiagram();
		// IMPORTANT: CruiseControl must be the last one to be in front of the others!
		this.ppimap = this.ppiMapProvider.createPpiMapComponent();

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
		DefaultSingleCDockable bitediagramdock = new DefaultSingleCDockable("bitediagram", "BITE",
				this.bitediagram);
		DefaultSingleCDockable ppimapdock = new DefaultSingleCDockable("ppimap", "Map View", this.ppimap);

		this.ppiMapProvider.initPpiMap(rootframe, ppimap);

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
		bitediagramdock.setGrouping(new PlaceholderGrouping(dockingFramesControl, new Path("workspace", "bite")));
		bitediagramdock.setCloseable(false);

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
		dockingFramesControl.addDockable(bitediagramdock);
		dockingFramesControl.addDockable(ppimapdock);

		displaytreedock.setVisible(true);
		messagelogdock.setVisible(true);
		alertsdisplaydock.setVisible(true);
		siteselectiontabledock.setVisible(true);
		sitecontrolformdock.setVisible(true);
		bitediagramdock.setVisible(true);
		ppimapdock.setVisible(true);

		this.rootframe.pack();
		this.rootframe.setBounds(50, 50, 1000, 700);// TODO: get better size calcs
		this.rootframe.setVisible(true);
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
		center.gridPlaceholder(0, 5, 4, 1, new Path("workspace", "bite"));

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

	private JComponent createBiteDiagram() {

		return new BiteDiagram().getComponent();
	}

	private JComponent createSiteControlForm() {
		return new PropertySheetMain();
	}

	protected static class Zoom extends AbstractAction { 
		
		  double factor;  
		  
		  public Zoom(double factor) {  
		    super("Zoom " + (factor > 1.0 ? "In" : "Out"));
		    this.factor = factor;  
		    this.putValue(Action.SHORT_DESCRIPTION,   
		                  "Zoom " + (factor > 1.0 ? "In" : "Out"));  
		  }  
		  
		  public void actionPerformed(ActionEvent e) {  
		   /* view.setZoom(view.getZoom() * factor);  
		    // Adjusts the size of the view's world rectangle. The world rectangle   
		    // defines the region of the canvas that is accessible by using the   
		    // scrollbars of the view.  
		    Rectangle box = view.getGraph2D().getBoundingBox();  
		    view.setWorldRect(box.x - 20, box.y - 20, box.width + 40, box.height + 40);  
		    view.updateView();*/  
		  }  
		}  
	
}
