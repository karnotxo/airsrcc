package com.indra.srcc.airsrcc.client;

import com.indra.srcc.airsrcc.lib.GreeterGrpc;
import com.indra.srcc.airsrcc.lib.GreeterOuterClass;
import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import io.grpc.StatusRuntimeException;

@Service
public class GrpcClientService {

	@GrpcClient("local-grpc-server")
	private Channel serverChannel;

	public String sendMessage(String name) {
		try {
			GreeterGrpc.GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(serverChannel);
			GreeterOuterClass.HelloReply response = stub
					.sayHello(GreeterOuterClass.HelloRequest.newBuilder().setName(name).build());
			return response.getMessage();
		} catch (final StatusRuntimeException e) {
			return "FAILED with " + e.getStatus().getCode().name();
		}
	}
}