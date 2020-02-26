package com.indra.srcc.airsrcc.client.ui.forms;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import javax.swing.UIManager;

import com.formdev.flatlaf.icons.FlatAbstractIcon;
import com.formdev.flatlaf.ui.FlatUIUtils;

/**
 * "unexternalize" (actually "restore") icon for {@link javax.swing.JInternalFrame}.
 *
 * @uiDefault InternalFrame.iconColor			Color
 *
 * @author Karl Tauber
 */
public class FlatInternalFrameUnexternalizeIcon
	extends FlatAbstractIcon
{
	protected final boolean chevron = "chevron".equals( UIManager.getString( "Component.arrowType" ) );
	
	public FlatInternalFrameUnexternalizeIcon() {
		super( 16, 16, UIManager.getColor( "InternalFrame.iconColor" ) );
	}

	@Override
	protected void paintIcon( Component c, Graphics2D g ) {
		
		Path2D path = FlatUIUtils.createPath( false, 4, 4, 13, 4, 13, 13 );
		g.setStroke( new BasicStroke( 1f ) );
		g.draw( path );

		path = FlatUIUtils.createPath( false, 5,8, 5,12, 9,12 );
		
		g.draw( path );
		
		path = FlatUIUtils.createPath( false, 5,12, 9,8 );
		
		g.draw( path );
	}
}