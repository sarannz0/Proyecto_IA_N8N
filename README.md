
# 🏗️ Estructura Completa del Proyecto Weather App

Este documento detalla la organización de carpetas y archivos de todo el ecosistema, que integra un Backend en Spring Boot, un Frontend estático y un servidor MCP para IA.

## 📂 Árbol de Directorios

```text
Proyecto_IA_N8N/
├── 📁 backend/                        # Lógica del Servidor (Java Spring Boot)
│   ├── 📁 src/main/java/com/ponscio_studio/n8n/
│   │   ├── 📁 application/            # Capa de Orquestación
│   │   │   ├── 📁 customExceptions/   # Excepciones propias del flujo (e.g. WorkFlowDontStartsException)
│   │   │   ├── 📁 dto/                # Objetos de Transferencia (WeatherRequest, WeatherResponse)
│   │   │   ├── 📁 mapper/             # Conversión Dominio <-> Entidad (WeatherMapper)
│   │   │   └── 📁 service/            # Casos de Uso (WorkFlow.java - Conexión con n8n)
│   │   ├── 📁 domain/                 # Núcleo del Negocio (Puro Java)
│   │   │   ├── 📁 model/              # Modelos de dominio (Weather)
│   │   │   └── 📁 port/               # Interfaces para repositorios (WeatherRepository)
│   │   └── 📁 infrastructure/         # Adaptadores Técnicos
│   │       ├── 📁 entrypoint/         # Controladores REST (WeatherController)
│   │       └── 📁 persistence/        # Implementación de BDD (JPA, Entities)
│   │           ├── 📁 entity/         # Entidades de Base de Datos (WeatherEntity)
│   │           └── 📁 Exception/      # Manejo Global de Errores (HandlerExceptionController)
│   ├── 📄 Dockerfile                  # Configuración para contenerizar el Backend
│   └── 📄 pom.xml                     # Dependencias Maven (Spring Web, Data JPA, Lombok)
│
├── 📁 frontend/                       # Interfaz de Usuario (SPA Vanilla JS)
│   ├── 📁 assets/                     # Recursos estáticos (imágenes, iconos)
│   ├── 📁 css/
│   │   └── 📄 styles.css              # Estilos (Glassmorphism, Responsive)
│   ├── 📁 js/
│   │   └── 📄 App.js                  # Lógica cliente, fetch al backend y DOM manipulation
│   ├── 📄 index.html                  # Punto de entrada HTML
│   └── 📄 README.md                   # Documentación específica del frontend
│
├── 📁 mcp/                            # Model Context Protocol (Integración IA/n8n)
│   ├── 📄 Dockerfile                  # Entorno Node.js Alpine
│   ├── 📄 docker-compose.yml          # Orquestación local (n8n + MCP)
│   ├── 📄 index.js                    # Servidor SSE y definición de Tools (Open-Meteo)
│   ├── 📄 package.json                # Dependencias (@modelcontextprotocol/sdk, zod)
│   └── 📄 README.md                   # Documentación de herramientas y conexión
│
└── 📄 WeatherApp_n8n.json             # Workflow exportado de n8n (Lógica de IA Gemini)
```

---

## 🧩 Descripción de Módulos

### 1. Backend (Hexagonal Architecture)
El cerebro de la aplicación que expone la API REST. Sigue estrictamente la separación de preocupaciones:
*   **Domain:** Define *qué* es el clima y *cómo* se debe guardar (interfaces), sin saber de bases de datos.
*   **Application:** Coordina el flujo. Recibe la petición, llama al Webhook de n8n (`WorkFlow.java`) y procesa la respuesta.
*   **Infrastructure:** Implementa los detalles "sucios": controladores HTTP, conexión a base de datos MySQL/H2 y manejo de excepciones HTTP.

### 2. Frontend (Cliente Web)
Interfaz ligera y rápida.
*   Captura la ubicación y fecha del usuario.
*   Se comunica con el Backend en el puerto `6969`.
*   Muestra datos estructurados: Temperatura, Viento, y la **Recomendación de Vestimenta** generada por la IA.

### 3. MCP Server (Middleware IA)
Un servidor especializado que actúa como "herramienta" para el orquestador n8n.
*   **Protocolo:** Usa Server-Sent Events (SSE) para mantener una conexión viva con n8n.
*   **Función:** `get_weather`. Obtiene datos crudos de Open-Meteo, validando coordenadas con `Zod`.
*   **Objetivo:** Permitir que el modelo de lenguaje (Gemini) en n8n tenga acceso a datos reales y actualizados del clima.

