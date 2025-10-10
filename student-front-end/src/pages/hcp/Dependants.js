import React, { useEffect, useState } from "react";
import axios from "axios";
import DashboardLayout from "../../components/layout/DashboardLayout";
import SearchBar from "../commons/SearchBar";
import TableExport from "../commons/TableExport";
import getBaseUrl from "./BaseUrl";
import { useNavigate } from 'react-router-dom';
import { IoMdEye, IoMdAdd } from "react-icons/io";
import 'react-toastify/dist/ReactToastify.css';
import { toast, ToastContainer } from 'react-toastify';
import Load from "../../components/layout/Load";
import AuditTrailService from "../../services/AuditTrailService";
import Cookies from "js-cookie";

const Dependants = () => {
  const BASE_URL = getBaseUrl();
  const [patientsData, setPatientsData] = useState({ data: [], totalElements: 0 });
  const [searchTerm, setSearchTerm] = useState("");
  const [loading, setLoading] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 20;
  const navigate = useNavigate();

  const fetchData = async () => {
    setLoading(true)
    try {
      const response = await axios.get(BASE_URL + "api/v1/patients/dependants");
      setPatientsData(response.data);
      AuditTrailService.createLog("fetched dependancies demographics information ");
      setLoading(false)
    } catch (error) {
      console.error("Error fetching patients:", error);
      setLoading(false)
    }
  };


  useEffect(() => {
    fetchData();
  }, []);

  const handleSearch = (term) => {
    if (typeof term === 'string') {
      setSearchTerm(term);
    } else {
      console.error("Search term must be a string:", term);
      setSearchTerm("");
    }
  };

  const { data, totalElements } = patientsData;

  const filteredPatients = Array.isArray(data)
    ? data.filter((item) =>
      Object.values(item).some(
        (value) =>
          typeof value === "string" &&
          value.toLowerCase().includes(searchTerm.toLowerCase())
      )
    )
    : [];

  const formatDate = (dateString) => {
    if (!dateString) return "";
    const datePart = dateString.split("T")[0];
    const [year, month, day] = datePart.split("-");
    return `${day}-${month}-${year}`;
  };

  const totalPages = Math.ceil(filteredPatients.length / itemsPerPage);
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = filteredPatients.slice(indexOfFirstItem, indexOfLastItem);

  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  const getPaginationRange = () => {
    const range = [];
    const start = Math.max(1, currentPage - 4);
    const end = Math.min(totalPages, start + 9);
    for (let i = start; i <= end; i++) {
      range.push(i);
    }
    return range;
  };



  return (
    <DashboardLayout>
      <div className="p-4" style={{ padding: '3rem' }}>
        <h1 className="text-2xl font-bold mb-6">Dependant Patient Demographics</h1>
        <Load loading={loading} />
        <div className="flex mb-4">
          <div className="w-3/4">
            <SearchBar searchTerm={searchTerm} handleSearch={handleSearch} />
          </div>
          <div className="w-1/4 flex items-center">
            <TableExport data={filteredPatients} filename="dependants" />
          </div>
        </div>

        <div className="overflow-y-auto max-h-[50vh]">
          <table className="min-w-full border border-gray-300 shadow-lg rounded-lg">
            <thead className="bg-gray-300">
              <tr>
                {[
                  "Personnel No",
                  "Suffix",
                  "Full Name",
                  "ID Number",
                  "Gender",
                  "Relationship"
                ].map((header) => (
                  <th key={header} className={`py-3 px-4 text-sm text-left font-semibold border-b border-gray-400 ${header === "Age" || header === "Suffix" ? "w-16" : ""}`}>
                    {header}
                  </th>
                ))}
              </tr>
            </thead>
            <tbody>
              {currentItems.length === 0 ? (
                <tr>
                  <td colSpan="14" className="text-center py-4">No data</td>
                </tr>
              ) : (
                currentItems.map((patient) => (
                  <tr key={patient.personnelNumber} className="border-b border-gray-300 hover:bg-gray-100">
                    <td className="py-3 px-4 text-sm">{patient.personnelNumber}</td>
                    <td className="py-3 px-4 text-sm">{patient.suffix}</td>
                    <td className="py-3 px-4 text-sm">{patient.fullName}</td>
                    <td className="py-3 px-4 text-sm">{patient.idNumber}</td>
                    <td className="py-3 px-4 text-sm">{patient.gender}</td>
                    <td className="py-3 px-4 text-sm">{patient.relation}</td>

                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>

        <div className="flex justify-center mt-4">
          {getPaginationRange().map((pageNumber) => (
            <button
              key={pageNumber}
              onClick={() => handlePageChange(pageNumber)}
              className={`mx-1 px-4 py-2 rounded ${currentPage === pageNumber ? 'bg-customGreen text-white' : 'bg-gray-300'}`}
            >
              {pageNumber}
            </button>
          ))}
        </div>
      </div>
      <ToastContainer />
      <Load loading={loading} />
    </DashboardLayout>
  );
};

export default Dependants;