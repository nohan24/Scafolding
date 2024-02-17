package #packageName#;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class #className#Service {

    private final #className#Repository #className#Repository;

    @Autowired
    public #className#Service(#className#Repository #className#Repository) {
        this.#className#Repository = #className#Repository;
    }

    public #className# get#className#ById(Long id) {
        Optional<#className#> optional#className# = #className#Repository.findById(id);
        return optional#className#.orElse(null); // Gérer le cas où la ressource n'est pas trouvée
    }

    public #className# create#className#(#className# #className#) {
        // Logique de validation ou de traitement avant la sauvegarde
        return #className#Repository.save(#className#);
    }

    public #className# update#className#(Long id, #className# #className#) {
        Optional<#className#> optional#className# = #className#Repository.findById(id);
        if (optional#className#.isPresent()) {
            #className# existing#className# = optional#className#.get();
            // Mettre à jour les attributs nécessaires
            existing#className# = #className#Repository.save(existing#className#);
            return existing#className#;
        } else {
            // Gérer le cas où la ressource n'est pas trouvée
            return null;
        }
    }

    public void delete#className#(Long id) {
        #className#Repository.deleteById(id);
    }

    public List<#className#> getAll#className#s() {
        return #className#Repository.findAll();
    }
}