### 4. Workflow n8n (`WeatherApp_n8n.json`)
El flujo visual que conecta todo:
1.  **Webhook:** Recibe la solicitud del Backend.
2.  **MCP Tool:** Consulta el clima real usando el servidor MCP.
3.  **Gemini AI:**
    *   Analiza los datos numéricos.
    *   Genera una recomendación de vestimenta ("Usa chaqueta, va a llover").
    *   Determina el ENUM del clima (SOLEADO, LLUVIOSO, etc.).
4.  **Response:** Devuelve el JSON enriquecido al Backend.

---


# 🌤️ Backend Weather App - Arquitectura Hexagonal

Este repositorio contiene la lógica del lado del servidor para la aplicación de clima. El proyecto está construido con **Java** y **Spring Boot**, siguiendo los principios de la **Arquitectura Hexagonal (Puertos y Adaptadores)** para desacoplar la lógica de negocio de las implementaciones externas como bases de datos o servicios de terceros (n8n).

---

## 📂 Estructura del Proyecto

El código está organizado en tres capas principales dentro de `src/main/java/com/ponscio_studio/n8n`:

```text
backend/
├── application/           # Capa de Aplicación (Orquestación)
│   ├── customExceptions/  # Excepciones de negocio
│   ├── dto/               # Data Transfer Objects (Request/Response)
│   ├── mapper/            # Conversión entre Entidades y Modelos
│   └── service/           # Lógica de negocio y Casos de Uso (WorkFlow)
├── domain/                # Capa de Dominio (Núcleo)
│   ├── model/             # Modelos de dominio puros (Weather)
│   └── port/              # Interfaces (Puertos) para repositorios/servicios
└── infrastructure/        # Capa de Infraestructura (Adaptadores)
    ├── persistence/       # Implementación de bases de datos
    │   ├── entity/        # Entidades JPA/Hibernate
    │   └── Exception/     # Manejo global de errores (ControllerAdvice)
    └── ...

# 🌤️ Backend Weather App - Arquitectura Hexagonal

Este repositorio contiene la lógica del lado del servidor para la aplicación de clima. El proyecto está construido con **Java** y **Spring Boot**, siguiendo los principios de la **Arquitectura Hexagonal (Puertos y Adaptadores)** para desacoplar la lógica de negocio de las implementaciones externas como bases de datos o servicios de terceros (n8n).

---

## 🏛️ Arquitectura y Capas

### 1. Domain (Dominio)
Es el núcleo de la aplicación. No tiene dependencias de frameworks externos ni de la base de datos.

*   **Modelos (`model`):** Contiene las clases puras de Java que representan las entidades del negocio (ej. `Weather`).
*   **Puertos (`port`):** Define interfaces que la infraestructura debe implementar.
    *   `WeatherRepository`: Define las operaciones permitidas sobre la persistencia (Guardar, Buscar, Eliminar), permitiendo que el dominio ignore si se usa MySQL, Mongo o memoria.

### 2. Application (Aplicación)
Contiene la lógica que orquesta los flujos de datos y conecta el mundo exterior con el dominio.

*   **Services / Use Cases (`service`):**
    *   `WorkFlow.java`: Actúa como un servicio principal que se comunica con el sistema de orquestación **n8n**. Utiliza `RestTemplate` para enviar una petición POST al webhook de n8n, delegando la obtención compleja de datos climáticos al flujo externo.
*   **Mappers (`mapper`):**
    *   `WeatherMapper.java`: Se encarga de transformar los objetos de Dominio (`Weather`) a Entidades de Infraestructura (`WeatherEntity`) y viceversa. Esto asegura que los cambios en la base de datos no afecten al núcleo del negocio.
*   **DTOs (`dto`):** Objetos utilizados para recibir datos desde el controlador o enviarlos a n8n (`WeatherRequest`, `WeatherRequestFromWorkFlow`).

### 3. Infrastructure (Infraestructura)
Maneja los detalles técnicos, la persistencia y la exposición de la API.

*   **Persistence (`persistence`):** Implementa los repositorios definidos en el dominio usando tecnologías como Spring Data JPA.
*   **Entities (`entity`):** Representaciones de las tablas en la base de datos (`WeatherEntity`).
*   **Exception Handling:** Manejo centralizado de errores HTTP.

---

## ⚙️ Flujo de Trabajo (The Flow)

El flujo principal para obtener y procesar el clima es el siguiente:

1.  **Entrada:** Un controlador (Controller) recibe una petición HTTP desde el Frontend.
2.  **Procesamiento:**
    *   El servicio `WorkFlow` recibe la solicitud.
    *   Se construye un objeto `WeatherRequest`.
    *   El sistema hace una llamada externa al **Webhook de n8n** (`http://localhost:5678/webhook-test/...`).
