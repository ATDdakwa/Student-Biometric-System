let sbuValue = '';
let fullname = '';

const setSbu = (sbu) => {
    sbuValue = sbu;
};

const getLocalBaseUrl = () => {
         return 'http://localhost:8084/';
         
         

};

const getFullName = () => {
    return fullname;
};

const getSBU = () => {
    return sbuValue;
};

export { setSbu, getLocalBaseUrl as default };