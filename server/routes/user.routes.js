import express from "express";
import { linkedInLoginAuth } from "../controllers/linkedInLogin.controller.js";
import { facebookLoginAuth } from "../controllers/facebookLogin.controller.js";
import { googleLoginAuth } from "../controllers/googleLogin.controller.js";

const userRouter = express.Router();

userRouter.post("/google-login", googleLoginAuth);
userRouter.post("/facebook-login", facebookLoginAuth);
userRouter.post("/linkedIn-login", linkedInLoginAuth);

export default userRouter;
