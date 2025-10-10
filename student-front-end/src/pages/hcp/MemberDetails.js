import React, { useEffect, useState } from "react";
import axios from "axios";
import Load from "../../components/layout/Load";
import getBaseUrl from "./BaseUrl";
import { useLocation } from "react-router-dom";
import DashboardLayout from "../../components/layout/DashboardLayout";

const MemberDetails = () => {
    const location = useLocation();
    const member = location.state?.member;
    const BASE_URL = getBaseUrl();
    const [beneficiaries, setBeneficiaries] = useState([]);


    // const fetchData = async (policyNumber) => {
    //     try {
    //         if (policyNumber) {
    //             const response = await axios.get(BASE_URL + "api/v1/beneficiary/get/all/policyNumber", {
    //                 params: {
    //                     "policy-number": policyNumber,
    //                 }
    //             });
    //             setBeneficiaries(response.data.responseBody);
    //         }
    //     } catch (error) {
    //         console.error("Error fetching beneficiaries:", error);
    //     }
    // };

    const fetchData = async (memberId) => {
        try {
            if (memberId) {
                const response = await axios.get(BASE_URL + "api/v1/beneficiary/get/all/membership", {
                    params: {
                        memberId: memberId,
                    }
                });
                setBeneficiaries(response.data.responseBody);
            }
        } catch (error) {
            console.error("Error fetching beneficiaries:", error);
        }
    };

    useEffect(() => {
        if (member.id) {
            // fetchData(member.policyNumber);
            fetchData(member.id);
        }
    }, []);

    return (
        <DashboardLayout>
            <div className="p-4">
                <h1 className="text-2xl font-bold mb-6">MEMBER DETAILS</h1>
                <div className="bg-gray-200 p-4">
                    <div className="flex">
                        <div className="w-1/2">
                            <div className="flex flex-col items-start">
                                <div className="mb-2 flex">
                                    <label className="text-lg w-40 flex-shrink-0">Fullname:</label>
                                    <span className="text-sm">{member.firstName} - {member.lastName}</span>
                                </div>
                                <div className="mb-2 flex">
                                    <label className="text-lg w-40 flex-shrink-0">Status:</label>
                                    <span className="text-sm">{member.maritalStatus}</span>
                                </div>
                                <div className="mb-2 flex">
                                    <label className="text-lg w-40 flex-shrink-0">Gender:</label>
                                    <span className="text-sm">{member.gender}</span>
                                </div>
                                <div className="mb-2 flex">
                                    <label className="text-lg w-40 flex-shrink-0">National Id.:</label>
                                    <span className="text-sm">{member.nationalId}</span>
                                </div>
                                <div className="mb-2 flex">
                                    <label className="text-lg w-40 flex-shrink-0">Date Of Birth:</label>
                                    <span className="text-sm">{member.dateOfBirth}</span>
                                </div>
                            </div>
                        </div>
                        <div className="w-1/2">
                            <div className="flex flex-col items-start">
                                <div className="mb-2 flex">
                                    <label className="text-lg w-40 flex-shrink-0">Phone Number:</label>
                                    <span className="text-sm">{member.msisdn}</span>
                                </div>
                                <div className="mb-2 flex">
                                    <label className="text-lg w-40 flex-shrink-0">Policy No.:</label>
                                    <span className="text-sm">{member.policyNumber}</span>
                                </div>
                                <div className="mb-2 flex">
                                    <label className="text-lg w-40 flex-shrink-0">Policy Type:</label>
                                    <span className="text-sm">{member.policyType}</span>
                                </div>
                                <div className="mb-2 flex">
                                    <label className="text-lg w-40 flex-shrink-0">Reference:</label>
                                    <span className="text-sm">{member.reference}</span>
                                </div>
                                <div className="mb-2 flex">
                                    <label className="text-lg w-40 flex-shrink-0">Join Date:</label>
                                    <span className="text-sm">{member.createdDate}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <h1 className="text-sm font-bold mb-3 mt-3">Member Beneficiaries :</h1>
                <div className="overflow-x-auto mt-3">
                    <table className="min-w-full border border-gray-300">
                        <thead className="bg-gray-200">
                            <tr>
                                <th className="py-2 px-4 text-sm  text-left">Fullame</th>
                                <th className="py-2 px-4 text-sm  text-left">Gender</th>
                                <th className="py-2 px-4 text-sm  text-left">DOB</th>
                                <th className="py-2 px-4 text-sm  text-left">National Id.</th>
                                <th className="py-2 px-4 text-sm  text-left">MSISDN</th>
                                <th className="py-2 px-4 text-sm  text-left">Policy No.</th>
                                <th className="py-2 px-4 text-sm  text-left">Category</th>
                                <th className="py-2 px-4 text-sm  text-left">Relationship</th>
                                <th className="py-2 px-4 text-sm  text-left">Is Active</th>
                            </tr>
                        </thead>
                        <tbody>
                            {beneficiaries.map((item) => (
                                <tr key={item.beneficiaryId} className="border-b border-gray-300">
                                    <td className="py-2 px-4 text-sm">{item.title}  {item.firstName}   {item.lastName}</td>
                                    <td className="py-2 px-4 text-sm">{item.gender}</td>
                                    <td className="py-2 px-4 text-sm">{item.dateOfBirth}</td>
                                    <td className="py-2 px-4 text-sm">{item.nationalId}</td>
                                    <td className="py-2 px-4 text-sm">{item.msisdn}</td>
                                    <td className="py-2 px-4 text-sm">{item.policyNumber}</td>
                                    <td className="py-2 px-4 text-sm">{item.beneficiaryCategory}</td>
                                    <td className="py-2 px-4 text-sm">{item.relationship}</td>
                                    <td className="py-2 px-4 text-sm">{item.isActive ? "Yes" : "No"}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </DashboardLayout>
    );
};

export default MemberDetails;