import React from "react";

const Pagination = ({ currentPage, totalPages, onPageChange }) => {
  const pageNumbers = [];

  for (let i = 1; i <= totalPages; i++) {
    pageNumbers.push(i);
  }

  return (
    <div className="flex items-center justify-center mt-4">
    <nav>
      <ul className="flex items-center" style={{ display: "flex", listStyle: "none" }}>
        {pageNumbers.map((pageNumber, index) => (
          <li
            key={pageNumber}
            className={`page-item ${pageNumber === currentPage ? "active" : ""}`}
          >
            <button
              className={`px-3 py-2 rounded-md ${
                currentPage === index + 1
                  ? "bg-red text-white"
                  : "bg-white text-gray-500"
              }`}
              onClick={() => onPageChange(index + 1)}
            >
              {index + 1}
            </button>
          </li>
        ))}
      </ul>
    </nav>
    </div>
  );
};

export default Pagination;