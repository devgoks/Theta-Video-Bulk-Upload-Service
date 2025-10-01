package com.theta.ThetaVideoApiBulkUpload.grpc;

import com.theta.ThetaVideoApiBulkUpload.Service.ThetaVideoService;
import com.theta.ThetaVideoApiBulkUpload.apiModels.CheckVideoUploadResponse;
import com.theta.ThetaVideoApiBulkUpload.apiModels.Video;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@GrpcService
public class BulkUploadGrpcService extends BulkUploadServiceGrpc.BulkUploadServiceImplBase {

    private final ThetaVideoService thetaVideoService;

    public BulkUploadGrpcService(ThetaVideoService thetaVideoService) {
        this.thetaVideoService = thetaVideoService;
    }

    @Override
    public void processBulkUpload(BulkUploadRequest request, StreamObserver<com.theta.ThetaVideoApiBulkUpload.grpc.ProcessedVideoResponse> responseObserver) {
        try {
            List<byte[]> filesBytes = new ArrayList<>();
            for (int i = 0; i < request.getFilesCount(); i++) {
                filesBytes.add(request.getFiles(i).toByteArray());
            }

            List<CheckVideoUploadResponse> checkResponses = thetaVideoService.processBulkUpload(
                    filesBytes,
                    request.getThetaApiKey(),
                    request.getThetaApiSecret()
            );

            com.theta.ThetaVideoApiBulkUpload.apiModels.ProcessedVideoResponse serviceResponse = thetaVideoService.compileProcessedVideoResponse(checkResponses);
            com.theta.ThetaVideoApiBulkUpload.grpc.ProcessedVideoResponse grpcResponse = mapProcessedVideoResponse(serviceResponse);
            responseObserver.onNext(grpcResponse);
            responseObserver.onCompleted();
        } catch (ExecutionException | InterruptedException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void processBulkUploadAsync(BulkUploadRequest request, StreamObserver<com.theta.ThetaVideoApiBulkUpload.grpc.OperationStarted> responseObserver) {
        try {
            List<byte[]> filesBytes = new ArrayList<>();
            for (int i = 0; i < request.getFilesCount(); i++) {
                filesBytes.add(request.getFiles(i).toByteArray());
            }

            thetaVideoService.processBulkUploadAsync(
                    filesBytes,
                    request.getThetaApiKey(),
                    request.getThetaApiSecret(),
                    request.getWebhookUrl()
            );

            com.theta.ThetaVideoApiBulkUpload.grpc.OperationStarted started = com.theta.ThetaVideoApiBulkUpload.grpc.OperationStarted.newBuilder()
                    .setStatus("success")
                    .setMessage("Operation In Progress, You will get the final response on your webhook")
                    .build();
            responseObserver.onNext(started);
            responseObserver.onCompleted();
        } catch (ExecutionException | InterruptedException e) {
            responseObserver.onError(e);
        }
    }

    private com.theta.ThetaVideoApiBulkUpload.grpc.ProcessedVideoResponse mapProcessedVideoResponse(com.theta.ThetaVideoApiBulkUpload.apiModels.ProcessedVideoResponse model) {
        com.theta.ThetaVideoApiBulkUpload.grpc.ProcessedVideoResponse.Builder builder = com.theta.ThetaVideoApiBulkUpload.grpc.ProcessedVideoResponse.newBuilder()
                .setStatus(model.getStatus() == null ? "" : model.getStatus())
                .setMessage(model.getMessage() == null ? "" : model.getMessage());
        if (model.getVideos() != null) {
            for (Video v : model.getVideos()) {
                builder.addData(mapVideo(v));
            }
        }
        return builder.build();
    }

    private com.theta.ThetaVideoApiBulkUpload.grpc.Video mapVideo(Video v) {
        com.theta.ThetaVideoApiBulkUpload.grpc.Video.Builder b = com.theta.ThetaVideoApiBulkUpload.grpc.Video.newBuilder();
        if (v.getId() != null) b.setId(v.getId());
        if (v.getPlaybackUri() != null) b.setPlaybackUri(v.getPlaybackUri());
        if (v.getPlayerUri() != null) b.setPlayerUri(v.getPlayerUri());
        if (v.getUseDrm() != null) b.setUseDrm(v.getUseDrm());
        if (v.getDuration() != null) b.setDuration(v.getDuration());
        if (v.getCreateTime() != null) b.setCreateTime(v.getCreateTime());
        if (v.getUpdateTime() != null) b.setUpdateTime(v.getUpdateTime());
        if (v.getServiceAccountId() != null) b.setServiceAccountId(v.getServiceAccountId());
        if (v.getFileName() != null) b.setFileName(v.getFileName());
        if (v.getState() != null) b.setState(v.getState());
        if (v.getSubState() != null) b.setSubState(v.getSubState());
        if (v.getSourceUploadId() != null) b.setSourceUploadId(v.getSourceUploadId());
        if (v.getSourceUri() != null) b.setSourceUri(v.getSourceUri());
        if (v.getPlaybackPolicy() != null) b.setPlaybackPolicy(v.getPlaybackPolicy());
        b.setProgress(v.getProgress());
        if (v.getError() != null) b.setError(v.getError());
        if (v.getResolution() != null) b.setResolution(v.getResolution());
        if (v.getStartTimeOverride() != null) b.setStartTimeOverride(v.getStartTimeOverride());
        return b.build();
    }
}


