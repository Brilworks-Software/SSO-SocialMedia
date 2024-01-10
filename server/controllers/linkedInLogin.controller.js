import dotenv from "dotenv";
dotenv.config();
import axios from "axios";
import passport from "passport";
import { Strategy as LinkedInStrategy } from "passport-linkedin-oauth2";
import { User } from "../models/socialLogin.model.js";
import {
  LINKEDIN_AUTH_ERROR,
  FETCH_LINKEDIN_USER_ERROR,
  LINKEDIN_LOGIN_DATA_ERROR,
  ACCESS_TOKEN_REQUIRED_ERROR,
  USER_ALREADY_EXISTS,
} from "../utils/errors.js";

passport.use(
  new LinkedInStrategy(
    {
      clientID: process.env.LINKEDIN_CLIENT_ID,
      clientSecret: process.env.LINKEDIN_CLIENT_SECRET,
      callbackURL: process.env.LINKEDIN_CALLBACK_URL,
      scope: ["r_liteprofile", "r_emailaddress", "w_member_social"],
      state: true,
      passReqToCallback: true,
    },
    async (accessToken, refreshToken, profile, done) => {
      try {
        const user = {
          id: profile.id,
          email: profile.emails ? profile.emails[0].value : null,
          picture: profile.photos ? profile.photos[0].value : null,
        };
        return done(null, user);
      } catch (error) {
        console.error(LINKEDIN_AUTH_ERROR, error);
        return done(error, false);
      }
    }
  )
);

export const linkedInLoginData = async (req, res) => {
  const { accessToken, provider } = req.body;
  if (!accessToken) {
    return res.status(400).json({ error: ACCESS_TOKEN_REQUIRED_ERROR });
  }

  try {
    const response = await axios.get("https://api.linkedin.com/v2/userinfo", {
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
    });
    const { name, sub, email, picture } = response.data;

    const user = await User.findOne({ email });

    if (!user) {
      const newUser = new User({
        id: sub,
        email,
        name,
        picture,
        socialAccounts: [provider],
      });
      await newUser.save();
    } else {
      if (!user.socialAccounts.includes(provider)) {
        user.socialAccounts.push(provider);
        await user.save();
      }
      return res.status(200).json({
        user,
        message: USER_ALREADY_EXISTS,
      });
    }
    res.status(200).json(user);
  } catch (error) {
    console.error(
      FETCH_LINKEDIN_USER_ERROR,
      error.response ? error.response.data : error.message
    );
    res.status(500).json({ error: FETCH_LINKEDIN_USER_ERROR });
  }
};
