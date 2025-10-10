import React, { ReactNode, useState, useEffect } from "react";
import authImage from "../../assets/images//bgpic.jpg";
import authImage2 from "../../assets/images/fml-health.jpg";
import fmlLogo from "../../assets/images/first_mutual_logo.png";
import PropTypes from "prop-types";

const AuthLayout = ({
  children,
  showTabPanels,
  title,
  subTitle,
  tabs,
  activeTab,
  setActiveTab,
}) => {
  const [currentAuthImage, setCurrentAuthImage] = useState(authImage);
  const [isFirstImage, setIsFirstImage] = useState(true);

  useEffect(() => {
    const interval = setInterval(() => {
      setIsFirstImage((prevIsFirstImage) => !prevIsFirstImage);
    }, 5000);

    return () => clearInterval(interval);
  }, []);

  useEffect(() => {
    setCurrentAuthImage(isFirstImage ? authImage : authImage);
  }, [isFirstImage]);

  return (
    <div
      className="min-h-screen flex justify-center items-center"
      style={{
        backgroundImage: `url(${currentAuthImage})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
        backgroundRepeat: "no-repeat",
        position: "relative",
      }}
    >
      <div className="absolute inset-0 bg-black opacity-10"></div>
      <div className="max-w-screen-xl m-0 sm:m-10 bg-transparent sm:rounded-lg flex justify-center">
        <div className="flex flex-col relative z-10">
  
          {showTabPanels ? (
            <ul className="flex justify-start items-start my-2 mb-6 gap-4">
              {tabs.map((tab, index) => (
                <li
                  key={index}
                  onClick={() => setActiveTab(index)}
                  className={`cursor-pointer py-1 ${
                    activeTab === index
                      ? "border-b-4 border-green font-bold"
                      : "border-b-2 border-transparent"
                  }`}
                  style={{ backgroundColor: "white", padding: "10px", borderRadius: "5px" }}
                >
                  {tab}
                </li>
              ))}
            </ul>
          ) : (
            ""
          )}

          <div className="flex flex-col flex-grow" style={{ backgroundColor: "white", padding: "20px", borderRadius: "10px",width:'80rem',height:'45rem' }}>
          <div  className="flex justify-center items-center">
            <img src={fmlLogo} className="mx-auto ml-0" alt="" style={{ width: '200px', height: 'auto' }}/>
          </div>
            <div className="max-w-sm break-words text-black">
              <h1 className="text-2xl xl:text-3xl font-bold">{title}</h1>
              <h1 className="text-md xl:text-lg mt-2">{subTitle}</h1>
            </div>
            <div className="flex-1 mt-8">{children}</div>
          </div>
        </div>
      </div>
    </div>
  );
};

AuthLayout.propTypes = {
  title: PropTypes.string,
  subTitle: PropTypes.string,
  tabs: PropTypes.arrayOf(PropTypes.string),
  activeTab: PropTypes.number,
  setActiveTab: PropTypes.func,
  children: ReactNode,
  showTabPanels: PropTypes.bool.isRequired,
};

export default AuthLayout;