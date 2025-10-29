import React from 'react';

const MemberPagination = ({ membersPerPage, totalMembers, currentPage, paginate }) => {
  const pageNumbers = [];

  for (let i = 1; i <= Math.ceil(totalMembers / membersPerPage); i++) {
    pageNumbers.push(i);
  }

  return (
    <nav>
      <ul className="flex justify-center">
        {pageNumbers.map((number) => (
          <li key={number}>
            <button
              className={`${
                number === currentPage
                  ? 'bg-blue-500 text-white'
                  : 'bg-gray-300 text-gray-700'
              } font-semibold py-1 px-2 rounded mx-1`}
              onClick={() => paginate(number)}
            >
              {number}
            </button>
          </li>
        ))}
      </ul>
    </nav>
  );
};

export default MemberPagination;