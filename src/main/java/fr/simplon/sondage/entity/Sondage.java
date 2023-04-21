package fr.simplon.sondage.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;


@Entity
@Table(name = "sondage")
public class Sondage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Size(max = 64)
    @NotBlank
    private String nom;

    @Size(min = 3, max = 120)
    @NotBlank
    private String description;

    @Size(max = 120)
    @NotBlank
    private String question;
    private LocalDate date_creation;
   @Future
    private LocalDate date_cloture;

    public Sondage(String nom, String description, String question, LocalDate date_creation, LocalDate date_cloture) {
        this.nom = nom;
        this.description = description;
        this.question = question;
        this.date_creation = date_creation;
        this.date_cloture = date_cloture;
    }

    public Sondage() {
        this.date_creation = LocalDate.now();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public LocalDate getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(LocalDate date_creation) {
        this.date_creation = date_creation;
    }

    public LocalDate getDate_cloture() {
        return date_cloture;
    }

    public void setDate_cloture(LocalDate date_cloture) {
        this.date_cloture = date_cloture;
    }
}


