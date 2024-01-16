import axios from "axios";

const postRequest = async (url, data) => {
  try {
    const response = await axios.post(url, data);
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const googleLoginApi = async (tokenId, provider) => {
  const url = `${import.meta.env.VITE_API_URL}/v1/user/google-login`;
  const data = { tokenId, provider };
  return postRequest(url, data);
};

export const facebookLoginApi = async (accessToken, provider) => {
  const url = `${import.meta.env.VITE_API_URL}/v1/user/facebook-login`;
  const data = { accessToken, provider };
  return postRequest(url, data);
};

export const linkedInLoginApi = async (accessToken, provider) => {
  const url = `${import.meta.env.VITE_API_URL}/v1/user/linkedIn-login`;
  const data = { accessToken, provider };
  return postRequest(url, data);
};
