import React from "react";
import { IoMdPerson } from "react-icons/io";
import User from "../../../src/assets/images/bgpic.jpg";
import Cookies from "js-cookie";
import  getName from "../../pages/hcp/BaseUrl";
import  getSBU from "../../pages/hcp/BaseUrl";

const Header = () => {
  const sbu = Cookies.get('sbu');
  const name = Cookies.get('name');
  let sbuName ="";


  // const name = getName();
  // const sbu = getSBU();

  return (
    <div className="flex items-center justify-between bg-black h-16 px-5" style={{
      backgroundImage: `url(${User})`,
      backgroundRepeat: 'no-repeat',
      backgroundSize: 'cover',
    }}>
      <div className="text-2xl font-semibold text-white">STUDENT BIOMETRIC SYSTEM</div>
      <div className="flex items-center text-white">
        <span>{name}</span>
        <IoMdPerson size={20} className="ml-2" />
      </div>
    </div>
  );
};

export default Header;