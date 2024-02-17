import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import database.Column;
import database.Database;

public class Generation {
    private static Database database = new Database();

    private static void generateModel(String className, String packageName, String table){
        // template anle modele
        Path path = Paths.get("model/model.tpl");
        String namespace = getProjectName().concat(".Models");
        if(!packageName.equals(null))namespace.concat(".".concat(packageName));
        String fileName = packageName != null ? "Models/".concat(packageName).concat("/").concat(capitalize(className)).replace('.', '/') : "Models/".concat(capitalize(className));
        File directory = new File((fileName + ".cs").replace('/', '\\'));
        // jerena raha efa misy
        if(directory.exists()){
            System.out.println(namespace.concat("." + className + ".cs").concat(" existe déjà !"));
        }else{
            try {
                // mamorona anle dossier anle package
                File dir = new File("Models/".concat(packageName).replace('.', '/'));
                dir.mkdirs();
                PrintWriter writer = new PrintWriter(new FileWriter(fileName.concat(".cs")));
                String attribut = "";
                List<Column> columns = database.getTableColumns(table);
                // mampiditra anle getter setter sy ny attributs
                for(Column c : columns){
                    attribut += "\t\t".concat(c.getGetset()).concat("\n");
                }

                // micree fonction crud raha précisena ny table 

                // generena ny controller mifanaraka raha make:crud

                // manamboatra ny package controller raha misy

                // generena ny view apres controller

                // manamboatra package view raha misy
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    } 

    private static void generateCrud(){

    }

    static String capitalize(String input){
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    private static String getProjectName(){
        String currentDirectory = System.getProperty("user.dir");
        currentDirectory = currentDirectory.replace('\\', '/');
        String [] splt = currentDirectory.split("/");
        return splt[splt.length - 1];
    }

    private static void generateView(){

    }

    private static void generateController(){

    }

    public static void main(String[] args) {
        generateModel("Test", "ff", "test");
        generateController();
        generateCrud();
        generateView();
    }

    
}
