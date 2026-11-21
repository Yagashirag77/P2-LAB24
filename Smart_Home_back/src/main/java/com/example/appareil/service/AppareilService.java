package com.example.appareil.service;

import com.example.appareil.entity.Appareil;
import com.example.appareil.entity.Categorie;
import com.example.appareil.repository.AppareilRepository;
import com.example.appareil.repository.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class AppareilService {


    @Autowired
    private AppareilRepository appareilRepository;
    @Autowired
    private CategorieRepository categorieRepository;

    public Appareil save(Appareil appareil) {
        if (appareil.getLabel() == null || appareil.getLabel().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le nom de l'appareil est obligatoire.");
        }
        if (appareil.getCategorie() == null || appareil.getCategorie().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La catégorie est obligatoire.");
        }
        Optional<Categorie> categorie = categorieRepository.findById(appareil.getCategorie().getId());
        if (categorie.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Catégorie introuvable.");
        }
        // On rattache l'entité chargée en base, pas l'objet envoyé par le navigateur.
        appareil.setLabel(appareil.getLabel().trim());
        appareil.setCategorie(categorie.get());
        return appareilRepository.save(appareil);
    }

    public Optional<Appareil> findById(Long id) {
        return Optional.of(appareilRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appareil introuvable.")));
    }

    public void deleteById(Long id) {
        if (!appareilRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Appareil introuvable.");
        }
        appareilRepository.deleteById(id);
    }

    public List<Appareil> findAll() {
        return appareilRepository.findAll();
    }

    public void update(Long id, Appareil appInfo) {
        Appareil appareil = appareilRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appareil introuvable."));
        appareil.setState(appInfo.isState());
        appareilRepository.save(appareil);
    }

    public void updateAll(boolean state) {
        List<Appareil> appareilList = appareilRepository.findAll();
        appareilList.forEach(s -> s.setState(state));
        appareilRepository.saveAll(appareilList);
    }
}
