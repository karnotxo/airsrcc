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
		
		g.fill( FlatUIUtils.createRectangle( 6, 6, 8, 8, 1 ) );
		
		if( chevron ) {
			// chevron arrow
			Path2D path = FlatUIUtils.createPath( false, 1,1, 5,5, 9,1 );
			g.setStroke( new BasicStroke( 1f ) );
			g.draw( path );
		} else {
			// triangle arrow
			g.fill( FlatUIUtils.createPath( 0.5,0, 5,5, 9.5,0 ) );
		}
	}
}