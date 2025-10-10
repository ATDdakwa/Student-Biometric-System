import React, { useState } from "react";
import axios from "axios";
import DashboardLayout from "../../components/layout/DashboardLayout";
import Load from "../../components/layout/Load";
import getBaseUrl from "./BaseUrl";
import { ToastContainer, toast } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';

const CampusActivityLog = () => {
    const BASE_URL = getBaseUrl();

    const [loading, setLoading] = useState(false);
    const [studentNumber, setStudentNumber] = useState("");
    const [accessPoint, setAccessPoint] = useState("");
    const [fromDate, setFromDate] = useState("");
    const [toDate, setToDate] = useState("");
    const [events, setEvents] = useState([]);

    const fetchActivity = async () => {
        if (!fromDate || !toDate) {
            toast.error("Please select both from and to dates");
            return;
        }

        setLoading(true);
        try {
            const response = await axios.get(`${BASE_URL}api/v1/tracking/events`, {
                params: {
                    studentNumber: studentNumber || undefined,
                    accessPointCode: accessPoint || undefined,
                    from: new Date(fromDate).toISOString(),
                    to: new Date(toDate).toISOString(),
                },
            });
            setEvents(response.data);
        } catch (error) {
            console.error("Error fetching activity:", error);
            toast.error("Failed to fetch campus activity");
        } finally {
            setLoading(false);
        }
    };

    const summary = () => {
        const totalEntries = events.filter(e => e.direction === "ENTRY").length;
        const totalExits = events.filter(e => e.direction === "EXIT").length;
        const verified = events.filter(e => e.verified).length;
        return { totalEntries, totalExits, verified };
    };

    const { totalEntries, totalExits, verified } = summary();

    return (
        <DashboardLayout>
            <div className="p-4" style={{ padding: '3rem' }}>
                <h1 className="text-2xl font-bold mb-6">Campus Activity Log</h1>
                <Load loading={loading} />

                {/* Filters */}
                <div className="flex gap-4 mb-6 items-end">
                    <div>
                        <label className="block text-sm font-medium">Student Number</label>
                        <input
                            type="text"
                            placeholder="Enter student number"
                            value={studentNumber}
                            onChange={(e) => setStudentNumber(e.target.value)}
                            className="border rounded p-1"
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium">Access Point</label>
                        <input
                            type="text"
                            placeholder="Enter access point"
                            value={accessPoint}
                            onChange={(e) => setAccessPoint(e.target.value)}
                            className="border rounded p-1"
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium">From</label>
                        <input
                            type="datetime-local"
                            value={fromDate}
                            onChange={(e) => setFromDate(e.target.value)}
                            className="border rounded p-1"
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium">To</label>
                        <input
                            type="datetime-local"
                            value={toDate}
                            onChange={(e) => setToDate(e.target.value)}
                            className="border rounded p-1"
                        />
                    </div>

                    <button
                        onClick={fetchActivity}
                        className="bg-blue-600 hover:bg-blue-800 text-white font-bold py-2 px-4 rounded"
                    >
                        Fetch Activity
                    </button>
                </div>

                {/* Summary */}
                <div className="flex gap-8 mb-6">
                    <div className="p-4 bg-gray-100 rounded shadow">
                        <p className="font-semibold">Total Entries: {totalEntries}</p>
                        <p className="font-semibold">Total Exits: {totalExits}</p>
                        <p className="font-semibold">Verified Attempts: {verified}</p>
                    </div>
                </div>

                {/* Activity Table */}
                <div className="overflow-y-auto max-h-[50vh]">
                    <table className="min-w-full border border-gray-300 shadow-lg rounded-lg">
                        <thead className="bg-gray-300">
                        <tr>
                            {["Student Number", "Access Point", "Timestamp", "Direction", "Verified", "Reason"].map((header) => (
                                <th key={header} className="py-3 px-4 text-left font-semibold border-b border-gray-400">{header}</th>
                            ))}
                        </tr>
                        </thead>
                        <tbody>
                        {events.length === 0 ? (
                            <tr>
                                <td colSpan={6} className="py-4 px-4 text-center text-gray-500">No activity records found</td>
                            </tr>
                        ) : (
                            events.map((e) => (
                                <tr
                                    key={e.id}
                                    className={`border-b border-gray-300 hover:bg-gray-100 ${
                                        e.direction === "ENTRY" ? "bg-green-50" : e.verified === false ? "bg-red-50" : ""
                                    }`}
                                >
                                    <td className="py-3 px-4">{e.student?.studentNumber}</td>
                                    <td className="py-3 px-4">{e.accessPoint?.name}</td>
                                    <td className="py-3 px-4">{new Date(e.timestamp).toLocaleString()}</td>
                                    <td className="py-3 px-4">{e.direction}</td>
                                    <td className="py-3 px-4">{e.verified ? "Yes" : "No"}</td>
                                    <td className="py-3 px-4">{e.reason || "-"}</td>
                                </tr>
                            ))
                        )}
                        </tbody>
                    </table>
                </div>
            </div>
            <ToastContainer />
        </DashboardLayout>
    );
};

export default CampusActivityLog;
