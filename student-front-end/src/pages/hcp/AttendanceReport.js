import React, { useEffect, useState } from "react";
import axios from "axios";
import DashboardLayout from "../../components/layout/DashboardLayout";
import SearchBar from "../commons/SearchBar";
import TableExport from "../commons/TableExport";
import Load from "../../components/layout/Load";
import getBaseUrl from "./BaseUrl";
import { useNavigate } from 'react-router-dom';
import { IoMdEye, IoMdAdd } from "react-icons/io";
import 'react-toastify/dist/ReactToastify.css';
import { toast, ToastContainer } from 'react-toastify';
import FormSelect2 from "../../components/widgets/FormSelect2";

const AttendanceReport = () => {
  const BASE_URL = getBaseUrl();

  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const [selected1, setSelected1] = useState({});
  const [selected2, setSelected2] = useState({});
  const [selected3, setSelected3] = useState({});
  const [selected4, setSelected4] = useState({});
  const [selected5, setSelected5] = useState({});

  const medicalReportData = [
    { "spouseCount": 400, "childUnder18Count": 300, "child18-23Count": 300, "childOverCount": 300, "other": 300 },
  ]


  const genderReportData = [
    { "femaleCount": 400, "maleCount": 300 },
  ]

  const reports = [
    "Employee Permanent",
    "Employee Contract",
    "Dependancies",
    "Medical Aid",
  ];

  const schemas = [
    "MBIS", "MIF", "CIMAS", "Mkwasini", "Hippo", "Triangle", "Out Growers"
  ];
  const companies = [
    "7300 - Hippo Valley Estates Ltd",
    "7400 - Triangle Limited",
    "7210 - Zimbabwe Sugar Association",
    "External"
  ];
  const division = [
    "ZIM11 - Experiment Station",
    "ZIM12 - Experiment Station",
  ];

  const departments = [
    "IT",
    "HR",
  ];

  const handleSearch = async () => {
    // try {
    //   const response = await axios.post("http://localhost:6115/api/v1/patients/create", newPatient);
    //   onAddPatient(response.data); // Call the function passed as prop
    //   toast.success("Patient added successfully!"); // Show success toast
    //   resetForm(); // Reset the form
    //   onClose(); // Close the modal
    // } catch (error) {
    //   console.error("Error adding patient:", error);
    //   toast.error("Error adding patient. Please try again.");
    // }
  };

  useEffect(() => {

  }, []);



  return (
    <DashboardLayout>
      <div className="p-4" style={{ padding: '3rem' }}>
        <h1 className="text-2xl font-bold mb-6">Patient Attendance Reports Statistics</h1>
        <Load loading={loading} />
        <div className="flex mb-4" style={{ display: 'flex', gap: '10px', marginBottom:'50px',marginTop:'60px' }}>
          <select
            value={selected1}
            style={{ fontSize: '0.9rem', borderRadius: '10px' }}
            onChange={(e) => setSelected1(e.target.value)}
            className="border p-1 mb-2 w-full h-10 mx-auto"
          >
            <option value="">Select Report Name</option>
            {reports.map((r, index) => (
              <option key={index} value={r}>{r}</option>
            ))}
          </select>

          {selected1 === 'Medical Aid' && (
            <select
              value={selected5}
              style={{ fontSize: '0.9rem', borderRadius: '10px' }}
              onChange={(e) => setSelected5(e.target.value)}
              className="border p-1 mb-2 w-full h-10 mx-auto"
            >
              <option value="">Select Schema Name</option>
              {schemas.map((r, index) => (
                <option key={index} value={r}>{r}</option>
              ))}
            </select>

          )}
          <select
            value={selected2}
            style={{ fontSize: '0.9rem', borderRadius: '10px' }}
            onChange={(e) => setSelected2(e.target.value)}
            className="border p-1 mb-2 w-full h-10 mx-auto"
          >
            <option value="">Select Company</option>
            {companies.map((co, index) => (
              <option key={index} value={co}>{co}</option>
            ))}
          </select>
          <select
            value={selected3}
            style={{ fontSize: '0.9rem', borderRadius: '10px' }}
            onChange={(e) => setSelected3(e.target.value)}
            className="border p-1 mb-2 w-full h-10 mx-auto"
          >
            <option value="">Select Division</option>
            {division.map((div, index) => (
              <option key={index} value={div}>{div}</option>
            ))}
          </select>
          <select
            value={selected4}
            style={{ fontSize: '0.9rem', borderRadius: '10px' }}
            onChange={(e) => setSelected4(e.target.value)}
            className="border p-1 mb-2 w-full h-10 mx-auto"
          >
            <option value="">Select Department</option>
            {departments.map((dep, index) => (
              <option key={index} value={dep}>{dep}</option>
            ))}
          </select>

          <button
            style={{ fontSize: '0.9rem', backgroundColor: '#000630', borderRadius: '10px',height:'2.5rem' }}
            onClick={handleSearch}
            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-4 rounded mr-2"
          >
            Submit
          </button>
        </div>



        <div className="overflow-y-auto max-h-[50vh]">
          <table className="min-w-full border border-gray-300 shadow-lg rounded-lg">
            <thead className="bg-gray-300">
              {selected1 === 'Employee Contract' && (
                <tr>
                  {[
                    "Options",
                    "Total"
                  ].map((header) => (
                    <th key={header} className={`py-3 px-4 text-sm text-left font-semibold border-b border-gray-400 ${header === "Age" || header === "Suffix" ? "w-16" : ""}`}>
                      {header}
                    </th>
                  ))}
                </tr>
              )}
              {selected1 === 'Employee Permanent' && (
                <tr>
                  {[
                    "Options",
                    "Total"
                  ].map((header) => (
                    <th key={header} className={`py-3 px-4 text-sm text-left font-semibold border-b border-gray-400 ${header === "Age" || header === "Suffix" ? "w-16" : ""}`}>
                      {header}
                    </th>
                  ))}
                </tr>
              )}
              {selected1 === 'Medical Aid'  && (
                <tr>
                  {[
                    "Options",
                    "Male Total",
                    "Female Total",
                    "Grant Total"
                  ].map((header) => (
                    <th key={header} className={`py-3 px-4 text-sm text-left font-semibold border-b border-gray-400 ${header === "Age" || header === "Suffix" ? "w-16" : ""}`}>
                      {header}
                    </th>
                  ))}
                </tr>
              )}

              {selected1 === 'Dependancies' && (
                <tr>
                  {[
                    "Options",
                    "Male Total",
                    "Female Total",
                    "Grant Total"
                  ].map((header) => (
                    <th key={header} className={`py-3 px-4 text-sm text-left font-semibold border-b border-gray-400 ${header === "Age" || header === "Suffix" ? "w-16" : ""}`}>
                      {header}
                    </th>
                  ))}
                </tr>
              )}
            </thead>
            <tbody>
              {selected1 === 'Employee Permanent' && (
                genderReportData.map((patient) => (
                  <>
                    <tr key={1} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Male</td>
                      <td className="py-3 px-4 text-sm">{patient.maleCount}</td>

                    </tr>
                    <tr key={2} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Female</td>
                      <td className="py-3 px-4 text-sm">{patient.femaleCount}</td>

                    </tr>
                  </>
                ))
              )
              }


              {selected1 === 'Employee Contract' && (
                genderReportData.map((patient) => (
                  <>
                    <tr key={1} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Male</td>
                      <td className="py-3 px-4 text-sm">{patient.maleCount}</td>

                    </tr>
                    <tr key={2} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Female</td>
                      <td className="py-3 px-4 text-sm">{patient.femaleCount}</td>

                    </tr>
                  </>
                ))
              )}


              {selected1 === 'Dependancies' && (
                genderReportData.map((patient) => (
                  <>
                    <tr key={1} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Spouse</td>
                      <td className="py-3 px-4 text-sm">{patient.maleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.femaleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.maleCount}</td>

                    </tr>
                    <tr key={2} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Children Under 18 years</td>
                      <td className="py-3 px-4 text-sm">{patient.maleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.femaleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.femaleCount}</td>

                    </tr>
                    <tr key={3} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Children Under 18 - 23 years</td>
                      <td className="py-3 px-4 text-sm">{patient.maleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.femaleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.femaleCount}</td>

                    </tr>

                    <tr key={4} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Children Over 23 years</td>
                      <td className="py-3 px-4 text-sm">{patient.maleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.femaleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.femaleCount}</td>

                    </tr>

                    <tr key={5} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Other Dependances</td>
                      <td className="py-3 px-4 text-sm">{patient.maleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.femaleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.femaleCount}</td>

                    </tr>
                  </>
                ))
              )}



              {selected1 === 'Medical Aid' && (
                genderReportData.map((patient) => (
                  <>
                    <tr key={1} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Principal Member</td>
                      <td className="py-3 px-4 text-sm">{patient.maleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.femaleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.maleCount}</td>
                    </tr>
                    <tr key={1} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Spouse</td>
                      <td className="py-3 px-4 text-sm">{patient.maleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.femaleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.maleCount}</td>
                    </tr>
                    <tr key={2} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Children Under 18 years</td>
                      <td className="py-3 px-4 text-sm">{patient.maleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.femaleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.femaleCount}</td>

                    </tr>
                    <tr key={3} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Children Under 18 - 23 years</td>
                      <td className="py-3 px-4 text-sm">{patient.maleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.femaleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.femaleCount}</td>

                    </tr>

                    <tr key={4} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Children Over 23 years</td>
                      <td className="py-3 px-4 text-sm">{patient.maleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.femaleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.femaleCount}</td>

                    </tr>

                    <tr key={5} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Other Dependances</td>
                      <td className="py-3 px-4 text-sm">{patient.maleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.femaleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.femaleCount}</td>

                    </tr>
                  </>
                ))
              )}



            </tbody>
          </table>
        </div>


      </div>
      <ToastContainer />
    </DashboardLayout>
  );
};

export default AttendanceReport;