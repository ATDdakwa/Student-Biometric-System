import React, { useState, useEffect } from 'react';
import { toast, ToastContainer } from 'react-toastify'; // Import toast functions
import DashboardLayout from "../../components/layout/DashboardLayout";
import Load from "../../components/layout/Load";
import { useLocation, useNavigate } from 'react-router-dom';
import 'react-toastify/dist/ReactToastify.css'; // Import the styles
import BackButton from '../commons/BackButton';
import axios from 'axios';
import getBaseUrl from './BaseUrl';
import AuditTrailService from '../../services/AuditTrailService';
import { IoMdEye, IoMdAdd, IoIosFingerPrint } from "react-icons/io";
import getLocalBaseUrl from './LocalBaseUrl';


const MainStudentVerification = () => {
    const [loading, setLoading] = useState(false);
    const location = useLocation();
    const [error, setError] = useState('');
    const [matchingScore, setMatchingScore] = useState(null);
    const [fingerPrintsData, setFingerPrintsData] = useState(null);
    const [fingerToCompare, setFingerToCompare] = useState(null);
    const { nationalId, patient } = location.state; //
    const [isCaptured, setIsCaptured] = useState(false); // New state to track capture status
    const [isReset, setIsReset] = useState(false);
    const navigate = useNavigate();
    const BASE_URL = getBaseUrl();
    const [enrolmentStatus, setEnrolmentStatus] = useState('NOT ENROLLED');
    const [matchStatus, setMatchStatus] = useState('');
    const [mantraScanResponse, setMantraScanResponse] = useState({});
    const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms));
    const { student, accessPoint } = location.state || {};

    const mantraInitializeDevice = async () => {

        try {
            const response = await axios.post(BASE_URL + 'api/v1/biometric-scanner/initialise-device/MFS500');
            console.log(response.data);
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };
    const mantraScannFringeprint = async () => {
        setLoading(true);
        mantraInitializeDevice();
        const memberNumberAndSuffix = student.studentNumber;
        const url = 'http://localhost:8084/api/v1/biometric-scanner/start-capture';
        try {
            const response = await axios.post(url);
            setMantraScanResponse(response.data);

            if (response.data.bufferedImage === null) {
                toast.error(response.data.message);
            } else {
                setFingerToCompare(response.data.bufferedImage)
                console.log(response.data)
                if(response.data.message ==="Fingerprint Data could not be found"){
                    setLoading(false);

                }
                if(response.data.message !=="Fingerprint Data could not be found"){
                    setLoading(true);

                }

                toast.warning("Please wait... still processing", { autoClose: 7000 }); // 10 seconds
                // setMatchStatus(response.data.message);
                setIsCaptured(true)
                setIsReset(true);
                document.getElementById('FPImage1').src = "data:image/bmp;base64," + response.data.bufferedImage;
                mantraMatchFringeprint();
                // await delay(100);

                mantraStopCapture();
                // mantraDeInitializeDevice();
            }
        } catch (error) {
            console.error('Error:', error);
            setLoading(false);
        }
    };
    const validateRequestBody = (data) => {
        return (
            data.firstFingerImage &&
            data.secondFingerImage &&
            data.thirdFingerImage &&
            data.firstFingerTemplate &&
            data.secondFingerTemplate &&
            data.thirdFingerTemplate &&
            data.fingerToCompare
        );
    };
    const mantraMatchFringeprint = async () => {
        console.log(fingerToCompare)
        setLoading(true);
        // mantraInitializeDevice(); // Uncomment if needed
        const memberNumberAndSuffix = student.studentNumber;
        const url = 'http://localhost:8084/api/v1/biometric-scanner/match-finger-print/FMR_V2011';

        // Create the request body
        const requestBody = {
            firstFingerImage: fingerPrintsData.firstFingerImage,
            secondFingerImage: fingerPrintsData.secondFingerImage,
            thirdFingerImage: fingerPrintsData.thirdFingerImage,
            firstFingerTemplate: fingerPrintsData.firstFingerTemplate,
            secondFingerTemplate: fingerPrintsData.secondFingerTemplate,
            thirdFingerTemplate: fingerPrintsData.thirdFingerTemplate,
            fingerToCompare: fingerPrintsData.firstFingerImage
            // Add any additional fields required by your API here
        };
        if (!validateRequestBody(requestBody)) {
            toast.error("Invalid fingerprint data. Please try again.");
            setLoading(false);
            return;
        }
        try {
            const response = await axios.post(url, requestBody);
            setMantraScanResponse(response.data);

            if (response.data.bufferedImage === null) {
                toast.error(response.data.message);
            } else {
                console.log(response.data);
                toast.success(response.data.message);
                setMatchStatus(response.data.message);
                setIsCaptured(true);
                setIsReset(true);
                // document.getElementById('FPImage1').src = "data:image/bmp;base64," + response.data.bufferedImage;
                // await delay(1000);
                setLoading(false);
                mantraStopCapture();
                // mantraDeInitializeDevice(); // Uncomment if needed
            }
        } catch (error) {
            console.error('Error:', error);
            setLoading(false);
        }
    };


    const mantraStopCapture = async () => {
        mantraInitializeDevice()
        const url = BASE_URL + 'api/v1/biometric-scanner/stop-capture';

        try {
            const response = await axios.post(url);
            console.log(response.data);
        } catch (error) {
            console.error('Error:', error);
        }
    };

    const resetFingerprints = async () => {
        document.getElementById('FPImage1').src = './PlaceFinger.bmp'
        setIsReset(false);
        setIsCaptured(false)
        setMatchStatus('')

    };

    const mantraDeInitializeDevice = async () => {

        try {
            const response = await axios.post(BASE_URL + 'api/v1/biometric-scanner/uninitialise-device');
            console.log(response.data);
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };
    const fetchPrincipalFingerPrintsData = async (memberNum) => {
        console.log("Fetching fingerprints for memberNum: " + memberNum);

        try {
            const response = await axios.get(BASE_URL + `api/v1/fingerprints/membership/` + memberNum);

            // ✅ Check if response.data is empty or missing
            if (!response.data || Object.keys(response.data).length === 0) {
                toast.warning("No fingerprint data found for this member.");
                setFingerPrintsData(null);  // Clear any previous data
                return;
            }

            setFingerPrintsData(response.data);
            console.log(response.data);

            if (response.data.enrolmentStatus !== undefined) {
                setEnrolmentStatus(response.data.enrolmentStatus);
            }
        } catch (error) {
            console.error("Error fetching data:", error);
            toast.error("Failed to fetch fingerprint data. Please try again.");
            setLoading(false)
        }
    };
/*    useEffect(() => {
        if (patient?.personnelNumber !== undefined) {
            fetchPrincipalFingerPrintsData(patient?.personnelNumber + '-00');
        }
    }, []);*/
    useEffect(() => {
        if (student?.studentNumber) {
            fetchPrincipalFingerPrintsData(student.studentNumber);
        }
    }, [student]);

    // Handle missing student state
    if (!student) {
        return (
            <DashboardLayout>
                <div className="p-8 text-center">
                    <h2 className="text-xl text-red-500 font-bold">
                        No student data found. Please go back to the verification list.
                    </h2>
                    <button
                        onClick={() => navigate(-1)}
                        className="bg-[#000630] hover:bg-green-700 text-white font-bold py-2 px-4 rounded mt-4"
                    >
                        Go Back
                    </button>
                </div>
            </DashboardLayout>
        );
    }



    return (
        <DashboardLayout>
            <div className="p-4">
                <h1 className="text-2xl font-bold mb-6">Principal Patient biometric Verification</h1>
                <hr></hr>
                <div className="bg-gray-200 p-4" style={{ borderRadius: '20px', paddingBottom: '70px', paddingTop: '70px', paddingLeft: '50px', marginTop: '30px' }} >

                    <div className="grid grid-cols-2 gap-1" > {/* Reduced gap to 2 */}
                        <p className="text-lg"><strong>First Name:</strong></p>
                        <p className="text-lg">{student.firstName}</p>

                        <p className="text-lg"><strong>Last Name:</strong></p>
                        <p className="text-lg">{student.surname}</p>

                        <p className="text-lg"><strong>Student Number:</strong></p>
                        <p className="text-lg">{student.studentNumber}</p>

                        <p className="text-lg"><strong>Enrolment Status:</strong></p>
                        <p className="text-lg">
                            <span style={{ fontStyle: 'italic', color: student.enrolmentStatus === 'NOT ENROLLED' ? 'red' : 'green' }}>
                                {student.enrolmentStatus}
                            </span>
                        </p>

                        <p className="text-lg"><strong>Biometric Match Status:</strong></p>
                        <p className="text-lg">
                            <span style={{ fontStyle: 'italic', color: 'green' }}>
                                {matchStatus}</span>
                        </p>
                    </div>

                    <div style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', marginTop: '40px' }}>
                        <div className="flex flex-col items-start">
                            <img id="FPImage1" alt="Fingerprint Image 1" height={240} width={130} style={{ borderRadius: '20px' }} src="./PlaceFinger.bmp" />
                            {!isCaptured && ( // Render the Scan button only if fingerprint not captured

                                <div className="flex space-x-4 mb-4">
                                    <button
                                        onClick={() => mantraScannFringeprint()}
                                        style={{ backgroundColor: '#000630', cursor: 'pointer', marginLeft: '-70px', marginTop: '50px' }}
                                        className="hover:bg-green-700 text-white font-bold py-2 px-4 rounded text-sm flex items-center"
                                    >
                                        <IoIosFingerPrint className="mr-1 text-lg" />
                                        <span>Scan patient fingerprint for verification</span>
                                    </button>
                                </div>
                            )}

                            {isReset && (

                                <div className="flex space-x-4 mb-4">
                                    <button
                                        onClick={() => resetFingerprints()}
                                        style={{ backgroundColor: 'darkred', cursor: 'pointer', marginTop: '10%' }}
                                        className="hover:bg-green-700 text-white font-bold py-2 px-4 rounded text-sm flex items-center"
                                    >
                                        <IoIosFingerPrint className="mr-1 text-lg" />
                                        <span>Clear Fingerprints</span>
                                    </button>
                                </div>

                            )}

                            {loading && <Load loading={loading} />}
                            {error && <p className="text-red-500 mt-2">{error}</p>}
                        </div>




                        {matchingScore !== null && (
                            <div className="mt-4">
                                <h4 className="font-bold">Matching Percentage: {((matchingScore / 199) * 100).toFixed(2)}% </h4>
                            </div>
                        )}

                    </div>

                </div>
                <ToastContainer /> {/* Add ToastContainer here */}
            </div>
        </DashboardLayout>
    );
};

export default MainStudentVerification;
