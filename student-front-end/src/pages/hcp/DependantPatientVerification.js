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


const DependantPatientVerification = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [matchingScore, setMatchingScore] = useState(null);
    const secugen_lic = ""; // Your license string
    const [fingerPrintFromTheScannerDevice, setFingerPrintFromTheScannerDevice] = useState('');
    const [template2, setTemplate2] = useState('');
    const [verificationStatus, setVerificationStatus] = useState('WAITING');
    const location = useLocation();
    const { suffix } = location.state;
    const { fullName } = location.state;
    const { personnelNumber } = location.state;
    const { patient } = location.state || {};
    const [fingerPrintsData, setFingerPrintsData] = useState(null);
    const [fingerPrintFromTheDatabase, setFingerPrintFromTheDatabase] = useState('');
    const [isCaptured, setIsCaptured] = useState(false); // New state to track capture status
    const navigate = useNavigate();
    const BASE_URL = getBaseUrl();
    const [enrolmentStatus, setEnrolmentStatus] = useState('NOT ENROLLED');
    // Function to initiate fingerprint capture for the first finger
    const captureFP1 = () => {
        setLoading(true);
        const timeoutId = setTimeout(() => {
            setLoading(false);
            toast.error("Fingerprint capture timed out. Please try again.");
        }, 15000); // 15 seconds timeout

        CallSGIFPGetData((result) => {
            clearTimeout(timeoutId); // Clear timeout on success
            SuccessFunc1(result);
        }, (status) => {
            clearTimeout(timeoutId); // Clear timeout on error
            ErrorFunc(status);
        });
    };

    const SuccessFunc1 = (result) => {
        console.log('SuccessFunc1 Result:', result);
        setLoading(false);
        if (result.ErrorCode === 0) {
            if (result.BMPBase64) {
                document.getElementById('FPImage1').src = "data:image/bmp;base64," + result.BMPBase64;
            }
            setFingerPrintFromTheScannerDevice(result.TemplateBase64); // Store the template base64 directly
            setIsCaptured(true);
        } else {
            toast.error("Fingerprint Capture Error Code: " + result.ErrorCode + ". " + ErrorCodeToString(result.ErrorCode));
        }
    };

    const CallSGIFPGetData = (successCall, failCall) => {
        const uri = "https://localhost:8443/SGIFPCapture";
        const xmlhttp = new XMLHttpRequest();

        xmlhttp.onreadystatechange = () => {
            if (xmlhttp.readyState === 4) {
                if (xmlhttp.status === 200) {
                    const fpobject = JSON.parse(xmlhttp.responseText);
                    setFingerPrintFromTheScannerDevice(fpobject.BMPBase64)
                    successCall(fpobject);
                } else {
                    console.error('Error in CallSGIFPGetData:', xmlhttp.status);
                    failCall(xmlhttp.status);
                }
            }
        };

        xmlhttp.onerror = () => {
            console.error('Network error in CallSGIFPGetData');
            failCall("Network error");
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



    const ErrorFunc = (status) => {
        console.error("Error in ErrorFunc:", status);
        toast.error("Check if SGIBIOSRV is running; Status = " + status);
        setLoading(false);
    };

    // const matchScore = () => {
    //     console.log("templates")
    //     console.log(template1)
    //     console.log(template2)
    //     if (!template1 || !template2) {
    //         toast.error("Please scan the finger and ensure template2 is fetched.");
    //         return;
    //     }
    //     const uri = "https://localhost:8443/SGIMatchScore";
    //     const xmlhttp = new XMLHttpRequest();
    //     xmlhttp.onreadystatechange = () => {
    //         if (xmlhttp.readyState === 4) {
    //             if (xmlhttp.status === 200) {
    //                 const result = JSON.parse(xmlhttp.responseText);
    //                 succMatch(result);
    //                 console.log("result............")
    //                 console.log(result)
    //             } else {
    //                 failureFunc(xmlhttp.status);
    //             }
    //         }
    //     };

    //     xmlhttp.onerror = () => {
    //         failureFunc("Network error");
    //     };

    //     const params = new URLSearchParams({
    //         template1: encodeURIComponent(template1),//device
    //         template2: encodeURIComponent(template2),//db
    //         licstr: encodeURIComponent(secugen_lic),
    //         templateFormat: "ISO",
    //     });

    //     xmlhttp.open("POST", uri, true);
    //     xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    //     xmlhttp.send(params.toString());
    // };

    const matchScore = () => {
        if (!fingerPrintFromTheScannerDevice) {
            toast.error("Please scan the finger and ensure template2 is fetched.");
            return;
        }

        const fingerprintList = [
            fingerPrintFromTheDatabase.firstFingerPrint,
            fingerPrintFromTheDatabase.secondFingerPrint,
            fingerPrintFromTheDatabase.thirdFingerPrint
        ];

        const uri = "https://localhost:8443/SGIMatchScore";

        const checkFingerprintMatch = (index) => {
            if (index >= fingerprintList.length) {
                toast.error("No match found ....");
                return;
            }

            const xmlhttp = new XMLHttpRequest();
            xmlhttp.onreadystatechange = () => {
                if (xmlhttp.readyState === 4) {
                    if (xmlhttp.status === 200) {
                        const result = JSON.parse(xmlhttp.responseText);
                        if (result.match) {
                            // Match found
                            succMatch(result);
                            toast.success("Match found for fingerprint at index:", index);
                        } else {
                            // No match, move to the next fingerprint
                            checkFingerprintMatch(index + 1);
                        }
                    } else {
                        failureFunc(xmlhttp.status);
                    }
                }
            };

            xmlhttp.onerror = () => {
                failureFunc("Network error");
            };

            const params = new URLSearchParams({
                template1: encodeURIComponent(fingerPrintFromTheScannerDevice),
                template2: encodeURIComponent(fingerprintList[index]),
                licstr: encodeURIComponent(secugen_lic),
                templateFormat: "ISO",
            });

            xmlhttp.open("POST", uri, true);
            xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            xmlhttp.send(params.toString());
        };

        // Start checking the fingerprints from index 0
        checkFingerprintMatch(0);
    };

    const succMatch = async (result) => {
        console.log(result)
        if (result.ErrorCode === 0) {
            setMatchingScore(result.MatchingScore);
            if (result.MatchingScore >= 50) {
                toast.success(`Fingerprints for ${fullName}  matched successfully`); // Successful match
                // Optionally, navigate to another page or reset the page here
            } else {
                toast.error(`Fingerprints for ${fullName}  did not match!`); // Failed match

                // Reset state to allow for re-scanning
                setFingerPrintFromTheScannerDevice(''); // Clear template1
                setIsCaptured(false); // Allow for capturing again
                document.getElementById('FPImage1').src = "./PlaceFinger.bmp"; // Reset the fingerprint image
                setMatchingScore(null); // Clear previous matching score
            }
        } else {
            toast.error(`Error Scanning Fingerprint: Error Code = ${result.ErrorCode}`);

            // Reset state to allow for re-scanning
            setFingerPrintFromTheScannerDevice(''); // Clear template1
            setIsCaptured(false); // Allow for capturing again
            document.getElementById('FPImage1').src = "./PlaceFinger.bmp"; // Reset the fingerprint image
            setMatchingScore(null); // Clear previous matching score
        }
    };


    const failureFunc = (error) => {
        toast.error("On Match Process, failure has been called: " + error);
    };

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


    const fetchDependantFingerPrintsData = async (memberNum) => {
        try {
            const response = await axios.get(BASE_URL + `api/v1/fingerprints/membership/` + memberNum);
            setFingerPrintsData(response.data)
            setFingerPrintFromTheDatabase(response.data.firstFingerPrint)
            if (response.data.enrolmentStatus !== undefined) { setEnrolmentStatus(response.data.enrolmentStatus) }


        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };


    useEffect(() => {

        if (personnelNumber) {
            fetchDependantFingerPrintsData(personnelNumber + "-" + suffix)
        }
    }, [personnelNumber]);

    return (
        <DashboardLayout>
            <div className="p-4">
                <h1 className="text-2xl font-bold mb-6">Dependant biometric Verification</h1>
                <hr></hr>
                <div className="bg-gray-200 p-4" style={{ borderRadius: '20px', paddingBottom: '70px', paddingTop: '70px', paddingLeft: '50px', marginTop: '30px' }} >

                    <div className="grid grid-cols-2 gap-1" > {/* Reduced gap to 2 */}
                        <p className="text-lg"><strong>Full Name:</strong></p>
                        <p className="text-lg">{patient.fullName}</p>
                        <p className="text-lg"><strong>National Id.:</strong></p>
                        <p className="text-lg">{patient.idNumber}</p>
                        <p className="text-lg"><strong>Personnel Number:</strong></p>
                        <p className="text-lg">{patient.personnelNumber} - {patient.suffix} </p>
                        <p className="text-lg"><strong>Verification Status:</strong></p>
                        <p className="text-lg" > <span style={{ fontStyle: 'italic', color: enrolmentStatus === 'NOT ENROLLED' ? 'red' : 'green' }}>
                            {enrolmentStatus}</span> </p>
                    </div>

                    <div style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', marginTop: '40px' }}>
                        <div className="flex flex-col items-start">
                            <img id="FPImage1" alt="Fingerprint Image 1" height={300} width={150} style={{ borderRadius: '20px' }} src="./PlaceFinger.bmp" />
                            {!isCaptured && ( // Render the Scan button only if fingerprint not captured

                                <div className="flex space-x-4 mb-4">
                                    <button
                                        onClick={() => captureFP1()}
                                        style={{ backgroundColor: '#000630', cursor: 'pointer', marginLeft: '-70px', marginTop: '50px' }}
                                        className="hover:bg-green-700 text-white font-bold py-2 px-4 rounded text-sm flex items-center"
                                    >
                                        <IoIosFingerPrint className="mr-1 text-lg" />
                                        <span>Scan patient fingerprint for verification</span>
                                    </button>
                                </div>
                            )}

                            {loading && <Load loading={loading} />}
                            {error && <p className="text-red-500 mt-2">{error}</p>}
                        </div>
                        {isCaptured && ( // Render the Submit button only if fingerprint captured
                            <div className="flex space-x-4 mb-4">
                                <button
                                    onClick={() => matchScore()}
                                    style={{ backgroundColor: '#000630', cursor: 'pointer', marginLeft: '-70px', marginTop: '50px' }}
                                    className="hover:bg-green-700 text-white font-bold py-2 px-4 rounded text-sm flex items-center"
                                >
                                    <IoIosFingerPrint className="mr-1 text-lg" />
                                    <span>Verify patient biometrics</span>
                                </button>
                            </div>
                        )}
                        {matchingScore !== null && (
                            <div className="mt-4">
                                <h4 className="font-bold">Matching Percentage: {((matchingScore / 199) * 100).toFixed(2)}% </h4>
                            </div>
                        )}

                    </div>

                </div>
                {/* <BackButton />  */}
                {/* Include the BackButton here */}
                <ToastContainer /> {/* Add ToastContainer here */}
            </div>
        </DashboardLayout>
    );
};

export default DependantPatientVerification;