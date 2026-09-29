# Audit — Solar Green ⚡

> Audit du projet `solargreen` (web + Android).
> Date : 27/09/2026 · Branche : `main` · Remote : `https://github.com/Lunkht/Solar-Green.git`

---

## 1. Vue d'ensemble

| Module   | Techno                        | État                     |
|----------|-------------------------------|--------------------------|
| `web/`   | Vite 5 + React (SPA)          | Fonctionnel, build OK    |
| `android/` | Java natif (Gradle 8.7 / AGP 8.4) | En cours (non commité)   |

---

## 2. Travaux récemment réalisés sur le web

| Date | Contenu | Commit |
|------|---------|--------|
| 27/09 | Texte du bouton `.btn-primary` en blanc | `5066144`¹ |
| 27/09 | Effet fil d'énergie qui suit le curseur (Hero + CTA) | `6b14942`, `44c2e0a` |
| 27/09 | Boutons nav + toggle thème mobile (icônes SVG réelles, texte blanc) | `cc2ca8e`, `6f3e79d` |
| 27/09 | Suppression du texte hero-eyebrow | `8fdc4ea` |
| 27/09 | Photos produits : panneaux 550W/450W, onduleur 5kW, batterie, kit, pompe | `5146149`, `9d08bf6` |
| 27/09 | Produit « Câble Solaire H1Z2Z2-K » + photos usine/batterie/câbles | `ae2f25c` |
| 27/09 | Logo SVG sombre/clair (icônes blanc/noir) + favicon | `02afee5`, `8a507bd` |
| 27/09 | Coordonnées : `+224 614 65 87 17` · `L50 Route Prince Lambanyi, Conakry` | `bc4e3ef` |
| 27/09 | Footer « Fait avec iBilium,inc en Guinée » | `bc4e3ef` |

¹ Commit initial sur l'ancien remote DKZ-Solar (avant bascule vers Solar-Green.git).

---

## 3. État Git

- Branche : `main` — à jour avec `origin/main`.
- Dernier commit poussé : `bc4e3ef`.
- **En suspens (non poussés)** : modifications Android (app) faites localement, non commitées :
  - Redesign complet : nav drawer, stats (conso/production), mode de sélection, notifications, device list, gestion batterie, thèmes.
  - Ces fichiers sont en `M` ou `??` dans `git status`.

---

## 4. Points d'attention

### 4.1 Déploiement
- Site 100 % statique → `dist/`. Pas de workflow CI/GitHub Actions détecté.
- Le remote a changé (`DKZ-Solar.git` → `Solar-Green.git`) : tout nouveau push va sur le nouveau repo.

### 4.2 Images
- Images produits stockées dans `web/public/` (référencées en racine `/…`).
- Attention : noms de fichiers avec espaces (`Kit Maison Premium.png`, `Solar Borehole Pump.png`) — fonctionne, mais à éviter à l'avenir.
- Poids : ~2 Mo par photo PNG. Optimisation possible (WebP/AVIF, compression) pour un site plus rapide.

### 4.3 Références en dur
- Email : `contact@solargreen.com` (QuoteForm, Footer).
- Chemin des images : en dur dans `products.js` et `Rows.jsx`.

### 4.4 Applis Android
- Travail en cours non commité → à sauvegarder/pousser sous un commit dédié (à valider avec le propriétaire).

---

## 5. Recommandations

1. Committer et pousser le travail Android dans un commit séparé.
2. Renommer les images avec espaces en `kebab-case` (`kit-maison-premium.png`, `solar-borehole-pump.png`) et mettre à jour les références.
3. Compresser les photos (WebP) pour améliorer le temps de chargement.
4. Ajouter un workflow Git (Netlify/Vercel/GitHub Pages) pour automatiser la mise en production de `dist/`.
5. Vérifier le SEO : titre, meta `og:`, `sitemap.xml`.

---

## 6. Conclusion

Projet web en bon état de fonctionnement : catalogue complet avec photos réelles, effets visuels, thèmes clair/sombre, coordonnées réelles. **Actions bloquées** : pousser l'app Android, optimiser les images, automatiser le déploiement.