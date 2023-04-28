import boto3
from app_config import AppConfig
from boto3.dynamodb.conditions import Key, Attr

class DbOps:

    def __init__(self) -> None:
        config = AppConfig()
        session = None
        profile = config.get_aws_profile()

        if (profile=="default"):
            session = boto3.Session()
        else:
            session = boto3.Session(profile_name=profile)    

        dynamodb = session.resource('dynamodb')
        self.table = dynamodb.Table(config.get_db_table())

    def delete(self, user:str, note_id:int) -> int:
        self.table.delete_item(
            Key={
                'UserId': user,
                'NoteId': note_id
            }
        )        
        return note_id    
    
    def create_update(self, user:str, note_id:int, note: str) -> int:
        self.table.put_item(
            Item={
                'UserId': user,
                'NoteId': note_id,
                'Note': note
            }
        )
        return note_id  
    
    def list(self, user:str) -> list:
        records = self.table.query(
                KeyConditionExpression=Key("UserId").eq(user)
            )            
        return records['Items']
    
    def search(self, user:str, note_text:str) -> list:
        records = self.table.query(
                KeyConditionExpression=Key("UserId").eq(user),
                FilterExpression=Attr("Note").contains(note_text)
            )            
        return records['Items']