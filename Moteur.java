import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class Moteur {
    private static final String URL = "jdbc:postgresql://localhost:5432/scafolding";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    private static String capitalize(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
   
    public static void generateModel(String lang, String packageName, String className, String tableName){
        Path path = Paths.get("model/model.tpl");
        Path l = Paths.get("model/" + lang + ".mdl");
        List<String> need = new ArrayList<>();
        need.add("package");
        String file = null;

        try {

            file = Files.readString(path);
            if(tableName == null){
                PrintWriter writer = new PrintWriter(new FileWriter(className + "." + lang));

                try {
                    for(String s : Files.readAllLines(l)){
                        String[] splitage = s.split("=>");
                        if(need.contains(splitage[0])){
                            file = file.replace("#"+splitage[0]+"#", splitage[1]);
                        }
                    }
                    file = file.replace("#packageName#", packageName);
                    file = file.replace("#className#", className);
                     file = file.replace("#attributs#", "");
                    file = file.replace("#getters#", "");
                     file = file.replace("#setters#", "");
                    System.out.println(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                writer.println(file);
                writer.close();
            }else{
                Connection connection = null;
                try {
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(URL, USER, PASSWORD);
                    DatabaseMetaData metadata = connection.getMetaData();
                    if(tableName.equals("*")){

                        ResultSet tables = metadata.getTables(null, null, null, new String[]{"TABLE"});
                        while(tables.next()){
                            file = Files.readString(path);
                            System.out.println(tables.getString(3));
                            ResultSet rs = metadata.getColumns(null, null, tables.getString(3), null);
                            String setters = "";
                            String getters = "";
                            String attributs = "";
                            
                            PrintWriter writer = new PrintWriter(new FileWriter(capitalize(tables.getString(3)) + "." + lang));
                            while(rs.next()){
                                String columnName = rs.getString("COLUMN_NAME");
                                String dataType = rs.getString("TYPE_NAME");
                                for(String s : Files.readAllLines(l)){
                                    String[] splitage = s.split("=>");
                                    if(Pattern.matches("^"+splitage[0]+".*",dataType)){
                                        attributs = "   " + attributs + splitage[1] +" "+columnName+";\n";
                                        getters = getters + "   public "+splitage[1]+" get"+capitalize(columnName)+"(){\n"+
                                        "\t return this."+columnName+";\n    }\n";
                                        setters = setters + "   public void set"+capitalize(columnName)+"("+splitage[1]+" "+columnName+"){\n"+
                                                "\t this."+columnName+"="+columnName+";\n   }\n";
                                    }
                                }
                            }
                            file = file.replace("#attributs#", attributs);
                            file = file.replace("#getters#", getters);
                            file = file.replace("#setters#", setters);
                            for(String s : Files.readAllLines(l)){
                                String[] splitage = s.split("=>");
                                if(need.contains(splitage[0])){
                                    file = file.replace("#"+splitage[0]+"#", splitage[1]);
                                }
                            }
                            file = file.replace("#packageName#", packageName);
                            file = file.replace("#className#", tables.getString(3));
                            System.out.println(file);
                            writer.println(file);
                            writer.close();
                        }
                    }else{
                        PrintWriter writer = new PrintWriter(new FileWriter(className + "." + lang));

                        ResultSet rs = metadata.getColumns(null, null, tableName, null);
                        String setters = "";
                        String getters = "";
                        String attributs = "";
                        while(rs.next()){
                            
                            String columnName = rs.getString("COLUMN_NAME");
                            String dataType = rs.getString("TYPE_NAME");
                            for(String s : Files.readAllLines(l)){
                                String[] splitage = s.split("=>");
                                if(Pattern.matches("^"+splitage[0]+".*",dataType)){
                                    attributs = attributs + "\t"+splitage[1] +" "+columnName+";\n";
                                    getters = getters + "\tpublic "+splitage[1]+" get"+capitalize(columnName)+"(){\n"+
                                            "\treturn this."+columnName+";\n}\n";
                                    setters = setters + "\tpublic void set"+capitalize(columnName)+"("+splitage[1]+" "+columnName+"){\n"+
                                            "\tthis."+columnName+"="+columnName+";\n}\n";
                                }
                            }
                        }
                        file = file.replace("#attributs#", attributs);
                        file = file.replace("#getters#", getters);
                        file = file.replace("#setters#", setters);
                        for(String s : Files.readAllLines(l)){
                            String[] splitage = s.split("=>");
                            if(need.contains(splitage[0])){
                                file = file.replace("#"+splitage[0]+"#", splitage[1]);
                            }
                        }
                        file = file.replace("#packageName#", packageName);
                        file = file.replace("#className#", className);
                        writer.println(file);
                        writer.close();
                    }

                    connection.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void generateView(){}

    public static void generateControllerJava(String lang, String packageName, String className) {
        Path path = Paths.get("controller/controller.tpl");
        List<String> need = new ArrayList<>();
        need.add("package");
        String file = null;
        try {
            file = Files.readString(path);
            PrintWriter writer = new PrintWriter(new FileWriter(className + "Controller." + lang));
            file = file.replace("#packageName#", packageName);
            file = file.replace("#className#", className);
            System.out.println(file);
            writer.println(file);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void generateControllerDotnet(String lang, String packageName, String className) {
        if(className.equals("*")){
            Connection connection = null;
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                DatabaseMetaData metadata = connection.getMetaData();
                ResultSet tables = metadata.getTables(null, null, null, new String[]{"TABLE"});
                while(tables.next()){
                    Path path = Paths.get("controller/dotnet.tpl");
                    List<String> need = new ArrayList<>();
                    need.add("package");
                    String file = null;
                    try {
                        file = Files.readString(path);
                        PrintWriter writer = new PrintWriter(new FileWriter(tables.getString(3) + "Controller." + lang));
                        file = file.replace("#packageName#", packageName);
                        file = file.replace("#className#", tables.getString(3));
                        System.out.println(file);
                        writer.println(file);
                        writer.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }else{
            Path path = Paths.get("controller/dotnet.tpl");
            List<String> need = new ArrayList<>();
            need.add("package");
            String file = null;
            try {
                file = Files.readString(path);
                PrintWriter writer = new PrintWriter(new FileWriter(className + "Controller." + lang));
                file = file.replace("#packageName#", packageName);
                file = file.replace("#className#", className);
                System.out.println(file);
                writer.println(file);
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void generateRepository(String lang, String packageName, String className) {
        Path path = Paths.get("controller/repository.tpl");
        List<String> need = new ArrayList<>();
        need.add("package");
        String file = null;
        try {
            file = Files.readString(path);
            PrintWriter writer = new PrintWriter(new FileWriter(className + "Repository." + lang));
            file = file.replace("#packageName#", packageName);
            file = file.replace("#className#", className);
            System.out.println(file);
            writer.println(file);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void generateService(String lang, String packageName, String className) {
        Path path = Paths.get("controller/service.tpl");
        List<String> need = new ArrayList<>();
        need.add("package");
        String file = null;
        try {
            file = Files.readString(path);
            PrintWriter writer = new PrintWriter(new FileWriter(className + "Service." + lang));
            file = file.replace("#packageName#", packageName);
            file = file.replace("#className#", className);
            System.out.println(file);
            writer.println(file);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void generateControllerSpring(String lang, String packageName, String className) {
        if(className.equals("*")){
            Connection connection = null;
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                DatabaseMetaData metadata = connection.getMetaData();
                ResultSet tables = metadata.getTables(null, null, null, new String[]{"TABLE"});
                while(tables.next()){
                    generateControllerJava(lang,packageName,tables.getString(3));
                    generateRepository(lang,packageName,tables.getString(3));
                    generateService(lang,packageName,tables.getString(3));
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }else{
            generateControllerJava(lang,packageName,className);
            generateRepository(lang,packageName,className);
            generateService(lang,packageName,className);
        }
    }
    public static void generateController(String lang, String packageName, String className) {
        if(lang.equals("java")){
            generateControllerSpring(lang,packageName,className);
        }else if(lang.equals("cs")){
            generateControllerDotnet(lang,packageName,className);
        }
    }

    public static void main(String[] args) {
        String tableName = null;
        String className = args[0];
        String packageName = null;
        String type = null;
        String extension = null;
        String[] ss;
        for(String arg : args){
            ss = arg.split(":");
            if(ss[0].equals("type")){
                type = ss[1];
            }
            if(ss[0].equals("extension")){
                extension = ss[1];
            }
            if(ss[0].equals("table")){
                tableName = ss[1];
            }
            if(ss[0].equals("package")){
                packageName = ss[1];
                System.out.println(packageName);
            }
        }
        if(type == null){
            generateModel(extension, packageName, className, tableName);
        }else if(type.contains("controller")){
            generateController(extension, packageName, className);
        }
    }
}