3.  **Persistencia (Opcional/Paralela):**
    *   Utilizando el `WeatherMapper`, los datos pueden ser convertidos y guardados en la base de datos a través de la implementación de `WeatherRepository`.
4.  **Respuesta:** El resultado procesado se devuelve al cliente.

---

## 🛡️ Manejo de Errores (Error Handling)

El proyecto implementa un manejo de excepciones centralizado y robusto utilizando `@RestControllerAdvice` en la capa de infraestructura. Esto garantiza que el cliente siempre reciba respuestas JSON estructuradas, incluso cuando ocurren fallos.

**Ubicación:** `infrastructure/persistence/Exception/HandlerExceptionController.java`

### Estructura de Respuesta de Error (`ErrorCustom`)
Cada vez que ocurre un error, la API devuelve un objeto con:
*   `timestamp`: Fecha y hora del error.
*   `message`: Descripción legible del problema.
*   `status`: Código HTTP.
*   `errors`: Mapa de detalles (útil para validaciones de formularios).

### Tipos de Errores Manejados
1.  **Validación de Datos (`MethodArgumentNotValidException`)**
    *   **Causa:** Cuando los datos enviados en el JSON no cumplen con las anotaciones de validación (ej. `@NotNull`, `@Size`) en los DTOs.
    *   **Respuesta:** Retorna un HTTP **400 (Bad Request)** y un mapa detallando qué campo falló y por qué.

2.  **Errores de Negocio / Flujo (`WorkFlowDontStartsException`)**
    *   **Causa:** Esta es una excepción personalizada (`customExceptions`). Se lanza explícitamente en `WorkFlow.java` cuando falla la comunicación con n8n (ej. `restTemplate` falla al conectar).
    *   **Respuesta:** Retorna un HTTP **500 (Internal Server Error)** indicando que el flujo de trabajo no pudo iniciarse.

---

## 🎮 Detalles del Controlador

El controlador expone el endpoint `/weather/get`, recibe el JSON del frontend, lo mapea al DTO `WeatherRequest` y delega el procesamiento a la clase `WorkFlow` en la capa de aplicación.

*   **Ruta (`@RequestMapping`):** Se configura en `/weather` y el método en `/get` para coincidir con `http://localhost:6969/weather/get` definido en `App.js`.
*   **Inyección (`@RequiredArgsConstructor`):** Lombok genera el constructor para inyectar automáticamente el servicio `WorkFlow`.
*   **CORS (`@CrossOrigin`):** Necesario porque tu frontend probablemente corre en un puerto distinto al backend (6969).
*   **Validación (`@Valid`):** Activa las validaciones (como `@NotNull`) que tengas definidas en tu DTO `WeatherRequest`, disparando el `HandlerExceptionController` si fallan.
*   **Comunicación:** Llama a `workFlow.getWeatherData(request)` (asumiendo que este es el método público de tu servicio `WorkFlow` que inicia la llamada a n8n).

---

## 🚀 Tecnologías
*   **Java 17+**
*   **Spring Boot 3.x** (Web, Data JPA)
*   **Maven** (Gestión de dependencias)
*   **Lombok** (Reducción de código repetitivo)
*   **Docker** (Para despliegue de n8n y base de datos)

## 📦 Cómo Ejecutar
Desde la raíz de la carpeta `backend`:

```bash
# Ejecutar con Maven Wrapper (Windows)
./mvnw.cmd spring-boot:run

# Ejecutar con Maven Wrapper (Linux/Mac)
./mvnw spring-boot:run
```



# 🌤️ Frontend Weather App

