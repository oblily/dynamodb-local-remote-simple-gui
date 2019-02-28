package dynamodbclient.dynamodbclientwebgui.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "aws.dynamodb")
public class AwsDynamodbConfig {
    private String endpoint; 
}