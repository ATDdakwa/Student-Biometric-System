import React, { useState } from "react";
import { Link } from "react-router-dom";
import FormInput from "../../components/widgets/FormInput";
import Button from "../../components/widgets/Button";
import AuthLayout from "../../components/layout/AuthLayout";

const ForgetPassword = () => {
  const [email, setEmail] = useState("");
  const [isEmailExists, setIsEmailExists] = useState(false);
  const [isEmailValid, setIsEmailValid] = useState(true);

  const checkIfEmailExists = async () => {
    try {
      const response = await fetch("", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email }),
      });

      if (response.ok) {
        setIsEmailExists(true);
        setIsEmailValid(true);
      } else {
        setIsEmailExists(false);
        setIsEmailValid(false);
      }
    } catch (error) {
      // console.error("Error:", error);
      setIsEmailExists(false);
      setIsEmailValid(false);
    }
  };

  const handleResetPasswordClick = () => {
    checkIfEmailExists();
  };

  return (
    <AuthLayout
      title="Reset Password "
      subTitle="Please enter the email address you registered with to receive password reset information."
    >
      {isEmailExists ? (
        <div className="resetpassword">
          <FormInput title="New Password" type="password" />
          <FormInput title="Confirm Password" type="password" />
          <Button buttonText="Create Password" className="mt-5" />
        </div>
      ) : (
        <div className="verifyemail">
          <FormInput
            title="Email address"
            type="email"
            value={email}
            invalid={!isEmailValid}
            InvalidText="Email not found"
            onChange={(e) => setEmail(e.target.value)}
          />
          <Button
            buttonText="Reset Password"
            className="mt-5 bg-red "
            onClick={handleResetPasswordClick}
          />
        </div>
      )}
      <p className="mt-4 text-md text-gray-500 text-start">
        Already have a HCP account?
        <Link to="/login" className="text-black ml-2 font-medium">
          Sign In
        </Link>
      </p>
    </AuthLayout>
  );
};

export default ForgetPassword;
