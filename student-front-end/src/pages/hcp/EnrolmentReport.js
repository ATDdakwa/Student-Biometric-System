import React, { useEffect, useState } from "react";
import axios from "axios";
import DashboardLayout from "../../components/layout/DashboardLayout";
import SearchBar from "../commons/SearchBar";
import TableExport from "../commons/TableExport";
import Load from "../../components/layout/Load";
import { Link } from "react-router-dom";
import getBaseUrl from "./BaseUrl";
import { useNavigate } from 'react-router-dom';
import { IoMdEye, IoMdAdd } from "react-icons/io";
import 'react-toastify/dist/ReactToastify.css';
import { toast, ToastContainer } from 'react-toastify';
import { FaUser, FaFingerprint, FaChartBar, FaAddressCard, FaFileAlt } from "react-icons/fa";
import AuditTrailService from "../../services/AuditTrailService";

const EnrolmentReport = () => {
  const BASE_URL = getBaseUrl();
  const [searchTerm, setSearchTerm] = useState("");
  const [loading, setLoading] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const navigate = useNavigate();


  useEffect(() => {

  }, []);




  return (
    <DashboardLayout>
      <div className="p-4" style={{ padding: '3rem' }}>
        <h1 className="text-2xl font-bold mb-6">Patient Enrolment Statistical Reports</h1>
        <Load loading={loading} />

        <div className="overflow-y-auto max-h-[70vh] mt-20">

          <h1 style={{ textAlign: 'center' }} className="text-2xl font-bold ">Employee Permanent Reports</h1>
          <Link
            to="/male-patient-employee-permanent-report"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'pink' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >


            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Male Patient</h2>
          </Link>

          <Link
            to="/female-patient-employee-permanent-report"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'pink' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >


            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Female Patient </h2>
          </Link>
          <h1 style={{ textAlign: 'center' }} className="text-2xl font-bold ">Employee Contract Reports</h1>
          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'gray' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Male Patient</h2>
          </Link>

          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'gray' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Female Patient </h2>
          </Link>


          <h1 style={{ textAlign: 'center' }} className="text-2xl font-bold ">Dependents Permanent Employees Reports</h1>
          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: '#000630' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}


            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Spouse</h2>
          </Link>
          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: '#000630' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Children Under 18 Years</h2>
          </Link>

          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: '#000630' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Children 18 - 23 Years</h2>
          </Link>

          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: '#000630' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Children Over 23 Years</h2>
          </Link>

          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: '#000630' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Other</h2>
          </Link>

          {/* dependance fixe term */}
          <h1 style={{ textAlign: 'center' }} className="text-2xl font-bold ">Dependents Fixed Term Contract Reports</h1>

          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'maroon' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Spouse </h2>
          </Link>
          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'maroon' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Children Under 18 Years</h2>
          </Link>

          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'maroon' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Children 18 - 23 Years </h2>
          </Link>

          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'maroon' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Children Over 23 Years </h2>
          </Link>

          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'maroon' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Other </h2>
          </Link>

          {/* end fixed term */}
          <h1  style={{ textAlign: 'center' }} className="text-2xl font-bold mb-6">Medical Aid </h1>
          {/* medical */}

          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'darkblue' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Principal Member  Report</h2>
          </Link>
          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'darkblue' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Spouse Report</h2>
          </Link>

          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'darkblue' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Children Under 18 Years Report</h2>
          </Link>

          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'darkblue' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Children 18 - 23 Years  Report</h2>
          </Link>

          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'darkblue' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Children Over 23 Years Report</h2>
          </Link>

          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'darkblue' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Other Dependents  Report</h2>
          </Link>

          {/* end medical */}




          {/* mukwasini */}
          <h1  style={{ textAlign: 'center' }} className="text-2xl font-bold mb-6">Out Growers Mkwasini Mill Group </h1>
          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'darkgreen' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Principal Member  Report</h2>
          </Link>
          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'darkgreen' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Spouse Report</h2>
          </Link>

          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'darkgreen' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Children Under 18 Years Report</h2>
          </Link>

          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'darkgreen' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Children 18 - 23 Years  Report</h2>
          </Link>

          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'darkgreen' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Children Over 23 Years Report</h2>
          </Link>

          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'darkgreen' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Other Dependents  Report</h2>
          </Link>

          {/* end mukwasini */}



          
          {/* hippo */}
          <h1  style={{ textAlign: 'center' }} className="text-2xl font-bold mb-6">Out Growers Hippo Mill Group </h1>
          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'purple' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Principal Member  Report</h2>
          </Link>
          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'purple' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Spouse Report</h2>
          </Link>

          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'purple' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Children Under 18 Years Report</h2>
          </Link>

          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'purple' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Children 18 - 23 Years  Report</h2>
          </Link>

          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'purple' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Children Over 23 Years Report</h2>
          </Link>

          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'purple' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Other Dependents  Report</h2>
          </Link>

          {/* end hippo */}

          {/* triangle */}
          <h1  style={{ textAlign: 'center' }} className="text-2xl font-bold mb-6">Out Growers Triangle Mill Group </h1>
          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'black' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Principal Member  Report</h2>
          </Link>
          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'black' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Spouse Report</h2>
          </Link>

          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'black' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Children Under 18 Years Report</h2>
          </Link>

          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'black' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Children 18 - 23 Years  Report</h2>
          </Link>

          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'black' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Children Over 23 Years Report</h2>
          </Link>

          <Link
            to="/dashboard"
            style={{ width: '70%', marginLeft: '10rem', backgroundColor: 'black' }}
            className="h-15  rounded-lg flex flex-col items-center justify-center mx-4 mb-2 pt-2"
          >
            {/* <FaFileAlt size={35} color="orange" /> */}

            <h2 style={{ color: 'white' }} className="text-white text-2xl font-bold mt-0 mb-5">Other Dependents  Report</h2>
          </Link>

          {/* end triangle */}

        </div>

      </div>
      <ToastContainer />
    </DashboardLayout>
  );
};

export default EnrolmentReport;