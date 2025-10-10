import React, { useState } from "react";

const ProjectImages = () => {
  const [images, setImages] = useState([]);

  const handleAddImage = (event) => {
    event.preventDefault();
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setImages((prevImages) => [...prevImages, reader.result]);
      };
      reader.readAsDataURL(file);
    }
  };

  return (
    <div>
      <input
        id="image-upload"
        type="file"
        accept="image/*"
        onChange={handleAddImage}
      />
      <br></br>
      <div className="pt-2 ">
      <button
        htmlFor="image-upload"
        className="mb-2"
        style={{
            color: "green",
            backgroundColor: "white",
            border: "1px solid green",
            borderRadius: "10px",
            padding: "0.5rem 1rem",
        }}
        onClick={() => {
            document.getElementById("image-upload").click();
        }}
        >
        + Add Project
        </button>
        </div>
      <div className="mt-4 flex">
        {images.map((image, index) => (
          <img
            key={index}
            src={image}
            alt={`Project Image ${index + 1}`}
            className="mr-2 h-24"
          />
        ))}
      </div>
    </div>
  );
};

export default ProjectImages;