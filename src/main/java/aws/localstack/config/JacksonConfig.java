package aws.localstack.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(AttributeValue.class, new JsonSerializer<AttributeValue>() {
            @Override
            public void serialize(AttributeValue attributeValue, com.fasterxml.jackson.core.JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                // Implement serialization logic here
                // Example:
                if (attributeValue.s() != null) {
                    jsonGenerator.writeString(attributeValue.s());
                } else if (attributeValue.n() != null) {
                    jsonGenerator.writeNumber(attributeValue.n());
                } else if (attributeValue.bool() != null) {
                    jsonGenerator.writeBoolean(attributeValue.bool());
                } else {
                    jsonGenerator.writeNull();
                }
            }
        });
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.registerModule(module);
        return mapper;
    }

}