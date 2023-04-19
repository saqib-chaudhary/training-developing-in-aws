package com.tpicap.cms;

import java.nio.file.Paths;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        S3Ops s3 = new S3Ops();
        s3.listBuckets();
        String bucketName = "saqib-test-55663";

        //s3.createBucket(bucketName);
        s3.listBuckets();
        //  s3.deleteAllObjectsInBucket(bucketName);
        //  s3.deleteBucket(bucketName);
        // s3.listBuckets();
        s3.uploadFile(bucketName, "test", Paths.get("C:\\temp\\1851_3_3279076_1681671656_AWS Course Completion Certificate.pdf"));

    }
}
