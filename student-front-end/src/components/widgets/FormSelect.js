import React from 'react';
import Select from 'react-select';

const FormSelect = ({ id, options, value, onChange }) => {
  return (
    <div className="mb-5">
      <label htmlFor={id} className="block text-sm font-medium text-gray-700">
        Select Option
      </label>
      <Select
        id={id}
        options={options}
        value={value}
        onChange={onChange}
      />
    </div>
  );
};

export default FormSelect;