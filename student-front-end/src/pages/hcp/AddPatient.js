import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { toast } from 'react-toastify';
import DashboardLayout from "../../components/layout/DashboardLayout";
import { useNavigate } from 'react-router-dom';
import getBaseUrl from "./BaseUrl";
import AuditTrailService from '../../services/AuditTrailService';

const AddStudent = () => {
    const BASE_URL = getBaseUrl();
    const navigate = useNavigate();

    // ✅ Helper function to generate random Student Number
    const generateStudentNumber = () => {
        const currentYear = new Date().getFullYear().toString().slice(-2); // e.g. 2025 -> "25"
        const randomNum = Math.floor(1000 + Math.random() * 9000); // 4-digit random number
        const randomLetter = String.fromCharCode(65 + Math.floor(Math.random() * 26)); // Random A-Z
        return `R${currentYear}${randomNum}${randomLetter}`; // e.g. R250228B
    };

    const [newStudent, setNewStudent] = useState({
        studentNumber: generateStudentNumber(),
        firstName: "",
        surname: "",
        dob: "",
        nationality: "",
        idNumber: "",
        gender: "",
        email: "",
        faculty: "",
        programme: "",
        status: "REGISTERED",
        enrolmentStatus: "PENDING", // ✅ default value
        isBiometric: false,
        biometricTag: ""
    });

    const genders = ["Male", "Female"];
    const statuses = ["REGISTERED", "SUSPENDED"];
    const enrolmentStatuses = ["PENDING", "ENROLLED"];
    const faculties = ["Science", "Engineering", "Humanities", "Business", "ICT"];
    const programmes = ["BSc Computer Science", "BEng Civil", "BA English", "BCom Accounting"];

    // ✅ Regenerate new student number on page load
    useEffect(() => {
        setNewStudent((prev) => ({
            ...prev,
            studentNumber: generateStudentNumber(),
        }));
    }, []);

    const handleAddStudent = async () => {
        try {
            const response = await axios.post(BASE_URL + "api/v1/students/create", newStudent);
            toast.success("Student added successfully!");
            AuditTrailService.createLog("Successfully added a new student");
            navigate("/patients");
        } catch (error) {
            console.error("Error adding student:", error);
            AuditTrailService.createLog("Failed to add a new student");
            toast.error("Error adding student. Please try again.");
        }
    };

    const handleCancel = () => {
        navigate('/patients');
    };

    return (
        <DashboardLayout>
            <div className="p-8">
                <h1 className="text-2xl font-bold mb-6">Add New Student</h1>

                <div className="bg-gray-100 shadow-lg rounded-lg p-8 space-y-3">
                    {/* ✅ Auto-generated Student Number (disabled) */}
                    <input
                        type="text"
                        placeholder="Student Number"
                        value={newStudent.studentNumber}
                        disabled
                        className="border p-2 w-full rounded text-sm bg-gray-200 cursor-not-allowed"
                    />

                    <input
                        type="text"
                        placeholder="First Name"
                        value={newStudent.firstName}
                        onChange={(e) => setNewStudent({ ...newStudent, firstName: e.target.value })}
                        className="border p-2 w-full rounded text-sm"
                    />

                    <input
                        type="text"
                        placeholder="Surname"
                        value={newStudent.surname}
                        onChange={(e) => setNewStudent({ ...newStudent, surname: e.target.value })}
                        className="border p-2 w-full rounded text-sm"
                    />

                    <input
                        type="date"
                        placeholder="Date of Birth"
                        value={newStudent.dob}
                        onChange={(e) => setNewStudent({ ...newStudent, dob: e.target.value })}
                        className="border p-2 w-full rounded text-sm"
                    />

                    <input
                        type="text"
                        placeholder="Nationality"
                        value={newStudent.nationality}
                        onChange={(e) => setNewStudent({ ...newStudent, nationality: e.target.value })}
                        className="border p-2 w-full rounded text-sm"
                    />

                    <input
                        type="text"
                        placeholder="ID Number"
                        value={newStudent.idNumber}
                        onChange={(e) => setNewStudent({ ...newStudent, idNumber: e.target.value })}
                        className="border p-2 w-full rounded text-sm"
                    />

                    <select
                        value={newStudent.gender}
                        onChange={(e) => setNewStudent({ ...newStudent, gender: e.target.value })}
                        className="border p-2 w-full rounded text-sm"
                    >
                        <option value="">Select Gender</option>
                        {genders.map(g => (
                            <option key={g} value={g}>{g}</option>
                        ))}
                    </select>

                    <input
                        type="email"
                        placeholder="Email"
                        value={newStudent.email}
                        onChange={(e) => setNewStudent({ ...newStudent, email: e.target.value })}
                        className="border p-2 w-full rounded text-sm"
                    />

                    <select
                        value={newStudent.faculty}
                        onChange={(e) => setNewStudent({ ...newStudent, faculty: e.target.value })}
                        className="border p-2 w-full rounded text-sm"
                    >
                        <option value="">Select Faculty</option>
                        {faculties.map(f => (
                            <option key={f} value={f}>{f}</option>
                        ))}
                    </select>

                    <select
                        value={newStudent.programme}
                        onChange={(e) => setNewStudent({ ...newStudent, programme: e.target.value })}
                        className="border p-2 w-full rounded text-sm"
                    >
                        <option value="">Select Programme</option>
                        {programmes.map(p => (
                            <option key={p} value={p}>{p}</option>
                        ))}
                    </select>

                    <select
                        value={newStudent.status}
                        onChange={(e) => setNewStudent({ ...newStudent, status: e.target.value })}
                        className="border p-2 w-full rounded text-sm"
                    >
                        {statuses.map(s => (
                            <option key={s} value={s}>{s}</option>
                        ))}
                    </select>

                    <select
                        value={newStudent.enrolmentStatus}
                        onChange={(e) => setNewStudent({ ...newStudent, enrolmentStatus: e.target.value })}
                        className="border p-2 w-full rounded text-sm"
                    >
                        {enrolmentStatuses.map(es => (
                            <option key={es} value={es}>{es}</option>
                        ))}
                    </select>

                    <div className="flex justify-center gap-4 mt-4">
                        <button
                            onClick={handleAddStudent}
                            className="bg-[#000630] text-white font-bold py-2 px-6 rounded hover:bg-blue-900 text-sm"
                        >
                            Submit
                        </button>
                        <button
                            onClick={handleCancel}
                            className="bg-[#000630] text-white font-bold py-2 px-6 rounded hover:bg-blue-900 text-sm"
                        >
                            Cancel
                        </button>
                    </div>
                </div>
            </div>
        </DashboardLayout>
    );
};

export default AddStudent;