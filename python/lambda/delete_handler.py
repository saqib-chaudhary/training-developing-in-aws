from db_ops import DbOps

def lambda_handler(event, context):

    db=DbOps()

    return db.delete(
        event["UserId"],
        int(event['NoteId'])
    )
