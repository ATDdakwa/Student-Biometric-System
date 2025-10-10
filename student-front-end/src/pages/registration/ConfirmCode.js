import React, { useState } from "react";
import { Link , useNavigate, useParams } from "react-router-dom";
import FormInput from "../../components/widgets/FormInput";
import Button from "../../components/widgets/Button";
import AuthLayout from "../../components/layout/AuthLayout";

const ConfirmCode = () => {
  const [code, setCode] = useState("");
  const navigate = useNavigate();
  const { username } = useParams();
  const passedUserName = username.replace('$', '')


  async function confirmSignUp() {
    // try {
    //   await Auth.confirmSignUp(passedUserName, code);
    //   handleConfirmCodeClick();
    // } catch (error) {
    //   // console.log('error confirming sign up', error);
    // }
  }

  const checkIfCodeExists = async () => {
    try {
      const response = await fetch("", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ code }),
      });

      if (response.ok) {
        checkIfCodeExists(true);
      } else {
        checkIfCodeExists(false);
      }
    } catch (error) {
      // console.error("Error:", error);
    }
  };

  const handleConfirmCodeClick = () => {
    navigate("/login");
    checkIfCodeExists();
  };

  return (
    <AuthLayout
      title="Confirm Code"
      subTitle="Please enter the code from your email."
    >
      <div className="verifyemail">
        <FormInput
          title="Confirm Code"
          type="number"
          value={code}
          InvalidText="Code not found"
          onChange={(e) => setCode(e.target.value)}
        />
        <Button
          buttonText="Confirm Code"
          className="mt-5"
          onClick={confirmSignUp}
        />
      </div>
        <p className="mt-4 text-md text-gray-500 text-start font-medium">
          Didn't receive verification email?
          <Link to="/" className="text-red ml-2 font-bold">
          Resend
        </Link>
        </p>

    </AuthLayout>
  );
};

export default ConfirmCode;
