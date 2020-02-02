package com.indra.srcc.airsrcc.client;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.indra.srcc.airsrcc.client.ui.SwingClientGuiStub;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class GrpcClientApplication implements CommandLineRunner {

	public static void main(String[] args) {
		//SpringApplication.run(GrpcClientApplication.class, args);
		
		ConfigurableApplicationContext context = new SpringApplicationBuilder(GrpcClientApplication.class).headless(false).run(args);
		System.setProperty("java.awt.headless", "false"); //Disables headless
		SwingClientGuiStub guiStub = context.getBean(SwingClientGuiStub.class);
	    guiStub.launchGUI();
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Into run...");
	}
}