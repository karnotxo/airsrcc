/**
 * 
 */
package com.indra.srcc.airsrcc.lib;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.pf4j.ExtensionPoint;

/**
 * @author jluzonm
 *
 */
public interface PpiMapProvider extends ExtensionPoint {

	JComponent createPpiMapComponent();
	void initPpiMap(JFrame rootframe, JComponent ppimap);
}
