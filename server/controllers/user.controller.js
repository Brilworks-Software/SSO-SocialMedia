import dotenv from 'dotenv';
import { User } from '../models/googleLoginUser.model.js';
import { OAuth2Client } from 'google-auth-library';
import jwt from 'jsonwebtoken';

dotenv.config();

const clientId = process.env.CLIENT_ID;
const authClient = new OAuth2Client(clientId);
const jwtSecret = process.env.JWT_SECRET;

export const saveUserData = async (req, res) => {
  const { tokenId } = req.body;

  try {
    if (!tokenId) {
      return res.status(400).json({ error: 'TokenId is required' });
    }

    const ticket = await authClient.verifyIdToken({
      idToken: tokenId,
      audience: clientId,
    });

    const payload = ticket.getPayload();
    const user = await User.findOne({ email: payload.email });

    if (!user) {
      const newUser = new User({
        googleId: payload.sub,
        email: payload.email,
        userName: payload.name,
        profilePicture: payload.picture,
      });

      await newUser.save();

      const token = jwt.sign(
        {
          userId: newUser._id,
          userName: newUser.userName,
          email: newUser.email,
        },
        jwtSecret,
        { expiresIn: '1h' }
      );

      return res.status(200).json({
        user: {
          _id: newUser._id,
          googleId: newUser.googleId,
          email: newUser.email,
          userName: newUser.userName,
          profilePicture: newUser.profilePicture,
        },
        token,
        message: 'User data saved successfully',
      });
    } else {
      const token = jwt.sign(
        {
          userId: user._id,
          userName: user.userName,
          email: user.email,
        },
        jwtSecret,
        { expiresIn: '1h' }
      );

      return res.status(200).json({
        user: {
          _id: user._id,
          googleId: user.googleId,
          email: user.email,
          userName: user.userName,
          profilePicture: user.profilePicture,
        },
        token,
        message: 'User already exists',
      });
    }
  } catch (error) {
    console.error('Error:', error);
    return res.status(500).json({ error: 'Internal Server Error' });
  }
};
