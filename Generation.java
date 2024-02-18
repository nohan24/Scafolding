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
    private static boolean action;
    private static String pckg = null;

    private static void generateModel(String packageName, String table, boolean isCrud){

        Path path = Paths.get("model/model.tpl");
        String namespace = getProjectName().concat(".Models");
        if(packageName != null)namespace.concat(".".concat(packageName));
        String fileName = packageName != null ? "Models/".concat(packageName).concat("/").concat(capitalize(table)).replace('.', '/') : "Models/".concat(capitalize(table));
        try {
            if(packageName != null){
                File dir = new File("Models/".concat(packageName).replace('.', '/'));
                dir.mkdirs();
            }
            PrintWriter writer = new PrintWriter(new FileWriter(fileName.concat(".cs")));
            String attribut = "";
            String crudFunction = "dfdf";

            List<Column> columns = database.getTableColumns(table);
            for(Column c : columns){
                attribut += "\n\t\t".concat(c.getGetset()).concat("\t\t");
            }

            if(isCrud){
                crudFunction = CrudFunction(table, columns);
            }

            String modelFile = Files.readString(path);
            modelFile = modelFile.replace("#packageName#", namespace);
            if(isCrud)modelFile = modelFile.replace("#className#", capitalize(tableName));
            else modelFile = modelFile.replace("#className#", capitalize(table));
            modelFile = modelFile.replace("#getset#", attribut.concat("\n\n"));
            modelFile = modelFile.replace("#crud#", crudFunction);
            writer.println(modelFile);
            writer.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }    
    } 

    private static void generateCrud(String packageName, String table, boolean isCrud){
        generateModel(packageName, table, true);
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

    private static void generateViewListe(String table, String packageName){

        Path path = Paths.get("view/liste.tpl");
        String namespace = getProjectName().concat(".Views");
        if(packageName != null)namespace.concat(".".concat(packageName));
        String fileName = packageName != null ? "Views/".concat(packageName).concat("/").concat(capitalize(table)).replace('.', '/') : "Views/".concat(capitalize(table));
        try {
            if(packageName != null){
                File dir = new File("Views/".concat(packageName).replace('.', '/'));
                dir.mkdirs();
            }
            PrintWriter writer = new PrintWriter(new FileWriter("liste".concat(fileName).concat(".cshtml")));
            String headerColumns = "";
            String rowColumns = "";

            List<Column> columns = database.getTableColumns(table);
            String idModel = "Id";
            for(Column c : columns){
                if (!c.isPk()) {
                    headerColumns += "\n".concat("<th>").concat(c.getColumn()).concat("</th>").concat("\t\t");
                    //rowColumns += "\n".concat("<td>").concat("@item.").concat(c.getValue()).concat("</td>").concat("\t\t");
                }else{
                    idModel = c.getValue();
                }
            }
            headerColumns += "\n <th>Actions</th> \t\t";
            rowColumns += "\n<td>\n";
            rowColumns += "\t\t<a asp-action=\"Edit\" asp-route-id=\"@item."+idModel+"\">Modifier</a> \n";
            rowColumns += "\t\t<a asp-action=\"Delete\" asp-route-id=\"@item."+idModel+"\"\">Supprimer</a>\n";
            rowColumns += "</td>\n";

            String viewFile = Files.readString(path);
            viewFile = viewFile.replace("#modelName#", table);
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
    
    private static void generateViewCreate(String table, String packageName){

        Path path = Paths.get("view/create.tpl");
        String namespace = getProjectName().concat(".Views");
        if(packageName != null)namespace.concat(".".concat(packageName));
        String fileName = packageName != null ? "Views/".concat(packageName).concat("/").concat(capitalize(table)).replace('.', '/') : "Views/".concat(capitalize(table));
        try {
            if(packageName != null){
                File dir = new File("Views/".concat(packageName).replace('.', '/'));
                dir.mkdirs();
            }
            PrintWriter writer = new PrintWriter(new FileWriter("insert".concat(fileName).concat(".cshtml")));
            String champs = "";

            List<Column> columns = database.getTableColumns(table);
            for(Column c : columns){
                if (!c.isPk()) {
                    champs += "\n".concat("<div class=\"form-group\">").concat("\t\t");
                    champs += "\n".concat("<label asp-for=\"").concat(c.getColumn()).concat("\" class=\"control-label\">").concat(c.getColumn()).concat(" : \t\t\t");
                    champs += "\n".concat("<input asp-for=\"").concat(c.getColumn()).concat("\" class=\"form-control\" />").concat("\t\t\t");
                    champs += "\n".concat("<span asp-validation-for=\"").concat(c.getColumn()).concat("\" class=\"text-danger\"></span>").concat("\t\t\t");
                    champs += "\n".concat("</div>").concat("\t\t");
                }
            }

            String viewFile = Files.readString(path);
            viewFile = viewFile.replace("#modelName#", table);
            viewFile = viewFile.replace("#champs#", champs);
            writer.println(viewFile);
            writer.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } 

    }
    
    private static void generateViewUpdate(String table, String packageName){

        Path path = Paths.get("view/update.tpl");
        String namespace = getProjectName().concat(".Views");
        if(packageName != null)namespace.concat(".".concat(packageName));
        String fileName = packageName != null ? "Views/".concat(packageName).concat("/").concat(capitalize(table)).replace('.', '/') : "Views/".concat(capitalize(table));
        try {
            if(packageName != null){
                File dir = new File("Views/".concat(packageName).replace('.', '/'));
                dir.mkdirs();
            }
            PrintWriter writer = new PrintWriter(new FileWriter("update".concat(fileName).concat(".cshtml")));
            String champs = "";

            List<Column> columns = database.getTableColumns(table);
            for(Column c : columns){
                if (!c.isPk()) {
                    champs += "\n".concat("<div class=\"form-group\">").concat("\t\t");
                    champs += "\n".concat("<label asp-for=\"").concat(c.getColumn()).concat("\" class=\"control-label\">").concat(c.getColumn()).concat(" : \t\t\t");
                    champs += "\n".concat("<input asp-for=\"").concat(c.getColumn()).concat("\" class=\"form-control\" />").concat("\t\t\t");
                    champs += "\n".concat("<span asp-validation-for=\"").concat(c.getColumn()).concat("\" class=\"text-danger\"></span>").concat("\t\t\t");
                    champs += "\n".concat("</div>").concat("\t\t");
                }else{
                    champs += "\n".concat("<input type=\"hidden\" asp-for=\"Id\" />").concat("\t\t");
                }
            }

            String viewFile = Files.readString(path);
            viewFile = viewFile.replace("#modelName#", table);
            viewFile = viewFile.replace("#champs#", champs);
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

    private static String CrudFunction(String table, List<Column> columns){
        String delete = "";
        for(Column c : columns){
            if(c.isPk()){
                delete = delete(table, c.getColumn());
                break;
            }
        }
        String list = all(table, columns);
        String update = "";
        String create = "";
        String ret = "";

        ret = ret.concat(create.concat("\n\t\t")).concat(update).concat("\n\t\t").concat(delete).concat("\n\t\t").concat(list).concat("\n\t\t");
        return ret;
    }

    static String all(String table, List<Column> columns){
        String ret = "";
        ret = ret + "public List<"+capitalize(capitalize(table))+"> getAll() { \n"
        .concat("\t\t\tList<"+capitalize(capitalize(table))+"> listA= new List<"+capitalize(capitalize(table))+">();\n")
        .concat("\t\t\tString connectionString = \"Host=localhost;Username=postgres;Password=root;Database=scafolding; \n")
        .concat("\t\t\tusing (NpgsqlConnection connection = new NpgsqlConnection(connectionString)) {\n")
        .concat("\t\t\t\tconnection.Open();\n")
        .concat("\t\t\t\tString sql = \"select * from "+ table +"\"; \n")
        .concat("\t\t\t\tusing (NpgsqlCommand command = new NpgsqlCommand(sql, connection)) {\n")
        .concat("\t\t\t\t\tusing (NpgsqlDataReader reader = command.ExecuteReader()) {\n")
        .concat("\t\t\t\t\t\twhile (reader.Read()) {\n")
        .concat("\t\t\t\t\t\t\tvar obj = new "+ capitalize(table) +"();  \n");

        var s = "";
        for(Column c : columns){
            if(c.getType() == "string"){
                s = "String";
            }else if(c.getType() == "int"){
                s = "Int32";
            }else if(c.getType() == "double"){
                s = "Double";
            }else if(c.getType() == "DateTime"){
                s = "DateTime";
            }

            ret = ret + "\t\t\t\t\t\t\tobj." + capitalize(c.getColumn()) + " = reader.Get"+s+"("+c.getColumn()+"); \n";
        }

        ret = ret + "\t\t\t\t\t\t\tlist.add(obj); \n \t\t\t\t\t\t} \n \t\t\t\t\t} \n \t\t\t\t}\n\t\t\t\tconnection.Close(); \n \t\t\t} \n\t\t\treturn listA; \n \t\t} \n\n ";
        return ret;
    }

    static String delete(String table, String id){
        String deleteCode = "public void delete(int id) {\n" +
                    "\t\t\tString connectionString = \"Host=localhost;Username=postgres;Password=root;Database=scafolding\";\n" +
                    "\t\t\tusing (NpgsqlConnection connection = new NpgsqlConnection(connectionString)) {\n" +
                    "\t\t\t\tconnection.Open();\n" +
                    "\t\t\t\tString sql = \"DELETE FROM "+ table +" WHERE "+ id +"=@id\";\n" +
                    "\t\t\t\tusing(NpgsqlCommand command = new NpgsqlCommand(sql, connection)) {\n" +
                    "\t\t\t\t\tcommand.Parameters.AddWithValue(\"@id\", id);\n" +
                    "\t\t\t\t\tcommand.ExecuteNonQuery();\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t\tconnection.Close();\n" +
                    "\t\t\t}\n" +
                    "\t\t}\n";
        return deleteCode;
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
            if(ss[0].equals("namespace")){
                pckg = ss[1];
            }
        }

        if(action){
            if(tableName == null)System.out.println("Veuillez préciser la table dans la base de données !");
            else{
                generateCrud(pckg, tableName, true);
            }
        }else{

            if(tableName == null)System.out.println("Veuillez préciser la table dans la base de données !");
            else{
                generateModel(pckg, tableName, false);
            }
        }
        
    }

    
}
