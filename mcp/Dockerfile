FROM node:20-alpine

WORKDIR /app

# Copiar archivos de dependencias
COPY package.json ./

# Instalar dependencias
RUN npm install

# Copiar código fuente
COPY index.js ./

# Exponer el puerto SSE
EXPOSE 3000

# Variable de entorno para el puerto (opcional)
ENV PORT=3000

# Iniciar el servidor
CMD ["node", "index.js"]
