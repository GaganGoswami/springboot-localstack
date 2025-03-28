package aws.localstack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class DynamoDbService {

    @Autowired
    private DynamoDbClient dynamoDbClient;

    // @PostConstruct
    // public void init() {
    //     // Create a table
    //     dynamoDbClient.createTable(CreateTableRequest.builder()
    //             .tableName("my-table")
    //             .keySchema(KeySchemaElement.builder().attributeName("id").keyType(KeyType.HASH).build())
    //             .attributeDefinitions(AttributeDefinition.builder().attributeName("id").attributeType(ScalarAttributeType.S).build())
    //             .provisionedThroughput(ProvisionedThroughput.builder().readCapacityUnits(5L).writeCapacityUnits(5L).build())
    //             .build());

    //     // Wait for table to be active (simple delay for LocalStack)
    //     try {
    //         Thread.sleep(2000); // LocalStack is fast, but add a small delay
    //     } catch (InterruptedException e) {
    //         e.printStackTrace();
    //     }

    //     // Put an item
    //     Map<String, AttributeValue> item = new HashMap<>();
    //     item.put("id", AttributeValue.builder().s("3").build());
    //     item.put("name", AttributeValue.builder().s("Gagan").build());
    //     dynamoDbClient.putItem(PutItemRequest.builder().tableName("my-table").item(item).build());

    //     // Get the item
    //     Map<String, AttributeValue> key = new HashMap<>();
    //     key.put("id", AttributeValue.builder().s("3").build());
    //     GetItemResponse response = dynamoDbClient.getItem(GetItemRequest.builder().tableName("my-table").key(key).build());
    //     System.out.println(response.item());
    // }

    public String createTable(String tableName) {
        dynamoDbClient.createTable(CreateTableRequest.builder()
                .tableName(tableName)
                .keySchema(KeySchemaElement.builder().attributeName("id").keyType(KeyType.HASH).build())
                .attributeDefinitions(AttributeDefinition.builder().attributeName("id").attributeType(ScalarAttributeType.S).build())
                .provisionedThroughput(ProvisionedThroughput.builder().readCapacityUnits(5L).writeCapacityUnits(5L).build())
                .build());
        try {
            Thread.sleep(2000); // Wait for table creation
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Table " + tableName + " created";
    }

    public String putItem(String tableName, String id, String name) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.builder().s(id).build());
        item.put("name", AttributeValue.builder().s(name).build());
        dynamoDbClient.putItem(PutItemRequest.builder().tableName(tableName).item(item).build());
        return "Item added to " + tableName;
    }

    public Map<String, AttributeValue> getItem(String tableName, String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", AttributeValue.builder().s(id).build());
        GetItemResponse response = dynamoDbClient.getItem(GetItemRequest.builder().tableName(tableName).key(key).build());
        return response.item();
    }
}