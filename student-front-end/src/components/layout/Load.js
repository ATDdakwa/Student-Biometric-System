import React, { useEffect } from "react";

const Load = ({ loading }) => {
  useEffect(() => {
    if (loading) {
      document.body.style.cursor = "wait";
    } else {
      document.body.style.cursor = "default";
    }
  }, [loading]);

  return (
    <>
      {loading && (
        <div className="fixed top-0 left-0 w-full h-full bg-customGreen bg-opacity-50 flex items-center justify-center">
          <div className="w-16 h-16 border-4 border-light-white rounded-full animate-spin"></div>
        </div>
      )}
    </>
  );
};

export default Load;