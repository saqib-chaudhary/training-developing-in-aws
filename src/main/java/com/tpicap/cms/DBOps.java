package com.tpicap.cms;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;

public class DBOps implements AutoCloseable {

        private DynamoDbClient dynamoDbClient;
        private String tableName = null;

        public DBOps(AppConfig appConfig) {
                dynamoDbClient = DynamoDbClient.builder()
                                .region(appConfig.getAwsRegion())
                                .build();
                tableName = appConfig.getDynamoDBTable();
        }

        public DynamoDbEnhancedClient enhancedClient() {
                return DynamoDbEnhancedClient.builder()
                                .dynamoDbClient(dynamoDbClient)
                                .build();
        }

        public Note getItem(String userName, Integer noteId) {
                DynamoDbTable<Note> table = enhancedClient()
                                .table(tableName,
                                                TableSchema.fromBean(Note.class));

                Key key = Key.builder()
                                .partitionValue(userName).sortValue(noteId)
                                .build();

                return table.getItem(
                                (GetItemEnhancedRequest.Builder requestBuilder) -> requestBuilder.key(key));
        }

        public Note addItem(String userName, Integer noteId, String noteText) {
                DynamoDbTable<Note> table = enhancedClient()
                                .table(tableName,
                                                TableSchema.fromBean(Note.class));

                Note note = new Note();
                note.setNoteId(noteId);
                note.setUserId(userName);
                note.setNote(noteText);

                table.updateItem(note);

                return note;
        }

        public void deleteItem(String userName, Integer noteId) {
                DynamoDbTable<Note> table = enhancedClient()
                                .table(tableName,
                                                TableSchema.fromBean(Note.class));
                table.deleteItem(Key.builder()
                                .partitionValue(userName).sortValue(noteId)
                                .build());
        }

        public List<Note> findItems(String userName) {
                DynamoDbTable<Note> table = enhancedClient()
                                .table(tableName,
                                                TableSchema.fromBean(Note.class));
                QueryConditional queryConditional = QueryConditional
                                .keyEqualTo(
                                                Key.builder().partitionValue(userName).build());
                return table.query(queryConditional)
                                .items()
                                .stream()
                                .sorted(Comparator.comparing((Note n) -> n.getNoteId()))
                                .collect(Collectors.toList());
        }

        public List<Note> findItems(String userName, String text) {
                // https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/examples-dynamodb-enhanced.html
                DynamoDbTable<Note> table = enhancedClient()
                                .table(tableName,
                                                TableSchema.fromBean(Note.class));
                AttributeValue textAtt = AttributeValue.builder()
                                .s(text)
                                .build();
                Expression expression = Expression.builder()
                                .expression("contains (Note, :txt)")
                                .expressionValues(Map.of(":txt", textAtt))
                                .build();
                QueryConditional queryConditional = QueryConditional
                                .keyEqualTo(
                                                Key.builder().partitionValue(userName).build());
                return table.query(rb -> rb.queryConditional(queryConditional).filterExpression(expression))
                                .items()
                                .stream()
                                .sorted(Comparator.comparing((Note n) -> n.getNoteId()))
                                .collect(Collectors.toList());

        }

        public void close() {
                if (dynamoDbClient != null)
                        dynamoDbClient.close();
        }

}
