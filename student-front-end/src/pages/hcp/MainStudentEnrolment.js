import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import DashboardLayout from "../../components/layout/DashboardLayout";
import Load from "../../components/layout/Load"; // Ensure you have a loading component
import axios from 'axios'; // Axios for making HTTP requests
import BackButton from '../commons/BackButton';
import { toast, ToastContainer } from 'react-toastify'; // Import toast functions
import 'react-toastify/dist/ReactToastify.css';
import getBaseUrl from "./BaseUrl";
import AuditTrailService from '../../services/AuditTrailService';
import { IoMdEye, IoMdAdd, IoIosFingerPrint } from "react-icons/io";
import getLocalBaseUrl from './LocalBaseUrl';

import right_hand_1 from "../../assets/right_hand_1.png"
import right_hand_2 from "../../assets/right_hand_2.png"
import right_hand_3 from "../../assets/right_hand_3.png"

const MainStudentEnrolment = () => {
    const BASE_URL = getBaseUrl();
    const location = useLocation();
    const navigate = useNavigate();
    const [membership, setMembership] = useState('');
    const { nationalId, patient } = location.state; //
    const [uniqueID, setUniqueID] = useState(0);
    const [fingerResponseData, setFingerResponseData] = useState(null);
    const [mantraScanResponse, setMantraScanResponse] = useState({});
    const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms));
    const [loading, setLoading] = useState(false);
    const [isReset, setIsReset] = useState(false);
    const [error, setError] = useState('');
    const [isCaptured, setIsCaptured] = useState(false); // New state to track capture status

    const secugen_lic = ""; // Your license string
    const [progress, setProgress] = useState(0);
    const [enrolmentStatus, setEnrolmentStatus] = useState('NOT ENROLLED');
    const [fingerPrintsArray, setFingerPrintsArray] = useState([]);
    useEffect(() => {
        if (!patient) {
            toast.error("No student data found. Redirecting...");
            navigate("/patient-biometrics");
        }
    }, [patient, navigate]);


    const addFingerprint = (newFingerprint) => {
        setFingerPrintsArray((prevArray) => [...prevArray, newFingerprint]);
    };

    const fetchPrincipalFingerPrintsData = async () => {
        setProgress(0)
        const memberNum = (patient.studentNumber || patient.personnelNumber);
        try {
            const response = await axios.get(BASE_URL + `api/v1/fingerprints/membership/` + memberNum);
            const { id, firstFingerPrint, firstFingerImage, secondFingerPrint, secondFingerImage, thirdFingerPrint, thirdFingerImage, enrolmentStatus } = response.data;
            setFingerResponseData(response.data)

            setUniqueID(id)
            setEnrolmentStatus(enrolmentStatus)
            // Calculate the progress based on the presence of values in the fields
            let completedFields = 0;
            if (firstFingerPrint) {
                completedFields++;
                document.getElementById('FPImage1').src = "data:image/bmp;base64," + firstFingerImage;
                const newProgress = (completedFields / 3) * 100;
                setProgress(newProgress);

            }
            if (secondFingerPrint) {
                completedFields++;
                document.getElementById('FPImage2').src = "data:image/bmp;base64," + secondFingerImage;
                const newProgress = (completedFields / 3) * 100;
                setProgress(newProgress);

            }
            if (thirdFingerPrint) {
                completedFields++;
                document.getElementById('FPImage3').src = "data:image/bmp;base64," + thirdFingerImage;
                const newProgress = (completedFields / 3) * 100;
                setProgress(newProgress);

            }


        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };

    const mantraDeInitializeDevice = async () => {

        try {
            const response = await axios.post(getLocalBaseUrl + 'api/v1/biometric-scanner/uninitialise-device');
            console.log(response.data);
            toast.success(response.data)
        } catch (error) {
            toast.error('Error fetching data:', error)
            console.error('Error fetching data:', error);
        }
    };

    const mantraInitializeDevice = async () => {

        try {
            const response = await axios.post(getLocalBaseUrl + 'api/v1/biometric-scanner/initialise-device/MFS500');
            console.log("response.data");
            toast.success(response.data)
            console.log(response.data);
        } catch (error) {
            toast.success('Error fetching data.........:', error)
            console.error('Error fetching data.........:', error);
        }
    };

    const resetFingerprints = async () => {
        document.getElementById('FPImage1').src = right_hand_1
        document.getElementById('FPImage2').src = right_hand_2
        document.getElementById('FPImage3').src = right_hand_3
        setIsReset(false);

    };

    const mantraScanFringeprint = async (fingerOption) => {
        setLoading(true);
        mantraStopCaptureWithNoNotification();
        const memberNumberAndSuffix = patient.personnelNumber + '-00';
        const url = BASE_URL + 'api/v1/biometric-scanner/start-capture/' + memberNumberAndSuffix + '/' + fingerOption;
        let response;
        try {
            response = await axios.post(url);
            setMantraScanResponse(response.data);

            if (response.data.bufferedImage === null) {
                toast.error(response.data.message);
            } else {
                mantraSuccessFingerScanned(response.data.bufferedImage, fingerOption, response.data.message);
                setLoading(false);
                setIsReset(true);
                // Call the delay function with a delay of 5 seconds
                await delay(100);

                mantraStopCaptureWithNoNotification();
            }

            console.log(response.data);
            // Handle the response data as needed
        } catch (error) {
            console.error('Error........:', error.response.data);
            toast.error(error.response.data.message)
            setLoading(false);
            // Handle errors here
        }
    };

    const mantraScanFringeprintExternalCall = async (option) => {
        setLoading(true);
        const url = 'http://localhost:8084/api/v1/biometric-scanner/start-capture';
        let response;
        try {
            response = await axios.post(url);
            setMantraScanResponse(response.data);
            console.log(response.data.blobFinger)
            if (response.data.bufferedImage === null) {
                console.log(response.data)
                toast.error(response.data.message);
            } else {
                mantraSuccessFingerScanned(response.data.bufferedImage, option, response.data.message,response.data.blobFinger);
                setLoading(false);
                setIsReset(true);
                // Call the delay function with a delay of 5 seconds
                await delay(100);

                mantraStopCaptureWithNoNotification();
            }

            console.log(response.data);
            // Handle the response data as needed
        } catch (error) {
            console.error('Error........:', error.response.data);
            toast.error(error.response.data.message)
            setLoading(false);
            // Handle errors here
        }
    };
    const mantraStopCapture = async () => {
        const url = getLocalBaseUrl + 'api/v1/biometric-scanner/stop-capture';

        try {
            const response = await axios.post(url);
            console.log(response.data);
            toast.success(response.data)
        } catch (error) {
            console.error('Error:', error);
        }
    };

    const mantraStopCaptureWithNoNotification = async () => {
        const url = getLocalBaseUrl + 'api/v1/biometric-scanner/stop-capture';

        try {
            const response = await axios.post(url);
            console.log(response.data);
        } catch (error) {
            console.error('Error:', error);
        }
    };


    const mantraSuccessFingerScanned = (result, option, msg,blobFinger) => {
        setLoading(false);

        if (msg !== 'Device not initialized') {
            // Calculate the progress based on the presence of values in the fields
            let newProgress = 0;
            let completedFields = 0;
            if (option === "first") {
                completedFields++;
                document.getElementById('FPImage1').src = "data:image/bmp;base64," + result;
                const newFingerprintObject = {
                    fingerId: 1,
                    fingerType: 'thumb',
                    fingerprintData: result,
                    firstFingerImage: result,
                    firstFingerTemplate: blobFinger
                };
                toast.success('First fingerprint captured');
                addFingerprint(newFingerprintObject);
                newProgress = newProgress + (completedFields / 3) * 100;
                // setProgress(newProgress );
                setLoading(false)
            }
            if (option === "second") {
                completedFields++;
                document.getElementById('FPImage2').src = "data:image/bmp;base64," + result;
                const newFingerprintObject = {
                    fingerId: 2,
                    fingerType: 'index',
                    fingerprintData: result,
                    secondFingerImage: result,
                    secondFingerTemplate:blobFinger
                };
                toast.success('Second fingerprint captured');
                addFingerprint(newFingerprintObject);
                newProgress = newProgress + (completedFields / 3) * 100;
                // setProgress(newProgress);
                setLoading(false)
            }

            if (option === "third") {
                completedFields++;
                document.getElementById('FPImage3').src = "data:image/bmp;base64," + result;
                const newFingerprintObject = {
                    fingerId: 3,
                    fingerType: 'middle',
                    fingerprintData: result,
                    thirdFingerImage: result,
                    thirdFingerTemplate:blobFinger
                };
                toast.success('Third fingerprint captured');
                addFingerprint(newFingerprintObject);
                newProgress = newProgress + (completedFields / 3) * 100;
                // setProgress(newProgress);
                setLoading(false)
            }

        }
        setIsCaptured(true);

    };


    const enrolPatient = () => {
        setLoading(true);
        const firstFingerPrintData = fingerPrintsArray.find(obj => obj.fingerId === 1)?.fingerprintData || '';
        const firstFingerImage = fingerPrintsArray.find(obj => obj.fingerId === 1)?.firstFingerImage || '';
        const firstFingerBlob = fingerPrintsArray.find(obj => obj.fingerId === 1)?.firstFingerTemplate || '';

        const secondFingerPrintData = fingerPrintsArray.find(obj => obj.fingerId === 2)?.fingerprintData || '';
        const secondFingerImage = fingerPrintsArray.find(obj => obj.fingerId === 2)?.secondFingerImage || '';
        const secondFingerBlob = fingerPrintsArray.find(obj => obj.fingerId === 2)?.secondFingerTemplate || '';

        const thirdFingerPrintData = fingerPrintsArray.find(obj => obj.fingerId === 3)?.fingerprintData || '';
        const thirdFingerImage = fingerPrintsArray.find(obj => obj.fingerId === 3)?.thirdFingerImage || '';
        const thirdFingerBlob = fingerPrintsArray.find(obj => obj.fingerId === 3)?.thirdFingerTemplate || '';


        const requestBody = {
            id: uniqueID,
            personnelNumberPlusSuffix: patient.studentNumber,
            firstFingerPrint: firstFingerPrintData,
            secondFingerPrint: secondFingerPrintData,
            thirdFingerPrint: thirdFingerPrintData,

            firstFingerImage: firstFingerImage,
            secondFingerImage: secondFingerImage,
            thirdFingerImage: thirdFingerImage,
            enrolmentStatus: 'ENROLLED',

            firstFingerTemplate: firstFingerBlob,
            secondFingerTemplate: secondFingerBlob,
            thirdFingerTemplate: thirdFingerBlob,

        }
        console.log(requestBody)
        if (firstFingerPrintData !== '' && secondFingerPrintData !== '' && thirdFingerPrintData !== '') {
            axios.post(BASE_URL + `api/v1/fingerprints/update`, requestBody).then(res => {
                if (res.status === 200) {
                    toast.success("successful principal member enrolment ");
                    AuditTrailService.createLog("successful principal member enrolment : " + membership);
                    fetchPrincipalFingerPrintsData();
                    // mantraDeInitializeDevice();
                    setLoading(false);
                }
            }).catch(
                err => {
                    toast.error("Failed to enrol student");
                    setLoading(false);
                    AuditTrailService.createLog("Failed to enrol student");
                }
            )
        } else {
            toast.error("Successful enrolment requires three biometrics ");
            setLoading(false);
        }
    }



    return (
        <DashboardLayout>
            <div className="p-4">
                <h1 className="text-2xl font-bold mb-6">Student Biometric Enrolment</h1>                <Load loading={loading} />
                <div className="bg-gray-200 p-4" style={{ borderRadius: '20px', padding: '30px' }}>
                    <div className="flex">
                        <div className="flex flex-col items-start flex-1">
                            <div className="text-lg font-bold mb-3">Firstname: <span>{patient.firstName}</span></div>
                        </div>
                        <div className="flex flex-col items-start flex-1">
                            <div className="text-lg font-bold mb-3">Surname: <span>{patient.surname}</span></div>
                        </div>
                    </div>
                    <div className="flex">
                        <div className="flex flex-col items-start flex-1">
                            <div className="text-lg font-bold mb-3">Student Number: <span>{patient.studentNumber || patient.personnelNumber}</span></div>
                        </div>
                        <div className="flex flex-col items-start flex-1">
                            <div className="text-lg font-bold mb-3">National Id: <span>{patient.idNumber}</span></div>
                        </div>
                    </div>

                    <div className="flex">
                        <div className="flex flex-col items-start flex-1">

                            <div className="text-lg font-bold mb-3">Enrolment Status: <span style={{ fontStyle: 'italic', color: patient.enrolmentStatus !== 'ENROLLED' ? 'red' : 'green' }}>
                                {patient.enrolmentStatus !== 'ENROLLED' ? 'NOT ENROLLED' : patient.enrolmentStatus}</span></div>
                        </div>
                        <div className="flex flex-col items-start flex-1">
                            <div className="text-lg font-bold mb-3"><span></span></div>
                        </div>
                    </div>


                    <div className="flex " style={{ backgroundColor: 'white', borderRadius: '15px', marginTop: 100, paddingTop: 10, marginBottom: 20 }}>
                        <div className="flex flex-col items-center flex-1"
                        >
                            <img id="FPImage1" alt="fingerprint" onClick={() => mantraScanFringeprintExternalCall('first')} title='Finger Print One'
                                 style={{ borderRadius: 20, cursor: 'pointer', marginBottom: 60, marginTop: 30, fontSize: '0.7rem' }}
                                 height={200} width={90} src={right_hand_1}

                            />

                        </div>
                        <div className="flex flex-col items-center flex-1">
                            <img id="FPImage2" alt="Fingerprint" onClick={() => mantraScanFringeprintExternalCall('second')} title='Finger Print Two'
                                 style={{ borderRadius: 20, cursor: 'pointer', marginBottom: 60, marginTop: 30, fontSize: '0.7rem' }}
                                 height={200} width={90} src={right_hand_2} />
                        </div>
                        <div className="flex flex-col items-center flex-1">
                            <img id="FPImage3" alt="Fingerprint" onClick={() => mantraScanFringeprintExternalCall('third')} title='Finger Print Three'
                                 style={{ borderRadius: 20, cursor: 'pointer', marginBottom: 60, marginTop: 30, fontSize: '0.7rem' }}
                                 height={200} width={90} src={right_hand_3} />

                        </div>
                        {/* captureFingerPrint */}

                    </div>

                    <div className="flex space-x-4 mb-4">
                        <button
                            onClick={() => enrolPatient()}
                            style={{ backgroundColor: '#000630', cursor: 'pointer' }}
                            className="hover:bg-green-700 text-white font-bold py-2 px-4 rounded text-sm flex items-center"
                        >
                            <IoIosFingerPrint className="mr-1 text-lg" />
                            <span>Enrol Student biometrics</span>
                        </button>
                        {isReset && (
                            <button
                                onClick={() => resetFingerprints()}
                                style={{ backgroundColor: 'darkred', cursor: 'pointer' }}
                                className="hover:bg-green-700 text-white font-bold py-2 px-4 rounded text-sm flex items-center"
                            >
                                <IoIosFingerPrint className="mr-1 text-lg" />
                                <span>Clear Fingerprints </span>
                            </button>
                        )}

                        <button
                            onClick={() => mantraStopCapture()}
                            style={{ backgroundColor: 'green', cursor: 'pointer' }}
                            className="hover:bg-green-700 text-white font-bold py-2 px-4 rounded text-sm flex items-center"
                        >
                            <IoIosFingerPrint className="mr-1 text-lg" />
                            <span>stop capture </span>
                        </button>
                        <button
                            onClick={() => mantraDeInitializeDevice()}
                            style={{ backgroundColor: 'black', cursor: 'pointer' }}
                            className="hover:bg-green-700 text-white font-bold py-2 px-4 rounded text-sm flex items-center"
                        >
                            <IoIosFingerPrint className="mr-1 text-lg" />
                            <span>uninitialise device </span>
                        </button>
                        <button
                            onClick={() => mantraInitializeDevice()}
                            style={{ backgroundColor: 'orange', cursor: 'pointer' }}
                            className="hover:bg-green-700 text-white font-bold py-2 px-4 rounded text-sm flex items-center"
                        >
                            <IoIosFingerPrint className="mr-1 text-lg" />
                            <span>initialize device </span>
                        </button>

                    </div>

                    <span style={{ fontSize: '0.9rem' }}>Progress: {progress}%</span>
                    <div style={{ width: '100%', backgroundColor: '#f0f0f0', borderRadius: 5, marginTop: 30, marginBottom: 40 }}>
                        <div
                            style={{
                                width: `${progress}%`,
                                height: 20,
                                backgroundColor: 'green',
                                borderRadius: 5,
                                transition: 'width 0.5s',
                            }}
                        />
                    </div>

                </div>

            </div>
            <ToastContainer />
        </DashboardLayout>

    );
};

export default MainStudentEnrolment;
