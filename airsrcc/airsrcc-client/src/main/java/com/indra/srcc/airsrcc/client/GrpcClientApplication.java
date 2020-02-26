package com.indra.srcc.airsrcc.client;

import java.util.List;

import org.pf4j.JarPluginManager;
import org.pf4j.PluginManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.indra.srcc.airsrcc.client.ui.SwingClientGuiStub;
import com.indra.srcc.airsrcc.lib.PpiMapProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class GrpcClientApplication implements CommandLineRunner {

	public static void main(String[] args) {
		//SpringApplication.run(GrpcClientApplication.class, args);
		
		PluginManager pluginManager = new JarPluginManager(); // or "new ZipPluginManager() / new DefaultPluginManager()"
	    
	    // start and load all plugins of application
	    pluginManager.loadPlugins();
	    pluginManager.startPlugins();
	    
	    List<PpiMapProvider> ppiMapsProvider = pluginManager.getExtensions(PpiMapProvider.class);
		
		ConfigurableApplicationContext context = new SpringApplicationBuilder(GrpcClientApplication.class).headless(false).run(args);
		System.setProperty("java.awt.headless", "false"); //Disables headless
		SwingClientGuiStub guiStub = context.getBean(SwingClientGuiStub.class);

	    guiStub.launchGUI(ppiMapsProvider.get(0));
	    
	    // stop and unload all plugins
	    pluginManager.stopPlugins();
	    //pluginManager.unloadPlugins();
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Into run...");
	}
}