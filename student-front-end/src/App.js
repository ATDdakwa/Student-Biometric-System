import React, { useState,useEffect } from "react";
import { BrowserRouter,useNavigate, Routes, Route } from "react-router-dom";
//Auth
import Login from "./pages/registration/Login";
import Registration from "./pages/registration/Registration";
import ForgetPassword from "./pages/registration/ForgetPassword";
import ProfileDetails from "./pages/registration/ProfileDetails";
import ConfirmCode from "./pages/registration/ConfirmCode";
import DashboardLayout from "./components/layout/DashboardLayout";
import Home from "./pages/hcp/Home";
import Cookies from "js-cookie";
import { jwtDecode } from "jwt-decode";
import MainStudentEnrolment from "./pages/hcp/MainStudentEnrolment";
import MainStudentVerification from "./pages/hcp/MainStudentVerification";

import StudentBiometrics from "./pages/hcp/StudentBiometrics";
import EnrolmentReport from "./pages/hcp/EnrolmentReport";
import BioStatsReport from "./pages/hcp/BioStatsReport";
import AuditTrail from "./pages/hcp/AuditTrail";
import AccessControl from "./pages/hcp/AccessControl";
import AccessPoints from "./pages/hcp/AccessPoints";
import StudentVerification from "./pages/hcp/StudentVerification";
import Students from "./pages/hcp/Students";
import AddStudent from "./pages/hcp/AddStudent";
import StudentFingerprintVerification from "./pages/hcp/StudentFingerprintVerification";
import EnrolmentStats from "./pages/hcp/StudentEnrolmentStats";
import BiometricAccessReport from "./pages/hcp/BiometricAccessReport";
import CampusActivityLog from "./pages/hcp/CampusActivityLog";

const isTokenExpired = (token) => {
  if (!token) {
    return true;
  }

  const decodedToken = jwtDecode(token);
  const currentTime = Date.now() / 500;

  return decodedToken.exp < currentTime;
};

const getTokenExpirationDate = (token) => {
  if (!token) {
    return null;
  }

  const decodedToken = jwtDecode(token);
  if (!decodedToken.exp) {
    return null;
  }

  const expirationDate = new Date(0);
  expirationDate.setUTCSeconds(decodedToken.exp);

  return expirationDate;
};

const App = () => {
  // const navigate = useNavigate();
  useEffect(() => {
    const token = Cookies.get("token");

    if (isTokenExpired(token)) {
      // Token has expired, log out the user
      Cookies.remove("token");
     
    } else {
      const expirationDate = getTokenExpirationDate(token);
      console.log("Token expires on:", expirationDate);
    }
  }, []);


  return (
    <BrowserRouter className="font-DMSans bg-lightPink">
      <Routes>
        {/* Auth */}
        <Route path="/register" element={<Registration />} />
        <Route path="/" element={<Login />} />
        <Route path="/resetpassword" element={<ForgetPassword />} />
        <Route path="/profiledetails" element={<ProfileDetails />} />
        <Route path="/confirmcode/:username" element={<ConfirmCode />} />
        <Route path="/dashboard" element={<Home />} />

        <Route path="/main-student-enrollment" element={<MainStudentEnrolment />} />
        <Route path="/main-student-verification" element={<MainStudentVerification />} />
        <Route path="/student-fingerprint-verification" element={<StudentFingerprintVerification />} />

        {/*<Route path="/dependant-patient-enrollment" element={<DependantPatientEnrolment />} />*/}
        {/*<Route path="/dependant-patient-verification" element={<DependantPatientVerification />} />*/}

        {/*<Route path="/view-patient" element={<ViewPatient />} />*/}
        {/*<Route path="/view-dependants" element={<ViewDependantForBiometrics />} />*/}
        {/*<Route path="/patients" element={<Patients />} />*/}
        <Route path="/students" element={<Students />} />
        <Route path="/add-student" element={<AddStudent />} />
        {/*<Route path="/dependants" element={<Dependants />} />*/}
        <Route path="/student-biometrics" element={<StudentBiometrics />} />
        <Route path="/student-verification" element={<StudentVerification />} />
        <Route path="/enrolment-reports" element={<BioStatsReport />} />
        <Route path="/enrolment-stats" element={<EnrolmentStats />} />
        <Route path="/enrolment-test" element={<EnrolmentReport />} />
        <Route path="/student-biometric-access-tracking" element={<BiometricAccessReport />} />
        <Route path="/activity-log" element={<CampusActivityLog />} />
        {/*<Route path="/upload-patient-dataset" element={<UploadPrincipalPatient />} />*/}
        <Route path="/audit-trail" element={<AuditTrail />} />
        <Route path="/access-control" element={<AccessControl />} />
        <Route path="/access-points" element={<AccessPoints />} />

        <Route
          path="/dashboard/*"
          element={<DashboardLayout />}
        />

      </Routes>
    </BrowserRouter>
  );
};
export default App;
