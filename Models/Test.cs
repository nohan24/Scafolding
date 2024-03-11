using Npgsql;
using System.Data;
using System.Collections.Generic;

namespace Scafolding.Models{

   public class Test {

      List<String> cols = new List<String>
      {
        "id",
		"nom",
		"argent",
		"id_dept",
		
      };

      private string connectionString = "Host=localhost;Username=postgres;Password=root;Database=scafolding";

      public Test(){}
      
		public int Id { get; set; }		
		public string Nom { get; set; }		
		public double Argent { get; set; }		
		public int Id_dept { get; set; }		



      public void insert() {
         using (NpgsqlConnection connection = new NpgsqlConnection(connectionString)) {
            connection.Open();
            string sql = "insert into (nom,argent,id_dept) values(@nom,@argent,@id_dept)";
            using (NpgsqlCommand command = new NpgsqlCommand(sql, connection)) {
                command.Parameters.AddWithValue("@nom", this.Nom);
				command.Parameters.AddWithValue("@argent", this.Argent);
				command.Parameters.AddWithValue("@id_dept", this.Id_dept);
				
               command.ExecuteNonQuery();
            }
            connection.Close();
         }
	   }

	public void insertCsv(List<Test> data)
	{
        using (NpgsqlConnection connection = new NpgsqlConnection(connectionString))
        {
            connection.Open();
            string sql = "insert into test(id ,nom ,argent ,id_dept ) values(@id ,@nom ,@argent ,@id_dept )";
            foreach (var d in data)
            {
                using (NpgsqlCommand command = new NpgsqlCommand(sql, connection))
                {
					command.Parameters.AddWithValue("@id", this.Id);
					command.Parameters.AddWithValue("@nom", this.Nom);
					command.Parameters.AddWithValue("@argent", this.Argent);
					command.Parameters.AddWithValue("@id_dept", this.Id_dept);
					
                    command.ExecuteNonQuery();
                }
            }
          
            connection.Close();
        }
    }

	public Test getById(int id) {
		Test obj = new Test();
		using (NpgsqlConnection connection = new NpgsqlConnection(connectionString)) {
			connection.Open();
			String sql = "select * from test where id=" + id;
			using (NpgsqlCommand command = new NpgsqlCommand(sql, connection)) {
				using (NpgsqlDataReader reader = command.ExecuteReader()) {
					while (reader.Read()) {
						obj = new Test();
						obj.Id = reader.GetInt32("id"); 
						obj.Nom = reader.GetString("nom"); 
						obj.Argent = reader.GetDouble("argent"); 
						obj.Id_dept = reader.GetInt32("id_dept"); 
						
					}
				}
			}
		}
		return obj;
	}

	public void update() {
		using (NpgsqlConnection connection = new NpgsqlConnection(connectionString)) {
			connection.Open();
			string sql = "UPDATE test SET nom=@nom ,argent=@argent ,id_dept=@id_dept  WHERE id=@id";
			using (NpgsqlCommand command = new NpgsqlCommand(sql, connection)) {
				command.Parameters.AddWithValue("@id", this.Id);
					command.Parameters.AddWithValue("@nom", this.Nom);
					command.Parameters.AddWithValue("@argent", this.Argent);
					command.Parameters.AddWithValue("@id_dept", this.Id_dept);
					
				command.ExecuteNonQuery();
			}
			connection.Close();
			}
	}

	public void delete(int id) {
		using (NpgsqlConnection connection = new NpgsqlConnection(connectionString)) {
			connection.Open();
			String sql = "DELETE FROM test WHERE id=@id";
			using(NpgsqlCommand command = new NpgsqlCommand(sql, connection)) {
            	command.Parameters.AddWithValue("@id", id);
				command.ExecuteNonQuery();
			}
			connection.Close();
		}
	}

	public List<Test> getAll() { 
		List<Test> listA= new List<Test>();
		using (NpgsqlConnection connection = new NpgsqlConnection(connectionString)) {
			connection.Open();
			string sql = "select * from test"; 
			using (NpgsqlCommand command = new NpgsqlCommand(sql, connection)) {
				using (NpgsqlDataReader reader = command.ExecuteReader()) {
					while (reader.Read()) {
						var obj = new Test();  
						obj.Id = reader.GetInt32("id"); 
						obj.Nom = reader.GetString("nom"); 
						obj.Argent = reader.GetDouble("argent"); 
						obj.Id_dept = reader.GetInt32("id_dept"); 
						
						listA.Add(obj); 
					} 
				} 
			}
			connection.Close(); 
		} 
		return listA; 
	}

	public Dictionary<string, string> fkDept() {
			Dictionary<string, string> listA = new Dictionary<string, string>();
			string connectionString = "Host=localhost;Username=postgres;Password=root;Database=scafolding";
			using (NpgsqlConnection connection = new NpgsqlConnection(connectionString)) {
				connection.Open();
				string sql = "select * from dept";
				using (NpgsqlCommand command = new NpgsqlCommand(sql, connection)) {
					using (NpgsqlDataReader reader = command.ExecuteReader()) {
						while (reader.Read()) {
							string str = "";
							str = str + reader.GetString("nom");
							listA.Add(reader.GetInt32("id").ToString(), str);
						}
					}
				}
			}
			return listA;
		}
 

      
   }
}

