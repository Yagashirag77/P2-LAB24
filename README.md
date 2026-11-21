# Smart Home — TP Docker

Application de gestion d'appareils et de catégories, avec un backend Spring Boot, un frontend Angular et une base MySQL.

## Structure

- `Smart_Home_back/` : API Spring Boot et Dockerfile Maven/Java
- `smartHome-front/` : application Angular servie par Nginx
- `docker-compose.yml` : configuration des quatre services

## Démarrage

Avec Docker Desktop lancé, depuis la racine du projet :

```bash
docker compose up -d --build
docker compose ps
```

| Service | Adresse |
| --- | --- |
| Application | http://localhost |
| API | http://localhost:8085 |
| phpMyAdmin | http://localhost:8081 |
| MySQL | localhost:3307 |

Dans phpMyAdmin, utiliser `root` / `root` par défaut. Le port MySQL 3307 est utilisé car le port 3306 était déjà occupé sur ma machine. Les données sont conservées dans un volume Docker.

Pour arrêter les services :

```bash
docker compose down
```

## Tests

La construction du backend lance les tests unitaires des règles métier :

```bash
docker compose build backend
```

Pour consulter les données de l'API dans PowerShell :

```powershell
Invoke-RestMethod http://localhost:8085/api/controller/categorie/
Invoke-RestMethod http://localhost:8085/api/controller/appareil/
```

Le test d'intégration Spring demande une base MySQL configurée et n'est pas lancé pendant le build Docker.

## Configuration

Le mot de passe `root` est prévu pour le TP en local. On peut définir `MYSQL_PASSWORD` dans un fichier `.env` à la racine avant le premier lancement. Si la base existe déjà, modifier cette variable ne change pas son mot de passe : MySQL conserve sa configuration dans le volume.
Screen:
<img width="2496" height="993" alt="Capture d&#39;écran 2026-11-21 104801" src="https://github.com/user-attachments/assets/ea091bff-449b-4671-8bb1-d664edba6120" />
