package aws.localstack.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;






class DynamoDbServiceTest {

    @Mock
    private DynamoDbClient dynamoDbClient;

    @InjectMocks
    private DynamoDbService dynamoDbService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTable() {
        when(dynamoDbClient.createTable(any(CreateTableRequest.class)))
                .thenReturn(CreateTableResponse.builder().build());

        String result = dynamoDbService.createTable("test-table");

        assertEquals("Table test-table created", result);
        verify(dynamoDbClient, times(1)).createTable(any(CreateTableRequest.class));
    }

    @Test
    void testPutItem() {
        when(dynamoDbClient.putItem(any(PutItemRequest.class)))
                .thenReturn(PutItemResponse.builder().build());

        String result = dynamoDbService.putItem("test-table", "1", "Test Name");

        assertEquals("Item added to test-table", result);
        verify(dynamoDbClient, times(1)).putItem(any(PutItemRequest.class));
    }

    @Test
    void testGetItem() {
        Map<String, AttributeValue> mockItem = Map.of(
                "id", AttributeValue.builder().s("1").build(),
                "name", AttributeValue.builder().s("Test Name").build()
        );

        when(dynamoDbClient.getItem(any(GetItemRequest.class)))
                .thenReturn(GetItemResponse.builder().item(mockItem).build());

        Map<String, AttributeValue> result = dynamoDbService.getItem("test-table", "1");

        assertEquals(mockItem, result);
        verify(dynamoDbClient, times(1)).getItem(any(GetItemRequest.class));
    }
}