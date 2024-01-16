import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { toast, ToastContainer } from "react-toastify";
import { gapi } from "gapi-script";
import { GoogleLogin } from "react-google-login";
import { FacebookProvider, LoginButton } from "react-facebook";
import { LoginSocialLinkedin } from "reactjs-social-login";
import {
  LinkedInLoginButton,
  GoogleLoginButton,
  FacebookLoginButton,
} from "react-social-login-buttons";
import { googleLoginApi, facebookLoginApi, linkedInLoginApi } from "../api.js";
import { useUser } from "../context/index";

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

const handleApiResponse = (response, apiFunction, successMessage, errorMessage) => {
  if (response?.error) {
    handleToast(`Error during ${apiFunction}. Please try again.`, "error");
    return false;
  }

  const { user, message, jwtToken } = response;

  if (user && jwtToken) {
    handleToast(successMessage, "success");
    return true;
  } else {
    handleToast(message || errorMessage, "error");
    return false;
  }
};

const handleGoogleResponse = async (response, storeUserData, navigate) => {
  const { idpId } = response.tokenObj || {};
  const { tokenId } = response;

  try {
    const apiResponse = await googleLoginApi(tokenId, idpId);
    if (handleApiResponse(apiResponse, "Google login", "Google login successful.", "Error during Google login. Please try again.")) {
      storeUserData(apiResponse.user);
      navigate("/");
    }
  } catch (error) {
    console.error("Google login error:", error.message);
    handleToast("Error during Google login. Please try again.", "error");
  }
};

const handleFacebookResponse = async (response, storeUserData, navigate) => {
  const { accessToken, graphDomain } = response.authResponse;

  try {
    const apiResponse = await facebookLoginApi(accessToken, graphDomain);
    if (handleApiResponse(apiResponse, "Facebook login", "Facebook login successful.", "Error during Facebook login. Please try again.")) {
      storeUserData(apiResponse.user);
      navigate("/");
    }
  } catch (error) {
    console.error("Facebook login error:", error.message);
    handleToast("Error during Facebook login. Please try again.", "error");
  }
};

const handleLinkedInResponse = async (response, storeUserData, navigate) => {
  const { provider } = response;
  const { access_token } = response.data;

  try {
    const apiResponse = await linkedInLoginApi(access_token, provider);
    if (handleApiResponse(apiResponse, "LinkedIn login", "LinkedIn login successful.", "Error during LinkedIn login. Please try again.")) {
      storeUserData(apiResponse.user);
      navigate("/");
    }
  } catch (error) {
    console.error("LinkedIn login error:", error.message);
    handleToast("Error during LinkedIn login. Please try again.", "error");
  }
};

const loadGapiAuth2 = () => {
  gapi.load("auth2", () => {
    gapi.auth2.init({
      client_id: import.meta.env.VITE_GOOGLE_CLIENT_ID,
      scope: import.meta.env.VITE_GOOGLE_API_USERINFO,
    });
  });
};

const SocialLoginForm = () => {
  const { storeUserData } = useUser();
  const navigate = useNavigate();

  useEffect(() => {
    loadGapiAuth2();
  }, []);

  return (
    <div className="h-full flex flex-col items-center justify-center bg-[#164E63]">
      <div className="w-full max-w-md p-6 bg-gray-300 rounded-md shadow-md flex flex-col gap-4 items-center">
        <GoogleLogin
          clientId={import.meta.env.VITE_GOOGLE_CLIENT_ID}
          onSuccess={(response) => handleGoogleResponse(response, storeUserData, navigate)}
          onFailure={(response) => handleGoogleResponse(response, storeUserData, navigate)}
          cookiePolicy={"single_host_origin"}
          className="w-full"
          render={(renderProps) => <GoogleLoginButton {...renderProps} />}
        />

        <FacebookProvider appId={import.meta.env.VITE_FACEBOOK_CLIENT_ID}>
          <LoginButton
            onError={(error) => console.error("Facebook login error:", error?.message)}
            onSuccess={(response) => handleFacebookResponse(response, storeUserData, navigate)}
            scope="email"
            className="w-full"
          >
            <FacebookLoginButton scope="email" />
          </LoginButton>
        </FacebookProvider>

        <LoginSocialLinkedin
          isOnlyGetToken
          client_id={import.meta.env.VITE_LINKEDIN_CLIENT_ID}
          client_secret={import.meta.env.VITE_LINKEDIN_SECRET_ID}
          redirect_uri={import.meta.env.VITE_LINKEDIN_REDIRECT_URL}
          scope="profile openid email w_member_social"
          onResolve={(response) => handleLinkedInResponse(response, storeUserData, navigate)}
          onReject={(err) => console.log(err)}
          className="w-full"
        >
          <LinkedInLoginButton />
        </LoginSocialLinkedin>

        <ToastContainer />
      </div>
    </div>
  );
};

export default SocialLoginForm;
