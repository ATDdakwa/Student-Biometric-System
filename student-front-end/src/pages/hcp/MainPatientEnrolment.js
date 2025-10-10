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

import right_hand_1 from "../../assets/right_hand_1.png"
import right_hand_2 from "../../assets/right_hand_2.png"
import right_hand_3 from "../../assets/right_hand_3.png"

const MainPatientEnrolment = () => {
    const BASE_URL = getBaseUrl();
    const location = useLocation();
    const navigate = useNavigate();
    const [template1, setTemplate1] = useState('');
    const [membership, setMembership] = useState('');
    const { nationalId, patient } = location.state; //
    const [uniqueID, setUniqueID] = useState(0);

    const [loading, setLoading] = useState(false);
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
            const { id, firstFingerPrint, secondFingerPrint, thirdFingerPrint, enrolmentStatus } = response.data;
            setUniqueID(id)
            if (enrolmentStatus !== undefined) { setEnrolmentStatus(enrolmentStatus) }
            // Calculate the progress based on the presence of values in the fields
            let completedFields = 0;
            if (firstFingerPrint) {
                completedFields++;
                document.getElementById('FPImage1').src = "data:image/bmp;base64," + firstFingerPrint;
            }
            if (secondFingerPrint) {
                completedFields++;
                document.getElementById('FPImage2').src = "data:image/bmp;base64," + secondFingerPrint;
            }
            if (thirdFingerPrint) {
                completedFields++;
                document.getElementById('FPImage3').src = "data:image/bmp;base64," + thirdFingerPrint;
            }
            // Set the progress percentage
            const newProgress = (completedFields / 3) * 100;
            setProgress(newProgress);

        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };

    const ErrorFunc = (status) => {
        setError(`Check if SGIBIOSRV is running; Status = ${status}`);
        setLoading(false);
    };

    const CallSGIFPGetData = (successCall, failCall) => {
        const uri = "https://localhost:8443/SGIFPCapture";
        const xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function () {
            if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
                const fpobject = JSON.parse(xmlhttp.responseText);
                successCall(fpobject);
            } else if (xmlhttp.status === 404) {
                failCall(xmlhttp.status);
            }
        };

        const params = new URLSearchParams({
            Timeout: "10000",
            Quality: "50",
            licstr: encodeURIComponent(secugen_lic),
            templateFormat: "ISO",
        });

        xmlhttp.open("POST", uri, true);
        xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xmlhttp.send(params.toString());
    };
    const captureFingerPrint = (option) => {
        setLoading(true);
        const timeoutId = setTimeout(() => {
            setLoading(false);
            toast.error("Fingerprint capture timed out. Please try again.");
        }, 15000); // 15 seconds timeout
       
        CallSGIFPGetData((result) => {
            clearTimeout(timeoutId); // Clear timeout on success
            SuccessFunc1(result, option);
        }, (status) => {
            clearTimeout(timeoutId); // Clear timeout on error
            ErrorFunc(status);
        });
    };

    const SuccessFunc1 = (result, option) => {
        setLoading(false);
        if (result.ErrorCode === 0) {

            if (result.ISOTemplateBase64) {
                // Calculate the progress based on the presence of values in the fields
                let completedFields = 0;
                if (option === "first") {
                    completedFields++;
                    document.getElementById('FPImage1').src = "data:image/bmp;base64," + result.BMPBase64;
                    const newFingerprintObject = {
                        fingerId: 1,
                        fingerType: 'thumb',
                        fingerprintData: result.ISOTemplateBase64,
                    };
                    addFingerprint(newFingerprintObject);
                }
                if (option === "second") {
                    completedFields++;
                    document.getElementById('FPImage2').src = "data:image/bmp;base64," + result.BMPBase64;
                    const newFingerprintObject = {
                        fingerId: 2,
                        fingerType: 'index',
                        fingerprintData: result.ISOTemplateBase64,
                    };
                    addFingerprint(newFingerprintObject);
                }

                if (option === "third") {
                    completedFields++;
                    document.getElementById('FPImage3').src = "data:image/bmp;base64," + result.BMPBase64;
                    const newFingerprintObject = {
                        fingerId: 3,
                        fingerType: 'middle',
                        fingerprintData: result.ISOTemplateBase64,
                    };
                    addFingerprint(newFingerprintObject);
                }
                // Set the progress percentage
                const newProgress = (completedFields / 3) * 100;
                setProgress(newProgress);

            }
            setTemplate1(result.TemplateBase64); // Store the template base64 directly
            setIsCaptured(true);
        } else {
            toast.error("Fingerprint Capture Error Code: " + result.ErrorCode + ". " + ErrorCodeToString(result.ErrorCode));
        }
    };
    const enrolPatient = () => {
        const firstFingerPrintData = fingerPrintsArray.find(obj => obj.fingerId === 1)?.fingerprintData || '';
        const secondFingerPrintData = fingerPrintsArray.find(obj => obj.fingerId === 2)?.fingerprintData || '';
        const thirdFingerPrintData = fingerPrintsArray.find(obj => obj.fingerId === 3)?.fingerprintData || '';
        const requestBody = {
            id: uniqueID,
            personnelNumberPlusSuffix: membership,
            firstFingerPrint: firstFingerPrintData,
            secondFingerPrint: secondFingerPrintData,
            thirdFingerPrint: thirdFingerPrintData,

        }
        if (firstFingerPrintData !== '' && secondFingerPrintData !== '' && thirdFingerPrintData !== '') {
            axios.post(BASE_URL + `api/v1/fingerprints/create`, requestBody).then(res => {
                if (res.status === 200) {
                    console.log(res.data)
                    AuditTrailService.createLog("successful principal member enrolment");
                }
            }).catch(
                err => {
                    alert("error biometric reg")
                }
            )
        } else {
            toast.error("Successful enrolment requires three biometrics ");
        }
    }


    const ErrorCodeToString = (ErrorCode) => {
        const errorDescriptions = {
            51: "System file load failure",
            52: "Sensor chip initialization failed",
            53: "Device not found",
            54: "Fingerprint image capture timeout",
            55: "No device available",
            56: "Driver load failed",
            57: "Wrong Image",
            58: "Lack of bandwidth",
            59: "Device Busy",
            60: "Cannot get serial number of the device",
            61: "Unsupported device",
            63: "SgiBioSrv didn't start; Try image capture again",
        };
        return errorDescriptions[ErrorCode] || "Unknown error code or update code to reflect latest result";
    };


    return (
        <DashboardLayout>
            <div className="p-4">
                <h1 className="text-2xl font-bold mb-6">Student Biometric Enrolment</h1>
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

                            <div className="text-lg font-bold mb-3">Enrolment Status: <span style={{fontStyle: 'italic',  color: enrolmentStatus === 'NOT ENROLLED' ? 'red' : 'green' }}>
                                {enrolmentStatus }</span></div>
                        </div>
                        <div className="flex flex-col items-start flex-1">
                            <div className="text-lg font-bold mb-3"><span></span></div>
                        </div>
                    </div>


                    <div className="flex " style={{ backgroundColor: 'white', borderRadius: '15px', marginTop: 100, paddingTop: 10, marginBottom: 20 }}>
                        <div className="flex flex-col items-center flex-1"
                        >

                            <img id="FPImage1" alt="fingerprint" onClick={() => captureFingerPrint('first')} title='Finger Print One'
                                style={{ borderRadius: 20, cursor: 'pointer', marginBottom: 60, marginTop: 30, fontSize: '0.7rem' }}
                                height={200} width={90} src={right_hand_1}

                            />
                        </div>
                        <div className="flex flex-col items-center flex-1">
                            <img id="FPImage2" alt="Fingerprint" onClick={() => captureFingerPrint('second')} title='Finger Print Two'
                                style={{ borderRadius: 20, cursor: 'pointer', marginBottom: 60, marginTop: 30, fontSize: '0.7rem' }}
                                height={200} width={90} src={right_hand_2} />
                        </div>
                        <div className="flex flex-col items-center flex-1">
                            <img id="FPImage3" alt="Fingerprint" onClick={() => captureFingerPrint('third')} title='Finger Print Three'
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
                            <span>Enrol patient biometrics</span>
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

export default MainPatientEnrolment;