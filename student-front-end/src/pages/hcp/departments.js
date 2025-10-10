// departments.js

// Import Axios
import axios from "axios";
import getBaseUrl from "./BaseUrl";

const BASE_URL = getBaseUrl();

// Function to fetch distinct departments from the API
const fetchDistinctDepartments = async () => {
    try {
        const response = await axios.get(BASE_URL + "api/v1/patients/distinct-departments");
        return response.data;
    } catch (error) {
        console.error('Error fetching departments:', error);
        return [];
    }
};

// Export the function to fetch distinct departments
module.exports = fetchDistinctDepartments;