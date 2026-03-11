document.addEventListener('DOMContentLoaded', () => {
    const citySearchTrigger = document.getElementById('citySearchTrigger');
    const searchModal = document.getElementById('searchModal');
    const closeSearchBtn = document.getElementById('closeSearchBtn');
    const searchInput = document.getElementById('searchInput');
    const clearSearchBtn = document.getElementById('clearSearchBtn');
    const modalContainer = searchModal.querySelector('div');

    function openModal() {
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
        if (e.target.value.length > 0) {
            clearSearchBtn.classList.remove('hidden');
        } else {
            clearSearchBtn.classList.add('hidden');
        }
    });

    // Handle clear button click
    clearSearchBtn.addEventListener('click', () => {
        searchInput.value = '';
        clearSearchBtn.classList.add('hidden');
        searchInput.focus();
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
