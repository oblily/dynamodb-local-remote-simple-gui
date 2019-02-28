package dynamodbclient.dynamodbclientwebgui.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ColumnFamilyValue {
    private String familyName;
    private List<ColumnValue> columnValues;

    @Override
    public String toString() {
        return "ColumnFamilyValue{" +
            "familyName='" + familyName + '\'' +
            ", columnValues=" + columnValues +
            '}';
    }
}