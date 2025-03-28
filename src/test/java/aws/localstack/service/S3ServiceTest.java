package aws.localstack.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.s3.S3Client;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.*;
import java.io.File;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;



class S3ServiceTest {

    @Mock
    private S3Client s3Client;

    @InjectMocks
    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBucket() {
        // Arrange
        String bucketName = "test-bucket";
        CreateBucketRequest request = CreateBucketRequest.builder().bucket(bucketName).build();

        // Act
        String result = s3Service.createBucket(bucketName);

        // Assert
        verify(s3Client, times(1)).createBucket(request);
        assertEquals("Bucket " + bucketName + " created", result);
    }

    @Test
    void testUploadFile() throws Exception {
        // Arrange
        String bucketName = "test-bucket";
        String key = "test-key";
        File file = new File("local_file.txt");
        if (file.exists()) {
            file.delete();
        }
        PutObjectRequest request = PutObjectRequest.builder().bucket(bucketName).key(key).build();

        // Act
        String result = s3Service.uploadFile(bucketName, key);

        // Assert
        verify(s3Client, times(1)).putObject(eq(request), any(RequestBody.class));
        assertTrue(file.exists());
        assertEquals("File uploaded to " + bucketName + " as " + key, result);

        // Cleanup
        file.delete();
    }

    @Test
    void testListObjects() {
        // Arrange
        String bucketName = "test-bucket";
        S3Object s3Object = S3Object.builder().key("test-key").build();
        ListObjectsV2Response response = ListObjectsV2Response.builder()
                .contents(Collections.singletonList(s3Object))
                .build();
        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(response);

        // Act
        List<String> result = s3Service.listObjectsRecursively(bucketName);

        // Assert
        verify(s3Client, times(1)).listObjectsV2(any(ListObjectsV2Request.class));
        assertEquals(1, result.size());
        assertEquals("test-key", result.get(0));
    }
}
