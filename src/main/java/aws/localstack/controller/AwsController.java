package aws.localstack.controller;

import aws.localstack.service.DynamoDbService;
import aws.localstack.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/aws")
public class AwsController {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private DynamoDbService dynamoDbService;

    @PostMapping("/s3/bucket/{bucketName}")
    public String createBucket(@PathVariable String bucketName) {
        return s3Service.createBucket(bucketName);
    }

    @PostMapping("/s3/upload/{bucketName}/{key}")
    public String uploadFile(@PathVariable String bucketName, @PathVariable String key) {
        return s3Service.uploadFile(bucketName, key);
    }

    @GetMapping("/s3/list/{bucketName}")
    public List<String> listObjects(@PathVariable String bucketName) {
        return s3Service.listObjects(bucketName);
    }

    @PostMapping("/dynamodb/table/{tableName}")
    public String createTable(@PathVariable String tableName) {
        return dynamoDbService.createTable(tableName);
    }

    @PostMapping("/dynamodb/put/{tableName}")
    public String putItem(@PathVariable String tableName, @RequestParam String id, @RequestParam String name) {
        return dynamoDbService.putItem(tableName, id, name);
    }

    @GetMapping("/dynamodb/get/{tableName}/{id}")
    public Map<String, AttributeValue> getItem(@PathVariable String tableName, @PathVariable String id) {
        return dynamoDbService.getItem(tableName, id);
    }
}