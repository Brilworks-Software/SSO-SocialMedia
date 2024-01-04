import { useEffect } from "react";
import { GoogleLogin } from "react-google-login";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { useUser } from "../context/index";
import { toast, ToastContainer } from "react-toastify";
import { gapi } from "gapi-script";

const GoogleLoginForm = () => {
  const { storeUserData } = useUser();
  const navigate = useNavigate();

  useEffect(() => {
    gapi.load("auth2", () => {
      gapi.auth2.init({
        client_id: import.meta.env.VITE_GOOGLE_CLIENT_ID,
        scope: import.meta.env.VITE_GOOGLE_API_USERINFO,
      });
    });
  }, []);

  const handleToast = (message, type = "info") => {
    toast[type](message, {
      position: "top-right",
      autoClose: 3000,
      hideProgressBar: true,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
    });
  };

  const responseGoogle = async (response) => {
    const { tokenId } = response;

    try {
      const { data } = await axios.post(
        `${import.meta.env.VITE_API_URL}/v1/user/saveUserData`,
        { tokenId }
      );

      const { user, message } = data;

      if (user) {
        storeUserData(user);
        handleToast(message, "success");
        navigate("/");
      } else {
        console.log("Existing user data:", data.user);
        handleToast(message);
      }
    } catch (error) {
      console.error("Error during login:", error);
      handleToast("Error during login. Please try again.", "error");
    }
  };

  return (
    <div>
      <GoogleLogin
        clientId={import.meta.env.VITE_GOOGLE_CLIENT_ID}
        buttonText="Login with Google"
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
