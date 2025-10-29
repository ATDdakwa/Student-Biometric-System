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

const AuditTrail = () => {
  const BASE_URL = getBaseUrl();
  const [logs, setLogs] = useState({ auditTrail: [], totalElements: 0 });
  const [searchTerm, setSearchTerm] = useState("");
  const [loading, setLoading] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 20;
  const navigate = useNavigate();

  const fetchData = async () => {
    setLoading(true);
    try {
        const response = await axios.get(BASE_URL + "api/v1/audit/get-all");
        setLogs({ auditTrail: response.data, totalElements: response.data.length });
        setLoading(false);
    } catch (error) {
        console.error("Error fetching logs:", error);
        setLoading(false);
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

  const { auditTrail, totalElements } = logs;

  const filteredLogs = Array.isArray(auditTrail)
    ? auditTrail.filter((item) =>
      Object.values(item).some(
        (value) =>
          typeof value === "string" &&
          value.toLowerCase().includes(searchTerm.toLowerCase())
      )
    )
    : [];


  const totalPages = Math.ceil(filteredLogs.length / itemsPerPage);
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = filteredLogs.slice(indexOfFirstItem, indexOfLastItem);

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
        <h1 className="text-2xl font-bold mb-6">Audit Trail</h1>
        <Load loading={loading} />
        <div className="flex mb-4">
          <div className="w-3/4">
            <SearchBar searchTerm={searchTerm} handleSearch={handleSearch} />
          </div>
          <div className="w-1/4 flex items-center">
            <TableExport data={filteredLogs} filename="logs" />
          </div>
        </div>

        

        <div className="overflow-y-auto max-h-[50vh]">
          <table className="min-w-full border border-gray-300 shadow-lg rounded-lg">
            <thead className="bg-gray-300">
              <tr>
                {[
                  "Date",
                  "Full Name",
                  "User Name",
                  "Role",
                  "Action Made",
                ].map((header) => (
                  <th key={header} style={{width :  header === "Action Made" ? "30%" : "10%"}} className={`py-3 px-4 text-sm text-left font-semibold border-b border-gray-400`}>
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
                currentItems.map((log) => (
                  <tr key={log.id} className="border-b border-gray-300 hover:bg-gray-100">
                    <td className="py-3 px-4 text-sm">{log.createdDate}</td>
                    <td className="py-3 px-4 text-sm">{log.fullName}</td>
                    <td className="py-3 px-4 text-sm">{log.userName}</td>
                    <td className="py-3 px-4 text-sm">{log.role}</td>
                    <td className="py-3 px-4 text-sm w-16">{log.narration}</td>
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

export default AuditTrail;