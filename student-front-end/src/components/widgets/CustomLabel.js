import React from "react";
import PropTypes from "prop-types";

const CustomLabel = ({ text }) => {
  return (
    <div  className="pb-5">
    <label className="text-black text-[2vw] font-semibold font-Roboto">
      {text}
    </label>
    </div>
  );
};

CustomLabel.propTypes = {
  text: PropTypes.string.isRequired,
};

export default CustomLabel;