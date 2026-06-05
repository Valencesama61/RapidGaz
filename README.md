# RapidGaz — API REST Backend

API REST pour la plateforme **RapidGaz**, permettant la gestion des vendeurs de gaz, de leurs stocks, de leurs produits et de leur localisation. Construite avec **Spring Boot 4** et **Java 21**.

---

## Table des matières

- [Technologies](#technologies)
- [Architecture du projet](#architecture-du-projet)
- [Prérequis](#prérequis)
- [Installation et configuration](#installation-et-configuration)
- [Lancer l'application](#lancer-lapplication)
- [Variables d'environnement](#variables-denvironnement)
- [Sécurité et authentification](#sécurité-et-authentification)
- [Rôles et autorisations](#rôles-et-autorisations)
- [Endpoints de l'API](#endpoints-de-lapi)
- [Documentation Swagger](#documentation-swagger)
- [Initialisation des données](#initialisation-des-données)
- [Structure des fichiers](#structure-des-fichiers)

---

## Technologies

| Technologie | Version |
|---|---|
| Java | 21 |
| Spring Boot | 4.0.6 |
| Spring Security | inclus dans Boot |
| Spring Data JPA | inclus dans Boot |
| PostgreSQL | driver runtime |
| JWT (jjwt) | 0.12.6 |
| Lombok | inclus dans Boot |
| SpringDoc OpenAPI | 3.0.2 |
| Maven | wrapper inclus |

---

## Architecture du projet

```
src/main/java/com/sama/RapidGaz/
├── configs/
│   ├── AdminUserDetailsService.java    # UserDetailsService pour les admins
│   ├── ApplicationConfiguration.java   # Beans de sécurité (PasswordEncoder, AuthProvider)
│   ├── DataInitializer.java            # Seed au démarrage (super admin + catalogue)
│   ├── JwtAuthenticationFilter.java    # Filtre JWT injecté dans la chaîne de sécurité
│   └── SecurityConfiguration.java      # Règles d'accès, CORS, session stateless
│
├── controllers/
│   ├── admin/
│   │   ├── AdminAuthController.java    # POST /api/admin/auth/login
│   │   └── AdminController.java        # Gestion vendeurs, catalogue, stats
│   └── v1/
│       ├── AuthController.java         # Inscription / connexion vendeur
│       ├── GasProductController.java   # CRUD produits gaz du vendeur
│       ├── GasSellerController.java    # Profil vendeur
│       ├── GasSellerLocationController.java  # Localisation du vendeur
│       ├── GasStockController.java     # Gestion du stock
│       └── PublicController.java       # Recherche publique (sans auth)
│
├── dtos/          # Objets de transfert en entrée (requêtes)
├── responses/     # Objets de transfert en sortie (réponses)
├── mappers/       # Conversion entité ↔ DTO / Response
├── model/         # Entités JPA (Admin, GasSeller, GasProduct, GasStock, GasSellerLocation...)
├── repository/    # Interfaces Spring Data JPA
├── services/      # Logique métier
├── enums/         # AdminRole, GasBrand, GasSize
└── exceptions/    # GlobalExceptionHandler, NotFoundException, NotAuthorizedException...
```

---

## Prérequis

- **Java 21** ou supérieur
- **Maven** (ou utiliser le wrapper `./mvnw` fourni)
- **PostgreSQL** (base de données `gas_db` accessible)

---

## Installation et configuration

### 1. Cloner le dépôt

```bash
git clone <url-du-repo>
cd RapidGaz
```

### 2. Créer la base de données PostgreSQL

```sql
CREATE DATABASE gas_db;
```

### 3. Configurer les variables locales

Copier le fichier d'exemple et remplir les valeurs :

```bash
cp src/main/resources/application-local.properties.example \
   src/main/resources/application-local.properties
```

Puis éditer `application-local.properties` :

```properties
DB_PASSWORD=votre_mot_de_passe_postgresql
JWT_SECRET=votre_cle_secrete_jwt_base64_encodee
SUPER_ADMIN_EMAIL=votre_email_super_admin
SUPER_ADMIN_PASSWORD=votre_mot_de_passe_super_admin
```

> `application-local.properties` est dans le `.gitignore` — il ne sera jamais commité.

---

## Lancer l'application

```bash
./mvnw spring-boot:run
```

L'API est disponible sur **http://localhost:8080** par défaut.

Pour builder un JAR exécutable :

```bash
./mvnw clean package
java -jar target/RapidGaz-0.0.1-SNAPSHOT.jar
```

---

## Variables d'environnement

Toutes les variables sensibles sont chargées depuis `application-local.properties` (ignoré par git).

| Variable | Description |
|---|---|
| `DB_PASSWORD` | Mot de passe PostgreSQL |
| `JWT_SECRET` | Clé secrète JWT encodée en Base64 |
| `SUPER_ADMIN_EMAIL` | Email du compte super admin |
| `SUPER_ADMIN_PASSWORD` | Mot de passe du compte super admin |

Pour générer une clé JWT sécurisée :

```bash
openssl rand -base64 32
```

---

## Sécurité et authentification

L'API utilise **JWT stateless** (pas de session serveur).

- Le token est transmis dans l'en-tête HTTP : `Authorization: Bearer <token>`
- Durée de vie du token d'accès : **1 heure** (3 600 000 ms)
- Durée de vie du refresh token : **7 jours** (604 800 000 ms)
- Les mots de passe sont hashés avec **BCrypt**
- Le filtre `JwtAuthenticationFilter` valide le token à chaque requête protégée

---

## Rôles et autorisations

| Rôle | Accès |
|---|---|
| **Public** (non authentifié) | `/api/public/**`, `/api/v1/auth/**`, `/api/admin/auth/**`, Swagger |
| **SELLER** (vendeur authentifié) | `/api/v1/**` — profil, produits, stock, localisation |
| **ADMIN** | `/api/admin/sellers/**`, `/api/admin/catalog/**`, `/api/admin/stats` |
| **SUPER_ADMIN** | Tout ce qu'ADMIN peut faire + `/api/admin/users/**` (gestion des admins) |

---

## Endpoints de l'API

### Authentification publique — Vendeur

| Méthode | Route | Description |
|---|---|---|
| `POST` | `/api/v1/auth/register` | Inscription d'un vendeur |
| `POST` | `/api/v1/auth/login` | Connexion vendeur, retourne un JWT |

### Authentification — Admin

| Méthode | Route | Description |
|---|---|---|
| `POST` | `/api/admin/auth/login` | Connexion admin / super admin |

### Recherche publique

| Méthode | Route | Description |
|---|---|---|
| `GET` | `/api/public/**` | Recherche de vendeurs et produits (sans auth) |

### Vendeur (authentifié)

| Méthode | Route | Description |
|---|---|---|
| `GET/PUT` | `/api/v1/seller` | Profil du vendeur connecté |
| `GET/POST/PUT/DELETE` | `/api/v1/products` | Gestion des produits gaz |
| `GET/POST/PUT` | `/api/v1/stock` | Gestion du stock |
| `GET/POST/PUT` | `/api/v1/location` | Localisation du point de vente |

### Administration

| Méthode | Route | Description |
|---|---|---|
| `GET` | `/api/admin/sellers` | Liste des vendeurs |
| `GET` | `/api/admin/sellers/{id}` | Détail d'un vendeur |
| `GET` | `/api/admin/stats` | Statistiques globales |
| `GET/POST/DELETE` | `/api/admin/catalog` | Gestion du catalogue (marques, tailles) |
| `GET/POST/DELETE` | `/api/admin/users` | Gestion des admins *(SUPER_ADMIN uniquement)* |

---

## Documentation Swagger

L'interface Swagger UI est accessible sans authentification :

```
http://localhost:8080/swagger-ui/index.html
```

La spec OpenAPI JSON est disponible sur :

```
http://localhost:8080/v3/api-docs
```

---

## Initialisation des données

Au démarrage, `DataInitializer` exécute automatiquement :

**Super Admin** — créé ou mis à jour depuis les variables d'environnement :
- Email : `SUPER_ADMIN_EMAIL`
- Mot de passe : `SUPER_ADMIN_PASSWORD`
- Rôle : `SUPER_ADMIN`

**Catalogue de référence** (si absent) :
- Marques : `ORYX`, `JNP`, `TOTAL`
- Tailles : `KG_6`, `KG_12`, `KG_25`

---

## Structure des fichiers

```
RapidGaz/
├── src/
│   ├── main/
│   │   ├── java/com/sama/RapidGaz/   # Code source
│   │   └── resources/
│   │       ├── application.properties              # Config principale (commitée)
│   │       ├── application-local.properties        # Variables sensibles (gitignorée)
│   │       └── application-local.properties.example # Modèle à copier
│   └── test/                                       # Tests
├── .mvn/wrapper/                                   # Maven wrapper
├── mvnw / mvnw.cmd                                 # Scripts Maven
├── pom.xml                                         # Dépendances Maven
└── .gitignore
```
