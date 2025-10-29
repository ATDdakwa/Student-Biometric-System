import React from "react";
import { Link, useLocation } from "react-router-dom";
import SideMenu from "./SideMenu";
import Header from "./Header";

const DashboardLayout = ({ children }) => {
  return (
    <div className="flex">
      <SideMenu />
      <div className="w-full h-full flex flex-col justify-start pt-0 px-0">
        <Header />
        <div className="text-2xl font-semibold text-black">
          {children}
        </div>
      </div>
    </div>
  );
};

export default DashboardLayout;