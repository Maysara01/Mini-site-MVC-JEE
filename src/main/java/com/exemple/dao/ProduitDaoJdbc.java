package com.exemple.dao;

import com.exemple.model.Produit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProduitDaoJdbc implements ProduitDao {

    @Override
    public List<Produit> trouverTous() {
        String sql = "SELECT id, nom, description, prix, stock FROM produit ORDER BY id";
        List<Produit> liste = new ArrayList<>();
        try (Connection conn = FabriqueConnexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(versProduit(rs));
            }
            return liste;
        } catch (SQLException e) {
            throw new DonneesException("Erreur lors de la récupération des produits", e);
        }
    }

    @Override
    public List<Produit> rechercher(String motCle, String tri) {
        String colonneTri = switch (tri == null ? "" : tri) {
            case "prix" -> "prix ASC";
            case "prix_desc" -> "prix DESC";
            case "stock" -> "stock ASC";
            default -> "nom ASC";
        };

        boolean aMotCle = motCle != null && !motCle.isBlank();
        String sql = aMotCle
                ? "SELECT id, nom, description, prix, stock FROM produit WHERE nom LIKE ? ORDER BY " + colonneTri
                : "SELECT id, nom, description, prix, stock FROM produit ORDER BY " + colonneTri;

        List<Produit> liste = new ArrayList<>();
        try (Connection conn = FabriqueConnexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (aMotCle) {
                ps.setString(1, "%" + motCle.trim() + "%");
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(versProduit(rs));
                }
            }
            return liste;
        } catch (SQLException e) {
            throw new DonneesException("Erreur lors de la recherche de produits", e);
        }
    }

    @Override
    public Optional<Produit> trouverParId(long id) {
        String sql = "SELECT id, nom, description, prix, stock FROM produit WHERE id = ?";
        try (Connection conn = FabriqueConnexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(versProduit(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DonneesException("Erreur lors de la recherche du produit #" + id, e);
        }
    }

    @Override
    public Produit creer(Produit produit) {
        String sql = "INSERT INTO produit (nom, description, prix, stock) VALUES (?, ?, ?, ?)";
        try (Connection conn = FabriqueConnexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, produit.getNom());
            ps.setString(2, produit.getDescription());
            ps.setBigDecimal(3, produit.getPrix());
            ps.setInt(4, produit.getStock());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    produit.setId(keys.getLong(1));
                }
            }
            return produit;
        } catch (SQLException e) {
            throw new DonneesException("Erreur lors de la création du produit", e);
        }
    }

    @Override
    public void modifier(Produit produit) {
        String sql = "UPDATE produit SET nom = ?, description = ?, prix = ?, stock = ? WHERE id = ?";
        try (Connection conn = FabriqueConnexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, produit.getNom());
            ps.setString(2, produit.getDescription());
            ps.setBigDecimal(3, produit.getPrix());
            ps.setInt(4, produit.getStock());
            ps.setLong(5, produit.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DonneesException("Erreur lors de la modification du produit #" + produit.getId(), e);
        }
    }

    @Override
    public void supprimer(long id) {
        String sql = "DELETE FROM produit WHERE id = ?";
        try (Connection conn = FabriqueConnexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451 || "23000".equals(e.getSQLState())) {
                throw new ContrainteException("Impossible de supprimer ce produit car il est lié à des ventes existantes.", e);
            }
            throw new DonneesException("Erreur lors de la suppression du produit #" + id, e);
        }
    }

    private Produit versProduit(ResultSet rs) throws SQLException {
        return new Produit(
                rs.getLong("id"),
                rs.getString("nom"),
                rs.getString("description"),
                rs.getBigDecimal("prix"),
                rs.getInt("stock")
        );
    }
}