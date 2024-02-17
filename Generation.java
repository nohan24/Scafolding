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
    private static String tableName = null;
    private static String className = null;
    private static boolean action;
    private static String pckg = null;

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

            if(isCrud){
                crudFunction = CrudFunction();
            }

            String modelFile = Files.readString(path);
            modelFile = modelFile.replace("#packageName#", namespace);
            if(isCrud)modelFile = modelFile.replace("#className#", capitalize(tableName));
            else modelFile = modelFile.replace("#className#", capitalize(className));
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

    private static void generateCrud(String className, String packageName, String table, boolean isCrud){
        generateModel(null, packageName, table, true);
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

    private static String CrudFunction(){
        String create = "";
        String update = "";
        String delete = "";
        String list = "";
        String ret = "";

        ret = ret.concat(create.concat("\n\t\t")).concat(update).concat("\n\t\t").concat(delete).concat("\n\t\t").concat(list).concat("\n\t\t");
        return ret;
    }

    public static void main(String[] args) {
        String[] ss;
        for(String arg: args){
            ss = arg.split(":");
            if(ss[0].equals("make")){
                if(ss[1].equals("crud")) action = true;
                continue;
            }
            if(ss[0].equals("table")){
                tableName = ss[1];
                continue;
            }
            if(ss[0].equals("class")){
                className = capitalize(ss[1]);
                continue;
            }
            if(ss[0].equals("namespace")){
                pckg = ss[1];
            }
        }

        if(action){
            if(tableName == null)System.out.println("Veuillez préciser la table dans la base de données !");
            else{
                generateCrud(null, pckg, tableName, true);
            }
        }else{
            if(className == null) System.out.println("Veuillez préciser le nom de la classe !");
            else if(tableName == null)System.out.println("Veuillez préciser la table dans la base de données !");
            else{
                generateModel("Test", pckg, tableName, false);
            }
        }
        
    }

    
}
