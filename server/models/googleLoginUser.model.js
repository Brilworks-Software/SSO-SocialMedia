import mongoose from "mongoose";

const googleLoginSchema = new mongoose.Schema(
  {
    googleId: {
      type: String,
      required: true,
    },
    userName: {
      type: String,
      required: true,
    },
    email: {
      type: String,
      required: true,
      unique: true,
    },
    profilePicture: {
      type: String,
      required: true,
    },
    isDeleted:{
      type:Boolean,
      default:false
    }
  },
  {
    timestamps: true,
  }
);

export const User = mongoose.model("User", googleLoginSchema);
