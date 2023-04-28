from db_ops import DbOps

def lambda_handler(event, context):

    db=DbOps()

    return db.list(
        event["UserId"]
    )

   
