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
import java.nio.file.Files;

public class Generation {
    private static Database database = new Database();

    private static void generateModel(String className, String packageName, String table, boolean isCrud){

        Path path = Paths.get("model/model.tpl");
        String namespace = getProjectName().concat(".Models");
        if(packageName != null)namespace.concat(".".concat(packageName));
        String fileName = packageName != null ? "Models/".concat(packageName).concat("/").concat(capitalize(className)).replace('.', '/') : "Models/".concat(capitalize(className));
        try {
            if(packageName != null){
                File dir = new File("Models/".concat(packageName).replace('.', '/'));
                dir.mkdirs();
            }
            PrintWriter writer = new PrintWriter(new FileWriter(fileName.concat(".cs")));
            String attribut = "";
            String crudFunction = "";

            List<Column> columns = database.getTableColumns(table);
            for(Column c : columns){
                attribut += "\n".concat(c.getGetset()).concat("\t\t");
            }

            String modelFile = Files.readString(path);
            modelFile = modelFile.replace("#packageName#", namespace);
            modelFile = modelFile.replace("#className#", capitalize(className));
            modelFile = modelFile.replace("#getset#", attribut);
            modelFile = modelFile.replace("#crud#", crudFunction);
            writer.println(modelFile);
            writer.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
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

    private static void generateViewListe(String modelName, String packageName){

        Path path = Paths.get("view/liste.tpl");
        String namespace = getProjectName().concat(".Views");
        if(packageName != null)namespace.concat(".".concat(packageName));
        String fileName = packageName != null ? "Views/".concat(packageName).concat("/").concat(capitalize(modelName)).replace('.', '/') : "Views/".concat(capitalize(modelName));
        try {
            if(packageName != null){
                File dir = new File("Views/".concat(packageName).replace('.', '/'));
                dir.mkdirs();
            }
            PrintWriter writer = new PrintWriter(new FileWriter("liste".concat(fileName).concat(".cshtml")));
            String headerColumns = "";
            String rowColumns = ""; // MBOLA mila atao

            List<Column> columns = database.getTableColumns(table);
            for(Column c : columns){
                headerColumns += "\n".concat("<th>").concat(c.getName()).concat("</th>").concat("\t\t");
            }
            headerColumns += "\n <th>Actions</th> \t\t";

            String viewFile = Files.readString(path);
            viewFile = viewFile.replace("#modelName#", modelName);
            viewFile = viewFile.replace("#HeaderColumns#", headerColumns);
            viewFile = viewFile.replace("#RowColumns#", rowColumns);
            writer.println(viewFile);
            writer.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } 

    }

    private static void generateController(){

    }

    public static void main(String[] args) {
        generateModel("Test", "test", null, false);
        generateController();
        generateCrud();
        generateView();
    }

    
}
