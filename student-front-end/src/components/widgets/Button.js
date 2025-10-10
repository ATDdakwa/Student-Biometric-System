import React from "react";
import PropTypes from "prop-types";

const Button = ({
  buttonText,
  id,
  name,
  type,
  disabled,
  onClick,
  className,
  icon,
}) => {
  const defaultButtonClasses =
    "tracking-wide font-semibold bg-green text-gray-100 w-full py-4 rounded-lg transition-all duration-300 ease-in-out flex items-center justify-center focus:shadow-outline focus:outline-none";
  const buttonClasses = className
    ? `${defaultButtonClasses} ${className}`
    : defaultButtonClasses;

  return (
    <button
      className={buttonClasses}
      id={id}
      name={name}
      type={type}
      disabled={disabled}
      onClick={onClick}
    >
      {icon && <span className="mr-2">{icon}</span>}
      <span>{buttonText}</span>
    </button>
  );
};

Button.propTypes = {
  buttonText: PropTypes.string,
  id: PropTypes.string,
  name: PropTypes.string,
  type: PropTypes.string,
  disabled: PropTypes.bool,
  onClick: PropTypes.func,
  className: PropTypes.string,
  icon: PropTypes.node,
};

export default Button;
