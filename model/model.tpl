using Npgsql;
using System.Data;
using System.Collections.Generic;
using System.Reflection;


namespace #packageName#{

   public class #className# {

      private List<String> Cols = new List<String>
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

private string getTypeAttribut(string col)
	{
        Type type = typeof(Employe);
        PropertyInfo[] properties = type.GetProperties();
		foreach (PropertyInfo property in properties)
		{
			if(property.Name.ToLower() == col.ToLower())
			{
				return property.PropertyType.ToString().Replace("System.", "");
			}
		}

		return null;

    }
	
	public void importCsv(IFormFile file)
		{
            using (var reader = new StreamReader(file.OpenReadStream()))
            {
                #className# e = new #className#();
                string headerLine = reader.ReadLine();
                string[] headers = headerLine.Split(';');
				if (headers.Length != e.Cols.Count) throw new Exception("Colonne non accepté !");
                foreach (var c in e.Cols)
                {
					if (!headers.Contains(c)) throw new Exception("Colonne non trouvé !");
                }

                Dictionary<string, int> columnIndexMap = new Dictionary<string, int>();
                for (int i = 0; i < headers.Length; i++)
                {
                    columnIndexMap[headers[i]] = i;
                }

				headerLine = headerLine.Replace(';', ',');

				using (NpgsqlConnection connection = new NpgsqlConnection(connectionString))
				{
                    connection.Open();

                    while (!reader.EndOfStream)
					{
						var line = reader.ReadLine();
						var values = line.Split(';');
						var script = "";
                        for (var i = 0; i < values.Length; i++)
                        {
							if (this.getTypeAttribut(headers[i]) == "String" || this.getTypeAttribut(headers[i]) == "DateTime"){
								script += "'" + values[i] + "',";
							}
							else
							{
								script += values[i] + ",";
							}
                        }
                        script = script.Substring(0, script.Length - 1);
                        using (NpgsqlCommand command = new NpgsqlCommand("insert into #table# (" + headerLine + ") values("+ script +")", connection))
                        {
              
                            command.ExecuteNonQuery();
                        }
                      
                    }
					connection.Close();
				}
     
			
            }

        }	


	#foreignkey# 
   }
}
