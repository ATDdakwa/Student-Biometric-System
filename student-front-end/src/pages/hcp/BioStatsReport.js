import React, { startTransition, useEffect, useState } from "react";
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
import AuditTrailService from "../../services/AuditTrailService";


const BioStatsReport = () => {
  const BASE_URL = getBaseUrl();

  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const [report, setReport] = useState({});
  const [company, setCompany] = useState({});
  const [division, setDivision] = useState({});
  const [department, setDepartment] = useState({});
  const [medicalAid, setMedicalAid] = useState({});
  const [departments, setDepartments] = useState([]);
  const [divisions, setDivisions] = useState([]);
  const [principal, setPrincipal] = useState({}); // same as spouse

  const [under18, setUnder18] = useState({}); // children under 18
  const [p18to25, setP18To23] = useState({}); // children  18-23
  const [over23, setOver23] = useState({}); // children  18-23

  const [empPermanent, setEmpPermanent] = useState({}); // children  18-23



  const fetchDistinctDepartments = async () => {
    try {
      const response = await axios.get(BASE_URL + "api/v1/patients/distinct-departments");
      setDepartments(response.data);
      AuditTrailService.createLog("successful fetched THZ departments");
    } catch (error) {
      console.error('Error fetching departments:', error);
    }
  };


  const fetchDistinctDivisions = async () => {
    setLoading(true)
    try {
      const response = await axios.get(BASE_URL + "api/v1/patients/distinct-divisions");
      setDivisions(response.data);
      AuditTrailService.createLog("successful fetched THZ divisions");
      setLoading(false)
    } catch (error) {
      setLoading(false)
      console.error('Error fetching divisions:', error);
    }
  };


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

  const handleSearch = async () => {

    setLoading(true)
    try {
      const response = await axios.get(BASE_URL + "api/v1/patients/get-all-reports");
      console.log("company " + company)
      console.log("medicalAid " + medicalAid)
      console.log("department " + department)
      console.log("division " + division)
      AuditTrailService.createLog("successful fetched report");
      // employee permanent start
      const permanentGrantTotal = response.data.filter(patient =>
        patient.company === company &&
        patient.division === division &&
        patient.department === department
      ).length;
      const permanentGrantTotalFemale = response.data.filter(patient =>
        patient.company === company &&
        patient.division === division &&
        patient.department === department &&
        patient.gender === 'Female'
      ).length;
      const permanentGrantTotalMale = response.data.filter(patient =>
        patient.company === company &&
        patient.division === division &&
        patient.department === department &&
        patient.gender === 'Male'
      ).length;
      // employee permanent end

      // medicalAid aid start
      // grant total
      const grantTotal = response.data.filter(patient =>
        patient.company === company &&
        patient.division === division &&
        patient.scheme === medicalAid &&
        patient.department === department
      ).length;

      // total female
      const grantTotalFemale = response.data.filter(patient =>
        patient.company === company &&
        patient.division === division &&
        patient.scheme === medicalAid &&
        patient.department === department &&
        patient.gender === 'Female'
      ).length;

      // total female
      const grantTotalMale = response.data.filter(patient =>
        patient.company === company &&
        patient.division === division &&
        patient.scheme === medicalAid &&
        patient.department === department &&
        patient.gender === 'Male'
      ).length;


      // total age under 18
      const grantTotalUnder18 = response.data.filter(patient =>
        patient.company === company &&
        patient.division === division &&
        patient.scheme === medicalAid &&
        patient.department === department &&
        patient.age < 18
      ).length;

      const grantTotalUnder18Female = response.data.filter(patient =>
        patient.company === company &&
        patient.division === division &&
        patient.scheme === medicalAid &&
        patient.department === department &&
        patient.age < 18 &&
        patient.gender === 'Female'
      ).length;

      const grantTotalUnder18Male = response.data.filter(patient =>
        patient.company === company &&
        patient.division === division &&
        patient.scheme === medicalAid &&
        patient.department === department &&
        patient.age < 18 &&
        patient.gender === 'Male'
      ).length;

      // total age under 18 - 23
      const grantTotal18to23 = response.data.filter(patient =>
        patient.company === company &&
        patient.division === division &&
        patient.scheme === medicalAid &&
        patient.department === department &&
        patient.age >= 18 && patient.age < 23
      ).length;

      const grantTotal18to23Female = response.data.filter(patient =>
        patient.company === company &&
        patient.division === division &&
        patient.scheme === medicalAid &&
        patient.department === department &&
        patient.age >= 18 && patient.age < 23 &&
        patient.gender === 'Female'
      ).length;

      const grantTotal18to23Male = response.data.filter(patient =>
        patient.company === company &&
        patient.division === division &&
        patient.scheme === medicalAid &&
        patient.department === department &&
        patient.age >= 18 && patient.age < 23 &&
        patient.gender === 'Male'
      ).length;

      // total age over 23
      const grantTotalOver23 = response.data.filter(patient =>
        patient.company === company &&
        patient.division === division &&
        patient.scheme === medicalAid &&
        patient.department === department &&
        patient.age >= 23
      ).length;

      const grantTotalOver23Female = response.data.filter(patient =>
        patient.company === company &&
        patient.division === division &&
        patient.scheme === medicalAid &&
        patient.department === department &&
        patient.age >= 23 &&
        patient.gender === 'Female'
      ).length;

      const grantTotalOver23Male = response.data.filter(patient =>
        patient.company === company &&
        patient.division === division &&
        patient.scheme === medicalAid &&
        patient.department === department &&
        patient.age >= 23 &&
        patient.gender === 'Male'
      ).length;

      // medicalAid aid end

      console.log("grantTotal " + grantTotal);
      console.log("grantTotalFemale " + grantTotalFemale);
      console.log("grantTotalMale " + grantTotalMale);
      console.log("grantTotalUnder18 " + grantTotalUnder18);
      console.log("grantTotal18to23 " + grantTotal18to23);
      console.log("grantTotalOver23 " + grantTotalOver23);
      // medicals start
      setPrincipal({
        "grantTotal": grantTotal,
        "grantTotalFemale": grantTotalFemale,
        "grantTotalMale": grantTotalMale
      })

      setUnder18({
        "grantTotal": grantTotalUnder18,
        "grantTotalFemale": grantTotalUnder18Female,
        "grantTotalMale": grantTotalUnder18Male
      })

      setP18To23({
        "grantTotal": grantTotal18to23,
        "grantTotalFemale": grantTotal18to23Female,
        "grantTotalMale": grantTotal18to23Male
      })

      setOver23({
        "grantTotal": grantTotalOver23,
        "grantTotalFemale": grantTotalOver23Female,
        "grantTotalMale": grantTotalOver23Male
      })

      // medicals end
      setEmpPermanent({
        "grantTotal": permanentGrantTotal,
        "grantTotalFemale": permanentGrantTotalFemale,
        "grantTotalMale": permanentGrantTotalMale
      })

      setLoading(false)
    } catch (error) {
      console.error("Error fetching patients:", error);
      setLoading(false)
    }

  };


  useEffect(() => {
    fetchDistinctDepartments();
    fetchDistinctDivisions();

  }, []);



  return (
    <DashboardLayout>
      <div className="p-4" style={{ padding: '3rem' }}>
        <h1 className="text-2xl font-bold mb-6">Patient Biometric Enrolment Reports Statistics</h1>
        <Load loading={loading} />
        <div className="flex mb-4" style={{ display: 'flex', gap: '10px', marginBottom: '50px', marginTop: '60px' }}>
          <select
            value={report}
            style={{ fontSize: '0.9rem', borderRadius: '10px' }}
            onChange={(e) => setReport(e.target.value)}
            className="border p-1 mb-2 w-full h-10 mx-auto"
          >
            <option value="">Select Report Name</option>
            {reports.map((r, index) => (
              <option key={index} value={r}>{r}</option>
            ))}
          </select>

          {report === 'Medical Aid' && (
            <select
              value={medicalAid}
              style={{ fontSize: '0.9rem', borderRadius: '10px' }}
              onChange={(e) => setMedicalAid(e.target.value)}
              className="border p-1 mb-2 w-full h-10 mx-auto"
            >
              <option value="">Select Schema Name</option>
              {schemas.map((r, index) => (
                <option key={index} value={r}>{r}</option>
              ))}
            </select>

          )}
          <select
            value={company}
            style={{ fontSize: '0.9rem', borderRadius: '10px' }}
            onChange={(e) => setCompany(e.target.value)}
            className="border p-1 mb-2 w-full h-10 mx-auto"
          >
            <option value="">Select Company</option>
            {companies.map((co, index) => (
              <option key={index} value={co}>{co}</option>
            ))}
          </select>
          <select
            value={division}
            style={{ fontSize: '0.9rem', borderRadius: '10px' }}
            onChange={(e) => setDivision(e.target.value)}
            className="border p-1 mb-2 w-full h-10 mx-auto"
          >
            <option value="">Select Division</option>
            {divisions.map((div, index) => (
              <option key={index} value={div}>{div}</option>
            ))}
          </select>
          <select
            value={department}
            style={{ fontSize: '0.9rem', borderRadius: '10px' }}
            onChange={(e) => setDepartment(e.target.value)}
            className="border p-1 mb-2 w-full h-10 mx-auto"
          >
            <option value="">Select Department</option>
            {departments.map((dep, index) => (
              <option key={index} value={dep}>{dep}</option>
            ))}
          </select>

          <button
            style={{ fontSize: '0.9rem', backgroundColor: '#000630', borderRadius: '10px', height: '2.5rem' }}
            onClick={handleSearch}
            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-4 rounded mr-2"
          >
            Submit
          </button>
        </div>



        <div className="overflow-y-auto max-h-[50vh]">
          <table className="min-w-full border border-gray-300 shadow-lg rounded-lg">
            <thead className="bg-gray-300">
              {report === 'Employee Contract' && (
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
              {report === 'Employee Permanent' && (
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
              {report === 'Medical Aid' && (
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

              {report === 'Dependancies' && (
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
              {report === 'Employee Permanent' && (
             
                  <>
                    <tr key={1} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Permanent Employee</td>
                      <td className="py-3 px-4 text-sm">{empPermanent.grantTotalMale}</td>
                      <td className="py-3 px-4 text-sm">{empPermanent.grantTotalFemale}</td>
                      <td className="py-3 px-4 text-sm">{empPermanent.grantTotal}</td>

                    </tr>

                  </>
              
              )
              }


              {report === 'Employee Contract' && (
               
                  <>
                    <tr key={3} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Contract Employee</td>
                      <td className="py-3 px-4 text-sm">{empPermanent.grantTotalMale}</td>
                      <td className="py-3 px-4 text-sm">{empPermanent.grantTotalFemale}</td>
                      <td className="py-3 px-4 text-sm">{empPermanent.grantTotal}</td>

                    </tr>
                  </>
           
              )}


              {report === 'Dependancies' && (
              
                  <>
                    <tr key={5} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Spouse</td>
                      <td className="py-3 px-4 text-sm">{principal.grantTotalMale}</td>
                      <td className="py-3 px-4 text-sm">{principal.grantTotalFemale}</td>
                      <td className="py-3 px-4 text-sm">{principal.grantTotal}</td>

                    </tr>
                    <tr key={6} className="border-b border-gray-300 hover:bg-gray-100">
                    <td className="py-3 px-4 text-sm">Children Under 18 years</td>
                      <td className="py-3 px-4 text-sm">{under18.grantTotalMale}</td>
                      <td className="py-3 px-4 text-sm">{under18.grantTotalFemale}</td>
                      <td className="py-3 px-4 text-sm">{under18.grantTotal}</td>

                    </tr>
                    <tr key={7} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Children Under 18 - 23 years</td>
                      <td className="py-3 px-4 text-sm">{p18to25.grantTotalMale}</td>
                      <td className="py-3 px-4 text-sm">{p18to25.grantTotalFemale}</td>
                      <td className="py-3 px-4 text-sm">{p18to25.grantTotal}</td>

                    </tr>

                    <tr key={8} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Children Over 23 years</td>
                      <td className="py-3 px-4 text-sm">{over23.grantTotalMale}</td>
                      <td className="py-3 px-4 text-sm">{over23.grantTotalFemale}</td>
                      <td className="py-3 px-4 text-sm">{over23.grantTotal}</td>

                    </tr>

                    {/* <tr key={9} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Other Dependances</td>
                      <td className="py-3 px-4 text-sm">{patient.maleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.femaleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.femaleCount}</td>

                    </tr> */}
                  </>
             
              )}



              {report === 'Medical Aid' && (
                  <>
                    <tr key={10} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Principal Member</td>
                      <td className="py-3 px-4 text-sm">{principal.grantTotalMale}</td>
                      <td className="py-3 px-4 text-sm">{principal.grantTotalFemale}</td>
                      <td className="py-3 px-4 text-sm">{principal.grantTotal}</td>
                    </tr>
                    <tr key={11} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Spouse</td>
                      <td className="py-3 px-4 text-sm">{principal.grantTotalMale}</td>
                      <td className="py-3 px-4 text-sm">{principal.grantTotalFemale}</td>
                      <td className="py-3 px-4 text-sm">{principal.grantTotal}</td>
                    </tr>
                    <tr key={12} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Children Under 18 years</td>
                      <td className="py-3 px-4 text-sm">{under18.grantTotalMale}</td>
                      <td className="py-3 px-4 text-sm">{under18.grantTotalFemale}</td>
                      <td className="py-3 px-4 text-sm">{under18.grantTotal}</td>

                    </tr>
                    <tr key={13} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Children Under 18 - 23 years</td>
                      <td className="py-3 px-4 text-sm">{p18to25.grantTotalMale}</td>
                      <td className="py-3 px-4 text-sm">{p18to25.grantTotalFemale}</td>
                      <td className="py-3 px-4 text-sm">{p18to25.grantTotal}</td>

                    </tr>

                    <tr key={14} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Children Over 23 years</td>
                      <td className="py-3 px-4 text-sm">{over23.grantTotalMale}</td>
                      <td className="py-3 px-4 text-sm">{over23.grantTotalFemale}</td>
                      <td className="py-3 px-4 text-sm">{over23.grantTotal}</td>

                    </tr>

                    {/* <tr key={15} className="border-b border-gray-300 hover:bg-gray-100">
                      <td className="py-3 px-4 text-sm">Other Dependances</td>
                      <td className="py-3 px-4 text-sm">{patient.maleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.femaleCount}</td>
                      <td className="py-3 px-4 text-sm">{patient.femaleCount}</td>

                    </tr> */}
                  </>
             
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

export default BioStatsReport;