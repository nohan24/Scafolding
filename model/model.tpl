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
      #getset##crud#
   }
}
