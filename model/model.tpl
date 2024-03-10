using Npgsql;
using System.Data;
using System.Collections.Generic;

namespace #packageName#{

   public class #className# {

      List<String> cols = new List<String>
      {
        #cols#
      };

      private string connectionString = "Host=localhost;Username=postgres;Password=root;Database=scafolding";

      public #className#(){}
      #getset#

      public void insert() {
         using (NpgsqlConnection connection = new NpgsqlConnection(connectionString)) {
            connection.Open();
            string sql = "#sqlinsert#";
            using (NpgsqlCommand command = new NpgsqlCommand(sql, connection)) {
               #commandinsert#
               command.ExecuteNonQuery();
            }
            connection.Close();
         }
	   }

	public void insertCsv(List<Employe> data)
	{
        using (NpgsqlConnection connection = new NpgsqlConnection(connectionString))
        {
            connection.Open();
            string sql = "insert into employe(id, nom ,date_naissance ) values(@id, @nom ,@date_naissance)";
            foreach (var d in data)
            {
                using (NpgsqlCommand command = new NpgsqlCommand(sql, connection))
                {
                    command.Parameters.AddWithValue("@nom", d.Nom);
                    command.Parameters.AddWithValue("@date_naissance", d.Date_naissance);
					command.Parameters.AddWithValue("@id", d.Id);
                    command.ExecuteNonQuery();
                }
            }
          
            connection.Close();
        }
    }

	public Employe getById(int id) {
		Employe obj = new Employe();
		using (NpgsqlConnection connection = new NpgsqlConnection(connectionString)) {
			connection.Open();
			String sql = "select * from employe where id=" + id;
			using (NpgsqlCommand command = new NpgsqlCommand(sql, connection)) {
				using (NpgsqlDataReader reader = command.ExecuteReader()) {
					while (reader.Read()) {
						obj = new Employe();
						obj.Id = reader.GetInt32("id");  
						obj.Nom = reader.GetString("nom");  
						obj.Date_naissance = reader.GetDateTime("date_naissance");  
					}
				}
			}
		}
		return obj;
	}

	public void update() {
		using (NpgsqlConnection connection = new NpgsqlConnection(connectionString)) {
			connection.Open();
			string sql = "UPDATE employe SET nom=@nom ,date_naissance=@date_naissance   WHERE id=@id";
			using (NpgsqlCommand command = new NpgsqlCommand(sql, connection)) {
				command.Parameters.AddWithValue("@id", this.Id);
				command.Parameters.AddWithValue("@nom", this.Nom);
				command.Parameters.AddWithValue("@date_naissance", this.Date_naissance);
				command.ExecuteNonQuery();
			}
			connection.Close();
			}
	}

	public void delete(int id) {
		using (NpgsqlConnection connection = new NpgsqlConnection(connectionString)) {
			connection.Open();
			String sql = "DELETE FROM employe WHERE id=@id";
			using(NpgsqlCommand command = new NpgsqlCommand(sql, connection)) {
				command.Parameters.AddWithValue("@id", id);
				command.ExecuteNonQuery();
			}
			connection.Close();
		}
	}

	public List<Employe> getAll() { 
		List<Employe> listA= new List<Employe>();
		using (NpgsqlConnection connection = new NpgsqlConnection(connectionString)) {
			connection.Open();
			string sql = "select * from employe"; 
			using (NpgsqlCommand command = new NpgsqlCommand(sql, connection)) {
				using (NpgsqlDataReader reader = command.ExecuteReader()) {
					while (reader.Read()) {
						var obj = new Employe();  
						obj.Id = reader.GetInt32("id"); 
						obj.Nom = reader.GetString("nom"); 
						obj.Date_naissance = reader.GetDateTime("date_naissance"); 
						listA.Add(obj); 
						} 
					} 
				}
			connection.Close(); 
			} 
		return listA; 
		} 

      
   }
}
