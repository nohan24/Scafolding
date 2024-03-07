import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import database.Column;
import database.Database;
import java.nio.file.Files;

public class Generation {
    private static Database database = new Database();
    private static String tableName = null;
    private static boolean action;
    private static String pckg = null;
    static Path path = Paths.get("model/model.tpl");
    static Path cs = Paths.get("model/cs.mdl");

    private static List<Column> generateModel(String packageName, String table, boolean isCrud){

   
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
            String crudFunction = "";

            List<Column> columns = database.getTableColumns(table, cs);
            String cols = "";
            for(Column c : columns){
                attribut += "\n\t\t".concat(c.getGetset()).concat("\t\t");
                cols += "\"" + c.getColumn() + "\",\n\t\t";
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
            modelFile = modelFile.replace("#cols#", cols);

            writer.println(modelFile);
            writer.close();
            return columns;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }    

        return null;
    } 

    private static void generateCrud(String packageName, String table, boolean isCrud){
        List<Column> columns = generateModel(packageName, table, true);
        generateController(table, columns);
        generateView(table, columns);
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

    private static void generateView(String table, List<Column> columns){
        File dir = new File("Views/".concat(capitalize(table)));
        dir.mkdirs();
        try {
            generateViewListe(table, columns);
            generateViewCreate(table, columns);
            generateViewUpdate(table, columns);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void generateViewListe(String table, List<Column> columns) throws SQLException{
        Path path = Paths.get("view/liste.tpl");
        String fileName = "Views/".concat(capitalize(table)).concat("/Liste");
        try {

            PrintWriter writer = new PrintWriter(new FileWriter(fileName.concat(".cshtml")));
            String headerColumns = "";
            String rowColumns = "";
            
            String idModel = "Id";
    
            for(Column c : columns){
                if (!c.isPk()) {
                    headerColumns += "\n".concat("<th>").concat(capitalize(c.getColumn())).concat("</th>").concat("\t\t");
                }else{
                    idModel = c.getColumn();
                }
            }
            headerColumns += "\n <th>Actions</th> \t\t";

            for(Column c : columns){
                if(!c.isPk()){
                    rowColumns += "\t\t\t\t\n<td>";
                    rowColumns += "\t\t\t\t\n@item."+ capitalize(c.getColumn()) +"\n";
                    rowColumns += "\t\t\t\t\n</td>";
                }
            }
            
            rowColumns += "\n<td>\n";
            rowColumns += "\t\t<a asp-action=\"Update\" asp-controller=\""+ capitalize(table) +"\" asp-route-id=\"@item."+capitalize(idModel)+"\">Modifier</a> \n";
            rowColumns += "\t\t<a asp-action=\"Delete\" asp-controller=\""+ capitalize(table) +"\" asp-route-id=\"@item."+capitalize(idModel)+"\">Supprimer</a>\n";
            rowColumns += "</td>\n";

            String viewFile = Files.readString(path);
            viewFile = viewFile.replace("#modelName#", capitalize(table));
            viewFile = viewFile.replace("#HeaderColumns#", headerColumns);
            viewFile = viewFile.replace("#RowColumns#", rowColumns);
            writer.println(viewFile);
            writer.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    private static void generateViewCreate(String table, List<Column> columns){
        Path path = Paths.get("view/create.tpl");
        String fileName = "Views/".concat(capitalize(table)).concat("/Create");
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(fileName.concat(".cshtml")));
            String champs = "";

            for(Column c : columns){
                if (!c.isPk()) {
                    if(c.isFk()){
                        champs += "\n".concat("<div class=\"form-group\">").concat("\t\t");
                        champs += "\n".concat("<label asp-for=\"").concat(capitalize(c.getColumn())).concat("\" class=\"control-label\">").concat(capitalize(c.getColumn())).concat(" : ").concat("</label> \t\t\t");
                        champs += "\n".concat("<select asp-for=\"").concat(capitalize(c.getColumn())).concat("\" class=\"form-control\" >").concat("\t\t\t");

                        champs += "\n".concat("@foreach (var item in ViewData[\""+ c.getFk_table() +"\"] as Dictionary<string, string>)\n{");
                        champs += "\n".concat("<option value=\"@item.Key\">@item.Value</option>");
                        champs += "\n".concat("}");

                        champs += "\n".concat("</select>").concat("\t\t\t");
                        champs += "\n".concat("<span asp-validation-for=\"").concat(capitalize(c.getColumn())).concat("\" class=\"text-danger\"></span>").concat("\t\t\t");
                        champs += "\n".concat("</div><br/>").concat("\t\t");
                    }else{
                        champs += "\n".concat("<div class=\"form-group\">").concat("\t\t");
                        champs += "\n".concat("<label asp-for=\"").concat(capitalize(c.getColumn())).concat("\" class=\"control-label\">").concat(capitalize(c.getColumn())).concat(" : ").concat("</label> \t\t\t");
                        champs += "\n".concat("<input asp-for=\"").concat(capitalize(c.getColumn())).concat("\" class=\"form-control\" />").concat("\t\t\t");
                        champs += "\n".concat("<span asp-validation-for=\"").concat(capitalize(c.getColumn())).concat("\" class=\"text-danger\"></span>").concat("\t\t\t");
                        champs += "\n".concat("</div><br/>").concat("\t\t");
                    }
                  
                }
            }

            String viewFile = Files.readString(path);
            viewFile = viewFile.replace("#modelName#", capitalize(table));
            viewFile = viewFile.replace("#champs#", champs);
            writer.println(viewFile);
            writer.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void generateViewUpdate(String table, List<Column> columns) throws SQLException{

        Path path = Paths.get("view/update.tpl");
        String fileName = "Views/".concat(capitalize(table)).concat("/Update");
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(fileName.concat(".cshtml")));
            String champs = "";

            for(Column c : columns){
                if (!c.isPk()) {
                    if(c.isFk()){
                        champs += "\n".concat("<div class=\"form-group\">").concat("\t\t");
                        champs += "\n".concat("<label asp-for=\"").concat(capitalize(c.getColumn())).concat("\" class=\"control-label\">").concat(capitalize(c.getColumn())).concat(" : ").concat("</label> \t\t\t");
                        champs += "\n".concat("<select asp-for=\"").concat(capitalize(c.getColumn())).concat("\" class=\"form-control\" >").concat("\t\t\t");
                        
                        champs += "\n".concat("@foreach (var item in ViewData[\""+ c.getFk_table() +"\"] as Dictionary<string, string>)\n{");
                        champs += "\n".concat("<option value=\"@item.Key\">@item.Value</option>");
                        champs += "\n".concat("}");

                        champs += "\n".concat("</select>").concat("\t\t\t");
                        champs += "\n".concat("<span asp-validation-for=\"").concat(capitalize(c.getColumn())).concat("\" class=\"text-danger\"></span>").concat("\t\t\t");
                        champs += "\n".concat("</div><br/>").concat("\t\t");
                    }else{
                        champs += "\n".concat("<div class=\"form-group\">").concat("\t\t");
                        champs += "\n".concat("<label asp-for=\"").concat(capitalize(c.getColumn())).concat("\" class=\"control-label\">").concat(capitalize(c.getColumn())).concat(" : ").concat("</label> \t\t\t");
                        champs += "\n".concat("<input value=\"@Model."+ capitalize(c.getColumn()) +"\" asp-for=\"").concat(capitalize(c.getColumn())).concat("\" class=\"form-control\" />").concat("\t\t\t");
                        champs += "\n".concat("<span asp-validation-for=\"").concat(capitalize(c.getColumn())).concat("\" class=\"text-danger\"></span>").concat("\t\t\t");
                        champs += "\n".concat("</div><br/>").concat("\t\t");
                    }
                  
                }
            }

            String viewFile = Files.readString(path);
            viewFile = viewFile.replace("#modelName#", capitalize(table));
            viewFile = viewFile.replace("#champs#", champs);
            viewFile = viewFile.replace("#id#", capitalize(database.getTableId(table)));
            writer.println(viewFile);
            writer.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static void generateController(String table, List<Column> columns){
        Path path = Paths.get("controller/cs.tpl");
        String namespace = getProjectName().concat(".Controllers");
        String fileName = "Controllers/".concat(capitalize(table));
        String fk_func = "";

        for (Column c : columns) {
            if(c.isFk()){
                fk_func = fk_func + "ViewData[\"" + c.getFk_table() + "\"]" + " = obj.fk"+ capitalize(c.getFk_table()) +"(); \n\t\t\t";
            }
        }

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(fileName.concat("Controller.cs")));
            String controllerFile = Files.readString(path);
            controllerFile = controllerFile.replace("#modelName#", capitalize(table));
            controllerFile = controllerFile.replace("#project#", getProjectName());
            controllerFile = controllerFile.replace("#namespace#", namespace);
            controllerFile = controllerFile.replace("#fk#", fk_func);

            writer.println(controllerFile);
            writer.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String CrudFunction(String table, List<Column> columns){
        String delete = "";
        String update = "";
        String find = "";
        String fk_function = "";
        for(Column c : columns){
            if(c.isPk()){
                delete = delete(table, c.getColumn());
                update = update(table, c.getColumn(), columns);
                find = find(table, c.getColumn(), columns);
                break;
            }
        }

        try {
            HashMap<String, List<String>> map = database.getFks(columns, cs);
            for(String key : map.keySet()){

                String fk_generator = "public Dictionary<string, string> fk"+ capitalize(key) +"() {\n" +
                    "\t\t\tDictionary<string, string> listA = new Dictionary<string, string>();\n" +
                    "\t\t\tstring connectionString = \"Host=localhost;Username=postgres;Password=root;Database=scafolding\";\n" +
                    "\t\t\tusing (NpgsqlConnection connection = new NpgsqlConnection(connectionString)) {\n" +
                    "\t\t\t\tconnection.Open();\n" +
                    "\t\t\t\tstring sql = \"select * from "+ key +"\";\n" +
                    "\t\t\t\tusing (NpgsqlCommand command = new NpgsqlCommand(sql, connection)) {\n" +
                    "\t\t\t\t\tusing (NpgsqlDataReader reader = command.ExecuteReader()) {\n" +
                    "\t\t\t\t\t\twhile (reader.Read()) {\n" +
                    "\t\t\t\t\t\t\tstring str = \"\";\n";
                    for(String s : map.get(key)){
                        fk_generator = fk_generator + "\t\t\t\t\t\t\tstr = str + reader.GetString(\""+ s +"\");\n";
                    }
                    fk_generator = fk_generator +
                    "\t\t\t\t\t\t\tlistA.Add(reader.GetInt32(\""+ database.getTableId(key) +"\").ToString(), str);\n" +
                    "\t\t\t\t\t\t}\n" +
                    "\t\t\t\t\t}\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t}\n" +
                    "\t\t\treturn listA;\n" +
                    "\t\t}\n";

                    fk_function = fk_function + fk_generator;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String list = all(table, columns);
        String create = create(table, columns);
        String ret = "";

        ret = ret.concat(create.concat("\n\t\t")).concat(find).concat("\n\t\t").concat(update).concat("\n\t\t").concat(delete).concat("\n\t\t").concat(list).concat("\n\t\t").concat(fk_function).concat("\n\t\t");
        return ret;
    }

    static String find(String table, String id, List<Column> columns){
        String getByIdCode = "public "+capitalize(table)+" getById(int id) {\n" +
                     "\t\t\t"+capitalize(table)+" obj = new "+ capitalize(table) +"();\n" +
                     "\t\t\tString connectionString = \"Host=localhost;Username=postgres;Password=root;Database=scafolding\";\n" +
                     "\t\t\tusing (NpgsqlConnection connection = new NpgsqlConnection(connectionString)) {\n" +
                     "\t\t\t\tconnection.Open();\n" +
                     "\t\t\t\tString sql = \"select * from "+table+" where "+ id +"=\" + id;\n" +
                     "\t\t\t\tusing (NpgsqlCommand command = new NpgsqlCommand(sql, connection)) {\n" +
                     "\t\t\t\t\tusing (NpgsqlDataReader reader = command.ExecuteReader()) {\n" +
                     "\t\t\t\t\t\twhile (reader.Read()) {\n" +
                     "\t\t\t\t\t\t\tobj = new "+capitalize(table)+"();\n";
                     var s = "";
                     for(Column c : columns){
                        if(c.getType().equals("string")){
                            s = "String";
                        }else if(c.getType().equals("int")){
                            s = "Int32";
                        }else if(c.getType().equals("double")){
                            s = "Double";
                        }else if(c.getType().equals("DateTime")){
                            s = "DateTime";
                        }
                        getByIdCode = getByIdCode + 
                        "\t\t\t\t\t\t\tobj."+ capitalize(c.getColumn()) +" = reader.Get"+ s +"(\""+ c.getColumn() +"\");  \n";
                     }
                     getByIdCode = getByIdCode + 
                     "\t\t\t\t\t\t}\n" +
                     "\t\t\t\t\t}\n" +
                     "\t\t\t\t}\n" +
                     "\t\t\t}\n" +
                     "\t\t\treturn obj;\n" +
                     "\t\t}\n";


        return getByIdCode;
    }

    static String all(String table, List<Column> columns){
        String ret = "";
        ret = ret + "public List<"+capitalize(capitalize(table))+"> getAll() { \n"
        .concat("\t\t\tList<"+capitalize(capitalize(table))+"> listA= new List<"+capitalize(capitalize(table))+">();\n")
        .concat("\t\t\tstring connectionString = \"Host=localhost;Username=postgres;Password=root;Database=scafolding\"; \n")
        .concat("\t\t\tusing (NpgsqlConnection connection = new NpgsqlConnection(connectionString)) {\n")
        .concat("\t\t\t\tconnection.Open();\n")
        .concat("\t\t\t\tstring sql = \"select * from "+ table +"\"; \n")
        .concat("\t\t\t\tusing (NpgsqlCommand command = new NpgsqlCommand(sql, connection)) {\n")
        .concat("\t\t\t\t\tusing (NpgsqlDataReader reader = command.ExecuteReader()) {\n")
        .concat("\t\t\t\t\t\twhile (reader.Read()) {\n")
        .concat("\t\t\t\t\t\t\tvar obj = new "+ capitalize(table) +"();  \n");

        var s = "";
        for(Column c : columns){
            if(c.getType().equals("string")){
                s = "String";
            }else if(c.getType().equals("int")){
                s = "Int32";
            }else if(c.getType().equals("double")){
                s = "Double";
            }else if(c.getType().equals("DateTime")){
                s = "DateTime";
            }

            ret = ret + "\t\t\t\t\t\t\tobj." + capitalize(c.getColumn()) + " = reader.Get"+s+"(\""+c.getColumn()+"\"); \n";
        }

        ret = ret + "\t\t\t\t\t\t\tlistA.Add(obj); \n \t\t\t\t\t\t} \n \t\t\t\t\t} \n \t\t\t\t}\n\t\t\t\tconnection.Close(); \n \t\t\t} \n\t\t\treturn listA; \n \t\t} \n\n ";
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

    static String update(String table, String id, List<Column> columns){
        var cols = "";
        var acols = "";
        var update = "";
        for(Column c : columns){
            if(!c.isPk()){
                cols = cols + c.getColumn() + " ,";
                acols = acols + "@" + c.getColumn() + " ,";
                update = update + c.getColumn() + "=@" + c.getColumn()+ " ,";
            }
        }

        cols = cols.substring(0, cols.length() - 1);

        update = update.substring(0, update.length() - 1);
        acols = acols.substring(0, acols.length() - 1);

        String updateCode = "public void update() {\n" +
                    "\t\t\tstring connectionString = \"Host=localhost;Username=postgres;Password=root;Database=scafolding\";\n" +
                    "\t\t\tusing (NpgsqlConnection connection = new NpgsqlConnection(connectionString)) {\n" +
                    "\t\t\t\tconnection.Open();\n" +
                    "\t\t\t\tstring sql = \"UPDATE " + table + 
                    " SET ";
                    updateCode = updateCode + update;
                    updateCode = updateCode + "  WHERE "+ id +"=@id\";\n" +
                    "\t\t\t\tusing (NpgsqlCommand command = new NpgsqlCommand(sql, connection)) {\n";
                    for(Column c : columns){
                        updateCode = updateCode + "\t\t\t\t\tcommand.Parameters.AddWithValue(\"@"+c.getColumn()+"\", this."+ capitalize(c.getColumn()) +");\n";
                    }
                    updateCode = updateCode +
                    "\t\t\t\t\tcommand.ExecuteNonQuery();\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t\tconnection.Close();\n" +
                    "\t\t\t\t}\n" +
                    "\t\t}\n";

        return updateCode;
    }

    static String create(String table, List<Column> columns){
        var cols = "";
        var acols = "";
        for(Column c : columns){
            if(!c.isPk()){
                cols = cols + c.getColumn() + " ,";
                acols = acols + "@" + c.getColumn() + " ,";
            }
        }

        cols = cols.substring(0, cols.length() - 1);
        acols = acols.substring(0, acols.length() - 1);
        String insertCode = "\t\tpublic void insert() {\n" +
                    "\t\t\tstring connectionString = \"Host=localhost;Username=postgres;Password=root;Database=scafolding\";\n" +
                    "\t\t\tusing (NpgsqlConnection connection = new NpgsqlConnection(connectionString)) {\n" +
                    "\t\t\t\tconnection.Open();\n" +
                    "\t\t\t\tstring sql = \"insert into "+table+"("+ cols +") values("+ acols +")\";\n" +
                    "\t\t\t\tusing (NpgsqlCommand command = new NpgsqlCommand(sql, connection)) {\n";
                    for(Column c : columns){
                        if(!c.isPk()){
                            insertCode = insertCode + "\t\t\t\t\tcommand.Parameters.AddWithValue(\"@"+c.getColumn()+"\", this."+ capitalize(c.getColumn()) +");\n";
                        }
                    }
                insertCode = insertCode +
                    "\t\t\t\t\tcommand.ExecuteNonQuery();\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t\tconnection.Close();\n" +
                    "\t\t\t}\n" +
                    "\t\t}\n";
        return insertCode;
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
