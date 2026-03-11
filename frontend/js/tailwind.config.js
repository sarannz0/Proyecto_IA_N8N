tailwind.config = {
    theme: {
        extend: {
            colors: {
                primary: '#000000',
                background: '#FFFFFF',
                surface: '#F5F5F5',
                muted: '#888888',
                accent: '#FF3300',
                divider: '#E0E0E0'
            },
            fontFamily: {
                // Using Inter as a widely available and highly similar fallback for Geist
                sans: ['Inter', 'sans-serif'],
                display: ['Space Grotesk', 'sans-serif'],
            },
            spacing: {
                'sm': '8px',
                'md': '24px',
                'lg': '64px',
                'xl': '120px',
            }
        }
    }
}
