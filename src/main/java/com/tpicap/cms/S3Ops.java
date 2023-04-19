package com.tpicap.cms;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketResponse;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

public class S3Ops {

    private S3Client s3 = null;

    public S3Ops() {
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create("tpicap");
        Region region = Region.US_EAST_2;
        s3 = S3Client.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();

    }

    public void listBuckets() {
        ListBucketsRequest listBucketsRequest = ListBucketsRequest
                .builder()
                .build();
        ListBucketsResponse listBucketsResponse = s3.listBuckets(listBucketsRequest);
        listBucketsResponse.buckets().stream().forEach(x -> System.out.println(x.name()));
    }

}
