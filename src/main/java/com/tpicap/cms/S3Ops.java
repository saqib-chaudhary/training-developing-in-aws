package com.tpicap.cms;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketResponse;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.waiters.S3AsyncWaiter;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.FileUpload;
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;
import software.amazon.awssdk.transfer.s3.progress.LoggingTransferListener;

public class S3Ops {

    // https://aws.amazon.com/blogs/developer/introducing-crt-based-s3-client-and-the-s3-transfer-manager-in-the-aws-sdk-for-java-2-x/

    private S3AsyncClient  s3 = null;

    public S3Ops() {
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create("tpicap");
        Region region = Region.US_EAST_2;
        s3 = S3AsyncClient .builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();

    }

    public void listBuckets() {
        ListBucketsRequest listBucketsRequest = ListBucketsRequest
                .builder()
                .build();
        CompletableFuture<ListBucketsResponse> result = s3.listBuckets(listBucketsRequest);
        result.join().buckets().stream().forEach(x -> System.out.println(x.name()));
    }

    public void createBucket(String bucketName) {
        CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                .bucket(bucketName)
                .build();

        s3.createBucket(bucketRequest);

        S3AsyncWaiter s3Waiter = s3.waiter();
        HeadBucketRequest bucketRequestWait = HeadBucketRequest.builder()
                .bucket(bucketName)
                .build();

        CompletableFuture<WaiterResponse<HeadBucketResponse>> result = 
            s3Waiter.waitUntilBucketExists(bucketRequestWait);
        result.join().matched().response().ifPresent(System.out::println);
        System.out.println(bucketName + " is ready");

    }

    public void deleteAllObjectsInBucket(String bucket) {

        // To delete a bucket, all the objects in the bucket must be deleted first.
        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                .bucket(bucket)
                .build();
        CompletableFuture<ListObjectsV2Response> result;
        ListObjectsV2Response listObjectsV2Response ;

        do {
            result = s3.listObjectsV2(listObjectsV2Request);
            listObjectsV2Response = result.join();
            List<S3Object> contents = listObjectsV2Response.contents();
            for (S3Object s3Object : contents) {
                DeleteObjectRequest request = DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(s3Object.key())
                        .build();
                s3.deleteObject(request);
            }
        } while (listObjectsV2Response.isTruncated());
        // snippet-end:[s3.java2.s3_bucket_deletion.delete_objects]

    }

    public void deleteBucket(String bucketName) {
        DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder().bucket(bucketName).build();
        s3.deleteBucket(deleteBucketRequest);

        S3AsyncWaiter s3Waiter = s3.waiter();
        HeadBucketRequest bucketRequestWait = HeadBucketRequest.builder()
                .bucket(bucketName)
                .build();

        WaiterResponse<HeadBucketResponse> waiterResponse = s3Waiter.waitUntilBucketNotExists(bucketRequestWait).join();
        waiterResponse.matched().response().ifPresent(System.out::println);
        System.out.println(bucketName + " is deleted");
    }

    public void uploadFile(String bucketName, String key, Path file) {
        // transfer manager does it all for you .....
        // https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/transfer/s3/S3TransferManager.html

        S3TransferManager transferManager = S3TransferManager.builder()
                .s3Client(s3)
                .build();

        UploadFileRequest uploadFileRequest = UploadFileRequest.builder()
                .putObjectRequest(req -> req.bucket(bucketName).key(key))
                .addTransferListener(LoggingTransferListener.create())
                .source(file)
                .build();
        FileUpload upload = transferManager.uploadFile(uploadFileRequest);
        upload.completionFuture().join();

    }

}
