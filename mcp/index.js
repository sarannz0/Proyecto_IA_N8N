// ============================================================
//  Weather MCP Server — Para n8n vía HTTP/SSE
//  Transporte: SSE (Server-Sent Events) en puerto 3000
//  API: Open-Meteo (sin API Key)
// ============================================================

import { McpServer } from "@modelcontextprotocol/sdk/server/mcp.js";
import { SSEServerTransport } from "@modelcontextprotocol/sdk/server/sse.js";
import { z } from "zod";
import http from "http";
import { URL } from "url";

// ─── Crear el servidor MCP ───────────────────────────────────
const server = new McpServer({
  name: "weather-mcp-n8n",
  version: "1.0.0",
});

// ════════════════════════════════════════════════════════════
//  TOOL: get_weather — Consulta Open-Meteo y devuelve JSON crudo
// ════════════════════════════════════════════════════════════
server.tool(
  "get_weather",
  "Consulta el clima en Open-Meteo con latitud y longitud. Devuelve el JSON completo de la API incluyendo los últimos 7 días y el pronóstico.",
  {
    latitude: z
      .number()
      .describe("Latitud de la ubicación, ej: 7.1254 para Bucaramanga"),
    longitude: z
      .number()
      .describe("Longitud de la ubicación, ej: -73.1198 para Bucaramanga"),
  },
  async ({ latitude, longitude }) => {
    // Construir la URL exacta que usa n8n
    const url = new URL("https://api.open-meteo.com/v1/forecast");
    url.searchParams.set("latitude", latitude);
    url.searchParams.set("longitude", longitude);
    url.searchParams.set(
      "daily",
      [
        "temperature_2m_max",
        "temperature_2m_min",
        "apparent_temperature_max",
        "apparent_temperature_min",
        "rain_sum",
        "showers_sum",
        "snowfall_sum",
        "precipitation_sum",
        "wind_speed_10m_max",
        "wind_gusts_10m_max",
      ].join(",")
    );
    url.searchParams.set("past_days", "7");

    const res = await fetch(url.toString());

    if (!res.ok) {
      throw new Error(
        `Open-Meteo respondió con error ${res.status}: ${res.statusText}`
      );
    }

    // Devolver el JSON crudo tal cual lo entrega la API
    const data = await res.json();

    return {
      content: [
        {
          type: "text",
          text: JSON.stringify(data, null, 2),
        },
      ],
    };
  }
);

// ════════════════════════════════════════════════════════════
//  Servidor HTTP con rutas SSE para n8n
// ════════════════════════════════════════════════════════════
const PORT = process.env.PORT || 3000;
const transports = {}; // Guarda sesiones activas

const httpServer = http.createServer(async (req, res) => {
  const reqUrl = new URL(req.url, `http://localhost:${PORT}`);

  // ── CORS headers (necesarios para n8n Cloud) ─────────────
  res.setHeader("Access-Control-Allow-Origin", "*");
  res.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
  res.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept");

  if (req.method === "OPTIONS") {
    res.writeHead(204);
    res.end();
    return;
  }

  // ── Ruta SSE: n8n se conecta aquí para recibir eventos ───
  if (req.method === "GET" && reqUrl.pathname === "/sse") {
    console.log("🔗 Nueva conexión SSE desde n8n");

    const transport = new SSEServerTransport("/messages", res);
    transports[transport.sessionId] = transport;

    res.on("close", () => {
      console.log(`❌ Conexión cerrada: ${transport.sessionId}`);
      delete transports[transport.sessionId];
    });

    await server.connect(transport);
    return;
  }

  // ── Ruta POST: n8n envía los mensajes/llamadas aquí ──────
  if (req.method === "POST" && reqUrl.pathname === "/messages") {
    const sessionId = reqUrl.searchParams.get("sessionId");
    const transport = transports[sessionId];

    if (!transport) {
      res.writeHead(404, { "Content-Type": "application/json" });
      res.end(JSON.stringify({ error: "Sesión no encontrada" }));
      return;
    }

    let body = "";
    req.on("data", (chunk) => (body += chunk));
    req.on("end", async () => {
      try {
        await transport.handlePostMessage(req, res, JSON.parse(body));
      } catch (err) {
        res.writeHead(500, { "Content-Type": "application/json" });
        res.end(JSON.stringify({ error: err.message }));
      }
    });
    return;
  }

  // ── Health check ─────────────────────────────────────────
  if (req.method === "GET" && reqUrl.pathname === "/health") {
    res.writeHead(200, { "Content-Type": "application/json" });
    res.end(
      JSON.stringify({
        status: "ok",
        server: "weather-mcp-n8n",
        version: "1.0.0",
        tools: ["get_weather"],
      })
    );
    return;
  }

  res.writeHead(404);
  res.end("Not found");
});

httpServer.listen(PORT, () => {
  console.log(`✅ Weather MCP Server corriendo en http://localhost:${PORT}`);
  console.log(`   SSE endpoint:  http://localhost:${PORT}/sse`);
  console.log(`   Health check:  http://localhost:${PORT}/health`);
});
