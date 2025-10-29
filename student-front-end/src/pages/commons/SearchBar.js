import React from "react";

const SearchBar = ({ searchTerm, handleSearch }) => {
  const handleInputChange = (event) => {
    const value = event.target.value;
    console.log("Input value:", value); // Log the input value
    handleSearch(value); // Pass the input value to handleSearch
  };
  return (
    <div className="flex mb-4">
      <input
        type="text"
        placeholder="Search data..."
        className="p-2 border border-gray-300 rounded-l h-8 w-1/2"
        value={searchTerm}
        style={{fontSize:'0.9rem'}}
        onChange={handleInputChange}
      />
      <button className="bg-gray-300 p-2 rounded-r">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
          className="feather feather-search h-4 w-4"
        >
          <circle cx="11" cy="11" r="8" />
          <line x1="21" y1="21" x2="16.65" y2="16.65" />
        </svg>
      </button>
    </div>
  );
};

export default SearchBar;