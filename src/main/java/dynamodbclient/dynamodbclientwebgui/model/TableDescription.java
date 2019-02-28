package dynamodbclient.dynamodbclientwebgui.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TableDescription {

    private String tableName;
    private List<String> columnFamilies;

    public TableDescription(String tableName) {
        this.tableName = tableName;
        this.columnFamilies = null;

    }

    @Override
    public String toString() {
        return "TableDescription{" +
            "tableName='" + tableName + '\'' +
            ", columnFamilies=" + columnFamilies +
            '}';
    }
}