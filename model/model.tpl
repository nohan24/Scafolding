using Npgsql;
using System.Data;
using System.Collections.Generic;

namespace #packageName#{

   public class #className# {

      List<String> cols = new List<String>
      {
        #cols#
      };

      public #className#(){}
      #getset#

      	public void insert() {
		string connectionString = "Host=localhost;Username=postgres;Password=root;Database=scafolding";
		using (NpgsqlConnection connection = new NpgsqlConnection(connectionString)) {
			connection.Open();
			string sql = "insert into employe(nom ,date_naissance ) values(@nom ,@date_naissance )";
			using (NpgsqlCommand command = new NpgsqlCommand(sql, connection)) {
				command.Parameters.AddWithValue("@nom", this.Nom);
				command.Parameters.AddWithValue("@date_naissance", this.Date_naissance);

				command.ExecuteNonQuery();
			}
			connection.Close();
		}
	}

	public void insertCsv(List<Employe> data)
	{
        string connectionString = "Host=localhost;Username=postgres;Password=root;Database=scafolding";
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
		String connectionString = "Host=localhost;Username=postgres;Password=root;Database=scafolding";
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
		string connectionString = "Host=localhost;Username=postgres;Password=root;Database=scafolding";
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
		String connectionString = "Host=localhost;Username=postgres;Password=root;Database=scafolding";
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
		string connectionString = "Host=localhost;Username=postgres;Password=root;Database=scafolding"; 
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
