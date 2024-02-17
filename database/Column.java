package database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class Column {
    private String column;
    private String type;
    private String getset;
    private Path mdl = Paths.get("model/cs.mdl");

    public Column(String column, String type) throws IOException{
        setColumn(column);
        setGetset("public string ".concat(capitalize(column).concat(" { get; set; }")));
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
    public String getGetset() {
        return getset;
    }
    public void setGetset(String getset) {
        this.getset = getset;
    }

}
