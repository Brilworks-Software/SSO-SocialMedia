import express from "express";
import { linkedInLoginData } from "../controllers/linkedInLogin.controller.js";
import {
  facebookLoginAuth,
  facebookLoginCallback,
} from "../controllers/facebookLogin.controller.js";
import { saveUserData } from "../controllers/googleLogin.controller.js";

const userRouter = express.Router();

userRouter.post("/saveUserData", saveUserData);
userRouter.post("/facebookLoginData", facebookLoginAuth, facebookLoginCallback);
userRouter.post("/linkedInLoginData", linkedInLoginData);

export default userRouter;
