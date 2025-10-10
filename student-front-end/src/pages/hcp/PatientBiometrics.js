import React, { useEffect, useState } from "react";
import axios from "axios";
import DashboardLayout from "../../components/layout/DashboardLayout";
import SearchBar from "../../pages/commons/SearchBar";
import TableExport from "../../pages/commons/TableExport";
import Load from "../../components/layout/Load";
import getBaseUrl from "./BaseUrl";
import { IoMdEye, IoIosFingerPrint } from "react-icons/io";
import "react-toastify/dist/ReactToastify.css";
import { toast, ToastContainer } from "react-toastify";
import {useLocation, useNavigate} from "react-router-dom";

const PatientBiometrics = () => {
    const BASE_URL = getBaseUrl();
    const [studentsData, setStudentsData] = useState({ content: [], totalElements: 0 });
    const [searchTerm, setSearchTerm] = useState("");
    const [loading, setLoading] = useState(false);
    const [currentPage, setCurrentPage] = useState(0);
    const [pageSize] = useState(20);

    // Modal state
    const [selectedStudent, setSelectedStudent] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const location = useLocation();
    const navigate = useNavigate();

    const fetchData = async (page = 0) => {
        setLoading(true);
        try {
            const response = await axios.get(`${BASE_URL}api/v1/students/get-all?page=${page}&size=${pageSize}`);
            setStudentsData(response.data);
        } catch (error) {
            console.error("Error fetching students:", error);
            toast.error("Failed to fetch student biometrics data");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchData(currentPage);
    }, [currentPage]);

    const handleSearch = (term) => setSearchTerm(term || "");

    const handlePageChange = (pageNumber) => {
        if (pageNumber >= 0 && pageNumber < totalPages) {
            setCurrentPage(pageNumber);
        }
    };

    const handleView = (student) => {
        setSelectedStudent(student);
        setShowModal(true);
    };

    // const handleEnroll = (student) => {
    //     // Navigate to enrollment page
    //     window.location.href = `/main-patient-enrollment?id=${student.id}`;
    // };
    const handleEnroll = (student) => {
        navigate("/main-patient-enrollment", { state: { patient: student, nationalId: student.idNumber } });
    };


    const { content = [], totalElements = 0, totalPages = 1 } = studentsData;

    const filteredStudents = content.filter((student) =>
        Object.values(student).some(
            (value) =>
                typeof value === "string" &&
                value.toLowerCase().includes(searchTerm.toLowerCase())
        )
    );

    const formatDate = (dateString) => {
        if (!dateString) return "";
        try {
            const [year, month, day] = dateString.split("-");
            return `${day}-${month}-${year}`;
        } catch {
            return dateString;
        }
    };

    return (
        <DashboardLayout>
            <div className="p-4" style={{ padding: "3rem" }}>
                <h1 className="text-2xl font-bold mb-6">Biometric Enrollment</h1>
                <Load loading={loading} />

                {/* Search & Export */}
                <div className="flex mb-4">
                    <div className="w-3/4">
                        <SearchBar searchTerm={searchTerm} handleSearch={handleSearch} />
                    </div>
                    <div className="w-1/4 flex items-center">
                        <TableExport data={filteredStudents} filename="biometric_students" />
                    </div>
                </div>

                {/* Table */}
                <div className="overflow-y-auto max-h-[60vh] rounded-lg border border-gray-300 shadow-md">
                    <table className="min-w-full border-collapse">
                        <thead className="bg-gray-300">
                        <tr>
                            {[
                                "Student No",
                                "First Name",
                                "Surname",
                                "D.O.B",
                                "Age",
                                "ID Number",
                                "Gender",
                                "Nationality",
                                "Faculty",
                                "Programme",
                                "Biometric Status",
                                "Actions",
                            ].map((header) => (
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
                        {filteredStudents.length === 0 ? (
                            <tr>
                                <td colSpan="12" className="text-center py-4">
                                    No data available
                                </td>
                            </tr>
                        ) : (
                            filteredStudents.map((student) => (
                                <tr
                                    key={student.id}
                                    className="border-b border-gray-300 hover:bg-gray-100"
                                >
                                    <td className="py-3 px-4 text-sm">{student.studentNumber}</td>
                                    <td className="py-3 px-4 text-sm">{student.firstName}</td>
                                    <td className="py-3 px-4 text-sm">{student.surname}</td>
                                    <td className="py-3 px-4 text-sm">{formatDate(student.dob)}</td>
                                    <td className="py-3 px-4 text-sm">{student.age}</td>
                                    <td className="py-3 px-4 text-sm">{student.idNumber}</td>
                                    <td className="py-3 px-4 text-sm">{student.gender}</td>
                                    <td className="py-3 px-4 text-sm">{student.nationality}</td>
                                    <td className="py-3 px-4 text-sm">{student.faculty}</td>
                                    <td className="py-3 px-4 text-sm">{student.programme}</td>
                                    <td
                                        className="py-3 px-4 text-sm font-medium"
                                        style={{
                                            color:
                                                student.biometricStatus === "ENROLLED"
                                                    ? "green"
                                                    : "red",
                                        }}
                                    >
                                        {student.biometricStatus || "NOT ENROLLED"}
                                    </td>
                                    <td className="py-3 px-4 text-sm flex space-x-2">
                                        <button
                                            onClick={() => handleView(student)}
                                            className="bg-customGreen text-white font-bold py-1 px-2 rounded"
                                            title="View Biometric"
                                        >
                                            <IoMdEye className="mr-1 text-lg" />
                                        </button>
                                        <button
                                            onClick={() => handleEnroll(student)}
                                            className="bg-blue-700 hover:bg-blue-800 text-white font-bold py-1 px-2 rounded"
                                            title="Enroll Biometric"
                                        >
                                            <IoIosFingerPrint className="mr-1 text-lg" />
                                        </button>
                                    </td>
                                </tr>
                            ))
                        )}
                        </tbody>
                    </table>
                </div>

                {/* Pagination Controls */}
                <div className="flex justify-center mt-6 space-x-2">
                    <button
                        onClick={() => handlePageChange(currentPage - 1)}
                        disabled={currentPage === 0}
                        className={`px-4 py-2 rounded ${
                            currentPage === 0 ? "bg-gray-300" : "bg-customGreen text-white"
                        }`}
                    >
                        Prev
                    </button>

                    <span className="px-4 py-2">
                        Page {currentPage + 1} of {totalPages || 1}
                    </span>

                    <button
                        onClick={() => handlePageChange(currentPage + 1)}
                        disabled={currentPage + 1 >= totalPages}
                        className={`px-4 py-2 rounded ${
                            currentPage + 1 >= totalPages
                                ? "bg-gray-300"
                                : "bg-customGreen text-white"
                        }`}
                    >
                        Next
                    </button>
                </div>
            </div>

            {/* Biometric View Modal */}
            {showModal && selectedStudent && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
                    <div className="bg-white rounded-lg shadow-lg w-2/3 max-w-2xl p-6 relative">
                        <h2 className="text-xl font-bold mb-4">View Biometric Details</h2>
                        <button
                            onClick={() => setShowModal(false)}
                            className="absolute top-2 right-3 text-gray-600 hover:text-black text-xl"
                        >
                            ✖
                        </button>

                        <div className="grid grid-cols-2 gap-4">
                            <p><strong>Student No:</strong> {selectedStudent.studentNumber}</p>
                            <p><strong>Name:</strong> {selectedStudent.firstName} {selectedStudent.surname}</p>
                            <p><strong>D.O.B:</strong> {formatDate(selectedStudent.dob)}</p>
                            <p><strong>Age:</strong> {selectedStudent.age}</p>
                            <p><strong>ID Number:</strong> {selectedStudent.idNumber}</p>
                            <p><strong>Gender:</strong> {selectedStudent.gender}</p>
                            <p><strong>Nationality:</strong> {selectedStudent.nationality}</p>
                            <p><strong>Faculty:</strong> {selectedStudent.faculty}</p>
                            <p><strong>Programme:</strong> {selectedStudent.programme}</p>
                            <p>
                                <strong>Status:</strong>{" "}
                                <span
                                    className={
                                        selectedStudent.biometricStatus === "ENROLLED"
                                            ? "text-green-600"
                                            : "text-red-600"
                                    }
                                >
                                    {selectedStudent.biometricStatus || "NOT ENROLLED"}
                                </span>
                            </p>
                        </div>
                    </div>
                </div>
            )}

            <ToastContainer />
        </DashboardLayout>
    );
};

export default PatientBiometrics;