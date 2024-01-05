import { useEffect } from "react";
import { GoogleLogin } from "react-google-login";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { useUser } from "../context/index";
import { toast, ToastContainer } from "react-toastify";
import { gapi } from "gapi-script";

const GoogleLoginForm = () => {
  // Accessing context for user data management
  const { storeUserData } = useUser();
  const navigate = useNavigate();

  // Initializing Google API on component mount
  useEffect(() => {
    gapi.load("auth2", () => {
      gapi.auth2.init({
        client_id: import.meta.env.VITE_GOOGLE_CLIENT_ID,
        scope: import.meta.env.VITE_GOOGLE_API_USERINFO,
      });
    });
  }, []);

  // Helper function to display toasts
  const handleToast = (message, type = "info") => {
    toast[type](message, {
      position: "top",
      autoClose: 3000,
      hideProgressBar: true,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
    });
  };

  // Callback function for GoogleLogin component
  const responseGoogle = async (response) => {
    if (response.error) {
      // Handling Google login errors
      handleToast("Error during Google login. Please try again.", "error");
      return;
    }

    const { tokenId } = response;

    try {
      // Making API request to save user data on the server
      const { data } = await axios.post(
        `${import.meta.env.VITE_API_URL}/v1/user/saveUserData`,
        { tokenId }
      );

      const { user, message, jwtToken } = data;
      if (user && jwtToken) {
        // Storing user data in context and navigating to the main page
        storeUserData(user);
        handleToast(message, "success");
        navigate("/");
      } else {
        // Handling cases where user or token is missing
        handleToast(message);
      }
    } catch (error) {
      // Handling general login errors
      handleToast("Error during login. Please try again.", "error");
    }
  };

  return (
    <div>
      <GoogleLogin
        clientId={import.meta.env.VITE_GOOGLE_CLIENT_ID}
        onSuccess={responseGoogle}
        onFailure={responseGoogle}
        cookiePolicy={"single_host_origin"}
        isSignedIn={true}
      />
      <ToastContainer />
    </div>
  );
};

export default GoogleLoginForm;
