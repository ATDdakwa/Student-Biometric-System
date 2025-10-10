import React, { useEffect, useState } from 'react';
import axios from 'axios';
import DashboardLayout from "../../components/layout/DashboardLayout";
import getBaseUrl from './BaseUrl';
import { useLocation } from 'react-router-dom';
import { IoMdEye, IoMdCreate, IoIosFingerPrint, IoIosCheckmark, IoMdAdd, IoMdDocument, IoMdOpen, IoMdRefresh } from "react-icons/io";
import Cookies from 'js-cookie';
import { useNavigate } from 'react-router-dom';
import Load from '../../components/layout/Load';
import AuditTrailService from '../../services/AuditTrailService';

const ViewDependantForBiometrics = () => {
  const BASE_URL = getBaseUrl();
  const location = useLocation();
  const { personnelNumber } = location.state; 
  const [patient, setPatient] = useState(null);
  const [dependants, setDependants] = useState([]);
  const [loading, setLoading] = useState(true);
  
  const navigate = useNavigate();



  const formatDate = (date) => {
    if (!date) return '';
    const d = new Date(date);
    const day = String(d.getDate()).padStart(2, '0');
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const year = d.getFullYear();
    return `${day}/${month}/${year}`;
  };

  const handleVerify = (patient) => {
    const personnelNumber = patient.personnelNumber;
    const fullName = patient.fullName;
    const suffix = patient.suffix;
    const idNumber = patient.idNumber;
    if (!personnelNumber) {
      return;
    }
    navigate(`/dependant-patient-verification`, { state: { personnelNumber, fullName, patient,suffix,idNumber } });
    
  };

  const handleEnroll = (patient) => {
    const personnelNumber = patient.personnelNumber;
    const fullName = patient.fullName;
    const suffix = patient.suffix;
    const idNumber = patient.idNumber;
    if (!personnelNumber) {
      return;
    }
    navigate(`/dependant-patient-enrollment`, { state: { personnelNumber, fullName, patient,suffix,idNumber } });

  };
  useEffect(() => {
    const fetchPatient = async () => {
      try {
        setLoading(true)
        const response = await axios.get(BASE_URL + `api/v1/patients/personnelNumber/${personnelNumber}`);
        setPatient(response.data);
        setLoading(false)
        AuditTrailService.createLog("successful viewed patient information");
        fetchDepedants(response.data.personnelNumber)
  
      } catch (error) {
        console.error("Error fetching patient:", error);
        setLoading(false)
      } finally {
        setLoading(false);
      }
    };

    fetchPatient();

  
  }, [BASE_URL, personnelNumber]);

  const fetchDepedants = async (personnelNumber) => {
     console.log(personnelNumber)
      try {
        const response = await axios.get(BASE_URL + `api/v1/patients/dependants/${personnelNumber}`);
        setDependants(response.data.data);
        AuditTrailService.createLog("successful viewed patient dependacies");
      } catch (error) {
        console.error("Error fetching dependants:", error);
      }
  
    
  };

  if (loading) return <Load loading={loading} />;
  if (!patient) return <div>Patient not found.</div>;

  return (
    <DashboardLayout>
      <div className="p-10">
        <h1 className="text-2xl font-bold mb-10">Principal Patient Information & Dependants Biometrics</h1>
        
        <div style={{backgroundColor:'lightgray',borderRadius:'20px'}} className="bg-white shadow-lg rounded p-10">
          <div className="grid grid-cols-2 gap-1"> {/* Reduced gap to 2 */}
            <p className="text-lg"><strong>Personnel Number:</strong></p>
            <p className="text-lg">{patient.personnelNumber}</p>
 
            <p className="text-lg"><strong>First Name:</strong></p>
            <p className="text-lg">{patient.firstName}</p>
  
            <p className="text-lg"><strong>Last Name:</strong></p>
            <p className="text-lg">{patient.surname}</p>
  
    
          </div>
        </div>

        <h1 className="text-2xl font-bold mt-8">Dependants</h1>
        {/* Add Patient Dependants */}
      <div className="overflow-y-auto max-h-[50vh] mt-8">
          <table className="min-w-full border border-gray-300 shadow-lg rounded-lg">
            <thead className="bg-gray-300">
              <tr>
                {[
                  "Personnel No",
                  "Full Name",
                  "D.O.B",
                  "Gender",
                  "Suffix",
                  "Relation",
                  "ID Number",
                  "Action",
                ].map((header) => (
                  <th key={header} className={`py-3 px-4 text-sm text-left font-semibold border-b border-gray-400 ${header === "Age" || header === "Suffix" ? "w-16" : ""}`}>
                    {header}
                  </th>
                ))}
              </tr>
            </thead>
            <tbody> 
              {dependants.length === 0 ? (
                <tr>
                  <td colSpan="14" className="text-center py-4">No data</td>
                </tr>
              ) : (
                dependants.map((patient) => (
                  <tr key={patient.personnelNumber} className="border-b border-gray-300 hover:bg-gray-100">
                     <td className="py-3 px-4 text-sm">{patient.personnelNumber}</td>
                    <td className="py-3 px-4 text-sm">{patient.fullName}</td>
                    <td className="py-3 px-4 text-sm">{patient.dob}</td>
                    <td className="py-3 px-4 text-sm">{patient.gender}</td>
                    <td className="py-3 px-4 text-sm">{patient.suffix}</td>
                    <td className="py-3 px-4 text-sm">{patient.relation}</td>
                    <td className="py-3 px-4 text-sm">{patient.idNumber}</td>
                    <td className="py-2 px-4 text-sm">
                      <div className="flex space-x-2">
                        {/* Only show Enroll and Verify buttons if isCoe is false */}
                        {!patient.isCoe && (
                          <>
                            <button
                              onClick={() => handleEnroll(patient)}
                              className="bg-customGreen hover:bg-green-700 text-white font-bold py-1 px-2 rounded"
                              title="Enroll Biometric"
                            >
                              <IoIosFingerPrint className="mr-1 text-lg" />
                            </button>
                            <button
                              onClick={() => handleVerify(patient)}
                              className="bg-customGreen hover:bg-yellow-700 text-white font-bold py-1 px-2 rounded"
                              title="Verify Fingerprints"
                            >
                              <IoIosCheckmark className="mr-1 text-lg" />
                            </button>
                          </>
                        )}
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      <Load loading={loading} />

    </DashboardLayout>
  );
};

export default ViewDependantForBiometrics;