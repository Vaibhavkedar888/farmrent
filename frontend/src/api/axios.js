import axios from 'axios';

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
    withCredentials: true,
    timeout: 15000,
    headers: {
        'Content-Type': 'application/json',
    },
});

console.log(">>> DEPLOYMENT DIAGNOSTIC | API URL:", api.defaults.baseURL);

export default api;
