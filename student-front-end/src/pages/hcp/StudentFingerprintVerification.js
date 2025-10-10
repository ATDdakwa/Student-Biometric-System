import React, { useState, useEffect } from 'react';
import { toast, ToastContainer } from 'react-toastify';
import DashboardLayout from "../../components/layout/DashboardLayout";
import Load from "../../components/layout/Load";
import { useLocation, useNavigate } from 'react-router-dom';
import 'react-toastify/dist/ReactToastify.css';
import { IoIosFingerPrint } from "react-icons/io";
import axios from 'axios';
import getBaseUrl from './BaseUrl';

const StudentFingerprintVerification = () => {
    const [loading, setLoading] = useState(false);
    const location = useLocation();
    const [error, setError] = useState('');
    const [matchingScore, setMatchingScore] = useState(null);
    const [fingerPrintsData, setFingerPrintsData] = useState(null);
    const [fingerPrintFromTheDatabase, setFingerPrintFromTheDatabase] = useState('');
    const [fingerPrintFromTheScannerDevice, setFingerPrintFromTheScannerDevice] = useState('');
    const [isCaptured, setIsCaptured] = useState(false);
    const [enrolmentStatus, setEnrolmentStatus] = useState('NOT ENROLLED');

    const navigate = useNavigate();
    const BASE_URL = getBaseUrl();
    const secugen_lic = ""; // Your license string

    const { student, accessPoint } = location.state || {};

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

    const captureFP1 = () => {
        setLoading(true);
        const timeoutId = setTimeout(() => {
            setLoading(false);
            toast.error("Fingerprint capture timed out. Please try again.");
        }, 15000); // 15 seconds timeout

        CallSGIFPGetData((result) => {
            clearTimeout(timeoutId);
            SuccessFunc1(result);
        }, (status) => {
            clearTimeout(timeoutId);
            ErrorFunc(status);
        });
    };

    const SuccessFunc1 = (result) => {
        setLoading(false);
        if (result.ErrorCode === 0) {
            if (result.BMPBase64) {
                document.getElementById('FPImage1').src = "data:image/bmp;base64," + result.BMPBase64;
            }
            setFingerPrintFromTheScannerDevice(result.TemplateBase64);
            setIsCaptured(true);
        } else {
            toast.error("Fingerprint Capture Error Code: " + result.ErrorCode);
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
                    failCall(xmlhttp.status);
                }
            }
        };

        xmlhttp.onerror = () => {
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
        toast.error("Check if SGIBIOSRV is running; Status = " + status);
        setLoading(false);
    };

    const matchScore = () => {
        if (!fingerPrintFromTheScannerDevice || !fingerPrintsData) {
            toast.error("Please scan the finger and ensure template2 is fetched.");
            return;
        }

        const fingerprintList = [
            fingerPrintsData.firstFingerPrint,
            fingerPrintsData.secondFingerPrint,
            fingerPrintsData.thirdFingerPrint
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
                            succMatch(result);
                            toast.success("Match found for fingerprint at index: " + index);
                        } else {
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

        checkFingerprintMatch(0);
    };

    const succMatch = (result) => {
        if (result.ErrorCode === 0) {
            setMatchingScore(result.MatchingScore);
            if (result.MatchingScore >= 50) {
                toast.success(`Fingerprints for ${student.firstName} ${student.surname} matched successfully`);
            } else {
                toast.error(`Fingerprints for ${student.firstName} ${student.surname} did not match!`);
                resetScan();
            }
        } else {
            toast.error(`Error Scanning Fingerprint: Error Code = ${result.ErrorCode}`);
            resetScan();
        }
    };

    const failureFunc = (error) => {
        toast.error("On Match Process, failure has been called: " + error);
    };

    const resetScan = () => {
        setFingerPrintFromTheScannerDevice('');
        setIsCaptured(false);
        document.getElementById('FPImage1').src = "./PlaceFinger.bmp";
        setMatchingScore(null);
    };

    const fetchPrincipalFingerPrintsData = async (studentNumber) => {
        try {
            const response = await axios.get(`${BASE_URL}api/v1/fingerprints/membership/${studentNumber}`);
            setFingerPrintsData(response.data);
            setFingerPrintFromTheDatabase(response.data.firstFingerPrint);
            if (response.data.enrolmentStatus) {
                setEnrolmentStatus(response.data.enrolmentStatus);
            }
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };


    return (
        <DashboardLayout>
            <div className="p-4">
                <h1 className="text-2xl font-bold mb-6">Student Biometric Verification</h1>
                <hr />

                <div className="bg-gray-200 p-4" style={{ borderRadius: '20px', padding: '70px 50px' }}>
                    <div className="grid grid-cols-2 gap-1">
                        <p className="text-lg"><strong>First Name:</strong></p>
                        <p className="text-lg">{student.firstName}</p>

                        <p className="text-lg"><strong>Last Name:</strong></p>
                        <p className="text-lg">{student.surname}</p>

                        <p className="text-lg"><strong>Student Number:</strong></p>
                        <p className="text-lg">{student.studentNumber}</p>

                        <p className="text-lg"><strong>Enrolment Status:</strong></p>
                        <p className="text-lg">
                            <span style={{ fontStyle: 'italic', color: enrolmentStatus === 'NOT ENROLLED' ? 'red' : 'green' }}>
                                {enrolmentStatus}
                            </span>
                        </p>
                    </div>

                    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', marginTop: '40px' }}>
                        <div className="flex flex-col items-start">
                            <img id="FPImage1" alt="Fingerprint Image" height={300} width={150} style={{ borderRadius: '20px' }} src="./PlaceFinger.bmp" />

                            {!isCaptured && (
                                <div className="flex space-x-4 mb-4 mt-4">
                                    <button
                                        onClick={captureFP1}
                                        style={{ backgroundColor: '#000630', cursor: 'pointer' }}
                                        className="hover:bg-green-700 text-white font-bold py-2 px-4 rounded text-sm flex items-center"
                                    >
                                        <IoIosFingerPrint className="mr-1 text-lg" />
                                        <span>Scan student fingerprint</span>
                                    </button>
                                </div>
                            )}

                            {loading && <Load loading={loading} />}
                            {error && <p className="text-red-500 mt-2">{error}</p>}
                        </div>

                        {isCaptured && (
                            <div className="flex space-x-4 mb-4 mt-4">
                                <button
                                    onClick={matchScore}
                                    style={{ backgroundColor: '#000630', cursor: 'pointer' }}
                                    className="hover:bg-green-700 text-white font-bold py-2 px-4 rounded text-sm flex items-center"
                                >
                                    <IoIosFingerPrint className="mr-1 text-lg" />
                                    <span>Verify student biometrics</span>
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
                <ToastContainer />
            </div>
        </DashboardLayout>
    );
};

export default StudentFingerprintVerification;