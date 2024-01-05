import { User } from "../models/googleLoginUser.model.js";
import { generateJwtToken } from "../config/generateToken.js";
import { OAuth2Client } from "google-auth-library";
import {
  DUPLICATE_USER,
  INTERNAL_ERROR,
  TOKEN_ID_REQUIRED,
  USERDATA_SAVED_SUCCESSFULLY,
} from "../utils/errors.js";

// Fetching client ID from environment variables
const clientId = process.env.CLIENT_ID;
console.log("clientId", clientId);

// Creating an instance of OAuth2Client with the provided client ID
const authClient = new OAuth2Client(clientId);

// Controller function to save user data from Google login
export const saveUserData = async (req, res) => {
  // Extracting tokenId from the request body
  const { tokenId } = req.body;

  try {
    // Checking if tokenId is present in the request
    if (!tokenId) {
      return res.status(400).json({ error: TOKEN_ID_REQUIRED });
    }

    // Verifying the Google ID token using the OAuth2Client
    const ticket = await authClient.verifyIdToken({
      idToken: tokenId,
      audience: clientId,
    });

    // Extracting user information from the token payload
    const { email, sub, name, picture } = ticket.getPayload();

    // Checking if the user already exists in the database
    const user = await User.findOne({ email });

    if (!user) {
      // Creating a new user if not found and saving to the database
      const newUser = new User({
        googleId: sub,
        email,
        userName: name,
        profilePicture: picture,
      });

      await newUser.save();

      // Generating JWT token for the new user
      const jwtToken = generateJwtToken(newUser);

      // Responding with user data and success message
      return res.status(200).json({
        user: newUser,
        jwtToken,
        message: USERDATA_SAVED_SUCCESSFULLY,
      });
    } else {
      // If user already exists, generate JWT token and respond with a user already exists message
      const jwtToken = generateJwtToken(user);
      return res.status(200).json({
        user,
        jwtToken,
        message: DUPLICATE_USER,
      });
    }
  } catch (error) {
    // Handling errors and responding with an internal server error message
    console.error("Error:", error.message);
    return res.status(500).json({ error: INTERNAL_ERROR });
  }
};
