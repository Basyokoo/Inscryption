<div align="center">

# 🃏 Inscryption — Projet SAé Java

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit_4-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Status](https://img.shields.io/badge/Status-En_cours-FFE81F?style=for-the-badge)

> *Vous vous retrouvez dans une cabane, perdu en pleine forêt. Dans l'obscurité, attablé face à vous se dresse un adversaire aux yeux inquiétants qui vous défie à un étrange jeu de cartes...*

</div>

---

## 📑 Table des matières

- [Présentation](#-présentation)
- [Phase 1 — Le jeu de base](#-phase-1--le-jeu-de-base)
- [Phase 2 — Pouvoirs & nouvelles cartes](#-phase-2--pouvoirs--nouvelles-cartes)
- [Affichage](#-affichage)
- [Structure du projet](#-structure-du-projet)
- [Tests](#-tests)
- [Organisation & calendrier](#-organisation--calendrier)
- [Consignes de rendu](#-consignes-de-rendu)

---

## 🎮 Présentation

Ce projet consiste à développer une application Java imitant le jeu **Inscryption** (phase 1 uniquement, version simplifiée). Il est réalisé en **binôme** dans le cadre d'une SAé sur 5 semaines.

---

## 🂠 Phase 1 — Le jeu de base

### Règles générales

| Élément | Description |
|---|---|
| 🏆 Condition de victoire | Premier joueur à atteindre un écart de **5 points** remporte la partie |
| 🎴 Main de départ | **4 cartes** piochées en début de partie |
| 📦 Pioche initiale | **15 cartes** (majorité d'écureuils) |
| 🔄 Parties | Le jeu se compose de **3 parties** — gagner les 3 pour remporter le jeu |

### Le plateau

```
  Adversaire  [ A1 ] [ A2 ] [ A3 ] [ A4 ]   ← ligne du haut (adversaire)
  ─────────────────────────────────────────
  Joueur      [ B1 ] [ B2 ] [ B3 ] [ B4 ]   ← ligne du bas (joueur)
```

### Déroulement d'un tour

1. **Début du tour** — L'adversaire annonce ses prochaines cartes
2. **Pioche** — Le joueur peut piocher **1 carte**
3. **Placement** — Le joueur place autant de cartes qu'il le souhaite (dans la limite des emplacements et des sacrifices requis)
4. **Attaque** — Chaque carte attaque à la fin du tour :
   - Carte adverse en face → elle perd des PV équivalents à l'attaque
   - Aucune carte en face → le score augmente du nombre de points d'attaque
   - 🕊️ **Cartes volantes** → attaquent directement le score, quelle que soit la carte en face

> Un message récapitule les dégâts infligés à la fin de chaque tour.

### Cartes obstacles

Des cartes obstacles peuvent être présentes sur le plateau en début de partie. Elles doivent être éliminées avant d'occuper leur emplacement.

| Carte | Points de vie |
|---|:---:|
| 🪨 Rocher | 5 |
| 🌲 Sapin | 3 |

### Liste des cartes animaux

| Carte | ⚔️ Attaque | ❤️ PV | 🩸 Sang | 🦴 Os | 🕊️ Volant |
|---|:---:|:---:|:---:|:---:|:---:|
| 🐱 Chat | 0 | 1 | 1 | 0 | ✗ |
| 🐻 Grizzly | 4 | 6 | 3 | 0 | ✗ |
| 🐺 Coyote | 2 | 1 | 0 | 4 | ✗ |
| 🐦 Moineau | 1 | 2 | 1 | 0 | ✓ |
| 🐦‍⬛ Corbeau | 2 | 3 | 2 | 0 | ✓ |
| 🐿️ Écureuil | 0 | 1 | 0 | 0 | ✗ |
| 🦦 Hermine | 1 | 3 | 1 | 0 | ✗ |
| 🐺 Louveteau | 1 | 1 | 1 | 0 | ✗ |
| 🐺 Loup | 3 | 2 | 2 | 0 | ✗ |
| 🐛 Punaise | 1 | 2 | 0 | 2 | ✗ |

> Une même carte peut apparaître en plusieurs exemplaires dans la pioche, la main et sur le plateau.

### Fin de partie

- À la fin de la **2e partie**, le joueur peut ajouter une nouvelle carte (parmi 2 proposées) à sa pioche.

---

## ⚡ Phase 2 — Pouvoirs & nouvelles cartes

### Pouvoirs

| Pouvoir | Description |
|---|---|
| 💚 Nombreuses vies | Reste sur le plateau même après avoir été sacrifié |
| 🐺 Croissance | Se transforme en **Loup** au début de son 2e tour sur le plateau |
| 🤢 Puant | Réduit de 1 l'attaque de la carte lui faisant face |
| 🏃 Coureur | Se déplace d'un emplacement vers la droite après son attaque (ou à gauche si bloqué) |
| ☠️ Contact Mortel | Tue instantanément toute créature à laquelle il inflige des dégâts |
| 🦔 Piques pointues | Inflige 1 dégât à la carte qui l'attaque |

### Cartes mises à jour (Phase 1 + pouvoirs)

| Carte | ⚔️ | ❤️ | 🩸 | 🦴 | 🕊️ | Pouvoir |
|---|:---:|:---:|:---:|:---:|:---:|---|
| 🐱 Chat | 0 | 1 | 1 | 0 | ✗ | 💚 Nombreuses vies |
| 🐺 Louveteau | 1 | 1 | 1 | 0 | ✗ | 🐺 Croissance |
| 🐛 Punaise | 1 | 2 | 0 | 2 | ✗ | 🤢 Puant |

### Nouvelles cartes

| Carte | ⚔️ | ❤️ | 🩸 | 🦴 | 🕊️ | Pouvoir |
|---|:---:|:---:|:---:|:---:|:---:|---|
| 🦌 Élan | 2 | 4 | 2 | 0 | ✗ | 🏃 Coureur |
| 🐍 Vipère | 1 | 1 | 2 | 0 | ✗ | ☠️ Contact Mortel |
| 🦔 Porc-épic | 1 | 2 | 1 | 0 | ✗ | 🦔 Piques pointues |

### Pierre de sacrifice

À la fin de la 2e partie, après avoir choisi une nouvelle carte, le joueur peut **sacrifier une carte** pour récupérer son pouvoir et l'attribuer à une autre carte de son choix.

---

## 🖥️ Affichage

L'affichage se fait en **mode texte dans le terminal**. Toutes les informations doivent être visibles. Exemple indicatif :

```
    Partie 1 — Tour 1

         *-----------*   *************   *-----------*   *************
         | Louveteau |   *           *   | Moineau   |   *           *
         | PV: 1     |   *           *   | PV: 2     |   *           *
         | Att: 1    |   *           *   | Att: 1    |   *           *
         *-----------*   *************   *-----------*   *************
               ||              ||              ||              ||
         *************   *************   *************   *************
         *     A1    *   *     A2    *   *     A3    *   *     A4    *
         *************   *************   *************   *************
 Score
   0
         *************   *-----------*   *************   *************
         *     B1    *   | Rocher    |   *     B3    *   *     B4    *
         *           *   | PV: 5     |   *           *   *           *
         *************   *-----------*   *************   *************

  Votre main :
    1. Écureuil  PV: 1  Att: 0  Sang: 0  Os: 0
    2. Hermine   PV: 3  Att: 1  Sang: 1  Os: 0

Actions possibles:
  [fin]                          Terminer votre tour
  [piocher]                      Piocher une carte
  [placer <num_carte> <pos>]     Placer une carte sur le plateau

$ _
```

---

## 📁 Structure du projet

```
inscryption/
├── README.md
├── .gitignore
├── deps/
│   ├── hamcrest-core-1.3.jar
│   └── junit-4.13.1.jar
├── out/
│   └── .gitkeep
├── src/
│   ├── Main.java
│   └── ...
├── tests/
│   └── ...
└── uml/
    ├── semaine1.puml
    └── ...
```

---

## 🧪 Tests

Les tests JUnit couvrent les fonctionnalités suivantes :

- [ ] Attaque d'une carte
- [ ] Attaque de toutes les cartes en fin de tour
- [ ] Tous les pouvoirs (Phase 2)
- [ ] Mécanisme de la pierre de sacrifice
- [ ] Mise à jour du score
- [ ] Placement des cartes sur le plateau
- [ ] Pioche d'une carte
- [ ] Mise en place d'une partie (plateau et pioche)
- [ ] Gagner ou perdre une partie
- [ ] Ajout de nouvelles cartes à la fin de la 2e partie
- [ ] Gagner ou perdre le jeu

---

## 📅 Organisation & calendrier

- 👥 Travail en **binôme** au sein d'un même groupe de TP
- ⏱️ Durée : **5 semaines**

| Date | Événement |
|---|---|
| Lundi **4 mai** | Dévoilement de la Phase 1 |
| Mardi **12 mai** | Début des séances TP dédiées |
| Lundi **25 mai** | Dévoilement de la Phase 2 |
| Mercredi **10 juin** à 12h30 | ⚠️ Rendu final |
| Jeudi **11 juin** – Vendredi **12 juin** | Soutenances |

### Rendus hebdomadaires

| Semaine | Date limite |
|---|---|
| Rendu 1 | Dimanche **17 mai** à 23h59 |
| Rendu 2 | Dimanche **24 mai** à 23h59 |
| Rendu 3 | Dimanche **31 mai** à 23h59 |
| Rendu 4 | Dimanche **7 juin** à 23h59 |

---

## 📬 Consignes de rendu

- Le projet doit être un **fork** de ce dépôt dans un groupe nommé `<nom_etudiant_1>-<nom_etudiant_2>`
- Ajouter l'enseignant TP et le responsable du module en tant que **Reporter**
- Chaque rendu = une branche nommée `rendu<numéro-rendu>` (ex: `rendu1`)
- Le **dernier rendu** sera considéré comme le rendu final

### Contenu obligatoire de chaque rendu

- ✅ Un programme qui **compile** (sources dans `src/`)
- ✅ Un **diagramme de classes** à jour dans `uml/` (`semaine<N>.puml`)

---

## ⚠️ Qualité du code

- Respecter les **P21 Guidelines**
- Code **lisible**, **maintenable** et **extensible**
- Gestion des **saisies incorrectes** : format indiqué, erreur expliquée, saisie recommencée
- **Commits réguliers** sur les branches de travail

---

<div align="center">

*Projet SAé — IUT Robert Schuman, Université de Strasbourg*

</div>
