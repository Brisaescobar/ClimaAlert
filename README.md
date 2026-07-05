# ClimAlert

Servicio autónomo (sin interfaz gráfica) desarrollado con **Spring Boot 4 / Java 21** que monitorea
condiciones climáticas a través de [WeatherAPI](https://www.weatherapi.com/) y envía alertas por
correo electrónico cuando detecta condiciones críticas.

## Contexto

Climalert se conecta periódicamente a un proveedor meteorológico externo, procesa los datos recibidos
y notifica por correo electrónico a las entidades correspondientes cuando se detectan condiciones
climáticas peligrosas o inusuales. En esta primera iteración se considera "alerta" a:

> **temperatura > 35°C y humedad > 60%**

## Funcionalidad

1. **Integración con WeatherAPI** — consulta el endpoint `/current.json` para una ubicación fija
   (CABA por defecto).
2. **Obtención y almacenamiento periódico** — cada **5 minutos** obtiene el clima actual y lo
   persiste en una base local (H2) para historial y análisis posterior.
3. **Análisis de alertas** — cada **1 minuto** evalúa el último registro disponible.
4. **Notificación por correo** — si se detecta una condición crítica, envía un mail con el detalle
   completo del clima a:
    - admin@clima.com
    - emergencias@clima.com
    - meteorologia@clima.com

## Arquitectura

```
client/      -> WeatherApiClient + DTOs de la respuesta de WeatherAPI
config/      -> Configuración de beans (RestTemplate)
diagramas/   -> Diagramas de secuencia de los dos flujos 
model/       -> Entidad JPA WeatherData
repository/  -> WeatherDataRepository (Spring Data JPA)
service/     -> WeatherService (fetch + persistencia) y AlertService (envío de mails)
scheduler/   -> WeatherFetchScheduler (cada 5 min) y AlertScheduler (cada 1 min)
```



## Requisitos

- Java 21
- Maven (o usar el wrapper `./mvnw` incluido)
- Una API key gratuita de [WeatherAPI](https://www.weatherapi.com/)
- Una cuenta de correo SMTP (ej. Gmail con contraseña de aplicación) para el envío de alertas

## Configuración

La aplicación lee credenciales desde variables de entorno (nunca se hardcodean en el repo):

| Variable | Descripción |
|---|---|
| `WEATHERAPI_KEY` | API key de WeatherAPI |
| `MAIL_HOST` | Host SMTP (default `smtp.gmail.com`) |
| `MAIL_PORT` | Puerto SMTP (default `587`) |
| `MAIL_USERNAME` | Usuario / remitente del correo |
| `MAIL_PASSWORD` | Contraseña / contraseña de aplicación |

### Opción A — Correr desde terminal (bash / Git Bash)

Creá un archivo `env.sh` (nunca lo subas al repo, agregalo a `.gitignore`) con:

```bash
export WEATHERAPI_KEY=tu_api_key
export MAIL_USERNAME=tu_correo@gmail.com
export MAIL_PASSWORD=tu_app_password
```

> ⚠️ El `export` en cada línea es obligatorio. Sin él, la variable solo existe en la
> terminal actual y **no se propaga** al proceso de Maven/Java, lo que produce el error
> `Could not resolve placeholder 'WEATHERAPI_KEY'`.

Y en la misma sesión de terminal (el `source` y el `run` deben ejecutarse sin cerrarla entre medio):

```bash
source env.sh
./mvnw spring-boot:run
```

### Opción B — Correr desde el botón ▶ de IntelliJ

IntelliJ lanza un proceso propio que **no hereda** las variables exportadas en tu terminal, así que
hay que cargarlas directamente en la Run Configuration:

`Run → Edit Configurations... → ClimaAlertApplication → Environment variables` y agregar:

```
WEATHERAPI_KEY=tu_api_key;MAIL_USERNAME=tu_correo@gmail.com;MAIL_PASSWORD=tu_app_password
```

La base de datos H2 se persiste en `./data/climalert.mv.db`. La consola H2 queda disponible en
`http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:file:./data/climalert`).


## Decisiones de diseño 
Link a docs con justificacion [Justificaciones-climalert](https://docs.google.com/document/d/1QmxJ-efW8wVBGQEfFQyhR2jRu1s8baqjaIb6pUa-bcc/edit?usp=drive_link)

---
## Autor 
Escobar Brisa 