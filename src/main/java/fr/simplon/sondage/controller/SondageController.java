package fr.simplon.sondage.controller;


import fr.simplon.sondage.dao.impl.SondageRepository;
import fr.simplon.sondage.entity.Sondage;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class SondageController {

    private final SondageRepository repo;


    public SondageController(SondageRepository sondage) {
          this.repo = sondage;

          //this.repo.save(new Sondage("Brandon","La cantine","Vous voulez des frite le vendredi",LocalDate.now(),LocalDate.now().plusDays(7)));
          //this.repo.save(new Sondage("stephanie","l'heure de la pause midi","12h30 ou 13h",LocalDate.now(),LocalDate.now().plusDays(7)));
    }
    @GetMapping("/rest/sondages")
    public List<Sondage> getSondages(){
        return repo.findAll();
    }

    @PostMapping("/rest/sondages")
    public Sondage addSondages(@RequestBody Sondage newSondage){
        return repo.save(newSondage);
    }

    @GetMapping("/rest/sondages/{id}")
    public Sondage getSondage(@PathVariable Long id){
        return repo.findById(id).orElse(new Sondage("inconnu","inconnu","inconnu", LocalDate.now(),LocalDate.now()));

    }

    @DeleteMapping("/rest/sondages/{id}")
    public Sondage delSondage(@PathVariable Long id){
        repo.deleteById(id);
        return null;
    }

    @PutMapping("/rest/sondages/{id}")
    public Sondage updateSondage(@RequestBody Sondage newSondage, @PathVariable long id) {
        return repo.findById(id)
                .map(sondage -> {
                    sondage.setNom(newSondage.getNom());
                    sondage.setDescription(newSondage.getDescription());
                    sondage.setQuestion(newSondage.getQuestion());
                    sondage.setDate_creation(newSondage.getDate_creation());
                    sondage.setDate_cloture(newSondage.getDate_cloture());
                    return  repo.save(sondage);
                })
                .orElseGet(() -> {
                    newSondage.setId(id);
                    return repo.save(newSondage);
                });
    }



}
