package dynamodbclient.dynamodbclientwebgui.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ColumnValue {
    private String column;
    private String value;

    @Override
    public String toString() {
        return "ColumnValue{" +
            "column='" + column + '\'' +
            ", value='" + value + '\'' +
            '}';
    }
}