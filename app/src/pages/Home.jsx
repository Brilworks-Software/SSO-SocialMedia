import LogoutButton from "../components/Logout";
import { useUser } from "../context/index";

const Home = () => {
  const { userData } = useUser();

  return (
    <div className="bg-[#164E63] flex grow items-center justify-center">
      <div className="bg-white p-8 rounded shadow-md max-w-md w-full mx-4">
        <h2 className="text-3xl font-semibold text-gray-800 mb-4 text-center">
          Welcome to Your Profile, {userData?.name}!
        </h2>
        {userData && (
          <div className="flex flex-col items-center gap-5">
            <img
              className="w-32 h-32 rounded-full border-4 border-indigo-500"
              src={userData?.picture}
              alt="User"
            />
            <div className="text-center flex flex-col gap-1">
              <p className="text-lg text-gray-700">
                <span className="font-bold">Name:</span> {userData?.name}
              </p>
              <p className="text-lg  text-gray-700">
                <span className="font-bold">Email:</span> {userData?.email}
              </p>
            </div>
            <LogoutButton />
          </div>
        )}
      </div>
    </div>
  );
};

export default Home;
