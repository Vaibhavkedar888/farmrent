# -------------------------------------------------
#  Build stage – compile the React/Vite app
# -------------------------------------------------
FROM node:20-alpine AS build

WORKDIR /app

# Install dependencies (cache layer)
# We expect to be in the frontend folder context
COPY package.json package-lock.json ./
RUN npm ci

# Copy source and build
COPY . .
RUN npm run build

# -------------------------------------------------
#  Runtime stage – serve the static files with Nginx
# -------------------------------------------------
FROM nginx:stable-alpine

# Remove the default nginx static files
RUN rm -rf /usr/share/nginx/html/*

# Copy the built assets from the build stage
COPY --from=build /app/dist /usr/share/nginx/html

# Copy a custom nginx config if you need one (e.g., for routing)
# COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
