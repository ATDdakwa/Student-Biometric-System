import React from "react";

const TransparentButton = ({ onClick, buttonText }) => {
  return (
    <div className="pt-1">
      <button
        htmlFor="transparent-button"
        style={{
          backgroundColor: "white",
          border: "1px solid #004C4C",
          borderRadius: "10px",
          padding: "0.5rem 1rem",
        }}
        onClick={onClick}
      >
        {buttonText}
      </button>
    </div>
  );
};

export default TransparentButton;