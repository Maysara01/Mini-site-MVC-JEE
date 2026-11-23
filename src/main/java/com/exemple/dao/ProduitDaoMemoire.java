package com.exemple.dao;

import com.exemple.model.Produit;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProduitDaoMemoire implements com.exemple.dao.ProduitDao {

    private final Map<Long, Produit> produits = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public ProduitDaoMemoire() {
        creer(new Produit(null, "Laptop", "Dell XPS 13", new BigDecimal("9500.00"), 5));
        creer(new Produit(null, "Souris", "Logitech MX Master", new BigDecimal("650.00"), 20));
        creer(new Produit(null, "Clavier", "Clavier mécanique", new BigDecimal("249.00"), 12));
        creer(new Produit(null, "Écran", "Écran 24 pouces", new BigDecimal("1490.00"), 0));
    }

    @Override
    public List<Produit> trouverTous() {
        return produits.values().stream()
                .sorted(Comparator.comparing(Produit::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Produit> rechercher(String motCle, String tri) {
        Stream<Produit> flux = produits.values().stream();
        if (motCle != null && !motCle.isBlank()) {
            String aiguille = motCle.trim().toLowerCase(Locale.ROOT);
            flux = flux.filter(p -> p.getNom() != null
                    && p.getNom().toLowerCase(Locale.ROOT).contains(aiguille));
        }
        return flux.sorted(comparateur(tri)).collect(Collectors.toList());
    }

    // only the 4 known values are accepted; anything else falls back to "nom"
    private Comparator<Produit> comparateur(String tri) {
        if ("prix".equals(tri)) {
            return Comparator.comparing(Produit::getPrix);
        } else if ("prix_desc".equals(tri)) {
            return Comparator.comparing(Produit::getPrix).reversed();
        } else if ("stock".equals(tri)) {
            return Comparator.comparingInt(Produit::getStock);
        }
        return Comparator.comparing(Produit::getNom, String.CASE_INSENSITIVE_ORDER);
    }

    @Override
    public Optional<Produit> trouverParId(long id) {
        return Optional.ofNullable(produits.get(id));
    }

    @Override
    public Produit creer(Produit produit) {
        produit.setId(sequence.incrementAndGet());
        produits.put(produit.getId(), produit);
        return produit;
    }

    @Override
    public void modifier(Produit produit) {
        produits.put(produit.getId(), produit);
    }

    @Override
    public void supprimer(long id) {
        produits.remove(id);
    }
}