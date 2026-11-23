package com.exemple.dao;

import com.exemple.model.Produit;
import java.util.List;
import java.util.Optional;

public interface ProduitDao {
    List<Produit> trouverTous();
    List<Produit> rechercher(String motCle, String tri);   
    Optional<Produit> trouverParId(long id);
    Produit creer(Produit produit);
    void modifier(Produit produit);
    void supprimer(long id);
}