Este directorio contiene la interfaz de usuario (UI) y la lógica del lado del cliente para la aplicación de clima. Está construida con tecnologías web estándar (**HTML, CSS, Vanilla JavaScript**) y se encarga de capturar la ubicación del usuario, comunicarse con el Backend (Spring Boot) y renderizar las recomendaciones generadas por la IA.

---

## 📂 Estructura del Proyecto

```text
frontend/
├── assets/            # Imágenes, íconos y recursos estáticos
├── css/               # Estilos de la aplicación
│   └── styles.css     # Diseño responsive y temas
├── js/                # Lógica de la aplicación
│   └── App.js         # Comunicación con API y manipulación del DOM
└── index.html         # Estructura principal de la página
```

---

## ⚙️ Flujo de la Aplicación (Frontend Flow)

El núcleo de la lógica se encuentra en el archivo `js/App.js`. A continuación se describe el ciclo de vida de una petición de clima:

### 1. Inicialización y Valores por Defecto
El sistema define constantes para una ubicación por defecto (Bucaramanga) en caso de que no se proporcionen coordenadas:
*   `DEFAULT_LAT`: 7.1255169
*   `DEFAULT_LON`: -73.1182624

### 2. Captura de Datos (Input)
La función principal es `cargarDatosDelClima(lat, lon, place, date)`. Esta función es invocada globalmente (por ejemplo, desde un mapa interactivo o un buscador en el HTML).
*   Utiliza variables globales `window.__selectedCity` y `window.__selectedCountry` para mantener el contexto de la ubicación seleccionada.

### 3. Comunicación con el Backend
Se realiza una petición HTTP asíncrona (`fetch`) al servidor:

*   **Endpoint:** `POST http://localhost:6969/weather/get`
*   **Headers:** `Content-Type: application/json`
*   **Body (JSON):**
    ```json
    {
      "place": "Nombre de la ciudad",
      "latitud": "7.12...",
      "longitud": "-73.11...",
      "date": "2023-10-27"
    }
    ```

### 4. Manejo de Respuesta y Errores
*   **Éxito:** Si el backend responde correctamente (HTTP 200), se procesa el JSON recibido.
*   **Error:** Si hay un fallo (HTTP 4xx/5xx), el código captura el mensaje de error enviado por el servidor (`errorData.message`) y lo lanza como excepción para mostrarlo en consola o alertar al usuario.

### 5. Renderizado en el DOM (UI Update)
Una vez recibidos los datos de la IA y n8n, el script actualiza dinámicamente los elementos HTML identificados por sus IDs:

| ID en HTML | Dato Asignado | Descripción |
| :--- | :--- | :--- |
| `temp-principal` | `data.temperature` | Muestra la temperatura actual (°C). |
| `temp-sensacion` | `data.weatherCondition` | Muestra la condición climática (ej. "Soleado"). |
| `wind-speed` | `data.windSpeed` | Muestra la velocidad del viento (Km/h). |
| `hora-actual` | `data.PONER_AQUI_LLAVE_HORA` | Actualiza la hora local del reporte. |
| `recomendacion-texto` | `data.recommendedClothes` | Inserta el texto generado por Gemini sobre qué vestir. |

---

## 🛠️ Funciones Principales (`App.js`)

### `window.cargarDatosDelClima`
Es la función expuesta globalmente que orquesta todo el proceso.

```javascript
window.cargarDatosDelClima = async (lat, lon, place, date) => {
    // 1. Prepara las coordenadas (usa default si son nulas)
    // 2. Construye el payload JSON
    // 3. Ejecuta fetch() al backend en puerto 6969
    // 4. Parsea la respuesta y actualiza el HTML
    // 5. Maneja errores de red o de API
};
```

---

## 🚀 Cómo Ejecutar

Al ser un frontend estático (sin necesidad de compilación como React/Angular), puedes ejecutarlo de varias formas:

1.  **Opción Recomendada (VS Code):** Instala la extensión "Live Server", haz clic derecho en `index.html` y selecciona "Open with Live Server".
2.  **Opción Manual:** Abre el archivo `index.html` directamente en tu navegador (aunque algunas funcionalidades de red pueden requerir un servidor local por políticas CORS).

> **Nota:** Asegúrate de que el Backend esté corriendo en el puerto `6969` antes de realizar consultas.


# 🌤️ Weather MCP Server - Integración n8n

