import Cookies from "js-cookie";
import getBaseUrl from "../pages/hcp/BaseUrl";
import axios from "axios";
const BASE_URL = getBaseUrl();


export default class AuditTrailService {

    static async createLog(narration) {
        const requestBody = {
            "fullName": Cookies.get("name"),
            "userName": Cookies.get("userName"),
            "role": Cookies.get("userRole"),
            "narration":narration
        }
        const response = await axios.post(BASE_URL + "api/v1/audit/create", requestBody);
          
    }

}