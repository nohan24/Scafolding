package database;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Database {
    private String connectionString;
    private String user;
    private String password;

    public String getConnectionString() {
        return connectionString;
    }
    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Connection getConnection(){
        Connection co = null;
        try{
            co = DriverManager.getConnection(connectionString, user, password);
            return co;
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return co;
    }

    public Database(){
        try {
            File fichierXML = new File("db.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(fichierXML);
            Element racine = document.getDocumentElement();
            NodeList nodeList = racine.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String nomElement = element.getNodeName();
                    String contenuElement = element.getTextContent();
                    if(nomElement.equals("connectionString")) setConnectionString(contenuElement);
                    if(nomElement.equals("user")) setUser(contenuElement);
                    if(nomElement.equals("password")) setPassword(contenuElement);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Column> getTableColumns(String table) throws SQLException, IOException{
        List<Column> ret = new ArrayList<>();
        if(table == null)return ret;
        Connection connection = getConnection();
        DatabaseMetaData metadata = connection.getMetaData();
        metadata.getPrimaryKeys(null, null, table);
        ResultSet r = metadata.getPrimaryKeys(null, null, table);
        String pk = "";
        if(r.next()) pk = r.getString("COLUMN_NAME");
        ResultSet rs = metadata.getColumns(null, null, table, null);
        while(rs.next()){
            ret.add(new Column(rs.getString("COLUMN_NAME"), rs.getString("TYPE_NAME"), rs.getInt("NULLABLE")));
        }
        return ret;
    }

    void getForeignKey(){

    }

    public static void main(String[] args) {
        Database d = new Database();
        try {
            d.getTableColumns("test");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