Este directorio contiene un servidor implementado bajo el estándar **MCP (Model Context Protocol)**. Su función es actuar como un "driver" o adaptador inteligente que permite a orquestadores de IA (como **n8n**) invocar funciones de código (Tools) de manera estandarizada.

En este proyecto, el MCP encapsula la lógica para consultar la API meteorológica de **Open-Meteo**, manejando la validación de parámetros y el transporte de datos.

---

## 📂 Estructura y Archivos

*   **`index.js`**: El corazón del servidor. Contiene la definición de las herramientas (`tools`), la validación de esquemas con `zod` y la configuración del servidor HTTP/SSE.
*   **`Dockerfile`**: Define el entorno de ejecución (Node.js 18+ alpine) para contenedores ligeros.
*   **`docker-compose.yml`**: Orquesta el despliegue conjunto con n8n, asegurando que ambos compartan la misma red Docker.

---

## 🛠️ Tecnologías y Librerías Usadas

1.  **`@modelcontextprotocol/sdk`**:
    *   Librería oficial para crear servidores MCP.
    *   **`McpServer`**: Clase principal que gestiona el registro de herramientas.
    *   **`SSEServerTransport`**: Manejador de transporte para **Server-Sent Events**, necesario para mantener una conexión viva con n8n.
2.  **`zod`**:
    *   Utilizado para definir y validar estrictamente los tipos de datos de entrada (`latitude`, `longitude`) antes de ejecutar la lógica.
3.  **`http` (Nativo Node.js)**:
    *   Levanta el servidor web que expone los endpoints `/sse`, `/messages` y `/health`.

---

## ⚙️ Flujo Interno (The Flow)

El servidor no funciona con el modelo clásico de Petición-Respuesta (Request-Response) única, sino que establece un canal de eventos.

### 1. Establecimiento de Conexión (Handshake)
*   **n8n** realiza una petición `GET` al endpoint `/sse`.
*   El MCP responde con headers `Content-Type: text/event-stream`.
*   Se crea una **Sesión de Transporte** única. A partir de aquí, el servidor puede "empujar" información a n8n.

### 2. Invocación de Herramienta (Tool Call)
*   Cuando el flujo de n8n llega al nodo "MCP Client", envía una petición `POST` al endpoint `/messages?sessionId=...`.
*   El cuerpo del mensaje es un JSON-RPC indicando que quiere ejecutar la tool `get_weather`.

### 3. Ejecución Lógica (`index.js`)
*   El servidor intercepta la solicitud.
*   **Validación:** Usa `zod` para verificar que `latitude` y `longitude` sean números.
*   **Consumo API:** Construye la URL de Open-Meteo solicitando datos actuales, pasados (7 días) y pronósticos.
*   **Fetch:** Realiza la petición HTTP externa.

### 4. Respuesta
*   El JSON crudo de Open-Meteo se empaqueta en un formato estándar MCP (`Content: text`).
*   Se envía de vuelta a n8n a través de la conexión SSE abierta.

---

## 🧰 Herramientas Disponibles (Tools)

| Endpoint | Método | Descripción |
|----------|--------|-------------|
| `/sse` | GET | Conexión SSE — n8n se conecta aquí |
| `/messages?sessionId=...` | POST | Mensajes MCP — n8n envía los tool calls |
| `/health` | GET | Health check del servidor |
Actualmente el servidor expone una única herramienta:

---
### `get_weather`
*   **Descripción:** Consulta el clima histórico y pronóstico.
*   **Parámetros:**
    *   `latitude` (number): Latitud decimal.
    *   `longitude` (number): Longitud decimal.
*   **Retorno:** Objeto JSON completo con arrays de `temperature_2m`, `rain_sum`, `wind_speed`, etc.

## 🧰 Tool disponible: `get_weather`

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `latitude` | number | Latitud, ej: `7.1254` |
| `longitude` | number | Longitud, ej: `-73.1198` |

**Datos que devuelve (últimos 7 días + pronóstico):**
- `temperature_2m_max` / `temperature_2m_min`
- `apparent_temperature_max` / `apparent_temperature_min`
- `rain_sum`, `showers_sum`, `snowfall_sum`
- `precipitation_sum`
- `wind_speed_10m_max`, `wind_gusts_10m_max`

---

## 🚀 Despliegue y Ejecución

