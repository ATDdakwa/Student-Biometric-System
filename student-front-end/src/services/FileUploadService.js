import axios from 'axios';
import getBaseUrl from '../pages/hcp/BaseUrl';
import Cookies from 'js-cookie';

const BASE_URL = getBaseUrl();

export default class FileUploadService {

    static async postFilePrincipal(formData) {
        const token = Cookies.get('access_token');

        try {
            const response = await axios.post(`${BASE_URL}api/v1/file/upload`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });

            return response.data;
        } catch (error) {
            // Handle error if needed
            console.error('Error:', error);
            return null;
        }
    }

    static async postFileDependant(formData) {
        const token = Cookies.get('access_token');

        try {
            const response = await axios.post(`${BASE_URL}api/v1/file/upload`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                }
            });

            return response.data;
        } catch (error) {
            // Handle error if needed
            console.error('Error:', error);
            return null;
        }
    }
   
}