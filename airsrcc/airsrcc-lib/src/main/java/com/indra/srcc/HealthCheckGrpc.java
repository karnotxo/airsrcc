package com.indra.srcc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.25.0)",
    comments = "Source: health_check.proto")
public final class HealthCheckGrpc {

  private HealthCheckGrpc() {}

  public static final String SERVICE_NAME = "HealthCheck";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.indra.srcc.HealthCheckOuterClass.Service,
      com.indra.srcc.HealthCheckOuterClass.Status> getCheckMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "check",
      requestType = com.indra.srcc.HealthCheckOuterClass.Service.class,
      responseType = com.indra.srcc.HealthCheckOuterClass.Status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.indra.srcc.HealthCheckOuterClass.Service,
      com.indra.srcc.HealthCheckOuterClass.Status> getCheckMethod() {
    io.grpc.MethodDescriptor<com.indra.srcc.HealthCheckOuterClass.Service, com.indra.srcc.HealthCheckOuterClass.Status> getCheckMethod;
    if ((getCheckMethod = HealthCheckGrpc.getCheckMethod) == null) {
      synchronized (HealthCheckGrpc.class) {
        if ((getCheckMethod = HealthCheckGrpc.getCheckMethod) == null) {
          HealthCheckGrpc.getCheckMethod = getCheckMethod =
              io.grpc.MethodDescriptor.<com.indra.srcc.HealthCheckOuterClass.Service, com.indra.srcc.HealthCheckOuterClass.Status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "check"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.indra.srcc.HealthCheckOuterClass.Service.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.indra.srcc.HealthCheckOuterClass.Status.getDefaultInstance()))
              .setSchemaDescriptor(new HealthCheckMethodDescriptorSupplier("check"))
              .build();
        }
      }
    }
    return getCheckMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static HealthCheckStub newStub(io.grpc.Channel channel) {
    return new HealthCheckStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static HealthCheckBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new HealthCheckBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static HealthCheckFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new HealthCheckFutureStub(channel);
  }

  /**
   */
  public static abstract class HealthCheckImplBase implements io.grpc.BindableService {

    /**
     */
    public void check(com.indra.srcc.HealthCheckOuterClass.Service request,
        io.grpc.stub.StreamObserver<com.indra.srcc.HealthCheckOuterClass.Status> responseObserver) {
      asyncUnimplementedUnaryCall(getCheckMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getCheckMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.indra.srcc.HealthCheckOuterClass.Service,
                com.indra.srcc.HealthCheckOuterClass.Status>(
                  this, METHODID_CHECK)))
          .build();
    }
  }

  /**
   */
  public static final class HealthCheckStub extends io.grpc.stub.AbstractStub<HealthCheckStub> {
    private HealthCheckStub(io.grpc.Channel channel) {
      super(channel);
    }

    private HealthCheckStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HealthCheckStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new HealthCheckStub(channel, callOptions);
    }

    /**
     */
    public void check(com.indra.srcc.HealthCheckOuterClass.Service request,
        io.grpc.stub.StreamObserver<com.indra.srcc.HealthCheckOuterClass.Status> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCheckMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class HealthCheckBlockingStub extends io.grpc.stub.AbstractStub<HealthCheckBlockingStub> {
    private HealthCheckBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private HealthCheckBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HealthCheckBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new HealthCheckBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.indra.srcc.HealthCheckOuterClass.Status check(com.indra.srcc.HealthCheckOuterClass.Service request) {
      return blockingUnaryCall(
          getChannel(), getCheckMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class HealthCheckFutureStub extends io.grpc.stub.AbstractStub<HealthCheckFutureStub> {
    private HealthCheckFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private HealthCheckFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HealthCheckFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new HealthCheckFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.indra.srcc.HealthCheckOuterClass.Status> check(
        com.indra.srcc.HealthCheckOuterClass.Service request) {
      return futureUnaryCall(
          getChannel().newCall(getCheckMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CHECK = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final HealthCheckImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(HealthCheckImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CHECK:
          serviceImpl.check((com.indra.srcc.HealthCheckOuterClass.Service) request,
              (io.grpc.stub.StreamObserver<com.indra.srcc.HealthCheckOuterClass.Status>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class HealthCheckBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    HealthCheckBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.indra.srcc.HealthCheckOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("HealthCheck");
    }
  }

  private static final class HealthCheckFileDescriptorSupplier
      extends HealthCheckBaseDescriptorSupplier {
    HealthCheckFileDescriptorSupplier() {}
  }

  private static final class HealthCheckMethodDescriptorSupplier
      extends HealthCheckBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    HealthCheckMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (HealthCheckGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new HealthCheckFileDescriptorSupplier())
              .addMethod(getCheckMethod())
              .build();
        }
      }
    }
    return result;
  }
}
