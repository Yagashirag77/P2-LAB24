package com.example.appareil;

import com.example.appareil.entity.Appareil;
import com.example.appareil.entity.Categorie;
import com.example.appareil.repository.AppareilRepository;
import com.example.appareil.repository.CategorieRepository;
import com.example.appareil.service.AppareilService;
import com.example.appareil.service.CategorieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceRulesTest {
    @Mock AppareilRepository appareils;
    @Mock CategorieRepository categories;
    @InjectMocks AppareilService appareilService;
    @InjectMocks CategorieService categorieService;

    @Test
    void appareilSansCategorieEstRefuse() {
        Appareil appareil = new Appareil();
        appareil.setLabel("Lampe du bureau");
        ResponseStatusException erreur = assertThrows(ResponseStatusException.class, () -> appareilService.save(appareil));
        assertEquals(HttpStatus.BAD_REQUEST, erreur.getStatusCode());
        verifyNoInteractions(categories, appareils);
    }

    @Test
    void categorieInconnueEstRefusee() {
        Categorie categorie = new Categorie();
        categorie.setId  (42L);
        Appareil appareil = new Appareil();
        appareil.setLabel("Lampe");
        appareil.setCategorie(categorie);
        when(categories.findById(42L)).thenReturn(Optional.empty());
        assertEquals(HttpStatus.NOT_FOUND, assertThrows(ResponseStatusException.class, () -> appareilService.save(appareil)).getStatusCode());
        verifyNoInteractions(appareils);
    }

    @Test
    void categorieUtiliseeNePeutPasEtreSupprimee() {
        Categorie categorie = new Categorie();
        categorie.setAppareilList(List.of(new Appareil()));
        when(categories.findById(1L)).thenReturn(Optional.of(categorie));
        assertEquals(HttpStatus.CONFLICT, assertThrows(ResponseStatusException.class, () -> categorieService.deleteById(1L)).getStatusCode());
        verify(categories, never()).deleteById(anyLong());
    }

    @Test
    void nomVideEstRefuse() {
        Categorie categorie = new Categorie();
        categorie.setLabel("  ");
        assertEquals(HttpStatus.BAD_REQUEST, assertThrows(ResponseStatusException.class, () -> categorieService.save(categorie)).getStatusCode());
        verifyNoInteractions(categories);
    }

    @Test
    void commandeGlobaleChangeTousLesEtats() {
        Appareil premier = new Appareil();
        Appareil second = new Appareil();
        when(appareils.findAll()).thenReturn(List.of(premier, second));
        appareilService.updateAll(true);
        assertTrue(premier.isState());
        assertTrue(second.isState());
        verify(appareils).saveAll(anyList());
    }
}
