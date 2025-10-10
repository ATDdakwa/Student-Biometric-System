import React, { useState, useEffect } from "react";
import DashboardLayout from "../../components/layout/DashboardLayout";
import SearchBar from "../commons/SearchBar";
import TableExport from "../commons/TableExport";
import { IoMdAdd, IoMdCreate, IoMdTrash } from "react-icons/io";
import { toast, ToastContainer } from "react-toastify";
import Load from "../../components/layout/Load";
import "react-toastify/dist/ReactToastify.css";
import axios from "axios";
import getBaseUrl from "./BaseUrl";

const AccessPoints = () => {
    const [accessPoints, setAccessPoints] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [loading, setLoading] = useState(false);
    const [newPoint, setNewPoint] = useState("");
    const [editingId, setEditingId] = useState(null);
    const [editingName, setEditingName] = useState("");

    const BASE_URL = getBaseUrl(); // Base URL for API

    // Fetch all access points from API
    const fetchAccessPoints = async () => {
        setLoading(true);
        try {
            const response = await axios.get(`${BASE_URL}api/v1/access-points`);
            setAccessPoints(response.data);
        } catch (error) {
            toast.error("Failed to fetch access points");
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchAccessPoints();
    }, []);

    const handleSearch = (term) => {
        if (typeof term === "string") setSearchTerm(term);
    };

    const filteredAccessPoints = accessPoints.filter((point) =>
        point.name.toLowerCase().includes(searchTerm.toLowerCase())
    );

    // Add new access point
    const handleAdd = async () => {
        if (!newPoint.trim()) {
            toast.error("Access point name cannot be empty");
            return;
        }
        try {
            const response = await axios.post(`${BASE_URL}api/v1/access-points`, {
                name: newPoint.trim(),
                code: newPoint.trim().toUpperCase().replace(/\s+/g, "_"), // Example code generation
                active: true
            });
            setAccessPoints([...accessPoints, response.data]);
            setNewPoint("");
            toast.success("Access point added successfully");
        } catch (error) {
            toast.error("Failed to add access point");
            console.error(error);
        }
    };

    // Start editing
    const handleEditStart = (id, name) => {
        setEditingId(id);
        setEditingName(name);
    };

    // Save edited access point
    const handleEditSave = async (id) => {
        if (!editingName.trim()) {
            toast.error("Access point name cannot be empty");
            return;
        }
        try {
            const response = await axios.put(`${BASE_URL}api/v1/access-points/${id}`, {
                name: editingName.trim(),
                code: editingName.trim().toUpperCase().replace(/\s+/g, "_"),
                active: true
            });
            setAccessPoints(
                accessPoints.map((point) => (point.id === id ? response.data : point))
            );
            setEditingId(null);
            setEditingName("");
            toast.success("Access point updated successfully");
        } catch (error) {
            toast.error("Failed to update access point");
            console.error(error);
        }
    };

    // Delete access point
    const handleDelete = async (id) => {
        if (!window.confirm("Are you sure you want to delete this access point?")) return;
        try {
            await axios.delete(`${BASE_URL}api/v1/access-points/${id}`);
            setAccessPoints(accessPoints.filter((point) => point.id !== id));
            toast.info("Access point deleted");
        } catch (error) {
            toast.error("Failed to delete access point");
            console.error(error);
        }
    };

    return (
        <DashboardLayout>
            <div className="p-4" style={{ padding: "3rem" }}>
                <h1 className="text-2xl font-bold mb-6">Campus Access Points</h1>
                <Load loading={loading} />

                {/* Search + Export */}
                <div className="flex mb-4">
                    <div className="w-3/4">
                        <SearchBar searchTerm={searchTerm} handleSearch={handleSearch} />
                    </div>
                    <div className="w-1/4 flex items-center">
                        <TableExport data={filteredAccessPoints} filename="access_points" />
                    </div>
                </div>

                {/* Add new access point */}
                <div className="flex space-x-4 mb-6">
                    <input
                        type="text"
                        placeholder="Enter new access point name..."
                        value={newPoint}
                        onChange={(e) => setNewPoint(e.target.value)}
                        className="border border-gray-300 rounded px-3 py-2 w-1/2 focus:outline-none focus:ring-2 focus:ring-[#000630]"
                    />
                    <button
                        onClick={handleAdd}
                        style={{ backgroundColor: "#000630" }}
                        className="hover:bg-green-700 text-white font-bold py-2 px-4 rounded text-sm flex items-center"
                    >
                        <IoMdAdd className="mr-1" />
                        <span>Add Access Point</span>
                    </button>
                </div>

                {/* Table */}
                <div className="overflow-y-auto max-h-[55vh]">
                    <table className="min-w-full border border-gray-300 shadow-lg rounded-lg">
                        <thead className="bg-gray-300">
                        <tr>
                            <th className="py-3 px-4 text-left text-sm font-semibold border-b border-gray-400 w-16">#</th>
                            <th className="py-3 px-4 text-left text-sm font-semibold border-b border-gray-400">Access Point Name</th>
                            <th className="py-3 px-4 text-center text-sm font-semibold border-b border-gray-400">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {filteredAccessPoints.length === 0 ? (
                            <tr>
                                <td colSpan="3" className="text-center py-6 text-gray-500">
                                    No access points found
                                </td>
                            </tr>
                        ) : (
                            filteredAccessPoints.map((point, index) => (
                                <tr key={point.id} className="border-b border-gray-300 hover:bg-gray-100">
                                    <td className="py-3 px-4 text-sm">{index + 1}</td>
                                    <td className="py-3 px-4 text-sm">
                                        {editingId === point.id ? (
                                            <input
                                                type="text"
                                                value={editingName}
                                                onChange={(e) => setEditingName(e.target.value)}
                                                className="border border-gray-300 rounded px-2 py-1 w-2/3"
                                            />
                                        ) : (
                                            point.name
                                        )}
                                    </td>
                                    <td className="py-3 px-4 text-center text-sm">
                                        {editingId === point.id ? (
                                            <button
                                                onClick={() => handleEditSave(point.id)}
                                                className="text-green-600 hover:text-green-800 font-medium"
                                            >
                                                Save
                                            </button>
                                        ) : (
                                            <>
                                                <button
                                                    onClick={() => handleEditStart(point.id, point.name)}
                                                    className="text-blue-600 hover:text-blue-800 mr-3"
                                                    title="Edit"
                                                >
                                                    <IoMdCreate size={18} />
                                                </button>
                                                <button
                                                    onClick={() => handleDelete(point.id)}
                                                    className="text-red-600 hover:text-red-800"
                                                    title="Delete"
                                                >
                                                    <IoMdTrash size={18} />
                                                </button>
                                            </>
                                        )}
                                    </td>
                                </tr>
                            ))
                        )}
                        </tbody>
                    </table>
                </div>
            </div>
            <ToastContainer />
            <Load loading={loading} />
        </DashboardLayout>
    );
};

export default AccessPoints;
