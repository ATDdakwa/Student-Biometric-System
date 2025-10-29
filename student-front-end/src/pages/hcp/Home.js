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

                    {/* Biometric & Gender Charts Side by Side */}
                    <div className="flex flex-col sm:flex-row justify-start mb-10 space-y-6 sm:space-y-0 sm:space-x-10">

                        {/* Biometric Enrolment */}
                        <div className="flex flex-col sm:flex-row items-center">
                            <BiometricDonutGraph
                                enrolled={records.enrolledStudents}
                                notEnrolled={records.notEnrolledStudents}
                            />
                            <Link
                                to="/students/enrolled"
                                className="w-60 ml-6 bg-lightseagreen rounded-lg flex flex-col items-center justify-center p-4"
                            >
                                <FaFingerprint size={60} color="white" />
                                <h2 className="text-white text-xl font-bold mt-2">Biometric Enrolment</h2>
                                <p className="text-white">Enrolled: {records.enrolledStudents}</p>
                                <p className="text-white">Not Enrolled: {records.notEnrolledStudents}</p>
                            </Link>
                        </div>

                        {/* Gender Distribution */}
                        <div className="flex flex-col sm:flex-row items-center">
                            <GenderPieChart maleCount={records.maleCount} femaleCount={records.femaleCount} />
                            <Link
                                to="/students/enrolled"
                                className="w-60 ml-6 bg-lightslategray rounded-lg flex flex-col items-center justify-center p-4"
                            >
                                <FaUser size={60} color="white" />
                                <h2 className="text-white text-xl font-bold mt-2">Gender Distribution</h2>
                                <p className="text-white">Male: {records.maleCount}</p>
                                <p className="text-white">Female: {records.femaleCount}</p>
                            </Link>
                        </div>
                    </div>


                    {/* Recently Enrolled Students */}
                    <div className="flex flex-col sm:flex-row justify-start mb-6">
                        <div className="w-full sm:w-[80%] bg-white rounded-lg shadow-md p-6 ml-10">
                            <div className="flex justify-between items-center mb-4">
                                <h2 className="text-2xl font-semibold text-gray-700">
                                    Recently Enrolled Students
                                </h2>
                                <Link
                                    to="/students"
                                    className="text-lightseagreen font-medium hover:underline"
                                >
                                    View All
                                </Link>
                            </div>
                            <div className="overflow-x-auto">
                                <table className="min-w-full border border-gray-200 rounded-lg">
                                    <thead>
                                    <tr className="bg-gray-100 text-gray-700 text-sm uppercase">
                                        <th className="py-3 px-4 text-left border-b">#</th>
                                        <th className="py-3 px-4 text-left border-b">Name</th>
                                        <th className="py-3 px-4 text-left border-b">Student Number</th>
                                        <th className="py-3 px-4 text-left border-b">Gender</th>
                                        {/*<th className="py-3 px-4 text-left border-b">Enrolled Date</th>*/}
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {students
                                        .filter(s => s.isBiometric)
                                        .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
                                        .slice(0, 5)
                                        .map((student, index) => (
                                            <tr
                                                key={student.id}
                                                className="hover:bg-gray-50 text-gray-700 text-sm"
                                            >
                                                <td className="py-2 px-4 border-b">{index + 1}</td>
                                                <td className="py-2 px-4 border-b">
                                                    {student.firstName} {student.lastName}
                                                </td>
                                                <td className="py-2 px-4 border-b">
                                                    {student.studentNumber || "-"}
                                                </td>
                                                <td className="py-2 px-4 border-b">
                                                    {student.gender || "-"}
                                                </td>
                                                {/*<td className="py-2 px-4 border-b">*/}
                                                {/*    {student.createdAt*/}
                                                {/*        ? new Date(student.createdAt).toLocaleDateString()*/}
                                                {/*        : "-"}*/}
                                                {/*</td>*/}
                                            </tr>
                                        ))}
                                    {students.filter(s => s.isBiometric).length === 0 && (
                                        <tr>
                                            <td
                                                colSpan="5"
                                                className="text-center py-4 text-gray-500 italic"
                                            >
                                                No enrolled students found.
                                            </td>
                                        </tr>
                                    )}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                </div>
            )}
        </DashboardLayout>
    );
};

export default Home;
