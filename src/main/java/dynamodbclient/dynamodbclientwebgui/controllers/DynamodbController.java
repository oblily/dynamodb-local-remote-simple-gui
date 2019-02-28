package dynamodbclient.dynamodbclientwebgui.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import dynamodbclient.dynamodbclientwebgui.services.DynamodbService;
import dynamodbclient.dynamodbclientwebgui.utils.ConnectionType;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.TableDescription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class DynamodbController {

    @Autowired
    private DynamodbService dynamodbService;

    @RequestMapping("/names")
    public    
    @ResponseBody
    List<String> names() {
        return dynamodbService.getTablenames();
    }

    @RequestMapping("/describe/{table}")
    public
    @ResponseBody
    TableDescription definition(@PathVariable String table) {
        return dynamodbService.getTableDescription(table);
    }

    @RequestMapping("/scan/{table}/items")
    public
    @ResponseBody
    List<Map<String, Object>> items(@PathVariable String table) {
        return dynamodbService.getTableItems(table);
    }

    @RequestMapping("/connection-type")
    public
    @ResponseBody
    ConnectionType connectionType() {
        return dynamodbService.getConnectionType();
    }
}