package dynamodbclient.dynamodbclientwebgui.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RowValue {

    private String rowKey;
    private List<ColumnFamilyValue> columnFamilies;
}