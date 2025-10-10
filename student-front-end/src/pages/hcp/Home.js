import React, { useState, useEffect } from "react";
import axios from "axios";
import DashboardLayout from "../../components/layout/DashboardLayout";
import { Link } from "react-router-dom";
import { FaUser, FaFingerprint, FaChartBar, FaUniversity } from "react-icons/fa";
import getBaseUrl from "./BaseUrl";
import BiometricDonutGraph from "./BiometricDonutGraph";
import GenderPieChart from "./GenderPieChart";
import Load from "../../components/layout/Load";
import AuditTrailService from "../../services/AuditTrailService";

const Home = () => {
    const BASE_URL = getBaseUrl();
    const [students, setStudents] = useState([]);
    const [loading, setLoading] = useState(false);
    const [records, setRecords] = useState({
        enrolledStudents: 0,
        notEnrolledStudents: 0,
        maleCount: 0,
        femaleCount: 0,
        totalAccessEvents: 0,
        verifiedAccess: 0,
        unverifiedAccess: 0,
    });

    useEffect(() => {
        const fetchStudentData = async () => {
            setLoading(true);
            try {
                // Fetch all students (paginated)
                const studentResponse = await axios.get(`${BASE_URL}api/v1/students/get-all`);
                const allStudents = studentResponse.data.content || []; // <-- fix here

                // Fetch all access events
                const eventsResponse = await axios.get(`${BASE_URL}api/v1/tracking/events`, {
                    params: {
                        from: new Date(0).toISOString(),
                        to: new Date().toISOString(),
                    },
                });
                const events = eventsResponse.data || []; // safe fallback if empty

                // Compute stats
                const enrolledStudents = allStudents.filter(s => s.isBiometric).length; // note your JSON uses "isBiometric"
                const notEnrolledStudents = allStudents.length - enrolledStudents;
                const maleCount = allStudents.filter(s => s.gender === "Male").length;
                const femaleCount = allStudents.filter(s => s.gender === "Female").length;
                const verifiedAccess = events.filter(e => e.verified).length;
                const unverifiedAccess = events.filter(e => !e.verified).length;

                setStudents(allStudents);
                setRecords({
                    enrolledStudents,
                    notEnrolledStudents,
                    maleCount,
                    femaleCount,
                    totalAccessEvents: events.length,
                    verifiedAccess,
                    unverifiedAccess,
                });

                AuditTrailService.createLog("Fetched student dashboard data");
                setLoading(false);
            } catch (error) {
                console.error("Error fetching student data:", error);
                setLoading(false);
            }
        };

        fetchStudentData();
    }, []);

    return (
        <DashboardLayout>
            <Load loading={loading} />
            {students.length > 0 && (
                <div className="overflow-y-auto max-h-[82vh] mt-10 ml-10">

                    {/* Biometric Enrolment */}
                    <div className="flex flex-col sm:flex-row justify-start mb-6">
                        <BiometricDonutGraph
                            enrolled={records.enrolledStudents}
                            notEnrolled={records.notEnrolledStudents}
                        />
                        <Link
                            to="/students/enrolled"
                            className="w-60 ml-10 bg-lightseagreen rounded-lg flex flex-col items-center justify-center p-4"
                        >
                            <FaFingerprint size={60} color="white" />
                            <h2 className="text-white text-xl font-bold mt-2">Biometric Enrolment</h2>
                            <p className="text-white">Enrolled: {records.enrolledStudents}</p>
                            <p className="text-white">Not Enrolled: {records.notEnrolledStudents}</p>
                        </Link>
                    </div>

                    {/* Gender Distribution */}
                    <div className="flex flex-col sm:flex-row justify-start mb-6">
                        <GenderPieChart maleCount={records.maleCount} femaleCount={records.femaleCount} />
                        <Link
                            to="/students/enrolled"
                            className="w-60 ml-10 bg-lightslategray rounded-lg flex flex-col items-center justify-center p-4"
                        >
                            <FaUser size={60} color="white" />
                            <h2 className="text-white text-xl font-bold mt-2">Gender Distribution</h2>
                            <p className="text-white">Male: {records.maleCount}</p>
                            <p className="text-white">Female: {records.femaleCount}</p>
                        </Link>
                    </div>

                    {/* Campus Access Summary */}
                    <div className="flex flex-col sm:flex-row justify-start mb-6">
                        <Link
                            to="/tracking/activity"
                            className="w-60 ml-10 bg-lightsteelblue rounded-lg flex flex-col items-center justify-center p-4"
                        >
                            <FaUniversity size={60} color="white" />
                            <h2 className="text-white text-xl font-bold mt-2">Campus Access Activity</h2>
                            <p className="text-white">Total Events: {records.totalAccessEvents}</p>
                            <p className="text-white">Verified: {records.verifiedAccess}</p>
                            <p className="text-white">Unverified: {records.unverifiedAccess}</p>
                        </Link>
                    </div>

                </div>
            )}
        </DashboardLayout>
    );
};

export default Home;