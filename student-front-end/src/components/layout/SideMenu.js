import React, { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import {
    IoMdAnalytics,
    IoMdChatboxes,
    IoIosRemove,
    IoIosFingerPrint,
    IoMdPeople,
    IoMdSettings,
    IoMdLogOut,
    IoIosQrScanner
} from "react-icons/io";
import { IoIosArrowDown, IoIosArrowForward } from "react-icons/io";
import logo from "../../assets/images/bgpic.jpg";
import Load from "./Load";
import store from "../../storage/store";
import Cookies from "js-cookie";

store.subscribe((state) => { console.log(state) });

const SideMenu = () => {
    const [open, setOpen] = useState(true);
    const [loading, setLoading] = useState(false);
    const [reportOpen, setReportOpen] = useState(false);
    const [settingsOpen, setSettingsOpen] = useState(false);
    const location = useLocation();
    const navigate = useNavigate();

    const gotoLoginPage = () => {
        Cookies.remove("sbu");
        Cookies.remove("token");
        Cookies.remove("name");
        navigate("/");
    };

    const BiometricMenus = [
        { title: "Dashboard", icon: IoMdAnalytics, path: "/dashboard" },
        { title: "Student Management", icon: IoMdPeople, path: "/students" },
        { title: "Biometric Enrolment", icon: IoIosFingerPrint, path: "/patient-biometrics" },
        { title: "Biometric Verification", icon: IoIosFingerPrint, path: "/student-verification" },
        { title: "Access Points", icon: IoIosQrScanner, path: "/access-points" },
        {
            title: "Reports",
            icon: IoMdChatboxes,
            expandable: true,
            children: [
                { title: "Enrolment Reports", icon: IoIosRemove, path: "/enrolment-stats" },
                { title: "Access Tracking", icon: IoIosRemove, path: "/student-biometric-access-tracking" },
                { title: "Campus Activity Log", icon: IoIosRemove, path: "/activity-log" },
            ],
        },
        {
            title: "Settings",
            icon: IoMdSettings,
            expandable: true,
            children: [
                { title: "User Management", icon: IoIosRemove, path: "/access-control" },
                { title: "Audit Trail", icon: IoIosRemove, path: "/audit-trail" },
            ],
        },
    ];

    const handleClick = () => {
        setLoading(true);
        setTimeout(() => setLoading(false), 1000);
    };

    const toggleMenu = (menuTitle) => {
        if (menuTitle === "Reports") setReportOpen(!reportOpen);
        if (menuTitle === "Settings") setSettingsOpen(!settingsOpen);
    };

    const handleSubmenuClick = (e, path) => {
        e.stopPropagation();
        handleClick();
        navigate(path);
    };

    return (
        <div
            className={`${open ? "w-72" : "w-20"} bg-[#000630] h-screen p-5 pt-8 relative duration-300`}
        >
            {/* Logo */}
            <div className="flex gap-x-4 items-center">
                <img
                    src={logo}
                    alt="logo"
                    className={`cursor-pointer duration-500 ${open && "rotate-[360deg]"}`}
                    style={{ width: "150px", height: "auto" }}
                />
            </div>

            <ul className="pt-6">
                {BiometricMenus.map((Menu, index) => (
                    <div key={index}>
                        {!Menu.children ? (
                            <li
                                className={`flex rounded-md p-2 cursor-pointer hover:bg-light-white text-gray-300 text-sm items-center gap-x-4 ${
                                    location.pathname === Menu.path ? "bg-light-white" : ""
                                }`}
                                onClick={handleClick}
                            >
                                <Menu.icon size={20} />
                                <Link to={Menu.path}>
                                    <span className={`${!open && "hidden"} origin-left duration-200`}>
                                        {Menu.title}
                                    </span>
                                </Link>
                            </li>
                        ) : (
                            <>
                                {/* Expandable Menu */}
                                <li
                                    className="flex justify-between rounded-md p-2 cursor-pointer hover:bg-light-white text-gray-300 text-sm items-center gap-x-4"
                                    onClick={() => toggleMenu(Menu.title)}
                                >
                                    <div className="flex items-center gap-x-4">
                                        <Menu.icon size={20} />
                                        <span className={`${!open && "hidden"} origin-left duration-200`}>
                                            {Menu.title}
                                        </span>
                                    </div>
                                    {open && (
                                        <span className="pr-2">
                                            {((Menu.title === "Reports" && reportOpen) ||
                                                (Menu.title === "Settings" && settingsOpen)) ? (
                                                <IoIosArrowDown size={16} />
                                            ) : (
                                                <IoIosArrowForward size={16} />
                                            )}
                                        </span>
                                    )}
                                </li>

                                {/* Submenus */}
                                {Menu.title === "Reports" && reportOpen && (
                                    <ul className="pl-8">
                                        {Menu.children.map((sub, subIndex) => (
                                            <li
                                                key={subIndex}
                                                className={`flex rounded-md p-2 cursor-pointer hover:bg-light-white text-gray-300 text-sm items-center gap-x-4 ${
                                                    location.pathname === sub.path ? "bg-light-white" : ""
                                                }`}
                                                onClick={(e) => handleSubmenuClick(e, sub.path)}
                                            >
                                                <sub.icon size={16} />
                                                <span className={`${!open && "hidden"} origin-left duration-200`}>
                                                    {sub.title}
                                                </span>
                                            </li>
                                        ))}
                                    </ul>
                                )}

                                {Menu.title === "Settings" && settingsOpen && (
                                    <ul className="pl-8">
                                        {Menu.children.map((sub, subIndex) => (
                                            <li
                                                key={subIndex}
                                                className={`flex rounded-md p-2 cursor-pointer hover:bg-light-white text-gray-300 text-sm items-center gap-x-4 ${
                                                    location.pathname === sub.path ? "bg-light-white" : ""
                                                }`}
                                                onClick={(e) => handleSubmenuClick(e, sub.path)}
                                            >
                                                <sub.icon size={16} />
                                                <span className={`${!open && "hidden"} origin-left duration-200`}>
                                                    {sub.title}
                                                </span>
                                            </li>
                                        ))}
                                    </ul>
                                )}
                            </>
                        )}
                    </div>
                ))}
            </ul>

            {/* Logout */}
            <button
                className="flex rounded-md p-2 cursor-pointer hover:bg-light-white text-gray-300 text-sm items-center gap-x-4 mt-4"
                onClick={gotoLoginPage}
            >
                <IoMdLogOut size={20} />
                <span className={`${!open && "hidden"} origin-left duration-200`}>Logout</span>
            </button>

            <Load loading={loading} />
        </div>
    );
};

export default SideMenu;