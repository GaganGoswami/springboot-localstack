package aws.localstack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Configuration
public class AwsConfig {

    private static final String LOCALSTACK_ENDPOINT = "http://localhost:4566";
    private static final AwsBasicCredentials DUMMY_CREDENTIALS = AwsBasicCredentials.create("test", "test");

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create(LOCALSTACK_ENDPOINT))
                .credentialsProvider(StaticCredentialsProvider.create(DUMMY_CREDENTIALS))
                .region(Region.US_EAST_1)
                .build();
    }

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .endpointOverride(URI.create(LOCALSTACK_ENDPOINT))
                .credentialsProvider(StaticCredentialsProvider.create(DUMMY_CREDENTIALS))
                .region(Region.US_EAST_1)
                .build();
    }

    // @Bean
    // public SqsClient sqsClient() {
    //     return SqsClient.builder()
    //             .endpointOverride(URI.create(LOCALSTACK_ENDPOINT))
    //             .credentialsProvider(StaticCredentialsProvider.create(DUMMY_CREDENTIALS))
    //             .region(Region.US_EAST_1)
    //             .build();
    // }

    // @Bean
    // public LambdaClient lambdaClient() {
    //     return LambdaClient.builder()
    //             .endpointOverride(URI.create(LOCALSTACK_ENDPOINT))
    //             .credentialsProvider(StaticCredentialsProvider.create(DUMMY_CREDENTIALS))
    //             .region(Region.US_EAST_1)
    //             .build();
    // }
}