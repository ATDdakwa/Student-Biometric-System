// BackButton.js
import React from 'react';
import { useNavigate } from 'react-router-dom';

const BackButton = ({ label = "Back" }) => {
    const navigate = useNavigate();

    const handleBack = () => {
        navigate(-1); // Navigate back to the previous page
    };

    return (
        <button
            onClick={handleBack}
            className="mt-4 bg-customGreen text-white py-2 px-4 rounded absolute bottom-4 right-4"
        >
            {label}
        </button>
    );
};

export default BackButton;