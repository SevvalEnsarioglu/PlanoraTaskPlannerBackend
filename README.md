# PlanoraTaskPlannerBackend

Video Oynatma Listesi Linki: https://bit.ly/blm4538-sevvalens-planora

---

## Proje Hakkında

Planora Backend, React Native mobil uygulamasına REST API hizmeti sunan bir Spring Boot uygulamasıdır. JWT tabanlı kimlik doğrulama, çok katmanlı mimari, AOP ile loglama ve OpenAPI (Swagger) dokümantasyonu içerir.

---

## Teknoloji Yığını

- **Framework:** Spring Boot 3
- **Dil:** Java 21
- **Veritabanı:** PostgreSQL (Spring Data JPA / Hibernate)
- **Güvenlik:** Spring Security + JWT (JJWT)
- **Mapping:** MapStruct
- **Dokümantasyon:** SpringDoc OpenAPI (Swagger UI — `/swagger-ui.html`)
- **Build:** Maven

---

## Proje Yapısı

```
PlanoraTaskPlannerBackend/
└── src/main/java/com/sevval/PlanoraTaskPlannerBackend/
    ├── aspect/        # LoggingAspect — servis metod çalışma süresi loglama (AOP)
    ├── config/        # CorsConfig, SwaggerConfig
    ├── controller/    # REST controller'lar
    │   ├── AuthController.java
    │   ├── TaskController.java
    │   ├── CategoryController.java
    │   ├── PriorityController.java
    │   ├── TagController.java
    │   ├── PomodoroController.java
    │   └── StatisticsController.java
    ├── exception/     # GlobalExceptionHandler, ApiError, özel exception'lar
    ├── mapper/        # MapStruct mapper'lar (Entity ↔ DTO)
    ├── model/
    │   ├── entity/    # User, Task, Category, Priority, Tag, Pomodoro, BaseEntity
    │   └── dto/       # Request ve Response DTO'lar
    ├── repository/    # Spring Data JPA repository'ler
    ├── security/      # JwtService, JwtAuthenticationFilter, SecurityConfig, SecurityUtil
    └── service/       # Servis arayüzleri ve impl/ altında uygulamaları
```

---

## API Endpoint'leri

| Grup | Endpoint Öneki | Açıklama |
|---|---|---|
| **Auth** | `POST /api/v1/auth/register` | Yeni kullanıcı kaydı |
| **Auth** | `POST /api/v1/auth/login` | JWT token ile giriş |
| **Auth** | `GET /api/v1/auth/me` | Giriş yapmış kullanıcı bilgisi |
| **Tasks** | `/api/v1/users/{userId}/tasks` | Görev CRUD; tarih ve durum filtresi |
| **Categories** | `/api/v1/users/{userId}/categories` | Kategori CRUD |
| **Priorities** | `/api/v1/users/{userId}/priorities` | Öncelik CRUD |
| **Tags** | `/api/v1/users/{userId}/tags` | Etiket CRUD |
| **Pomodoros** | `/api/v1/users/{userId}/pomodoros` | Pomodoro oturumu kayıt ve listeleme |
| **Statistics** | `/api/v1/users/{userId}/statistics` | Üretkenlik istatistikleri |
| **Statistics** | `/api/v1/users/{userId}/statistics/heatmap` | Günlük aktivite ısı haritası verisi |

---

## Temel Varlıklar (Entity)

| Entity | Açıklama |
|---|---|
| `User` | Kullanıcı hesabı (email, username, şifre hash) |
| `Task` | Görev (başlık, açıklama, bitiş tarihi, tamamlanma durumu) |
| `Category` | Kullanıcıya özel görev kategorisi |
| `Priority` | Kullanıcıya özel öncelik seviyesi (renk kodu ile) |
| `Tag` | Kullanıcıya özel etiket (renk kodu ile) |
| `Pomodoro` | Pomodoro odaklanma oturumu kaydı |

---

## Başlatma

```bash
# Uygulama özelliklerini ayarla (application.properties)
# - spring.datasource.url
# - jwt.secret
# - jwt.expiration-seconds

mvn spring-boot:run
```

Swagger UI: `http://localhost:8080/swagger-ui.html`
