import { useUser } from "../context/index";

const Home = () => {
  const { userData } = useUser();

  return (
    <div className="bg-gray-600 h-[85vh] flex items-center justify-center">
      <div className="bg-white p-8 rounded shadow-md">
        <h2 className="text-2xl mb-4">
          Welcome to Your Dashboard, {userData && userData.name}!
        </h2>
        {userData ? (
          <div className="flex flex-col items-center">
            <img
              className="w-32 h-32 rounded-full mb-4"
              src={userData.imageUrl}
              alt="User"
            />
            <p className="text-lg mb-2">Name: {userData.name}</p>
            <p className="text-lg">Email: {userData.email}</p>
          </div>
        ) : (
          <p className="text-lg text-gray-500">
            Please log in to see your personalized content.
          </p>
        )}
      </div>
    </div>
  );
};

export default Home;