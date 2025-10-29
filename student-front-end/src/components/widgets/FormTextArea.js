import React from "react";
import PropTypes from "prop-types";

const FormTextArea = ({
  title,
  id,
  value,
  name,
  placeholder,
  invalid,
  disabled,
  required,
  InvalidText,
  onChange,
  className
}) => {
  const defaultTextAreaClasses = `w-full px-5 py-4 rounded-lg font-medium border text-gray-900 placeholder-gray-900 placeholder:font-semibold text-sm focus:outline-none focus:border-gray-400 focus:bg-white ${
    invalid ? "border-red-500" : "border-gray-200"
  }`;
  const textAreaClasses = className
    ? `${defaultTextAreaClasses} ${className}`
    : defaultTextAreaClasses;
  return (
    <div className="relative w-full">
      <label className="bg-white px-1 text-sm duration-100 ease-linear cursor-text">
        {title}
      </label>
      <textarea
        className={textAreaClasses}
        id={id}
        value={value}
        name={name}
        placeholder={placeholder}
        disabled={disabled}
        required={required}
        onChange={onChange}
      />
      {invalid && (
        <p className="text-red-500 text-sm mt-1 ml-2">{InvalidText}</p>
      )}
    </div>
  );
};

FormTextArea.propTypes = {
  title: PropTypes.string,
  InvalidText: PropTypes.string,
  id: PropTypes.string,
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  name: PropTypes.string,
  placeholder: PropTypes.string,
  invalid: PropTypes.bool,
  disabled: PropTypes.bool,
  required: PropTypes.bool,
  onChange: PropTypes.func,
  className: PropTypes.string,
};

export default FormTextArea;
