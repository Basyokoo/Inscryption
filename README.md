<div align="center">

<img src="https://capsule-render.vercel.app/api?type=waving&color=0:0d0d0d,50:1a0a2e,100:4a0080&height=200&section=header&text=INSCRYPTION&fontSize=70&fontColor=c084fc&fontAlignY=40&desc=Un+jeu+de+cartes+en+Java+inspiré+du+jeu+Inscryption&descSize=16&descAlignY=62&descColor=aaaaaa&animation=fadeIn&fontFamily=Georgia" width="100%"/>

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com)
[![JUnit](https://img.shields.io/badge/JUnit_4-25A162?style=for-the-badge&logo=junit5&logoColor=white)](https://junit.org)
[![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/Basyokoo/Inscryption)
[![Status](https://img.shields.io/badge/Status-Terminé_✓-4FC3F7?style=for-the-badge)](#)

<br/>

> *« Vous vous retrouvez dans une cabane, perdu en pleine forêt. Dans l'obscurité,*
> *attablé face à vous se dresse un adversaire aux yeux inquiétants qui vous défie*
> *à un étrange jeu de cartes... »*

</div>

---

## 📑 Table des matières

- [À propos](#-à-propos)
- [Fonctionnalités](#-fonctionnalités)
- [Prérequis](#-prérequis)
- [Installation](#-installation)
- [Lancer le jeu](#-lancer-le-jeu)
- [Comment jouer](#-comment-jouer)
- [Les cartes](#-les-cartes)
- [Tests](#-tests)
- [Structure du projet](#-structure-du-projet)
- [Auteurs](#-auteurs)

---

## 🎮 À propos

**Inscryption** est une application Java en **mode terminal** qui reproduit les mécaniques du jeu de cartes roguelike du même nom. Le joueur affronte un adversaire géré par l'IA à travers 3 parties successives, en gérant une pioche de cartes animaux, des sacrifices et des stratégies d'attaque.

Ce projet a été réalisé dans le cadre d'une **SAé** à l'IUT Robert Schuman — Université de Strasbourg.

---

## ✨ Fonctionnalités

- 🃏 **Jeu de cartes complet** — pose, sacrifice et attaque de cartes animaux
- 🤖 **IA adversaire** — comportement déterministe et stratégique
- 🏆 **3 parties enchaînées** — avec évolution de la pioche entre les parties
- ⚡ **Pouvoirs spéciaux** — 6 pouvoirs uniques (Nombreuses vies, Croissance, Puant, Coureur, Contact Mortel, Piques pointues)
- 🪨 **Cartes obstacles** — Rocher et Sapin à éliminer avant de jouer
- 💎 **Pierre de sacrifice** — transfère le pouvoir d'une carte à une autre
- ✅ **Suite de tests JUnit** — couverture complète des mécaniques de jeu
- 🛡️ **Gestion des erreurs** — saisies incorrectes détectées et expliquées

---

## 🔧 Prérequis

Avant de lancer le jeu, assure-toi d'avoir installé :

- **Java JDK 11+** — [Télécharger ici](https://www.oracle.com/java/technologies/downloads/)
- **Git** — [Télécharger ici](https://git-scm.com/)

Vérifie ton installation :
```bash
java -version
javac -version
```

---

## 📦 Installation

### 1. Cloner le repository

```bash
git clone https://github.com/Basyokoo/Inscryption.git
cd Inscryption
```

### 2. Compiler le projet

```bash
# Créer le dossier de sortie si nécessaire
mkdir -p out

# Compiler toutes les sources
javac -d out/ src/*.java
```

---

## 🚀 Lancer le jeu

```bash
java -cp out/ Main
```

Le jeu se lance directement dans le terminal. Aucune interface graphique requise.

---

## 🕹️ Comment jouer

Une fois lancé, le plateau s'affiche dans le terminal avec votre main, le plateau de jeu et les actions disponibles.

```
    Partie 1 — Tour 1

         *-----------*   *************   *-----------*   *************
         | Louveteau |   *           *   | Moineau   |   *           *
         | PV: 1     |   *           *   | PV: 2     |   *           *
         | Att: 1    |   *           *   | Att: 1    |   *           *
         *-----------*   *************   *-----------*   *************

 Score        [ A1 ]         [ A2 ]         [ A3 ]         [ A4 ]
   0
              [ B1 ]         [ B2 ]         [ B3 ]         [ B4 ]

  Votre main :
    1. Écureuil  PV:1  Att:0  Sang:0  Os:0
    2. Hermine   PV:3  Att:1  Sang:1  Os:0

Actions possibles:
  [fin]                       Terminer votre tour
  [piocher]                   Piocher une carte
  [placer <num> <position>]   Placer une carte (ex: placer 2 B1)
```

### Commandes disponibles

| Commande | Description |
|---|---|
| `piocher` | Piocher une carte (1 par tour) |
| `placer <num> <pos>` | Placer la carte n°`<num>` à la position `<pos>` (ex: `placer 2 B3`) |
| `fin` | Terminer son tour et déclencher les attaques |

### Objectif

> Premier joueur à atteindre un écart de **5 points** remporte la partie. Gagnez les **3 parties** pour l'emporter !

---

## 🃏 Les cartes

### Cartes animaux

| Carte | ⚔️ Att | ❤️ PV | 🩸 Sang | 🦴 Os | 🕊️ | Pouvoir |
|---|:---:|:---:|:---:|:---:|:---:|---|
| 🐱 Chat | 0 | 1 | 1 | 0 | ✗ | 💚 Nombreuses vies |
| 🐻 Grizzly | 4 | 6 | 3 | 0 | ✗ | — |
| 🐺 Coyote | 2 | 1 | 0 | 4 | ✗ | — |
| 🐦 Moineau | 1 | 2 | 1 | 0 | ✓ | — |
| 🐦‍⬛ Corbeau | 2 | 3 | 2 | 0 | ✓ | — |
| 🐿️ Écureuil | 0 | 1 | 0 | 0 | ✗ | — |
| 🦦 Hermine | 1 | 3 | 1 | 0 | ✗ | — |
| 🐺 Louveteau | 1 | 1 | 1 | 0 | ✗ | 🐺 Croissance |
| 🐺 Loup | 3 | 2 | 2 | 0 | ✗ | — |
| 🐛 Punaise | 1 | 2 | 0 | 2 | ✗ | 🤢 Puant |
| 🦌 Élan | 2 | 4 | 2 | 0 | ✗ | 🏃 Coureur |
| 🐍 Vipère | 1 | 1 | 2 | 0 | ✗ | ☠️ Contact Mortel |
| 🦔 Porc-épic | 1 | 2 | 1 | 0 | ✗ | 🦔 Piques pointues |

### Cartes obstacles

| Carte | ❤️ PV |
|---|:---:|
| 🪨 Rocher | 5 |
| 🌲 Sapin | 3 |

---

## 🧪 Tests

Les tests sont écrits avec **JUnit 4**. Pour les lancer :

```bash
# Compiler les tests
javac -cp out/:deps/junit-4.13.1.jar:deps/hamcrest-core-1.3.jar -d out/ tests/*.java

# Lancer les tests
java -cp out/:deps/junit-4.13.1.jar:deps/hamcrest-core-1.3.jar org.junit.runner.JUnitCore <NomDeLaClasseDeTest>
```

Les tests couvrent notamment :

- ✅ Attaque d'une carte et attaque globale en fin de tour
- ✅ Chacun des 6 pouvoirs spéciaux
- ✅ Pierre de sacrifice et transfert de pouvoir
- ✅ Mise à jour du score
- ✅ Placement de cartes et pioche
- ✅ Mise en place d'une partie
- ✅ Conditions de victoire/défaite d'une partie et du jeu

---

## 📁 Structure du projet

```
Inscryption/
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

## 👥 Auteurs

<div align="center">

| Baptiste Metz | Axel Siegel |
|:---:|:---:|
| [![GitHub](https://img.shields.io/badge/GitHub-Basyokoo-181717?style=for-the-badge&logo=github)](https://github.com/Basyokoo) | ![GitHub](https://img.shields.io/badge/GitHub-Bientôt_disponible-555555?style=for-the-badge&logo=github) |

</div>

<br/>

---

<div align="center">

*Projet SAé — IUT Robert Schuman, Université de Strasbourg*
<br/>
*Inspiré du jeu [Inscryption](https://store.steampowered.com/app/1092790/Inscryption/) de Daniel Mullins Games*

</div>

<img src="https://capsule-render.vercel.app/api?type=waving&color=0:4a0080,50:1a0a2e,100:0d0d0d&height=120&section=footer" width="100%"/>
