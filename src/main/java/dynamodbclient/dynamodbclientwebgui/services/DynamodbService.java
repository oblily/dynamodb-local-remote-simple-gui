package dynamodbclient.dynamodbclientwebgui.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dynamodbclient.dynamodbclientwebgui.configs.AwsDynamodbConfig;
import dynamodbclient.dynamodbclientwebgui.utils.ConnectionType;
import dynamodbclient.dynamodbclientwebgui.utils.ItemUtil;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.ListTablesRequest;
import software.amazon.awssdk.services.dynamodb.model.ListTablesResponse;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.TableDescription;

@Slf4j
@Service
public class DynamodbService {

    @Autowired
    private DynamoDbClient dynamoDBClient;

    @Autowired
    private AwsDynamodbConfig awsDynamodbConfig;
   
    public ConnectionType getConnectionType() {
        log.info("Endpoint:{}", awsDynamodbConfig.getEndpoint().isEmpty());
        return (awsDynamodbConfig.getEndpoint().isEmpty()) ? ConnectionType.REMOTE : ConnectionType.LOCAL;
    }
    /**
     * Get all the tables name.
     */
    public List<String> getTablenames() {
        List<String> names = null;
        try {
            ListTablesResponse response = dynamoDBClient.listTables(ListTablesRequest.builder().limit(5).build());
            names = response.tableNames();
        } catch (UnsupportedOperationException ex) {
            names = Collections.emptyList();
            log.error(ex.getMessage());
        }
        return names;
    }

    /**
     * Get description for a table.
     */
    public TableDescription getTableDescription(String table) {
        DescribeTableResponse description = null;
        log.info("Getting Description for table:" + table);
        try {
            if (!table.isEmpty()) {
                description = dynamoDBClient.describeTable(
                    DescribeTableRequest.builder()
                    .tableName(table)
                    .build());
            }
        } catch (UnsupportedOperationException ex) {
            log.error(ex.getMessage());
        }
        log.info("table status:" + description.table().tableStatusAsString());
        log.info("table name:" + description.table().tableName());
        return description.table();
    }

    /**
     * Get items for a table.
     */
    public List<Map<String, Object>> getTableItems(String table) {
        log.info("Getting items for " + table);
        List<Map<String, Object>> items = new ArrayList<>();

        ScanRequest scanRequest = ScanRequest.builder()
                                    .tableName(table)
                                    //.attributesToGet(HASH_KEY_NAME)
                                    .build();
        List<Map<String, AttributeValue>> list = dynamoDBClient.scan(scanRequest).items();
        log.info("raw items ", list);
        for (Map<String, AttributeValue> item : list) {

            items.add(ItemUtil.toSimpleMapValue(item));
        }
        return items;
    }

}