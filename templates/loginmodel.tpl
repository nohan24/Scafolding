using Npgsql;
using System.Data;

namespace #project#.Models
{
    public class VMLogin
    {
        public string Email { get; set; }
        public string Password { get; set; }

        private string connectionString = "Host=localhost;Username=postgres;Password=root;Database=scafolding";

        public VMLogin login()
        {
            VMLogin obj = null;
            using (NpgsqlConnection connection = new NpgsqlConnection(connectionString))
            {
                connection.Open();
                String sql = "select * from users where email = '" + this.Email + "' and password = '" + this.Password + "'";
                using (NpgsqlCommand command = new NpgsqlCommand(sql, connection))
                {
                    using (NpgsqlDataReader reader = command.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            obj = new VMLogin();
                            obj.Email = reader.GetString("email");
                            obj.Password = reader.GetString("password");
                        }
                    }
                }
            }
            return obj;
        }
    }
}
