import { useEffect } from "react";
import { GoogleLogin } from "react-google-login";
import { useNavigate } from "react-router-dom";
import { gapi } from "gapi-script";
import axios from "axios";
import { useUser } from "../context/index";

const GoogleLoginForm = () => {
  const navigate = useNavigate();
  const { storeUserData } = useUser();

  useEffect(() => {
    gapi.load("auth2", () => {
      gapi.auth2.init({
        client_id: import.meta.env.VITE_GOOGLE_CLIENT_ID,
        scope: "https://www.googleapis.com/auth/userinfo.email",
      });
    });
  }, []);

  const responseGoogle = async (response) => {
    const { tokenId, profileObj } = response;
    const { email, name, imageUrl, googleId } = profileObj;
    console.log('response', response)
    try {
      const backendResponse = await axios.post(
        `${import.meta.env.VITE_API_URL}/v1/user/saveUserData`,
        {
          tokenId,
          email,
          name,
          imageUrl,
          googleId,
        }
      );
        console.log("User data saved successfully");
        storeUserData(profileObj);
    } catch (error) {
      console.error("Error saving user data:", error);
    }
    navigate("/");
  };

  return (
    <div>
      <GoogleLogin
        clientId={import.meta.env.VITE_GOOGLE_CLIENT_ID}
        buttonText="Login with Google"
        onSuccess={responseGoogle}
        onFailure={responseGoogle}
        cookiePolicy={"single_host_origin"}
      />
    </div>
  );
};

export default GoogleLoginForm;
