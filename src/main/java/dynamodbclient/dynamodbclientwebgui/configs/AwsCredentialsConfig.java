package dynamodbclient.dynamodbclientwebgui.configs;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Slf4j
@Import(WebAppConfig.class)
@Configuration
public class AwsCredentialsConfig {
    
    @Autowired
    private AwsCommonConfig awsCommonConfig;

    @Autowired
    private AwsDynamodbConfig awsDynamodbConfig;
    //private ProfileCredentialsProvider profileCredentialsProvider;

    @Autowired
    ResourceLoader resourceLoader;

    @Bean
    public DynamoDbClient amazonDynamoDBClient() throws URISyntaxException {
        log.info("amazonDynamoDBClient Start.");
        DynamoDbClient client = null;
        log.info("amazonDynamoDBClient.getEndpoint:{};",awsDynamodbConfig.getEndpoint());   
        if(!awsDynamodbConfig.getEndpoint().isEmpty()) {
            URI uri = new URI(awsDynamodbConfig.getEndpoint());
            log.info("URI:{}",uri);
        
            // client = DynamoDbClient.builder()
            //             .region(Region.AP_NORTHEAST_1)
            //             .endpointOverride(new URI(awsDynamodbConfig.getEndpoint()))
            //             .credentialsProvider(profileCredentialsProvider())
            //             .build();
            client = DynamoDbClient.builder()
                        .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create("test","test")))
                        .region(Region.of(awsCommonConfig.getRegion()))
                        .endpointOverride(new URI(awsDynamodbConfig.getEndpoint()))
                        .build();
        } else {
            client = DynamoDbClient.builder()
                        .region(Region.of(awsCommonConfig.getRegion()))
                        .build();
        }
        return client;              
    }

    // @Bean
    // public ProfileCredentialsProvider profileCredentialsProvider() {
    //     ProfileCredentialsProvider profileCredentialsProvider = ProfileCredentialsProvider.create();
    //     if(!awsCommonConfig.getProfile().isEmpty()) {
    //         log.info("awsCommonConfig.getProfile:{}",awsCommonConfig.getProfile());
    //         profileCredentialsProvider = ProfileCredentialsProvider.builder().profileFile(ProfileFile.builder()
    //                     .content(Paths.get(awsCommonConfig.getProfile())).build())
    //                     .build();
    //     }
    //     return profileCredentialsProvider;
    // }
}