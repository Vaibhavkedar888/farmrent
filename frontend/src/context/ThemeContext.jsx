import { createContext, useContext, useEffect, useState } from 'react';

export const ThemeContext = createContext();

export const ThemeProvider = ({ children }) => {
    const [isDark, setIsDark] = useState(() => {
        return localStorage.getItem('theme') === 'dark';
    });

    const [colorTheme, setColorTheme] = useState(() => {
        return localStorage.getItem('colorTheme') || 'emerald';
    });

    useEffect(() => {
        const root = document.documentElement;
        
        // Dark Mode
        if (isDark) {
            root.classList.add('dark');
            localStorage.setItem('theme', 'dark');
        } else {
            root.classList.remove('dark');
            localStorage.setItem('theme', 'light');
        }

        // Color Theme
        if (colorTheme === 'orange') {
            root.classList.add('theme-orange');
        } else {
            root.classList.remove('theme-orange');
        }
        localStorage.setItem('colorTheme', colorTheme);

    }, [isDark, colorTheme]);

    const toggleTheme = () => setIsDark(prev => !prev);
    
    const toggleColorTheme = () => {
        setColorTheme(prev => prev === 'emerald' ? 'orange' : 'emerald');
    };

    return (
        <ThemeContext.Provider value={{ isDark, toggleTheme, colorTheme, toggleColorTheme }}>
            {children}
        </ThemeContext.Provider>
    );
};

export const useTheme = () => {
    const context = useContext(ThemeContext);
    if (!context) throw new Error('useTheme must be used within ThemeProvider');
    return context;
};
