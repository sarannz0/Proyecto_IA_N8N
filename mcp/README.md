# 🌤️ Weather MCP — Integración con n8n (Docker)

MCP Server con transporte **HTTP/SSE** para conectarse a n8n.  
Llama la API de Open-Meteo y devuelve el **JSON crudo** completo.

---

## 🚀 Levantar el servidor

### Si ya tienes n8n corriendo en Docker

Solo levanta el MCP:

```bash
# Construir y levantar solo el MCP
docker build -t weather-mcp .
docker run -d -p 3000:3000 --name weather-mcp weather-mcp

# Verificar que está corriendo
curl http://localhost:3000/health
```

### Si quieres levantarlos juntos

```bash
docker-compose up -d

# Ver logs
docker-compose logs -f weather-mcp
```

---

## 🔧 Conectar en n8n

### Paso 1 — Agregar credencial MCP

1. En n8n ve a **Settings → Credentials → New Credential**
2. Busca **"MCP Client"**
3. Configura:
   - **SSE URL:** `http://weather-mcp:3000/sse`
     > Si n8n y el MCP están en la misma red Docker usa el nombre del servicio.
     > Si están separados usa: `http://TU_IP_SERVIDOR:3000/sse`

### Paso 2 — Usar el nodo MCP Client en tu workflow

1. Agrega el nodo **"MCP Client"**
2. Selecciona la credencial creada
3. Selecciona la tool: **`get_weather`**
4. Pasa los parámetros:
   ```json
   {
     "latitude": {{ $json.body.latitud }},
     "longitude": {{ $json.body.longitud }}
   }
   ```

### Paso 3 — La respuesta

El nodo MCP devolverá el JSON completo de Open-Meteo:

```json
{
  "latitude": 7.1254,
  "longitude": -73.1198,
  "timezone": "America/Bogota",
  "daily": {
    "time": ["2025-03-04", "2025-03-05", ...],
    "temperature_2m_max": [28.5, 27.1, ...],
    "temperature_2m_min": [18.2, 17.9, ...],
    "rain_sum": [0.0, 2.4, ...],
    "wind_speed_10m_max": [15.3, 12.1, ...]
  }
}
```

---

## 📡 Endpoints disponibles

| Endpoint | Método | Descripción |
|----------|--------|-------------|
| `/sse` | GET | Conexión SSE — n8n se conecta aquí |
| `/messages?sessionId=...` | POST | Mensajes MCP — n8n envía los tool calls |
| `/health` | GET | Health check del servidor |

---

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

## 🌐 Red Docker

Si n8n ya corre en una red Docker existente, añade el MCP a esa red:

```bash
# Ver redes existentes
docker network ls

# Conectar el contenedor MCP a la red de n8n
docker network connect TU_RED_N8N weather-mcp
```

Y en n8n usa: `http://weather-mcp:3000/sse`
