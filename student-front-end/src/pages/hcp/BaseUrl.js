let sbuValue = '';
let fullname = '';

const setSbu = (sbu) => {
    sbuValue = sbu;
};

const getBaseUrl = () => {
        // return 'http://10.71.64.40:6115/';
        return 'http://localhost:6003/';

};

const getFullName = () => {
    return fullname;
};

const getSBU = () => {
    return sbuValue;
};

export { setSbu, getBaseUrl as default };