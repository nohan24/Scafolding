package database;

import java.io.IOException;


public class Column {
    private String column;
    private String type;
    private String getset;
    private boolean isPk;
    private int nullable;
    private String defaultValue;
    private boolean isFk;
    private String value;
    private String fk_table;

    public Column(String column, String type, int nullable, boolean pk, String defaultv) throws IOException{
        setColumn(column);
        setGetset("public "+ type +" ".concat(capitalize(column).concat(" { get; set; }")));
        setType(type);
        setNullable(nullable);
        setPk(pk);
        setDefaultValue(defaultv);
    }

    String capitalize(String input){
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public boolean isPk() {
        return isPk;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getFk_table() {
        return fk_table;
    }

    public void setFk_table(String fk_table) {
        this.fk_table = fk_table;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setPk(boolean isPk) {
        this.isPk = isPk;
    }
    public int getNullable() {
        return nullable;
    }

    public void setNullable(int nullable) {
        this.nullable = nullable;
    }
    public boolean isFk() {
        return isFk;
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public void setFk(boolean isFk) {
        this.isFk = isFk;
    }

    public String getColumn() {
        return column;
    }
    public void setColumn(String column) {
        this.column = column;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }  
    public String getGetset() {
        return getset;
    }
    public void setGetset(String getset) {
        this.getset = getset;
    }

}
