package aws.localstack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {

    @Autowired
    private S3Client s3Client;

    public String createBucket(String bucketName) {
        s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
        return "Bucket " + bucketName + " created";
    }

    public String uploadFile(String bucketName, String key) {
        File file = new File("local_file.txt");
        if (!file.exists()) {
            try {
                java.nio.file.Files.write(file.toPath(), "Hello, S3!".getBytes());
            } catch (Exception e) {
                return "Error creating file: " + e.getMessage();
            }
        }
        s3Client.putObject(
                PutObjectRequest.builder().bucket(bucketName).key(key).build(),
                RequestBody.fromFile(file)
        );
        return "File uploaded to " + bucketName + " as " + key;
    }

    public List<String> listObjectsRecursively(String bucketName) {
        String continuationToken = null;
        List<String> allKeys = new java.util.ArrayList<>();

        do {
            ListObjectsV2Request.Builder requestBuilder = ListObjectsV2Request.builder().bucket(bucketName);
            if (continuationToken != null) {
                requestBuilder.continuationToken(continuationToken);
            }

            ListObjectsV2Response response = s3Client.listObjectsV2(requestBuilder.build());
            response.contents().forEach(s3Object -> System.out.println(s3Object.key()));
            allKeys.addAll(response.contents().stream().map(s3Object -> s3Object.key()).collect(Collectors.toList()));

            continuationToken = response.nextContinuationToken();
        } while (continuationToken != null);

        return allKeys;
    }
}
