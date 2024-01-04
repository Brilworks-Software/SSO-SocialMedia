import mongoose from 'mongoose';

const connectDB = async () => {
    try {
        const { MONGODB_URI } = process.env;

        if (!MONGODB_URI) {
            throw new Error("MongoDB URI is missing in the .env file");
        }

        await mongoose.connect(MONGODB_URI, { useNewUrlParser: true, useUnifiedTopology: true });

        console.log("Connected to MongoDB");
    } catch (error) {
        console.error("Error connecting to MongoDB:", error.message);
        process.exit(1);
    }
};

export default connectDB;
