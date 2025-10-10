import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import FormInput from "../../components/widgets/FormInput";
import AuthLayout from "../../components/layout/AuthLayout";
import axios from "axios";
import Cookies from "js-cookie";
import Load from "../../components/layout/Load";
import getBaseUrl from "./AuthBaseUrl";
import AuditTrailService from "../../services/AuditTrailService";
import { toast, ToastContainer } from 'react-toastify'; // Import toast components
import 'react-toastify/dist/ReactToastify.css'; // Import toast CSS

const Login = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const BASE_URL = getBaseUrl();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [fieldError, setFieldError] = useState(false);

  const handleClick = () => {
    setLoading(true);
    setTimeout(() => {
      setLoading(false);
    }, 1000);
  };

  const signIn = async (email, password) => {
    handleClick();
    console.log("Signing you in ...");
    const requestBody = {
      userName: email,
      password: password
    };

    try {
      const response = await axios.post(BASE_URL + 'api/v1/auth/login', requestBody);

      if (response.status === 200) {
        const sbu = response.data.login.user.sbu;
        const name = response.data.login.user.firstName + " " + response.data.login.user.lastName;
        const userRole = response.data.login.user.userRole;
        const username = response.data.login.user.userName;
        const jwtString = response.data.token;

        Cookies.set('sbu', sbu);
        Cookies.set('token', jwtString);
        Cookies.set('name', name);
        Cookies.set('userRole', userRole);
        Cookies.set('userName', username);
        AuditTrailService.createLog("successful authentication");
        if (sbu === "BIOMETRIC") {
          navigate(`/dashboard`);
          return;
        }
      }
    } catch (error) {
      console.error('Signin API call failed:', error);
      toast.error("Invalid username or password."); // Show toast on error
    }
  };

  const handleSignIn = () => {
    if (!email || !password) {
      setFieldError(true);
      return;
    }
    signIn(email, password);
  };

  return (
    <AuthLayout
      title="Good to see you back!"
      subTitle="Log in to your account to continue"
    >
      <FormInput
        title="Username"
        type="email"
        className="mb-5"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      <FormInput
        title="Password"
        type="password"
        className="mb-5"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <Link to="/resetpassword" className="text-black font-medium">
        Forget password
      </Link>
      {fieldError && (
        <p className="text-red text-sm mt-2">Please fill in all the fields</p>
      )}
      <button
        onClick={handleSignIn}
        style={{ backgroundColor: '#000630' }}
        className="mt-5 tracking-wide font-semibold text-gray-100 w-full py-4 rounded-lg transition-all duration-300 ease-in-out flex items-center justify-center focus:shadow-outline focus:outline-none"
      >
        <span className="ml-3">Sign In</span>
      </button>
      <Load loading={loading} />
      <ToastContainer /> {/* Add this line */}
    </AuthLayout>
  );
};

export default Login;