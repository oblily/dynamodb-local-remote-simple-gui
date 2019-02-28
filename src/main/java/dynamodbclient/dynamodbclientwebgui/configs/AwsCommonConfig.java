package dynamodbclient.dynamodbclientwebgui.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "aws.common")
public class AwsCommonConfig {
    private String region;
    private String profile; 
}