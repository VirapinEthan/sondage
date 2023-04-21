package fr.simplon.sondage;

import fr.simplon.sondage.dao.impl.SondageRepository;
import fr.simplon.sondage.entity.Sondage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SondageApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SondageApplicationTests {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private SondageRepository repository;

    /**
     * Cette méthode est executé avec la méthode @Test et vient supprimer toutes les données en BDD.
     *
     *@BeforeEach public void setUp()
     */

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }


    /**
     * Cette méthode crée des données fictives et test la récupération de tous les sondages présents dans la BDD.
     *
     * @GetMapping("/rest/votes") @Test public void testGetVotes()
     */
    @GetMapping("/rest/sondages")
    @Test
    void TestGetMapping() {
       Sondage sondage1 = new Sondage("moussa","voulez vous moins travaillez","2h de moins ?", LocalDate.now(),LocalDate.now().plusDays(7));
       Sondage sondage2 = new Sondage("peter","en vrai qui l'aime ? ","go ban le chef", LocalDate.now(),LocalDate.now().plusDays(7));

       repository.saveAll(List.of(sondage1,sondage2));

        // Envoyer une requête GET à /rest/sondages et récupérer la réponse

        ResponseEntity<Sondage[]> responseEntity = testRestTemplate.getForEntity("http://localhost:" + port + "/rest/sondages", Sondage[].class);
        Sondage[] sondages = responseEntity.getBody();

        // Vérifier que la réponse contient les deux sondages créés

        assertEquals((HttpStatus.OK), responseEntity.getStatusCode());


    }

    /**
     * Cette méthode vérifie si l’ajout d’un sondage fonctionne correctement dans la base de données.
     *
     * @PostMapping("/rest/votes") @Test void testAddVote()
     */
        @PostMapping("/rest/sondages")
        @Test
        void testAddSondage() {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);



            Sondage sondage = new Sondage("twitch","juste un champion de merde","pourquoi ?",LocalDate.now(),LocalDate.now().plusDays(7));
            HttpEntity<Sondage> request = new HttpEntity<>(sondage, headers);

            ResponseEntity<Sondage> response = restTemplate.postForEntity("http://localhost:" + port + "/rest/sondages", request, Sondage.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody().getId());
            assertEquals("juste un champion de merde", response.getBody().getDescription());


        }


        /**
          * Cette méthode crée un sondage dans la BDD , puis vérifie si la récupération d’un sondage à partir de la liste fonctionne.
          *
          * @GetMapping("/rest/votes/{id}") @Test void testGetVote()
          */
        @GetMapping("/rest/sondages/{id}")
        @Test
        void testGetSondage() {
            RestTemplate restTemplate = new RestTemplate();
            Sondage sondage = new Sondage("test", "test", "test", LocalDate.now(), LocalDate.now().plusDays(7));
            Sondage savedSondage = repository.save(sondage);
            ResponseEntity<Sondage> response = restTemplate.getForEntity("http://localhost:" + port + "/rest/sondages/" + savedSondage.getId(), Sondage.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("test", response.getBody().getNom());

        }

    /**
     * Cette méthode crée un sondage dans la BDD pour les tests, puis vérifie si la suppression fonctionne correctement.
     *
     * @DeleteMapping("/rest/votes/{id}") @Test void testDelVote()
     */
    @DeleteMapping("/rest/sondages/{id}")
    @Test
    void testDelSondage() {
        RestTemplate restTemplate = new RestTemplate();

        //Ajouter un sondage de test
        Sondage sondage = new Sondage("Test","Test", "Test", LocalDate.now(), LocalDate.now().plusDays(7));
        Sondage savedSondage = repository.save(sondage);

        //Supprimer le sondage de test
        restTemplate.delete("http://localhost:" + port + "/rest/sondages/" + savedSondage.getId());

        //Vérifier que le sondage n'existe plus
        Optional<Sondage> deletedSondage= repository.findById(savedSondage.getId());
        assertFalse(deletedSondage.isPresent());
    }




    /**
     * Requête de maj à l'endpoint spécifier
     * sauf l'id et la date_creation
     */
    @PutMapping("/rest/votes/{id}")
    @Test
    void testUpdateSondage() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //Ajouter un sondage de test
        Sondage sondage = new Sondage("Test","Test", "Test", LocalDate.now(), LocalDate.now().plusDays(7));
        Sondage savedSondage = repository.save(sondage);

        //Modifier le sondage de test
        savedSondage.setDescription("Test modifié");
        HttpEntity<Sondage> request = new HttpEntity<>(savedSondage, headers);
        restTemplate.put("http://localhost:" + port + "/rest/sondages/" + savedSondage.getId(), request, Sondage.class);

        //Vérifier que le sondage a été modifié
        Optional<Sondage> updatedVote = repository.findById(savedSondage.getId());
        assertTrue(updatedVote.isPresent());
        assertEquals("Test modifié", updatedVote.get().getDescription());
    }

}




