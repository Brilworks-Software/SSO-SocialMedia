import { Link } from "react-router-dom";

const Navbar = () => {
  return (
    <>
      <div className="bg-gray-100">
        <nav className="flex items-center justify-between p-4">
          <Link to="/" className="text-blue-600 hover:underline">Home</Link>
          <Link to="/login" className="text-blue-600 hover:underline">GoogleLogin</Link>
        </nav>
      </div>
    </>
  );
};

export default Navbar;
