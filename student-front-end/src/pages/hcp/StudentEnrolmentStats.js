import React, { useEffect, useState } from "react";
import axios from "axios";
import DashboardLayout from "../../components/layout/DashboardLayout";
import Load from "../../components/layout/Load";
import getBaseUrl from "./BaseUrl";
import { IoMdEye } from "react-icons/io";
import { useNavigate } from "react-router-dom";
import { toast, ToastContainer } from "react-toastify";
import AuditTrailService from "../../services/AuditTrailService";

const EnrolmentStats = () => {
    const BASE_URL = getBaseUrl();
    const [loading, setLoading] = useState(false);
    const [students, setStudents] = useState([]);
    const navigate = useNavigate();

    const fetchEnrolledStudents = async () => {
        setLoading(true);
        try {
            const response = await axios.get(BASE_URL + "api/v1/students/enrolled"); // API endpoint for enrolled students
            setStudents(response.data);
            AuditTrailService.createLog("Fetched enrolled students");
            setLoading(false);
        } catch (error) {
            console.error("Error fetching enrolled students:", error);
            toast.error("Failed to load enrolled students");
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchEnrolledStudents();
    }, []);

    const handleView = (studentId) => {
        navigate(`/students/${studentId}`); // Navigate to student profile or biometric verification
    };

    return (
        <DashboardLayout>
            <div className="p-4" style={{ padding: '3rem' }}>
                <h1 className="text-2xl font-bold mb-6">Enrolled Students</h1>
                <Load loading={loading} />

                <div className="overflow-y-auto max-h-[70vh]">
                    <table className="min-w-full border border-gray-300 shadow-lg rounded-lg">
                        <thead className="bg-gray-300">
                        <tr>
                            {["Reg. No", "Full Name", "Gender", "Age", "Faculty", "Programme", "Enrollment Date", "Actions"].map(header => (
                                <th
                                    key={header}
                                    className="py-3 px-4 text-sm text-left font-semibold border-b border-gray-400"
                                >
                                    {header}
                                </th>
                            ))}
                        </tr>
                        </thead>
                        <tbody>
                        {students.length === 0 ? (
                            <tr>
                                <td colSpan={8} className="text-center py-4">No students enrolled</td>
                            </tr>
                        ) : (
                            students.map((student) => (
                                <tr key={student.id} className="border-b border-gray-300 hover:bg-gray-100">
                                    <td className="py-3 px-4 text-sm">{student.studentNumber}</td>
                                    <td className="py-3 px-4 text-sm">{student.firstName + "  " + student.surname}</td>
                                    <td className="py-3 px-4 text-sm">{student.gender}</td>
                                    <td className="py-3 px-4 text-sm">{student.age}</td>
                                    <td className="py-3 px-4 text-sm">{student.faculty}</td>
                                    <td className="py-3 px-4 text-sm">{student.programme}</td>
                                    <td className="py-3 px-4 text-sm">{new Date(student.enrollmentDate).toLocaleDateString()}</td>
                                    <td className="py-3 px-4 text-sm">
                                        <button
                                            onClick={() => handleView(student.id)}
                                            className="flex items-center gap-1 bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-2 rounded"
                                        >
                                            <IoMdEye /> View
                                        </button>
                                    </td>
                                </tr>
                            ))
                        )}
                        </tbody>
                    </table>
                </div>
            </div>

            <ToastContainer />
            <Load loading={loading} />
        </DashboardLayout>
    );
};

export default EnrolmentStats;