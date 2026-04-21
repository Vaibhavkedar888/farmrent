# AgroRent – Smart Farming Equipment Rental

## Overview
AgroRent is a full‑stack application that lets farmers **rent** farming equipment and owners **list** their machinery. It includes:
- Spring Boot backend with MongoDB
- React/Vite frontend with a premium UI (dynamic themes, responsive design, data‑visualisation charts)
- Docker support for easy deployment on Render (or any container platform)

## Features
- Password‑based authentication (admin seeded with `admin@agrorent.com` / `1234`)
- Owner dashboard with **Revenue Performance** chart
- Farmer dashboard with **Expenditure Analytics** chart
- Theme switcher (Emerald & Orange) persisted via `localStorage`
- Responsive layout for mobile, tablet and desktop
- Email notifications for bookings

## Quick Start (Local Development)
```bash
# Clone the repo
git clone https://github.com/Vaibhavkedar888/farmrent.git
cd farmrent

# Backend (Java 17 + Maven)
./mvnw spring-boot:run
# Frontend (Node 20+)
cd frontend
npm install
npm run dev   # Vite dev server at http://localhost:5173
```

## Deployment on Render
1. **Create a new Web Service** for the backend and point it to the `backend` directory.
2. Render will automatically use the Dockerfile at `backend/Dockerfile` (specified in `render.yaml`).
3. **Create a Static Site** for the frontend – Render will run `npm install && npm run build` and serve the `dist` folder.
4. Set the following environment variables in the Render dashboard:
   - `MONGODB_URI` – your MongoDB Atlas connection string
   - `ALLOWED_ORIGINS` – URL of the frontend (e.g., `https://agrorent-frontend.onrender.com`)
   - `MAIL_USERNAME` & `MAIL_PASSWORD` – credentials for sending email notifications
   - `VITE_API_BASE_URL` – backend URL (e.g., `https://agrorent-backend.onrender.com`)
5. Deploy – Render will build the Docker image, start the Spring Boot app, and serve the static site.

## Docker (Local)
```bash
# Build the backend image
docker build -t agrorent-backend -f backend/Dockerfile .
# Run it (replace <MONGODB_URI> with your connection string)
docker run -p 8080:8080 -e MONGODB_URI='<MONGODB_URI>' agrorent-backend
```

## Contributing
Feel free to open issues or submit pull requests. Follow the existing code style and run `npm run lint` / `./mvnw verify` before submitting.

---
*Created by the AgroRent development team.*
