## Contexte du projet

- Contexte : Une start-up technologique qui fournit des solutions bancaires et financières.
- Objectif : Une application qui permettrait aux clients de transférer de l'argent pour gérer leurs finances ou payer leurs amis.
- Problème à résoudre : transactions d’argent peu pratiques :
  - Les banques ont actuellement un processus long et peu pratique de configuration d'un transfert d'argent.
  - Les transferts bancaires demandent trop de données (numéro de compte, code SWIFT, etc.).
  - Il est difficile de rembourser ou de transférer de l'argent à des amis ou de la famille.
  - Il est difficile de transférer de l'argent vers des comptes pour des achats.
- Solution :
  - Développer une application où les utilisateurs pourraient s'enregistrer facilement avec une adresse e-mail ou un compte de réseaux sociaux.
  - Les utilisateurs peuvent ajouter des amis à leur réseau pour leur transférer de l'argent.
  - Passer par une conception simple pour rationaliser la procédure et éviter les soucis.

## Fonctionnalités requises

- Besoins/fonctionnalités :
  - Les nouveaux utilisateurs doivent pouvoir s'enregistrer à l'aide d'un identifiant e-mail unique.
  - Les utilisateurs doivent pouvoir se connecter à partir de leurs comptes dans la base de données.
  - Après la connexion, les utilisateurs peuvent ajouter des personnes à leurs listes à partir de leur adresse e-mail (si la personne existe déjà dans la base de données).
  - Un utilisateur peut verser de l'argent sur son compte dans notre application.
  - À partir du solde disponible, les utilisateurs peuvent effectuer des paiements à leurs amis enregistrés sur l'application.
  - À tout moment, les utilisateurs peuvent transférer l'argent vers leur compte bancaire.
  - À chaque transaction, nous prélevons un pourcentage de 0,5 % pour monétiser l’application.

## Diagramme de classe UML

![Diagramme de classe UML](Readme/uml.png)

##  Modèle physique de données

![Modèle physique de données](Readme/physique.png)

##  Présentation pdf du projet
![Consulter la présentation](Readme/presentation.pdf)
