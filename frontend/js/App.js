const DEFAULT_LAT = '7.1255169';
const DEFAULT_LON = '-73.1182624';

document.addEventListener('DOMContentLoaded', () => {
    window.cargarDatosDelClima(DEFAULT_LAT, DEFAULT_LON);
});

window.cargarDatosDelClima = async (lat, lon) => {
    const latValue = lat ?? DEFAULT_LAT;
    const lonValue = lon ?? DEFAULT_LON;

    const baseUrl = 'http://localhost:8080/weather/get';
    let url = `${baseUrl}?PONER_AQUI_PARAM_LAT=${encodeURIComponent(latValue)}&PONER_AQUI_PARAM_LON=${encodeURIComponent(lonValue)}`;

    const selectedCity = window.__selectedCity;
    const selectedCountry = window.__selectedCountry;
    if (selectedCity) {
        url += `&PONER_AQUI_PARAM_CITY=${encodeURIComponent(selectedCity)}`;
    }
    if (selectedCountry) {
        url += `&PONER_AQUI_PARAM_COUNTRY=${encodeURIComponent(selectedCountry)}`;
    }

    try {
        console.log('Enviando a backend:', {
            lat: latValue,
            lon: lonValue,
            city: selectedCity,
            country: selectedCountry,
        });

        const response = await fetch(url, { method: 'GET' });

        if (!response.ok) {
            throw new Error(`Error HTTP: ${response.status}`);
        }

        const data = await response.json();
        console.log(data);

        const tempPrincipal = document.getElementById('temp-principal');
        if (tempPrincipal) {
            tempPrincipal.innerHTML = data.PONER_AQUI_LLAVE_TEMPERATURA;
        }

        const tempSensacion = document.getElementById('temp-sensacion');
        if (tempSensacion) {
            tempSensacion.innerHTML = data.PONER_AQUI_LLAVE_SENSACION;
        }

        const probLluvia = document.getElementById('prob-lluvia');
        if (probLluvia) {
            probLluvia.innerHTML = data.PONER_AQUI_LLAVE_LLUVIA;
        }

        const horaActual = document.getElementById('hora-actual');
        if (horaActual) {
            horaActual.innerHTML = data.PONER_AQUI_LLAVE_HORA;
        }

        const recomendacionTexto = document.getElementById('recomendacion-texto');
        if (recomendacionTexto) {
            recomendacionTexto.innerHTML = data.PONER_AQUI_LLAVE_RECOMENDACION;
        }

        const forecastLista = document.getElementById('forecast-lista');
        if (forecastLista) {
            forecastLista.innerHTML = data.PONER_AQUI_LLAVE_PRONOSTICO_7_DIAS;
        }
    } catch (error) {
        console.error('Fallo la conexion con el backend:', error);
    }
};
