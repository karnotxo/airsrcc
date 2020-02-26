package com.indra.srcc.airsrcc.lib;

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
 * <pre>
 * The detection service definition.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.25.0)",
    comments = "Source: detections.proto")
public final class DetectionGrpc {

  private DetectionGrpc() {}

  public static final String SERVICE_NAME = "Detection";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.indra.srcc.airsrcc.lib.Detections.DetectionDataRequest,
      com.indra.srcc.airsrcc.lib.Detections.DetectionDataReply> getSendDetectionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendDetection",
      requestType = com.indra.srcc.airsrcc.lib.Detections.DetectionDataRequest.class,
      responseType = com.indra.srcc.airsrcc.lib.Detections.DetectionDataReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.indra.srcc.airsrcc.lib.Detections.DetectionDataRequest,
      com.indra.srcc.airsrcc.lib.Detections.DetectionDataReply> getSendDetectionMethod() {
    io.grpc.MethodDescriptor<com.indra.srcc.airsrcc.lib.Detections.DetectionDataRequest, com.indra.srcc.airsrcc.lib.Detections.DetectionDataReply> getSendDetectionMethod;
    if ((getSendDetectionMethod = DetectionGrpc.getSendDetectionMethod) == null) {
      synchronized (DetectionGrpc.class) {
        if ((getSendDetectionMethod = DetectionGrpc.getSendDetectionMethod) == null) {
          DetectionGrpc.getSendDetectionMethod = getSendDetectionMethod =
              io.grpc.MethodDescriptor.<com.indra.srcc.airsrcc.lib.Detections.DetectionDataRequest, com.indra.srcc.airsrcc.lib.Detections.DetectionDataReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SendDetection"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.indra.srcc.airsrcc.lib.Detections.DetectionDataRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.indra.srcc.airsrcc.lib.Detections.DetectionDataReply.getDefaultInstance()))
              .setSchemaDescriptor(new DetectionMethodDescriptorSupplier("SendDetection"))
              .build();
        }
      }
    }
    return getSendDetectionMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DetectionStub newStub(io.grpc.Channel channel) {
    return new DetectionStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DetectionBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new DetectionBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DetectionFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new DetectionFutureStub(channel);
  }

  /**
   * <pre>
   * The detection service definition.
   * </pre>
   */
  public static abstract class DetectionImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Sends a detection
     * </pre>
     */
    public void sendDetection(com.indra.srcc.airsrcc.lib.Detections.DetectionDataRequest request,
        io.grpc.stub.StreamObserver<com.indra.srcc.airsrcc.lib.Detections.DetectionDataReply> responseObserver) {
      asyncUnimplementedUnaryCall(getSendDetectionMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSendDetectionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.indra.srcc.airsrcc.lib.Detections.DetectionDataRequest,
                com.indra.srcc.airsrcc.lib.Detections.DetectionDataReply>(
                  this, METHODID_SEND_DETECTION)))
          .build();
    }
  }

  /**
   * <pre>
   * The detection service definition.
   * </pre>
   */
  public static final class DetectionStub extends io.grpc.stub.AbstractStub<DetectionStub> {
    private DetectionStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DetectionStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DetectionStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DetectionStub(channel, callOptions);
    }

    /**
     * <pre>
     * Sends a detection
     * </pre>
     */
    public void sendDetection(com.indra.srcc.airsrcc.lib.Detections.DetectionDataRequest request,
        io.grpc.stub.StreamObserver<com.indra.srcc.airsrcc.lib.Detections.DetectionDataReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendDetectionMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * The detection service definition.
   * </pre>
   */
  public static final class DetectionBlockingStub extends io.grpc.stub.AbstractStub<DetectionBlockingStub> {
    private DetectionBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DetectionBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DetectionBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DetectionBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Sends a detection
     * </pre>
     */
    public com.indra.srcc.airsrcc.lib.Detections.DetectionDataReply sendDetection(com.indra.srcc.airsrcc.lib.Detections.DetectionDataRequest request) {
      return blockingUnaryCall(
          getChannel(), getSendDetectionMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * The detection service definition.
   * </pre>
   */
  public static final class DetectionFutureStub extends io.grpc.stub.AbstractStub<DetectionFutureStub> {
    private DetectionFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DetectionFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DetectionFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DetectionFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Sends a detection
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.indra.srcc.airsrcc.lib.Detections.DetectionDataReply> sendDetection(
        com.indra.srcc.airsrcc.lib.Detections.DetectionDataRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSendDetectionMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SEND_DETECTION = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final DetectionImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(DetectionImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SEND_DETECTION:
          serviceImpl.sendDetection((com.indra.srcc.airsrcc.lib.Detections.DetectionDataRequest) request,
              (io.grpc.stub.StreamObserver<com.indra.srcc.airsrcc.lib.Detections.DetectionDataReply>) responseObserver);
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

  private static abstract class DetectionBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DetectionBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.indra.srcc.airsrcc.lib.Detections.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Detection");
    }
  }

  private static final class DetectionFileDescriptorSupplier
      extends DetectionBaseDescriptorSupplier {
    DetectionFileDescriptorSupplier() {}
  }

  private static final class DetectionMethodDescriptorSupplier
      extends DetectionBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    DetectionMethodDescriptorSupplier(String methodName) {
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
      synchronized (DetectionGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DetectionFileDescriptorSupplier())
              .addMethod(getSendDetectionMethod())
              .build();
        }
      }
    }
    return result;
  }
}
