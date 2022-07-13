export const validateUsernameSpecialChars = str => !(/[^\w\-.]/g).test(str);

export const validatePwdLowerCase = pwd => (/[a-z]+/g).test(pwd);

export const validatePwdUpperCase = pwd => (/[A-Z]+/g).test(pwd);

export const validatePwdNumber = pwd => (/\d+/g).test(pwd);

export const validatePwdSpecial = pwd => (/[!@#$%^&*]+/g).test(pwd);

export default {
    validateUsernameSpecialChars,
    validatePwdLowerCase,
    validatePwdUpperCase,
    validatePwdNumber,
    validatePwdSpecial
};
