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

Ejemplo de ejecución local:

```bash
export WEATHERAPI_KEY=tu_api_key
export MAIL_USERNAME=tu_correo@gmail.com
export MAIL_PASSWORD=tu_app_password

./mvnw spring-boot:run
```

La base de datos H2 se persiste en `./data/climalert.mv.db`. La consola H2 queda disponible en
`http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:file:./data/climalert`).

## Cómo correr los tests

```bash
./mvnw test
```

## Decisiones de diseño / supuestos

- La ubicación se fija vía la propiedad `weatherapi.location` (consigna pide ubicación fija, ej. CABA).
- `alertCondition` se calcula y persiste en el momento del fetch (no en el momento del análisis), de
  forma que el scheduler de alertas solo lee el último registro y decide en base a un booleano ya
  calculado.
- Se usa H2 en modo archivo (no en memoria) para que el historial climático sobreviva reinicios,
  acorde a "almacenarlos localmente para registro histórico".
- Los destinatarios de alerta están fijos según la consigna, pero configurables vía
  `alert.email.recipients` por si se necesitan ajustar en otro ambiente.

## Pendientes / mejoras posibles (fuera del alcance de esta iteración)

- Tests unitarios para `WeatherService` y `AlertService` (mockeando `RestTemplate` / `JavaMailSender`).
- Manejo de reintentos ante fallos transitorios de WeatherAPI.
- Traducción de `condition` (actualmente queda en inglés, tal como lo devuelve la API).
- Soporte para múltiples ubicaciones.

## Repositorio

Link al repositorio público: **`https://github.com/Brisaescobar/ClimaAlert`**