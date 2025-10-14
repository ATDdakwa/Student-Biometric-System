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
        const currentYear = new Date().getFullYear().toString().slice(-2);
        const randomNum = Math.floor(1000 + Math.random() * 9000);
        const randomLetter = String.fromCharCode(65 + Math.floor(Math.random() * 26));
        return `R${currentYear}${randomNum}${randomLetter}`;
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
        enrolmentStatus: "PENDING",
        isBiometric: false,
        biometricTag: ""
    });

    const [errors, setErrors] = useState({});

    const genders = ["Male", "Female"];
    const statuses = ["REGISTERED", "SUSPENDED"];
    const enrolmentStatuses = ["PENDING"];
    const faculties = ["Science", "Engineering", "Humanities", "Business", "ICT"];
    const programmes = ["BSc Computer Science", "BEng Civil", "BA English", "BCom Accounting"];

    useEffect(() => {
        setNewStudent((prev) => ({
            ...prev,
            studentNumber: generateStudentNumber(),
        }));
    }, []);

    // ✅ Validation functions
    const validateName = (name, fieldName) => {
        if (!name.trim()) {
            return `${fieldName} is required`;
        }
        if (/\d/.test(name)) {
            return `${fieldName} should not contain numbers`;
        }
        if (name.trim().length < 2) {
            return `${fieldName} must be at least 2 characters`;
        }
        return "";
    };

    const validateZimID = (idNumber) => {
        if (!idNumber.trim()) {
            return "ID Number is required";
        }

        // Zimbabwe ID format: XX-XXXXXXX (6-7 digits) + Letter + XX
        // Examples: 83-2000838L83 or 63-123456A63
        const zimIDPattern = /^\d{2}-\d{6,7}[A-Z]\d{2}$/;

        if (!zimIDPattern.test(idNumber)) {
            return "Invalid Zimbabwe ID format. Expected: XX-XXXXXXX (6-7 digits) + Letter + XX (e.g., 83-2000838L83)";
        }
        return "";
    };

    const validateEmail = (email) => {
        if (!email.trim()) {
            return "Email is required";
        }
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailPattern.test(email)) {
            return "Invalid email format";
        }
        return "";
    };

    const validateDOB = (dob) => {
        if (!dob) {
            return "Date of Birth is required";
        }
        const birthDate = new Date(dob);
        const today = new Date();
        const age = today.getFullYear() - birthDate.getFullYear();

        if (age < 16 || age > 100) {
            return "Student must be between 16 and 100 years old";
        }
        return "";
    };

    const validateForm = () => {
        const newErrors = {};

        // Validate First Name
        const firstNameError = validateName(newStudent.firstName, "First Name");
        if (firstNameError) newErrors.firstName = firstNameError;

        // Validate Surname
        const surnameError = validateName(newStudent.surname, "Surname");
        if (surnameError) newErrors.surname = surnameError;

        // Validate DOB
        const dobError = validateDOB(newStudent.dob);
        if (dobError) newErrors.dob = dobError;

        // Validate Nationality
        if (!newStudent.nationality.trim()) {
            newErrors.nationality = "Nationality is required";
        }

        // Validate ID Number (Zimbabwe format)
        const idError = validateZimID(newStudent.idNumber);
        if (idError) newErrors.idNumber = idError;

        // Validate Gender
        if (!newStudent.gender) {
            newErrors.gender = "Gender is required";
        }

        // Validate Email
        const emailError = validateEmail(newStudent.email);
        if (emailError) newErrors.email = emailError;

        // Validate Faculty
        if (!newStudent.faculty) {
            newErrors.faculty = "Faculty is required";
        }

        // Validate Programme
        if (!newStudent.programme) {
            newErrors.programme = "Programme is required";
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleInputChange = (field, value) => {
        setNewStudent({ ...newStudent, [field]: value });

        // Clear error for this field when user types
        if (errors[field]) {
            setErrors({ ...errors, [field]: "" });
        }
    };

    const handleNameInput = (field, value) => {
        // Remove numbers from input
        const cleanedValue = value.replace(/[0-9]/g, '');
        handleInputChange(field, cleanedValue);
    };

    const handleIDInput = (value) => {
        // Allow only valid ID characters (digits, hyphen, and letters)
        const cleanedValue = value.toUpperCase().replace(/[^0-9A-Z-]/g, '');
        handleInputChange('idNumber', cleanedValue);
    };

    const handleAddStudent = async () => {
        // Validate form before submission
        if (!validateForm()) {
            toast.error("Please fix all validation errors before submitting");
            return;
        }

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
                    {/* Auto-generated Student Number */}
                    <div>
                        <input
                            type="text"
                            placeholder="Student Number"
                            value={newStudent.studentNumber}
                            disabled
                            className="border p-2 w-full rounded text-sm bg-gray-200 cursor-not-allowed"
                        />
                    </div>

                    {/* First Name */}
                    <div>
                        <input
                            type="text"
                            placeholder="First Name"
                            value={newStudent.firstName}
                            onChange={(e) => handleNameInput('firstName', e.target.value)}
                            className={`border p-2 w-full rounded text-sm ${errors.firstName ? 'border-red-500' : ''}`}
                        />
                        {errors.firstName && <p className="text-red-500 text-xs mt-1">{errors.firstName}</p>}
                    </div>

                    {/* Surname */}
                    <div>
                        <input
                            type="text"
                            placeholder="Surname"
                            value={newStudent.surname}
                            onChange={(e) => handleNameInput('surname', e.target.value)}
                            className={`border p-2 w-full rounded text-sm ${errors.surname ? 'border-red-500' : ''}`}
                        />
                        {errors.surname && <p className="text-red-500 text-xs mt-1">{errors.surname}</p>}
                    </div>

                    {/* Date of Birth */}
                    <div>
                        <input
                            type="date"
                            placeholder="Date of Birth"
                            value={newStudent.dob}
                            onChange={(e) => handleInputChange('dob', e.target.value)}
                            className={`border p-2 w-full rounded text-sm ${errors.dob ? 'border-red-500' : ''}`}
                            max={new Date().toISOString().split('T')[0]}
                        />
                        {errors.dob && <p className="text-red-500 text-xs mt-1">{errors.dob}</p>}
                    </div>

                    {/* Nationality */}
                    <div>
                        <input
                            type="text"
                            placeholder="Nationality"
                            value={newStudent.nationality}
                            onChange={(e) => handleInputChange('nationality', e.target.value)}
                            className={`border p-2 w-full rounded text-sm ${errors.nationality ? 'border-red-500' : ''}`}
                        />
                        {errors.nationality && <p className="text-red-500 text-xs mt-1">{errors.nationality}</p>}
                    </div>

                    {/* ID Number */}
                    <div>
                        <input
                            type="text"
                            placeholder="ID Number (e.g., 83-2000838L83)"
                            value={newStudent.idNumber}
                            onChange={(e) => handleIDInput(e.target.value)}
                            className={`border p-2 w-full rounded text-sm ${errors.idNumber ? 'border-red-500' : ''}`}
                            maxLength={14}
                        />
                        {errors.idNumber && <p className="text-red-500 text-xs mt-1">{errors.idNumber}</p>}
                    </div>

                    {/* Gender */}
                    <div>
                        <select
                            value={newStudent.gender}
                            onChange={(e) => handleInputChange('gender', e.target.value)}
                            className={`border p-2 w-full rounded text-sm ${errors.gender ? 'border-red-500' : ''}`}
                        >
                            <option value="">Select Gender</option>
                            {genders.map(g => (
                                <option key={g} value={g}>{g}</option>
                            ))}
                        </select>
                        {errors.gender && <p className="text-red-500 text-xs mt-1">{errors.gender}</p>}
                    </div>

                    {/* Email */}
                    <div>
                        <input
                            type="email"
                            placeholder="Email"
                            value={newStudent.email}
                            onChange={(e) => handleInputChange('email', e.target.value)}
                            className={`border p-2 w-full rounded text-sm ${errors.email ? 'border-red-500' : ''}`}
                        />
                        {errors.email && <p className="text-red-500 text-xs mt-1">{errors.email}</p>}
                    </div>

                    {/* Faculty */}
                    <div>
                        <select
                            value={newStudent.faculty}
                            onChange={(e) => handleInputChange('faculty', e.target.value)}
                            className={`border p-2 w-full rounded text-sm ${errors.faculty ? 'border-red-500' : ''}`}
                        >
                            <option value="">Select Faculty</option>
                            {faculties.map(f => (
                                <option key={f} value={f}>{f}</option>
                            ))}
                        </select>
                        {errors.faculty && <p className="text-red-500 text-xs mt-1">{errors.faculty}</p>}
                    </div>

                    {/* Programme */}
                    <div>
                        <select
                            value={newStudent.programme}
                            onChange={(e) => handleInputChange('programme', e.target.value)}
                            className={`border p-2 w-full rounded text-sm ${errors.programme ? 'border-red-500' : ''}`}
                        >
                            <option value="">Select Programme</option>
                            {programmes.map(p => (
                                <option key={p} value={p}>{p}</option>
                            ))}
                        </select>
                        {errors.programme && <p className="text-red-500 text-xs mt-1">{errors.programme}</p>}
                    </div>

                    {/* Status */}
                    <div>
                        <select
                            value={newStudent.status}
                            onChange={(e) => handleInputChange('status', e.target.value)}
                            className="border p-2 w-full rounded text-sm"
                        >
                            {statuses.map(s => (
                                <option key={s} value={s}>{s}</option>
                            ))}
                        </select>
                    </div>

                    {/* Enrolment Status */}
                    <div>
                        <select
                            value={newStudent.enrolmentStatus}
                            onChange={(e) => handleInputChange('enrolmentStatus', e.target.value)}
                            className="border p-2 w-full rounded text-sm"
                        >
                            {enrolmentStatuses.map(es => (
                                <option key={es} value={es}>{es}</option>
                            ))}
                        </select>
                    </div>

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
