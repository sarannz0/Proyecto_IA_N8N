document.addEventListener('DOMContentLoaded', () => {
    const citySearchTrigger = document.getElementById('citySearchTrigger');
    const citySearchValue = document.getElementById('citySearchValue');
    const searchModal = document.getElementById('searchModal');
    const closeSearchBtn = document.getElementById('closeSearchBtn');
    const searchInput = document.getElementById('searchInput');
    const clearSearchBtn = document.getElementById('clearSearchBtn');
    const searchResults = document.getElementById('searchResults');
    const modalContainer = searchModal.querySelector('div');

    const formDate = document.getElementById('form-date');
    const errorBox = document.getElementById('error-box');

    const GEOCODING_API_URL = 'https://geocoding-api.open-meteo.com/v1/search';
    const GEOCODING_API_COUNT = 20;
    const GEOCODING_API_LANGUAGE = 'es';
    const MIN_QUERY_LENGTH = 2;
    let activeRequestId = 0;

    function setResultsMessage(message) {
        if (!searchResults) return;
        searchResults.innerHTML = '';
        const messageEl = document.createElement('div');
        messageEl.className =
            'px-4 py-3 text-muted text-[11px] font-semibold tracking-[0.2em] uppercase';
        messageEl.textContent = message;
        searchResults.appendChild(messageEl);
    }

    function renderResults(items) {
        if (!searchResults) return;
        searchResults.innerHTML = '';

        items.forEach((item) => {
            const cityLabel = item.name || 'CIUDAD';
            const countryLabel = item.country || '';
            const regionLabel = item.admin1 || '';
            const latValue = item.latitude;
            const lonValue = item.longitude;
            const displayParts = [cityLabel];
            if (regionLabel) displayParts.push(regionLabel);
            if (countryLabel) displayParts.push(countryLabel);
            const displayLabel = displayParts.join(', ');

            const row = document.createElement('div');
            row.className =
                'flex justify-between items-center px-4 py-3 cursor-pointer hover:bg-surface transition-colors';

            const label = document.createElement('span');
            label.className = 'text-xl font-semibold';
            label.textContent = displayLabel;
            row.appendChild(label);

            const coords = document.createElement('span');
            coords.className =
                'text-[10px] font-semibold tracking-[0.1em] text-muted uppercase';
            coords.textContent = `LAT ${latValue}, LON ${lonValue}`;
            row.appendChild(coords);

            row.addEventListener('click', () => {
                if (citySearchValue) {
                    citySearchValue.textContent = displayLabel;
                }

                window.__selectedCity = cityLabel;
                if (countryLabel) {
                    window.__selectedCountry = countryLabel;
                }

                closeModal();

                if (typeof window.cargarDatosDelClima === 'function') {
                    let place = `${cityLabel}, ${countryLabel}`;
                    window.cargarDatosDelClima(latValue, lonValue, place, formDate.value);
                }
            });

            searchResults.appendChild(row);
        });
    }

    function debounce(fn, delay) {
        let timerId;
        return (...args) => {
            clearTimeout(timerId);
            timerId = setTimeout(() => fn(...args), delay);
        };
    }

    async function fetchSuggestions(query) {
        if (!searchResults) return;

        const requestId = ++activeRequestId;
        setResultsMessage('BUSCANDO...');

        const url =
            `${GEOCODING_API_URL}?name=${encodeURIComponent(query)}&count=${GEOCODING_API_COUNT}` +
            `&language=${encodeURIComponent(GEOCODING_API_LANGUAGE)}&format=json`;

        try {
            errorBox.textContent = "";
            if (!formDate.value) {
                errorBox.textContent = "Primero debes elegir una fecha!";
                closeModal();
                return;
            }
            const response = await fetch(url, { method: 'GET' });

            if (!response.ok) {
                throw new Error(`Error HTTP: ${response.status}`);
            }

            const data = await response.json();
            
            console.log('Sugerencias API:', data);

            if (requestId !== activeRequestId) return;

            const results = Array.isArray(data?.results) ? data.results : [];
            if (!Array.isArray(results) || results.length === 0) {
                setResultsMessage('SIN RESULTADOS');
                return;
            }

            renderResults(results);
        } catch (error) {
            console.error('Fallo al consultar la API de ciudades:', error);
            setResultsMessage('ERROR AL CONSULTAR');
        }
    }

    const debouncedFetchSuggestions = debounce(fetchSuggestions, 350);

    function openModal() {
        if (!formDate.value) {
            errorBox.textContent = "Primero debes elegir una fecha!";
            closeModal();
            return;
        }


        searchModal.classList.remove('hidden');
        // Small delay to allow display block to apply before animating opacity
        requestAnimationFrame(() => {
            searchModal.classList.remove('opacity-0');
            modalContainer.classList.remove('scale-95');
            searchInput.focus();
            // Select all text when opened for quick typing
            searchInput.select();

            // Toggle clear button depending on current value
            if (searchInput.value.length > 0) {
                clearSearchBtn.classList.remove('hidden');
            } else {
                clearSearchBtn.classList.add('hidden');
            }

            if (searchInput.value.trim().length < MIN_QUERY_LENGTH) {
                setResultsMessage('ESCRIBE 2+ LETRAS');
            } else {
                debouncedFetchSuggestions(searchInput.value.trim());
            }
        });
    }

    function closeModal() {
        searchModal.classList.add('opacity-0');
        modalContainer.classList.add('scale-95');

        // Wait for transition to finish before hiding
        setTimeout(() => {
            searchModal.classList.add('hidden');
        }, 300);
    }

    // Open on trigger click
    citySearchTrigger.addEventListener('click', openModal);

    // Update clear button visibility on type
    searchInput.addEventListener('input', (e) => {
        const query = e.target.value.trim();

        if (query.length > 0) {
            clearSearchBtn.classList.remove('hidden');
        } else {
            clearSearchBtn.classList.add('hidden');
        }

        if (query.length < MIN_QUERY_LENGTH) {
            setResultsMessage('ESCRIBE 2+ LETRAS');
            return;
        }

        debouncedFetchSuggestions(query);
    });

    // Handle clear button click
    clearSearchBtn.addEventListener('click', () => {
        searchInput.value = '';
        clearSearchBtn.classList.add('hidden');
        searchInput.focus();
        setResultsMessage('ESCRIBE 2+ LETRAS');
    });

    // Close on ESC click
    closeSearchBtn.addEventListener('click', closeModal);

    // Close when clicking outside the modal content
    searchModal.addEventListener('click', (e) => {
        if (e.target === searchModal) {
            closeModal();
        }
    });

    // Close on ESC key press
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && !searchModal.classList.contains('hidden')) {
            closeModal();
        }
    });
});
