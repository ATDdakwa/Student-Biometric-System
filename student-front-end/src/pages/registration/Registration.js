import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import FormInput from "../../components/widgets/FormInput";
import FormSelect from "../../components/widgets/FormSelect";
import Button from "../../components/widgets/Button";
import AuthLayout from "../../components/layout/AuthLayout";
import INTERNAL_API from '../../_api/covid-internal/internalapi'
import axios from "axios";
import store from '../../storage/store';
import Load from "../../components/layout/Load";
import getBaseUrl from "./AuthBaseUrl";

const Registration = () => {
  const [activeTab, setActiveTab] = useState(0);
  const tabs = ["Create your account here"];
  const [loading, setLoading] = useState(false);
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [passwordMatch, setPasswordMatch] = useState(true);
  const [fieldError, setFieldError] = useState(false);
  const [selectedRole, setSelectedRole] = useState({});
  const [selectedUser, setSelectedUser] = useState({});
  const [userName, setUserName] = useState("");
  const BASE_URL = getBaseUrl();
  const personalFields = (
    <>
      <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
        <FormInput
          title="First Name"
          type="text"
          className="mb-5"
          value={firstName}
          onChange={(e) => setFirstName(e.target.value)}
        />
        <FormInput
          title="Last Name"
          type="text"
          className="mb-5"
          value={lastName}
          onChange={(e) => setLastName(e.target.value)}
        />
      </div>
      <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
        <FormInput
          title="Email address"
          type="email"
          className="mb-5"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <FormInput
          title="Username"
          type="text"
          className="mb-5"
          value={userName}
          onChange={(e) => setUserName(e.target.value)}
        />
      </div>


      <FormSelect
        id="selectRole"
        options={[
          { value: "BIOMETRIC", label: "PATIENT BIOMETRIC" },
        ]}
        value={selectedRole}
        onChange={setSelectedRole}
      />

      <FormSelect
        id="selectUser"
        options={[
          { value: "", label: "Select User Role", isDisabled: true }, // Placeholder option
          { value: "User", label: "User" },
          { value: "HR", label: "HR" },
        ]}
        value={selectedUser}
        onChange={setSelectedUser}
      />

      <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
        <FormInput
          title="Password"
          type="password"
          className="mb-5"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <FormInput
          title="Confirm Password"
          type="password"
          className="mb-5"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
        />
      </div>
    </>
  );

  const organizationFields = (
    <>
      <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
        <FormInput
          title="First Name"
          type="text"
          className="mb-5"
          value={firstName}
          onChange={(e) => setFirstName(e.target.value)}
        />
        <FormInput
          title="Last Name"
          type="text"
          className="mb-5"
          value={lastName}
          onChange={(e) => setLastName(e.target.value)}
        />
      </div>
      <FormInput
        title="Email address"
        type="email"
        className="mb-5"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
        <FormInput
          title="Password"
          type="password"
          className="mb-5"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <FormInput
          title="Confirm Password"
          type="password"
          className="mb-5"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
        />
      </div>
    </>
  );

  const navigate = useNavigate();

  const validateForm = () => {
    if (activeTab === 0) {
      return !firstName || !lastName || !email || !password || !confirmPassword;
    } else if (activeTab === 1) {
      return !firstName || !lastName || !email || !password || !confirmPassword;
    }
    return false;
  };

  const handleCreateAccount = () => {
    const isFormInvalid = validateForm();
    if (isFormInvalid) {
      setFieldError(true);
      return;
    }

    if (password === confirmPassword) {
      signUp(email, password);
    } else {
      setPasswordMatch(false);
    }
  };

  async function signUp(username, password) {
    try {
      handleSignup();
      // navigate(`/confirmcode/${username}`);
    } catch (error) {
    }
  }

  const handleClick = () => {
    setLoading(true);
    setTimeout(() => {
      setLoading(false);
    }, 1000);
  };

  const handleSignup = (e) => {
    handleClick();
    console.log("Signing you up...")
    const requestBody = {
      "firstName": firstName,
      "lastName": lastName,
      "sbu": selectedRole.value,
      "email": email,
      "userName": userName,
      "password": password,
      "role": selectedUser.value
    }
    axios.post(BASE_URL + "api/v1/users/add-user", requestBody).then(res => {
      if (res.status === 200) {
        navigate("/");
        // alert("Signup Success, Redirecing to Login page")
      }
    }).catch(
      err => {
        console.error("auth/signup api call failed. " + JSON.stringify(err?.response?.data).data)
        alert("Signup failed, check username/password are provided.")
      }
    )
  }


  useEffect(() => {
    setPasswordMatch(password === confirmPassword);
  }, [password, confirmPassword]);

  return (
    <AuthLayout
      // title="Hospital Cash Plan"
      subTitle="You're one step away from your role"
      showTabPanels={true}
      tabs={tabs}
      activeTab={activeTab}
      setActiveTab={setActiveTab}
    >
      <div>
        {tabs.map((tab, index) => (
          <div key={index} className={activeTab === index ? "block" : "hidden"}>
            {activeTab === 0 ? personalFields : organizationFields}
          </div>
        ))}
      </div>

      {fieldError && (
        <p className="text-red-500 text-sm mb-5">Please fill in all the required fields</p>
      )}

      {!passwordMatch && (
        <p className="text-red-500 text-sm mb-5">Password and Confirm Password do not match</p>
      )}
      <button
        onClick={handleCreateAccount}
        style={{ backgroundColor: '#000630' }}
        className="mt-5 tracking-wide font-semibold text-gray-100 w-full py-4 rounded-lg transition-all duration-300 ease-in-out flex items-center justify-center focus:shadow-outline focus:outline-none"
      >
        <span className="ml-3">Create account</span>
      </button>
      <p className="mt-4 text-md  text-gray-500 text-start font-medium">
        Already have an account?
        <Link to="/" className="text-black ml-2 font-bold">
          Sign In
        </Link>
      </p>
      <Load loading={loading} />
    </AuthLayout>
  );
};

export default Registration;