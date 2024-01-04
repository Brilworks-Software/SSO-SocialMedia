import express from "express";
import { saveUserData } from "../controllers/user.controller.js";

const userRouter = express.Router();

userRouter.post("/saveUserData", saveUserData);

export default userRouter;
