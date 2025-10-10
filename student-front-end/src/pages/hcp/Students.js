import React, { useEffect, useState } from "react";
import axios from "axios";
import DashboardLayout from "../../components/layout/DashboardLayout";
import SearchBar from "../commons/SearchBar";
import TableExport from "../commons/TableExport";
import getBaseUrl from "./BaseUrl";
import { IoMdEye, IoMdAdd, IoMdClose } from "react-icons/io";
import 'react-toastify/dist/ReactToastify.css';
import { toast, ToastContainer } from 'react-toastify';
import Load from "../../components/layout/Load";
import AuditTrailService from "../../services/AuditTrailService";

const Students = () => {
    const BASE_URL = getBaseUrl();
    const [studentsData, setStudentsData] = useState({ content: [], totalElements: 0 });
    const [searchTerm, setSearchTerm] = useState("");
    const [loading, setLoading] = useState(false);
    const [currentPage, setCurrentPage] = useState(0);
    const [pageSize] = useState(20);
    const [selectedStudent, setSelectedStudent] = useState(null); // ✅ for modal
    const [showModal, setShowModal] = useState(false);

    const fetchData = async (page = 0) => {
        setLoading(true);
        try {
            const response = await axios.get(`${BASE_URL}api/v1/students/get-all?page=${page}&size=${pageSize}`);
            setStudentsData(response.data);
            AuditTrailService.createLog("Fetched student demographics information");
        } catch (error) {
            console.error("Error fetching students:", error);
            toast.error("Failed to fetch student data");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchData(currentPage);
    }, [currentPage]);

    const handleSearch = (term) => setSearchTerm(term || "");

    const handlePageChange = (pageNumber) => {
        const totalPages = studentsData.totalPages || 1;
        if (pageNumber >= 0 && pageNumber < totalPages) {
            setCurrentPage(pageNumber);
        }
    };

    // ✅ Show modal instead of navigating
    const handleView = (student) => {
        setSelectedStudent(student);
        setShowModal(true);
    };

    const closeModal = () => {
        setSelectedStudent(null);
        setShowModal(false);
    };

    const { content = [], totalPages = 1 } = studentsData;

    const filteredStudents = content.filter((student) =>
        Object.values(student).some(
            (value) => typeof value === "string" && value.toLowerCase().includes(searchTerm.toLowerCase())
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
                <h1 className="text-2xl font-bold mb-6">Student Demographics</h1>

                <Load loading={loading} />

                {/* Search & Export */}
                <div className="flex mb-4">
                    <div className="w-3/4">
                        <SearchBar searchTerm={searchTerm} handleSearch={handleSearch} />
                    </div>
                    <div className="w-1/4 flex items-center">
                        <TableExport data={filteredStudents} filename="students" />
                    </div>
                </div>

                {/* Add New Student */}
                <div className="flex space-x-4 mb-4">
                    <button
                        onClick={() => (window.location.href = "/add-student")}
                        style={{ backgroundColor: "#000630" }}
                        className="hover:bg-green-700 text-white font-bold py-2 px-4 rounded text-sm flex items-center"
                    >
                        <IoMdAdd className="mr-1" />
                        <span>Add New Student</span>
                    </button>
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
                                "Enrolment Status",
                                "Actions"
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
                                <td colSpan="14" className="text-center py-4">
                                    No data available
                                </td>
                            </tr>
                        ) : (
                            filteredStudents.map((student) => (
                                <tr key={student.id} className="border-b border-gray-300 hover:bg-gray-100">
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
                                                student.enrolmentStatus === "ENROLLED"
                                                    ? "green"
                                                    : "red",
                                        }}
                                    >
                                        {student.enrolmentStatus || "PENDING"}
                                    </td>
                                    <td className="py-3 px-4 text-sm">
                                        <button
                                            onClick={() => handleView(student)}
                                            className="bg-[#000630] text-white font-bold py-1 px-2 rounded"
                                            title="View Details"
                                        >
                                            <IoMdEye className="text-lg" />
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
                        className={`px-4 py-2 rounded ${currentPage === 0 ? "bg-gray-300" : "bg-[#000630] text-white"}`}
                    >
                        Prev
                    </button>

                    <span className="px-4 py-2">
                        Page {currentPage + 1} of {totalPages || 1}
                    </span>

                    <button
                        onClick={() => handlePageChange(currentPage + 1)}
                        disabled={currentPage + 1 >= totalPages}
                        className={`px-4 py-2 rounded ${currentPage + 1 >= totalPages ? "bg-gray-300" : "bg-[#000630] text-white"}`}
                    >
                        Next
                    </button>
                </div>
            </div>

            {/* ✅ Student Details Modal */}
            {showModal && selectedStudent && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white rounded-lg shadow-lg w-[600px] p-6 relative">
                        <button
                            onClick={closeModal}
                            className="absolute top-3 right-3 text-gray-600 hover:text-red-600"
                        >
                            <IoMdClose size={24} />
                        </button>

                        <h2 className="text-xl font-bold mb-4">Student Details</h2>
                        <div className="grid grid-cols-2 gap-4 text-sm">
                            <p><strong>Student No:</strong> {selectedStudent.studentNumber}</p>
                            <p><strong>Name:</strong> {selectedStudent.firstName} {selectedStudent.surname}</p>
                            <p><strong>DOB:</strong> {formatDate(selectedStudent.dob)}</p>
                            <p><strong>Gender:</strong> {selectedStudent.gender}</p>
                            <p><strong>ID Number:</strong> {selectedStudent.idNumber}</p>
                            <p><strong>Nationality:</strong> {selectedStudent.nationality}</p>
                            <p><strong>Faculty:</strong> {selectedStudent.faculty}</p>
                            <p><strong>Programme:</strong> {selectedStudent.programme}</p>
                            <p><strong>Status:</strong> {selectedStudent.status}</p>
                            <p><strong>Enrolment Status:</strong> {selectedStudent.enrolmentStatus}</p>
                        </div>

                        <div className="flex justify-end mt-6">
                            <button
                                onClick={closeModal}
                                className="bg-[#000630] text-white py-2 px-4 rounded hover:bg-blue-900 text-sm"
                            >
                                Close
                            </button>
                        </div>
                    </div>
                </div>
            )}

            <ToastContainer />
        </DashboardLayout>
    );
};

export default Students;