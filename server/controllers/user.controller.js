import axios from "axios";

export const saveUserData = async (req,res) =>{
  const {tokenId , email ,name,imageUrl,googleId} = req.body
  res.sendStatus(200)
}
