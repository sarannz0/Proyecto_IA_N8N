
async function search(pais) {
    const COUNTRY_URL = `https://geocoding-api.open-meteo.com/v1/search?name=${pais}&count=20&language=es&format=json`;
    const opciones = {
        method: 'GET'
    }

    try {
        const response = await fetch(COUNTRY_URL, opciones);
        if (!response.ok) throw new Error(response.status);
        return await response.json();
    } catch (error) {
        console.log(error);
    }
}
