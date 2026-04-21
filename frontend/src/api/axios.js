import axios from 'axios';

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080', // Backend URL
    withCredentials: true, // For cookies/session
    headers: {
        'Content-Type': 'application/json',
    },
});

console.log("DEBUG: Connecting to Backend at:", api.defaults.baseURL);

export default api;
