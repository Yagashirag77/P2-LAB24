# TP Smart Home — Docker

Le projet contient le backend Spring Boot dans `Smart_Home_back` et le frontend Angular dans `smartHome-front`.

## Lancer l'application

Démarrer Docker Desktop puis, dans ce dossier :

```powershell
docker compose build
docker compose up -d
docker compose ps
docker compose logs -f
```

- Frontend : http://localhost
- Backend : http://localhost:8085
- phpMyAdmin : http://localhost:8081 (serveur `mysql`, utilisateur `root`, mot de passe `root`)

Les ports 80, 8081, 8085 et 3307 doivent être libres. MySQL conserve ses données dans le volume `mysql_data`. Les identifiants de cet exercice sont destinés au développement local.

Pour arrêter les conteneurs : `docker compose down`. Pour supprimer également les données : `docker compose down -v`.

## Tester l'API

Une fois les services démarrés, ces commandes PowerShell lisent les données sans les modifier :

```powershell
Invoke-RestMethod http://localhost:8085/api/controller/categorie/
Invoke-RestMethod http://localhost:8085/api/controller/appareil/
```

Pour vérifier la validation, cette requête envoie un nom vide et doit retourner une erreur HTTP 400 :

```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8085/api/controller/categorie/ -ContentType 'application/json' -Body '{"label":""}'
```


## Améliorations et tests

Les noms vides et les appareils sans catégorie sont refusés. Une catégorie contenant des appareils ne peut pas être supprimée. La commande globale des états utilise PUT : elle modifie des données. Les erreurs renvoient un code HTTP et un message explicite.

Les cinq tests unitaires des règles métier s'exécutent pendant `docker compose build backend`. Pour les lancer localement, avec Java 17 et les dépendances Maven accessibles :

```powershell
cd Smart_Home_back
mvn -Dtest=ServiceRulesTest test
```

Le test d'intégration `AppareilApplicationTests` demande une base MySQL configurée ; il n'est pas exécuté par la construction Docker. `root` est uniquement le mot de passe de démonstration local. Pour utiliser une autre valeur, définir `MYSQL_PASSWORD` dans un fichier `.env` non suivi par Git avant la première création de la base. Changer cette variable ne modifie pas le mot de passe d'une base déjà initialisée dans le volume.

## Publication Git

Vérifier le contenu et les secrets avant publication, puis :

```powershell
git init
git status
git add .
git diff --cached
git commit -m "Containeriser et améliorer Smart Home"
git branch -M main
git remote add origin ADRESSE_DU_DEPOT
git push -u origin main
```
