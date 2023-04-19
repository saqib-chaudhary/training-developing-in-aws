package com.tpicap.cms;




/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        S3Ops s3 = new S3Ops();
        s3.listBuckets();

    }
}
