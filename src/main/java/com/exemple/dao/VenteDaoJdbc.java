package com.exemple.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VenteDaoJdbc implements VenteDao {

    @Override
    public void vendre(long produitId, int quantite) {
        String sqlSelect = "SELECT prix, stock FROM produit WHERE id = ? FOR UPDATE";
        String sqlUpdateStock = "UPDATE produit SET stock = stock - ? WHERE id = ?";
        String sqlInsertVente = "INSERT INTO vente (produit_id, quantite, prix_unitaire) VALUES (?, ?, ?)";

        try (Connection conn = FabriqueConnexion.getConnection()) {
            conn.setAutoCommit(false);
            try {
                BigDecimal prixUnitaire;
                int stockActuel;

                try (PreparedStatement psSelect = conn.prepareStatement(sqlSelect)) {
                    psSelect.setLong(1, produitId);
                    try (ResultSet rs = psSelect.executeQuery()) {
                        if (!rs.next()) {
                            throw new DonneesException("Produit introuvable (id=" + produitId + ")");
                        }
                        prixUnitaire = rs.getBigDecimal("prix");
                        stockActuel = rs.getInt("stock");
                    }
                }

                if (stockActuel < quantite) {
                    throw new StockInsuffisantException(
                            "Stock insuffisant (disponible : " + stockActuel + ", demandé : " + quantite + ").");
                }

                try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateStock)) {
                    psUpdate.setInt(1, quantite);
                    psUpdate.setLong(2, produitId);
                    psUpdate.executeUpdate();
                }

                try (PreparedStatement psInsert = conn.prepareStatement(sqlInsertVente)) {
                    psInsert.setLong(1, produitId);
                    psInsert.setInt(2, quantite);
                    psInsert.setBigDecimal(3, prixUnitaire);
                    psInsert.executeUpdate();
                }

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                if (e instanceof StockInsuffisantException sie) {
                    throw sie;
                }
                if (e instanceof DonneesException de) {
                    throw de;
                }
                throw new DonneesException("Erreur lors de l'enregistrement de la vente", e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DonneesException("Erreur de connexion lors de la vente", e);
        }
    }
}