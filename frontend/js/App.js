const DEFAULT_LAT = '7.1255169';
const DEFAULT_LON = '-73.1182624';

document.addEventListener('DOMContentLoaded', () => {
});

window.cargarDatosDelClima = async (lat, lon, place, date) => {
    const latValue = lat ?? DEFAULT_LAT;
    const lonValue = lon ?? DEFAULT_LON;

    const baseUrl = 'http://localhost:6969/weather/get';

    const selectedCity = window.__selectedCity;
    const selectedCountry = window.__selectedCountry;

    try {
        console.log('Enviando a backend:', {
            lat: latValue,
            lon: lonValue,
            city: place,
            country: selectedCountry,
        });

        const response = await fetch(baseUrl, { 
        method: 'POST',
        headers: {
                'Content-Type': 'application/json'
            },
        body: JSON.stringify({
            place: place,
            latitud: latValue,
            longitud: lonValue,
            date: date
        })
        });

        if (!response.ok) {
            // Intentamos leer el mensaje de error que envió el backend
            const errorData = await response.json().catch(() => ({})); 
            const mensaje = errorData.message || `Error desconocido (${response.status})`;
            
            throw new Error(mensaje);
        }

        const data = await response.json();
        console.log(data);



        const tempPrincipal = document.getElementById('temp-principal');
        if (tempPrincipal) {
            tempPrincipal.innerHTML = `${data.temperature } °C`;
        }

        const tempSensacion = document.getElementById('temp-sensacion');
        if (tempSensacion) {
            tempSensacion.innerHTML = data.weatherCondition;
        }

        const windSpeed = document.getElementById('wind-speed');
        if (windSpeed) {
            windSpeed.innerHTML = `${data.windSpeed} Km/h`;
        }

        const horaActual = document.getElementById('hora-actual');
        if (horaActual) {
            horaActual.innerHTML = data.PONER_AQUI_LLAVE_HORA;
        }

        const recomendacionTexto = document.getElementById('recomendacion-texto');
        if (recomendacionTexto) {
            recomendacionTexto.innerHTML = data.recommendedClothes.replace("VESTIMENTA: ", "");
        }
    } catch (error) {

        console.error('Fallo la conexion con el backend:', error);
    }
};
