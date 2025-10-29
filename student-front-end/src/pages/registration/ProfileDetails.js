import React, { useState } from "react";
import { Link } from "react-router-dom";
import FormInput from "../../components/widgets/FormInput";
import CustomLabel from "../../components/widgets/CustomLabel";
import FormTextArea from "../../components/widgets/FormTextArea";
import FormSelect from "../../components/widgets/FormSelect";
import Button from "../../components/widgets/Button";
import ProjectImages from "../life/ProjectImages";

const ProfileDetails = () => {

  const selectOptions = [
    { value: "option1", label: "Velocity" },
    { value: "option2", label: "Afrosoft" },
    { value: "option3", label: "PSMI ZIM" },
    // Add more options as needed
  ];

  return (
    <div className="p-5">
        <CustomLabel text="Project Details" />
   
      <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
        <FormInput title="Project Title" type="text" className="mb-5" />
        <FormInput title="Project Industry" type="text" className="mb-5" />
      </div>
      <FormTextArea title="Project Brief"  className="mb-5" />
      <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
        <FormInput title="Start Date" type="date" className="mb-5" />
        <FormInput title="End Date" type="date" className="mb-5" />
      </div>
      <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
      <FormSelect title="Experience From" name="selectOption" options={selectOptions}/>
        <FormInput title="Project Link" type="text" className="mb-5" />
      </div>
      <ProjectImages />
      <div className="grid grid-cols-1 gap-4 md:grid-cols-6">
      <Button buttonText="Save Details" className="mt-1" />
      </div>
      

      </div>
  );
};

export default ProfileDetails;
