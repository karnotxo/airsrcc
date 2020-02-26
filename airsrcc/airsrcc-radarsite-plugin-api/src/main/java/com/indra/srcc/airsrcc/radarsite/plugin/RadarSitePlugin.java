package com.indra.srcc.airsrcc.radarsite.plugin;

import org.pf4j.ExtensionPoint;

public interface RadarSitePlugin  extends ExtensionPoint {

	void configure();
	boolean start();
	boolean stop();
	void addPPIDataListener(PPIDataListener listener);
}
