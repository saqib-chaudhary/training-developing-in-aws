from db_ops import DbOps

def lambda_handler(event, context):

    db=DbOps()

    return db.create_update(
        event["UserId"],
        int(event['NoteId']),
        event['Note']
    )

