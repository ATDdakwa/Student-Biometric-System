import React, { useEffect, useState } from "react";
import axios from "axios";
import DashboardLayout from "../../components/layout/DashboardLayout";
import SearchBar from "../../pages/commons/SearchBar";
import TableExport from "../../pages/commons/TableExport";
import Load from "../../components/layout/Load";
import getBaseUrl from "./BaseUrl";
import { useNavigate, useLocation } from "react-router-dom";
import { IoIosCheckmark, IoMdRefresh } from "react-icons/io";
import "react-toastify/dist/ReactToastify.css";
import { toast, ToastContainer } from "react-toastify";

const StudentVerification = () => {
    const BASE_URL = getBaseUrl();
    const [students, setStudents] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [loading, setLoading] = useState(false);
    const [currentPage, setCurrentPage] = useState(1);
    const [accessPoint, setAccessPoint] = useState("");
    const itemsPerPage = 20;
    const navigate = useNavigate();
    const location = useLocation();

    // Capture access point (passed when navigating to this component)
    useEffect(() => {
        if (location.state && location.state.accessPoint) {
            setAccessPoint(location.state.accessPoint);
        } else {
            setAccessPoint("Main Hall"); // Default value if none is passed
        }
    }, [location]);

    // Fetch students from API
    const fetchStudents = async () => {
        setLoading(true);
        try {
            const response = await axios.get(`${BASE_URL}api/v1/students/get-all-students`);
            const studentsData = response.data || [];
            setStudents(studentsData);
        } catch (error) {
            console.error("Error fetching students:", error);
            toast.error("Failed to fetch student data");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchStudents();
    }, []);

    // Handle search input
    const handleSearch = (term) => {
        if (typeof term === "string") {
            setSearchTerm(term);
        } else {
            console.error("Search term must be a string:", term);
            setSearchTerm("");
        }
    };

    // Filter students by search term
    const filteredStudents = students.filter((student) =>
        Object.values(student).some(
            (value) =>
                typeof value === "string" &&
                value.toLowerCase().includes(searchTerm.toLowerCase())
        )
    );

    // Pagination logic
    const totalPages = Math.ceil(filteredStudents.length / itemsPerPage);
    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentItems = filteredStudents.slice(indexOfFirstItem, indexOfLastItem);

    const handlePageChange = (pageNumber) => setCurrentPage(pageNumber);

    const getPaginationRange = () => {
        const range = [];
        const start = Math.max(1, currentPage - 4);
        const end = Math.min(totalPages, start + 9);
        for (let i = start; i <= end; i++) range.push(i);
        return range;
    };

    // Handle verification navigation
    const handleVerify = (student) => {
        const studentNumber = student.studentNumber;
        if (!studentNumber) {
            toast.error("No student number found");
            return;
        }

        navigate("/student-fingerprint-verification", {
            state: { studentNumber, student, accessPoint },
        });
    };

    return (
        <DashboardLayout>
            <div className="p-4" style={{ padding: "3rem" }}>
                <h1 className="text-2xl font-bold mb-6">
                    Student Verification -{" "}
                    <span className="text-customGreen">{accessPoint}</span>
                </h1>

                <Load loading={loading} />

                <div className="flex mb-4">
                    <div className="w-3/4">
                        <SearchBar searchTerm={searchTerm} handleSearch={handleSearch} />
                    </div>
                    <div className="w-1/4 flex items-center">
                        <TableExport data={filteredStudents} filename="students_verification" />
                    </div>
                </div>

                <div className="flex space-x-4 mb-4">
                    <button
                        onClick={fetchStudents}
                        style={{ backgroundColor: "#000630" }}
                        className="hover:bg-green-700 text-white font-bold py-2 px-4 rounded text-sm flex items-center"
                    >
                        <IoMdRefresh className="mr-1" />
                        <span>Refresh</span>
                    </button>
                </div>

                <div className="overflow-y-auto max-h-[60vh] border border-gray-300 rounded-lg shadow-md">
                    <table className="min-w-full text-sm text-left">
                        <thead className="bg-gray-300">
                        <tr>
                            <th className="py-3 px-4 font-semibold">Student Number</th>
                            <th className="py-3 px-4 font-semibold">First Name</th>
                            <th className="py-3 px-4 font-semibold">Surname</th>
                            <th className="py-3 px-4 font-semibold">Faculty</th>
                            <th className="py-3 px-4 font-semibold">Programme</th>
                            <th className="py-3 px-4 font-semibold">Status</th>
                            <th className="py-3 px-4 font-semibold">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {currentItems.length === 0 ? (
                            <tr>
                                <td colSpan="7" className="text-center py-4">
                                    No data found
                                </td>
                            </tr>
                        ) : (
                            currentItems.map((student) => (
                                <tr
                                    key={student.id}
                                    className="border-b border-gray-200 hover:bg-gray-100"
                                >
                                    <td className="py-3 px-4">{student.studentNumber}</td>
                                    <td className="py-3 px-4">{student.firstName}</td>
                                    <td className="py-3 px-4">{student.surname}</td>
                                    <td className="py-3 px-4">{student.faculty}</td>
                                    <td className="py-3 px-4">{student.programme}</td>
                                    <td
                                        className="py-3 px-4 font-semibold"
                                        style={{
                                            color:
                                                student.enrolmentStatus === "ENROLLED"
                                                    ? "green"
                                                    : "red",
                                        }}
                                    >
                                        {student.enrolmentStatus || "PENDING"}
                                    </td>
                                    <td className="py-3 px-4">
                                        {student.enrolmentStatus === "ENROLLED" ? (
                                            <button
                                                onClick={() => handleVerify(student)}
                                                className="bg-customGreen hover:bg-green-700 text-white font-bold py-1 px-3 rounded flex items-center"
                                                title="Verify Fingerprint"
                                            >
                                                <IoIosCheckmark className="mr-1 text-lg" />
                                                Verify
                                            </button>
                                        ) : (
                                            <span className="text-gray-400 italic">Not Enrolled</span>
                                        )}
                                    </td>
                                </tr>
                            ))
                        )}
                        </tbody>
                    </table>
                </div>

                {/* Pagination */}
                <div className="flex justify-center mt-4">
                    {getPaginationRange().map((pageNumber) => (
                        <button
                            key={pageNumber}
                            onClick={() => handlePageChange(pageNumber)}
                            className={`mx-1 px-4 py-2 rounded ${
                                currentPage === pageNumber
                                    ? "bg-customGreen text-white"
                                    : "bg-gray-300"
                            }`}
                        >
                            {pageNumber}
                        </button>
                    ))}
                </div>
            </div>

            <Load loading={loading} />
            <ToastContainer />
        </DashboardLayout>
    );
};

export default StudentVerification;