El proyecto incluye un `docker-compose` listo para producción.

### 1. Construir e Iniciar
```

docker-compose up -d --build
```

### 2. Verificar Estado
Puedes consultar el endpoint de salud para verificar que el servicio está activo:
```bash
curl http://localhost:3000/health
```
Respuesta esperada:
```json
{ "status": "ok", "tools": ["get_weather"] }
```


### 3. Configuración en n8n
Para conectar este servidor en n8n:
1.  Crear credencial **MCP Client**.
2.  Tipo de conexión: **SSE**.
3.  URL: `http://weather-mcp:3000/sse` (si se usa la red interna de Docker) o `http://localhost:3000/sse` (si n8n corre en local fuera de Docker).

---


# 🚀 Guía de Ejecución Completa

Sigue estos pasos en orden para levantar el proyecto **Weather App** (MCP + n8n + Backend + Frontend).

---

## 1. Infraestructura (Docker)
Primero, necesitamos levantar el servidor **MCP** y **n8n**.

1. Abre una terminal en la carpeta `mcp`:
   ```bash
   cd mcp
   ```
2. Ejecuta Docker Compose:
   ```bash
   docker-compose up -d --build
   ```
3. Verifica que estén corriendo:
   *   **MCP:** http://localhost:3000/health (Debe responder `{"status":"ok"}`)
   *   **n8n:** http://localhost:5678

---

## 2. Configuración de n8n
Una vez n8n esté activo, debes configurar el flujo.

1.  **Abrir n8n:** Ve a http://localhost:5678 (Usuario/Pass definidos en `docker-compose.yml`: `admin` / `admin123`).
2.  **Configurar Credencial MCP:**
    *   Ve a **Settings** > **Credentials** > **Add Credential**.
    *   Busca **"MCP Client"**.
    *   Tipo de conexión: **SSE**.
    *   URL: `http://weather-mcp:3000/sse` (Nota: usamos el nombre del contenedor, no localhost).
    *   Guarda la credencial.
3.  **Importar Workflow:**
    *   En el lienzo de n8n, selecciona **Import from File**.
    *   Sube el archivo `WeatherApp_n8n.json` que está en la raíz del proyecto.
4.  **Activar:**
    *   Asegúrate de que el nodo **MCP Client** use la credencial que acabas de crear.
    *   Haz clic en **Activate** (switch arriba a la derecha) para que el Webhook escuche peticiones.

> **Nota:** El ID del Webhook en `WeatherApp_n8n.json` debe coincidir con la URL en `WorkFlow.java` del backend.

---

## 3. Backend (Spring Boot)
El backend debe correr en el puerto **6969** para que el Frontend pueda comunicarse con él.

1. Abre una terminal en la carpeta `backend`.
2. Asegúrate de tener configurado el puerto en `src/main/resources/application.properties`:
   ```properties
   server.port=6969
   ```
   *(Si no existe el archivo, créalo o pasa el parámetro al ejecutar)*.

3. Ejecuta el servidor:
   *   **Windows:**
       ```bash
       .\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=6969"
       ```
   *   **Linux/Mac:**
       ```bash
       ./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=6969
       ```

4. Espera a que aparezca el mensaje de "Started WeatherApplication".

---

## 4. Frontend
Finalmente, lanza la interfaz de usuario.

**Opción A (VS Code - Recomendada):**
1.  Abre la carpeta `frontend` en VS Code.
2.  Instala la extensión **"Live Server"**.
3.  Haz clic derecho en `index.html` -> **"Open with Live Server"**.

**Opción B (Manual):**
1.  Simplemente haz doble clic en `frontend/index.html` para abrirlo en tu navegador.
    *(Nota: Algunos navegadores bloquean peticiones a localhost desde archivos locales por CORS. Si falla, usa la Opción A).*

---

## 🧪 Probando el Flujo

1.  En la página web, verás la ubicación por defecto o podrás buscar una ciudad.
2.  Al seleccionar una ciudad/fecha, el frontend enviará los datos al Backend (Puerto 6969).
3.  El Backend llamará al Webhook de n8n.
4.  n8n usará el MCP para pedir el clima a Open-Meteo y Gemini para generar la recomendación.
5.  El Backend recibirá la respuesta y se la entregará al Frontend.
6.  **¡Verás el clima y la recomendación de ropa en pantalla!**
