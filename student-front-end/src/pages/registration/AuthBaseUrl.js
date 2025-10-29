let sbuValue = '';
let fullname = '';

const setSbu = (sbu) => {
    sbuValue = sbu;
};

const getBaseUrl = () => {
        return 'http://localhost:6002/';

};

const getFullName = () => {
    return fullname;
};

const getSBU = () => {
    return sbuValue;
};

export { setSbu, getBaseUrl as default };