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

	public void insertCsv(List<#className#> data)
	{
        using (NpgsqlConnection connection = new NpgsqlConnection(connectionString))
        {
            connection.Open();
            string sql = "#sqlinsertcsv#";
            foreach (var d in data)
            {
                using (NpgsqlCommand command = new NpgsqlCommand(sql, connection))
                {
					#commandinsertcsv#
                    command.ExecuteNonQuery();
                }
            }
          
            connection.Close();
        }
    }

	public #className# getById(int id) {
		#className# obj = new #className#();
		using (NpgsqlConnection connection = new NpgsqlConnection(connectionString)) {
			connection.Open();
			String sql = "select * from #table# where #id#=" + id;
			using (NpgsqlCommand command = new NpgsqlCommand(sql, connection)) {
				using (NpgsqlDataReader reader = command.ExecuteReader()) {
					while (reader.Read()) {
						obj = new #className#();
						#commandliste#
					}
				}
			}
		}
		return obj;
	}

	public void update() {
		using (NpgsqlConnection connection = new NpgsqlConnection(connectionString)) {
			connection.Open();
			string sql = "UPDATE #table# SET #sqlupdate#";
			using (NpgsqlCommand command = new NpgsqlCommand(sql, connection)) {
				#commandupdate#
				command.ExecuteNonQuery();
			}
			connection.Close();
			}
	}

	public void delete(int id) {
		using (NpgsqlConnection connection = new NpgsqlConnection(connectionString)) {
			connection.Open();
			String sql = "DELETE FROM #table# WHERE #sqldelete#";
			using(NpgsqlCommand command = new NpgsqlCommand(sql, connection)) {
            	#commanddelete#
				command.ExecuteNonQuery();
			}
			connection.Close();
		}
	}

	public List<#className#> getAll() { 
		List<#className#> listA= new List<#className#>();
		using (NpgsqlConnection connection = new NpgsqlConnection(connectionString)) {
			connection.Open();
			string sql = "select * from #table#"; 
			using (NpgsqlCommand command = new NpgsqlCommand(sql, connection)) {
				using (NpgsqlDataReader reader = command.ExecuteReader()) {
					while (reader.Read()) {
						var obj = new #className#();  
						#commandliste#
						listA.Add(obj); 
					} 
				} 
			}
			connection.Close(); 
		} 
		return listA; 
	}

	#foreignkey# 

      
   }
}
