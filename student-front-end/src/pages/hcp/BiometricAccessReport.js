import React, { useEffect, useState } from "react";
import axios from "axios";
import DashboardLayout from "../../components/layout/DashboardLayout";
import Load from "../../components/layout/Load";
import getBaseUrl from "./BaseUrl";
import { ToastContainer, toast } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';

const BiometricAccessReport = () => {
    const BASE_URL = getBaseUrl();

    const [loading, setLoading] = useState(false);
    const [studentNumber, setStudentNumber] = useState("");
    const [fromDate, setFromDate] = useState("");
    const [toDate, setToDate] = useState("");
    const [events, setEvents] = useState([]);

    const fetchEvents = async () => {
        if (!fromDate || !toDate) {
            toast.error("Please select both from and to dates");
            return;
        }

        setLoading(true);
        try {
            const response = await axios.get(`${BASE_URL}api/v1/tracking/events`, {
                params: {
                    studentNumber: studentNumber || undefined,
                    from: new Date(fromDate).toISOString(),
                    to: new Date(toDate).toISOString(),
                },
            });
            setEvents(response.data);
            setLoading(false);
        } catch (error) {
            console.error("Error fetching events:", error);
            toast.error("Error fetching events");
            setLoading(false);
        }
    };

    return (
        <DashboardLayout>
            <div className="p-4" style={{ padding: '3rem' }}>
                <h1 className="text-2xl font-bold mb-6">Student Biometric Access Tracking Report</h1>
                <Load loading={loading} />

                {/* Filters */}
                <div className="flex gap-4 mb-6 items-end">
                    <div>
                        <label className="block text-sm font-medium">Student Number:</label>
                        <input
                            type="text"
                            placeholder="Enter student number"
                            value={studentNumber}
                            onChange={(e) => setStudentNumber(e.target.value)}
                            className="border rounded p-1"
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium">From:</label>
                        <input
                            type="datetime-local"
                            value={fromDate}
                            onChange={(e) => setFromDate(e.target.value)}
                            className="border rounded p-1"
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium">To:</label>
                        <input
                            type="datetime-local"
                            value={toDate}
                            onChange={(e) => setToDate(e.target.value)}
                            className="border rounded p-1"
                        />
                    </div>

                    <button
                        onClick={fetchEvents}
                        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                    >
                        Submit
                    </button>
                </div>

                {/* Events Table */}
                <div className="overflow-y-auto max-h-[50vh]">
                    <table className="min-w-full border border-gray-300 shadow-lg rounded-lg">
                        <thead className="bg-gray-300">
                        <tr>
                            <th className="py-3 px-4 text-left font-semibold border-b border-gray-400">Student Number</th>
                            <th className="py-3 px-4 text-left font-semibold border-b border-gray-400">Access Point</th>
                            <th className="py-3 px-4 text-left font-semibold border-b border-gray-400">Timestamp</th>
                            <th className="py-3 px-4 text-left font-semibold border-b border-gray-400">Direction</th>
                            <th className="py-3 px-4 text-left font-semibold border-b border-gray-400">Verified</th>
                            <th className="py-3 px-4 text-left font-semibold border-b border-gray-400">Reason</th>
                        </tr>
                        </thead>
                        <tbody>
                        {events.length === 0 ? (
                            <tr>
                                <td colSpan={6} className="py-4 px-4 text-center text-gray-500">No records found</td>
                            </tr>
                        ) : (
                            events.map((event) => (
                                <tr key={event.id} className="border-b border-gray-300 hover:bg-gray-100">
                                    <td className="py-3 px-4 text-sm">{event.student?.studentNumber}</td>
                                    <td className="py-3 px-4 text-sm">{event.accessPoint?.name}</td>
                                    <td className="py-3 px-4 text-sm">{new Date(event.timestamp).toLocaleString()}</td>
                                    <td className="py-3 px-4 text-sm">{event.direction}</td>
                                    <td className="py-3 px-4 text-sm">{event.verified ? "Yes" : "No"}</td>
                                    <td className="py-3 px-4 text-sm">{event.reason || "-"}</td>
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

export default BiometricAccessReport;
