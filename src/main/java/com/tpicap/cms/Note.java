package com.tpicap.cms;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class Note {
    private String userId;
    private Integer noteId;
    private String note;

    @DynamoDbPartitionKey()
    @DynamoDbAttribute(value = "UserId")
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute(value = "NoteId")
    public Integer getNoteId() {
        return noteId;
    }
    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
    }

    @DynamoDbAttribute(value = "Note")
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    

    
}
