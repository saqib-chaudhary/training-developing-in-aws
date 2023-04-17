import boto3
import logging
from botocore.client import ClientError

v_session = boto3.session.Session(region_name="us-east-2")
logging.basicConfig(level=logging.INFO)
s3 = v_session.resource('s3')

def bucket_exists(bucket: object) :
    if (bucket is None):
        return False
    try:
        bucket.meta.client.head_bucket(Bucket=bucket.name)
        return True
    except ClientError:
        return False
        

def create_s3_bucket_cleanup():
    logging.info("creating bucket")
    bucket_name = 'demo-bucket-saqib-0417'
    bucket = None
    try:
        bucket = s3.Bucket(bucket_name)
        
        if (not bucket_exists(bucket)):
            logging.warning("creating bucket")
            bucket.create(
                CreateBucketConfiguration={
                   'LocationConstraint': 'us-east-2' 
                }
            )
            bucket.wait_until_exists()
            
            
        
    finally:
        if (bucket_exists(bucket)):
            bucket.objects.delete()
            bucket.delete()
            bucket.wait_until_not_exists()
            logging.info("cleaned up the bucket")
            
           
    
    
    
create_s3_bucket_cleanup()    
    