package com.indra.srcc.airsrcc.client.ui.forms;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import javax.swing.UIManager;

import com.formdev.flatlaf.icons.FlatAbstractIcon;
import com.formdev.flatlaf.ui.FlatUIUtils;

/**
 * "externalize" (actually "restore") icon for {@link javax.swing.JInternalFrame}.
 *
 * @uiDefault InternalFrame.iconColor			Color
 *
 * @author Karl Tauber
 */
public class FlatInternalFrameExternalizeIcon
	extends FlatAbstractIcon
{
	protected final boolean chevron = "chevron".equals( UIManager.getString( "Component.arrowType" ) );
	
	public FlatInternalFrameExternalizeIcon() {
		super( 16, 16, UIManager.getColor( "InternalFrame.iconColor" ) );
	}

	@Override
	protected void paintIcon( Component c, Graphics2D g ) {


		Path2D path = FlatUIUtils.createPath( false, 4, 4, 13, 4, 13, 13 );
		g.setStroke( new BasicStroke( 1f ) );
		g.draw( path );

		path = FlatUIUtils.createPath( false, 6,7, 10,7, 10,11 );
		
		g.draw( path );
		
		path = FlatUIUtils.createPath( false, 5,12, 10,7 );
		
		g.draw( path );
		
	}
}