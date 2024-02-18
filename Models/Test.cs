namespace Scafolding.Models{

   public class Test {

      public Test(){}
      
		public string Id { get; set; }		
		public string Nom { get; set; }		
		public string Argent { get; set; }		
		public string Id_dept { get; set; }		

		public void insert() {
			string connectionString = "Host=localhost;Username=postgres;Password=root;Database=scafolding";
			using (NpgsqlConnection connection = new NpgsqlConnection(connectionString)) {
				connection.Open();
				string sql = "insert into test(nom ,argent ,id_dept ) values(@nom ,@argent ,@id_dept )";
				using (NpgsqlCommand command = new NpgsqlCommand(sql, connection)) {
					command.Parameters.AddWithValue("@nom", "'"+thisNom+"'");
					command.Parameters.AddWithValue("@argent", this.Argent);
					command.Parameters.AddWithValue("@id_dept", this.Id_dept);
					command.ExecuteNonQuery();
				}
				connection.Close();
			}
		}

		
		public void delete(int id) {
			String connectionString = "Host=localhost;Username=postgres;Password=root;Database=scafolding";
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
			string connectionString = "Host=localhost;Username=postgres;Password=root;Database=scafolding; 
			using (NpgsqlConnection connection = new NpgsqlConnection(connectionString)) {
				connection.Open();
				string sql = "select * from test"; 
				using (NpgsqlCommand command = new NpgsqlCommand(sql, connection)) {
					using (NpgsqlDataReader reader = command.ExecuteReader()) {
						while (reader.Read()) {
							var obj = new Test();  
							obj.Id = reader.Get(id); 
							obj.Nom = reader.Get(nom); 
							obj.Argent = reader.Get(argent); 
							obj.Id_dept = reader.Get(id_dept); 
							listA.add(obj); 
 						} 
 					} 
 				}
				connection.Close(); 
 			} 
			return listA; 
 		} 

 
		
   }
}

