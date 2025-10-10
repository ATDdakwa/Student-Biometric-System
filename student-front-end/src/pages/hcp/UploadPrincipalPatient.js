import React, { useEffect, useState, useRef } from 'react';
import axios from 'axios';
import DashboardLayout from "../../components/layout/DashboardLayout";
import getBaseUrl from './BaseUrl';
import { useLocation } from 'react-router-dom';
import { IoMdEye, IoMdCreate, IoIosFingerPrint, IoIosCheckmark, IoMdAdd, IoMdDocument, IoMdOpen, IoMdRefresh } from "react-icons/io";
import Cookies from 'js-cookie';
import { useNavigate } from 'react-router-dom';
import Load from '../../components/layout/Load';
import AuditTrailService from '../../services/AuditTrailService';
import FileUploadService from '../../services/FileUploadService';
import TableExport from '../commons/TableExport';
import {toast} from "react-toastify";

const UploadPrincipalPatient = () => {
  const BASE_URL = getBaseUrl();
  const [patient, setPatient] = useState(null);
  const [loading, setLoading] = useState(false);
  const fileInputRef = useRef(null);
  const navigate = useNavigate();
  const [imageVisible, setImageVisible] = useState(false);
  const [fileType, setFileType] = useState(null)
  const [uploadedFile, setUploadedFile] = useState(null);
  const [selectedFile, setSelectedFile] = useState(null);
  const [principalData, setPrinciplaData] = useState([]);
  const [dependantsData, setDependantsData] = useState([]);

  useEffect(() => {

    fetchPrincipalData();
    fetchDependantsData();

  }, [BASE_URL]);

  const fetchPrincipalData = async () => {
    setLoading(true)
    try {
      const response = await axios.get(BASE_URL + "api/v1/patients/get-all");
      setPrinciplaData(response.data.patients);
      setLoading(false)
    } catch (error) {
      console.error("Error fetching patients:", error);
      setLoading(false)
    }
  };

  
  const fetchDependantsData = async () => {
    setLoading(true)
    try {
      const response = await axios.get(BASE_URL + "api/v1/patients/dependants");
      setDependantsData(response.data.data);
      setLoading(false)
    } catch (error) {
      console.error("Error fetching patients:", error);
      setLoading(false)
    }
  };
  const handleFileChangePrincipal = (e) => {
    const file = e.target.files[0];

    if (file) {
      handleFileUpload(file, 'principal')
      if (
        file.type === 'application/vnd.ms-excel' ||
        file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' ||
        file.type === 'text/csv'
      ) {
        setFileType('document');
      } else {
        setFileType(null);
        setUploadedFile(URL.createObjectURL(file));

      }

    }
  };

  const handleFileChangeDependant = (e) => {
    const file = e.target.files[0];

    if (file) {
      handleFileUpload(file, 'dependant')
      if (
        file.type === 'application/vnd.ms-excel' ||
        file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' ||
        file.type === 'text/csv'
      ) {
        setFileType('document');
      } else {
        setFileType(null);
        setUploadedFile(URL.createObjectURL(file));

      }

    }
  };


  const handleFileUpload = async (file, option) => {
    setLoading(true);
    if (file !== null) {
      const formData = new FormData();
      formData.append('file', file);

      try {
        let fileData = null;
        if (option === 'principal') {
          fileData = await FileUploadService.postFilePrincipal(formData);
        }
        if (option === 'dependant') {
          fileData = await FileUploadService.postFileDependant(formData);
        }

        console.log('File Data:', fileData);

        if (fileData?.statusCode === "00") {
          await toast.success("File was uploaded and processed successfully. Syncing to follow...");
          // Call Sync API
          await handleSyncData(option);
        } else {
          toast.error(fileData?.message || "File upload failed. Please try again.");
        }

        setSelectedFile(null);
      } catch (error) {
        toast.error('Error posting file data: ' + error);
      } finally {
        setLoading(false);
      }
    } else {
      toast.error('Please select a file');
      setLoading(false);
    }
  };

  const handleSyncData = async (option) => {
    try {
      let response = null;

      if (option === 'principal') {
        response = await axios.get(`${BASE_URL}api/v1/file/syncTempToPatients`);
      }
      if (option === 'dependant') {
        response = await axios.get(`${BASE_URL}api/v1/file/syncTempToDependants`);
      }

      if (response?.data?.statusCode === "00") {
        toast.success(response?.data?.message);
      } else {
        toast.error(response?.data?.message || 'Unknown error');
      }
    } catch (error) {
      console.error('Error during data sync:', error);
      toast.error('Error during data sync: ' + error.message);
    }
  };


  return (
    <DashboardLayout>
      <div className="p-10">
        <h1 className="text-2xl font-bold mb-10">Upload  batch of patient dataset</h1>

        <div style={{ backgroundColor: 'lightgray', borderRadius: '20px' }} className="bg-white shadow-lg rounded p-10">
          <div className="grid grid-cols-2 gap-1"> {/* Reduced gap to 2 */}
            <p className="text-lg"><strong>file fommat:</strong></p>
            <p className="text-lg">csv and excel files only</p>

            <p className="text-lg"><strong>file columns:</strong></p>
            <p className="text-lg">dataset columns must match the system standard</p>

            <p className="text-lg"><strong>recommendations</strong></p>
            <p className="text-lg">make sure you have current backup before taking this action</p>

            <p className="text-lg"><strong>principal dataset back-up</strong></p>
            <p className="text-lg">
              <TableExport data={principalData} filename="principal-members" />
            </p>

            <p className="text-lg"><strong>dependancies dataset back-up</strong></p>
            <p className="text-lg">
              <TableExport data={dependantsData} filename="dependancie-members" />
            </p>


          </div>
        </div>

        <div className="flex space-x-4 mb-4">
          <p className="text-lg" style={{ color: 'darkred', marginTop: "10%", fontSize: '1rem', paddingLeft: '20%' }}><strong>upload principal patient .csv file</strong></p>
          <input
            id="fileUpload"
            type="file"
            name="file"
            ref={fileInputRef}
            onChange={handleFileChangePrincipal}
            style={{ backgroundColor: 'darkred', marginTop: "10%", marginLeft: '6%', fontSize: '0.9rem', color: 'white' }}
          />
        </div>

        <div className="flex space-x-4 mb-4">
          <p className="text-lg" style={{ color: '#000630', marginTop: "3%", fontSize: '1rem', paddingLeft: '20%' }}><strong>upload dependant patient .csv file</strong></p>
          <input
            id="fileUpload"
            type="file"
            ref={fileInputRef}
            onChange={handleFileChangeDependant}
            style={{ backgroundColor: '#000630', marginTop: "3%", marginLeft: '5%', fontSize: '0.9rem', color: 'white' }}
          />
        </div>

      </div>

      <Load loading={loading} />

    </DashboardLayout>
  );
};

export default UploadPrincipalPatient;