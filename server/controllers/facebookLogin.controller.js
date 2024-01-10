import dotenv from "dotenv";
dotenv.config();
import axios from "axios";
import passport from "passport";
import FacebookTokenStrategy from "passport-facebook-token";
import jwt from "jsonwebtoken";
import { User } from "../models/socialLogin.model.js";
import {
  FACEBOOK_AUTH_ERROR,
  FETCH_FACEBOOK_USER_ERROR,
  USER_SAVE_ERROR,
  INTERNAL_SERVER_ERROR,
  USER_ALREADY_EXISTS,
  TOKEN_GENERATION_ERROR,
} from "../utils/errors.js";

passport.use(
  new FacebookTokenStrategy(
    {
      clientID: process.env.FACEBOOK_CLIENT_ID,
      clientSecret: process.env.FACEBOOK_CLIENT_SECRET,
      proxy: true,
    },
    async (accessToken, refreshToken, profile, done) => {
      try {
        const user = {
          id: profile.id,
          email: profile.email,
          picture: `https://graph.facebook.com/${profile.id}/picture?type=large`,
          provider: "facebook",
        };

        return done(null, user);
      } catch (error) {
        console.error(FACEBOOK_AUTH_ERROR, error);
        return done(error, false);
      }
    }
  )
);

export const facebookLoginAuth = async (req, res, next) => {
  const { accessToken, provider } = req.body;
  try {
    const response = await axios.get(
      `https://graph.facebook.com/me?fields=id,name,email,picture&access_token=${accessToken}`
    );

    const pictureUrl = `https://graph.facebook.com/${response.data.id}/picture?type=large`;

    const { name, id, email } = response.data;

    const user = await User.findOne({ email });

    if (!user) {
      const newUser = new User({
        id: id,
        email,
        name,
        picture: pictureUrl,
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
      FETCH_FACEBOOK_USER_ERROR,
      error.response ? error.response.data : error.message
    );
    res.status(500).json({ error: FETCH_FACEBOOK_USER_ERROR });
  }
};

export const facebookLoginCallback = (req, res) => {
  try {
    const backendToken = jwt.sign(
      { userId: req.user.id },
      process.env.JWT_SECRET
    );
    res.json({ token: backendToken });
  } catch (error) {
    console.error(TOKEN_GENERATION_ERROR, error);
    res.status(500).json({ error: TOKEN_GENERATION_ERROR });
  }
};
