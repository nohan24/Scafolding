package database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class Column {
    private String column;
    private String type;
    private String setters;
    private String getters;
    private Path mdl = Paths.get("model/cs.mdl");

    public Column(String column, String type) throws IOException{
        setColumn(column);
        setSetters("set" + capitalize(column));
        setGetters("get" + capitalize(column));
        for(String s : Files.readAllLines(mdl)){
            String[] splitage = s.split("=>");
            if(Pattern.matches("^" + splitage[0] + ".*", type)){
                setType(splitage[1]);            
            }
        }
    }

    String capitalize(String input){
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    String getColumnType(String type){

        return null;
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
    public String getSetters() {
        return setters;
    }
    public void setSetters(String setters) {
        this.setters = setters;
    }
    public String getGetters() {
        return getters;
    }
    public void setGetters(String getters) {
        this.getters = getters;
    }


}
