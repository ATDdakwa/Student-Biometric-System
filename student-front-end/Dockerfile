# Use the official Node.js image as a base image
FROM node:latest

# Set the working directory in the container
WORKDIR /app

# Copy package.json and package-lock.json to the container
COPY package*.json ./

# Install project dependencies
RUN npm install --legacy-peer-deps

# Copy the rest of the project files to the container
COPY . .

# Build the React app
RUN npm run build

# Expose the port your app runs on
EXPOSE 3005

# Start the React app
CMD ["npm", "start"]