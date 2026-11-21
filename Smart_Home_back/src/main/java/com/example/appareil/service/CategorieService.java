package com.example.appareil.service;

import com.example.appareil.entity.Appareil;
import com.example.appareil.entity.Categorie;
import com.example.appareil.repository.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategorieService {

    @Autowired
    private CategorieRepository categorieRepository;

    public List<Categorie> findAll() {
        return categorieRepository.findAll();
    }

    public Categorie  save (Categorie categorie) {
        validateLabel(categorie);
        return categorieRepository.save(categorie);
    }

    public Optional<Categorie> findById(Long id) {
        return Optional.of(categorieRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Catégorie introuvable.")));
    }

    public void deleteById(Long id) {
        Categorie categorie = categorieRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Catégorie introuvable."));
        if (categorie.getAppareilList() != null && !categorie.getAppareilList().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La catégorie contient encore des appareils.");
        }
        categorieRepository.deleteById(id);
    }

    public void update(Long id, Categorie catInfo) {
        validateLabel(catInfo);
        Categorie categorie=categorieRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Catégorie introuvable."));
        categorie.setLabel(catInfo.getLabel());
        categorieRepository.save(categorie);
    }

    private void validateLabel(Categorie categorie) {
        if (categorie.getLabel() == null || categorie.getLabel().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le nom de la catégorie est obligatoire.");
        }
        categorie.setLabel(categorie.getLabel().trim());
    }

}
