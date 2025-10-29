import React from "react";
import PropTypes from "prop-types";

const FormInput = ({
  title,
  id,
  value,
  name,
  placeholder,
  type,
  invalid,
  disabled,
  required,
  InvalidText,
  onChange,
  className
}) => {
  const defaultinputClasses = `w-full px-5 py-4 h-10 rounded-lg font-medium border text-gray-900 placeholder-gray-900 placeholder:font-semibold text-sm focus:outline-none focus:border-gray-400 focus:bg-white ${
    invalid ? "border-red-500" : "border-gray-200"
  }`;
  const inputClasses = className
  ? `${defaultinputClasses} ${className}`
  : defaultinputClasses;
  return (
    <div className="relative w-full">
        
      <label
        className="bg-white px-1 text-sm duration-100 ease-linear cursor-text"
      >
        {title}
      </label>
      <input
        className={inputClasses}
        id={id}
        value={value}
        name={name}
        type={type}
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

FormInput.propTypes = {
  title: PropTypes.string,
  InvalidText: PropTypes.string,
  id: PropTypes.string,
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  name: PropTypes.string,
  placeholder: PropTypes.string,
  type: PropTypes.string,
  invalid: PropTypes.bool,
  disabled: PropTypes.bool,
  required: PropTypes.bool,
  onChange: PropTypes.func,
  className: PropTypes.string,
};

export default FormInput;

