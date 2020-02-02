package com.indra.srcc.airsrcc.client.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.indra.srcc.airsrcc.client.GrpcClientController;

@Component
public class SwingClientController {


	@Autowired 
	GrpcClientController _grpcClientController;

	public void sendMessage() {
		_grpcClientController.printMessage("Jesus");		
	}

}
