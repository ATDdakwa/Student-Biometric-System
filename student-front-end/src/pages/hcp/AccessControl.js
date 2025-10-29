import React, { useEffect, useState } from "react";
import axios from "axios";
import DashboardLayout from "../../components/layout/DashboardLayout";
import SearchBar from "../commons/SearchBar";
import TableExport from "../commons/TableExport";
import getBaseUrl from "../registration/AuthBaseUrl";
import { useNavigate } from 'react-router-dom';
import { IoMdEye, IoMdAdd, IoMdCreate, IoIosClose } from "react-icons/io";
import 'react-toastify/dist/ReactToastify.css';
import { toast, ToastContainer } from 'react-toastify';
import Load from "../../components/layout/Load";
import FormInput from "../../components/widgets/FormInput";
import FormSelect from "../../components/widgets/FormSelect";


const AccessControl = () => {
  const BASE_URL = getBaseUrl();
  const [users, setUsers] = useState({ user: [], totalElements: 0 });
  const [searchTerm, setSearchTerm] = useState("");
  const [loading, setLoading] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 20;
  const navigate = useNavigate();
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [passwordMatch, setPasswordMatch] = useState(true);
  const [fieldError, setFieldError] = useState(false);
  const [selectedRole, setSelectedRole] = useState({});
  const [selectedUser, setSelectedUser] = useState({});
  const [userName, setUserName] = useState("");
  const [addUserMode, setAddUser] = useState(false);
  const [editUserMode, setEditUser] = useState(false);
  const [viewUserMode, setViewUserMode] = useState(false);
  const [heading, setHeading] = useState("User Control");

  const [singleUser, setSingleUser] = useState({});
  const fetchData = async () => {
    setLoading(true);
    try {
      const response = await axios.get(BASE_URL + "api/v1/users/get-all");
      setUsers({ user: response.data, totalElements: response.data.length });
      setLoading(false);
    } catch (error) {
      console.error("Error fetching logs:", error);
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleSearch = (term) => {
    if (typeof term === 'string') {
      setSearchTerm(term);
    } else {
      console.error("Search term must be a string:", term);
      setSearchTerm("");
    }
  };
  const addUser = () => {
    navigate('/register');
  
  };

  const cancel = () => {
    setHeading('Add Conrol')
    setAddUser(false)
    setEditUser(false)
    setViewUserMode(false)
    setSearchTerm("");
  };
  const handleView = (user) => {
    setSingleUser(user);
    setViewUserMode(true);
    setAddUser(false);
    setEditUser(false);
    setSearchTerm(user.userName);
};

  const handleEdit = (user) => {
    setHeading('Edit User')
    setSearchTerm(user.userName);
    setAddUser(true)
    setEditUser(false)
    setViewUserMode(false)
    setEditUser(true)
    setFirstName(user.firstName)
    setLastName(user.lastName)
    setEmail(user.userEmail)
    setUserName(user.userName)
  };

  const handleRemove = (user) => {

  };

  const handleResetPassword = async () => {
    if (password !== confirmPassword) {
        toast.error("Passwords do not match.");
        return;
    }

    // Use the email from the selected user
    const userEmail = singleUser.userEmail;

    if (!userEmail) {
        toast.error("Email is required.");
        return;
    }

    try {
        const response = await axios.put(`${BASE_URL}api/v1/users/reset-password`, {
            pin: password,
            confirmPin: confirmPassword
        }, {
            params: { email: userEmail }  
            // Use userEmail here
        });

        if (response.status === 200) {
            toast.success("Password reset successful!");
            cancel(); // Reset the form/view
        }
    } catch (error) {
        console.error("Error resetting password:", error);
        toast.error("Failed to reset password.");
    }
};


  const { user, totalElements } = users;

  const filteredData = Array.isArray(user)
    ? user.filter((item) =>
      Object.values(item).some(
        (value) =>
          typeof value === "string" &&
          value.toLowerCase().includes(searchTerm.toLowerCase())
      )
    )
    : [];



  const totalPages = Math.ceil(filteredData.length / itemsPerPage);
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = filteredData.slice(indexOfFirstItem, indexOfLastItem);

  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  const getPaginationRange = () => {
    const range = [];
    const start = Math.max(1, currentPage - 4);
    const end = Math.min(totalPages, start + 9);
    for (let i = start; i <= end; i++) {
      range.push(i);
    }
    return range;
  };

  return (
    <DashboardLayout>
      <div className="p-4" style={{ padding: '3rem' }}>
        <h1 className="text-2xl font-bold mb-6">Access Control</h1>
        <Load loading={loading} />


        <div style={{ display: 'flex', }}>
          <div style={{ flex: 1, marginRight: '10px', backgroundColor: '#f0f0f0', borderRadius: '10px', padding: '8px' }}>

            <p style={{ fontSize: '1rem', marginBottom: '4px' }}>System users</p><hr />

            <div className="flex mb-4">
              <div className="w-3/4">
                <SearchBar searchTerm={searchTerm} handleSearch={handleSearch} />
              </div>
              <div className="w-1/4 flex items-center">
                <TableExport data={filteredData} filename="users" />
              </div>
            </div>

            <button
              onClick={() => addUser()}
              style={{ backgroundColor: '#000630', marginBottom: '2%' }}
              className="hover:bg-green-700 text-white font-bold py-2 px-4 rounded text-sm flex items-center"
            >
              <IoMdAdd className="mr-1" />
              <span>Add User</span>
            </button>

            <div className="overflow-y-auto max-h-[50vh]">
              <table className="min-w-full border border-gray-300 shadow-lg rounded-lg">
                <thead className="bg-gray-300">
                  <tr >
                    {[
                      "Full Name",
                      "User Name",
                      "Role",
                      "Action",
                    ].map((header) => (
                      <th key={header} style={{ width: header === "Action" ? "2%" : "10%" }} className={`py-3 px-4 text-sm text-left font-semibold border-b border-gray-400`}>
                        {header}
                      </th>
                    ))}
                  </tr>
                </thead>
                <tbody>
                  {currentItems.length === 0 ? (
                    <tr>
                      <td colSpan="14" className="text-center py-4">No data</td>
                    </tr>
                  ) : (
                    currentItems.map((user) => (
                      <tr key={user.id} className="border-b border-gray-300 hover:bg-gray-100">
                        <td className="py-3 px-4 text-sm">{user.firstName} {user.lastName}</td>
                        <td className="py-3 px-4 text-sm">{user.userName}</td>
                        <td className="py-3 px-4 text-sm">{user.userRole}</td>

                        <td className="py-2 px-4 text-sm">
                          <div className="flex space-x-2">
                            <>
                              <button
                                onClick={() => handleView(user)}
                                className="bg-customGreen text-white font-bold py-1 px-2 rounded"
                                title="View User Details"
                              >
                                <IoMdEye className="mr-1 text-lg" />
                              </button>
                              <button
                                onClick={() => handleEdit(user)}
                                className="bg-customGreen hover:bg-yellow-700 text-white font-bold py-1 px-2 rounded"
                                title="Edit User Details"
                                style={{ backgroundColor: 'green' }}
                              >
                                <IoMdCreate className="mr-1 text-lg" />
                              </button>
                              <button
                                onClick={() => handleRemove(user)}
                                className="bg-customGreen hover:bg-yellow-700 text-white font-bold py-1 px-2 rounded"
                                title="Delete User"
                                style={{ backgroundColor: 'darkred' }}
                              >
                                <IoIosClose className="mr-1 text-lg" />
                              </button>
                            </>
                          </div>
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>
            <div className="flex justify-center mt-4">
              {getPaginationRange().map((pageNumber) => (
                <button
                  key={pageNumber}
                  onClick={() => handlePageChange(pageNumber)}
                  className={`mx-1 px-4 py-2 rounded ${currentPage === pageNumber ? 'bg-customGreen text-white' : 'bg-gray-300'}`}
                >
                  {pageNumber}
                </button>
              ))}
            </div>
          </div>
          <div style={{ flex: 1, marginLeft: '10px', backgroundColor: 'white', borderRadius: '10px', padding: '8px', borderWidth: '1px', borderColor: '#000630' }}>
            <p style={{ fontSize: '1rem', marginBottom: '4px' }}>{heading}</p><hr />
            {viewUserMode && (
              <div className="grid grid-cols-2 gap-1" > {/* Reduced gap to 2 */}
                <p className="text-lg"><strong>Firstname:</strong></p>
                <p className="text-lg">{singleUser.firstName}</p>
                <p className="text-lg"><strong> Lastname:</strong></p>
                <p className="text-lg">{singleUser.lastName}</p>
                <p className="text-lg"><strong>Username:</strong></p>
                <p className="text-lg">{singleUser.userName} </p>
                <p className="text-lg"><strong>Role:</strong></p>
                <p className="text-lg">{singleUser.userRole} </p>
                <p className="text-lg"><strong>Email:</strong></p>
                <p className="text-lg">{singleUser.userEmail} </p>
                <p className="text-lg"><strong>Username:</strong></p>
                <p className="text-lg">{singleUser.userName} </p>

                <p className="text-lg"><strong>New Password:</strong></p>
                <p style={{paddingRight:20}} className="text-lg">
                  <FormInput
                    type="password"
                    className="mb-5"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                  />
                </p>

                <p className="text-lg"><strong>Confirm Password:</strong></p>
                <p style={{paddingRight:20}} className="text-lg">
                  <FormInput
                    type="password"
                    className="mb-5"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                  />
                </p>
              </div>
            )}
            {addUserMode && (
              <>
                <div style={{ padding: '10px' }}>
                  <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
                    <FormInput
                      title="First Name"
                      type="text"
                      className="mb-5"
                      value={firstName}
                      onChange={(e) => setFirstName(e.target.value)}
                    />
                    <FormInput
                      title="Last Name"
                      type="text"
                      className="mb-5"
                      value={lastName}
                      onChange={(e) => setLastName(e.target.value)}
                    />
                  </div>
                  <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
                    <FormInput
                      title="Email address"
                      type="email"
                      className="mb-5"
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                    />
                    <FormInput
                      title="Username"
                      type="text"
                      className="mb-5"
                      value={userName}
                      onChange={(e) => setUserName(e.target.value)}
                    />
                  </div>


                  <FormSelect
                    id="selectRole"
                    options={[
                      { value: "BIOMETRIC", label: "STUDENT BIOMETRIC" },
                    ]}
                    value={selectedRole}
                    onChange={setSelectedRole}
                  />

                  <FormSelect
                    id="selectUser"
                    options={[
                      { value: "", label: "Select User Role", isDisabled: true }, // Placeholder option
                      { value: "User", label: "User" },
                      { value: "HR", label: "HR" },
                    ]}
                    value={selectedUser}
                    onChange={setSelectedUser}
                  />

                  
                </div>
              </>
            )}
            <>
              <div className="flex space-x-4 mb-4">
                {(editUserMode || addUserMode) && (
                  <button
                    style={{ backgroundColor: '#000630', marginLeft: '30%' }}
                    className="hover:bg-green-700 text-white font-bold py-2 px-4 rounded text-sm flex items-center"
                  >
                    <IoMdCreate className="mr-1" />
                    <span>Make Changes</span>
                  </button>
                )}
                {viewUserMode && (
    <button
        onClick={handleResetPassword}
        style={{ backgroundColor: '#000630', marginLeft: '30%' }}
        className="hover:bg-green-700 text-white font-bold py-2 px-4 rounded text-sm flex items-center"
    >
        <IoMdCreate className="mr-1" />
        <span>Reset User Password</span>
    </button>
)}
                {(viewUserMode || editUserMode || addUserMode ) && (
                <button
                  onClick={() => cancel()}
                  style={{ backgroundColor: 'darkred', marginLeft: '2%' }}
                  className="hover:bg-green-700 text-white font-bold py-2 px-4 rounded text-sm flex items-center"
                >
                  <IoIosClose className="mr-1" />
                  <span>Cancel</span>
                </button>
                )}
              </div></>
          </div>
        </div>

      </div>
      <ToastContainer />
      <Load loading={loading} />
    </DashboardLayout>
  );
};

export default AccessControl;
