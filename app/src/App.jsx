import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Navbar from "./components/Navbar";
import Home from "./pages/Home";
import { UserProvider, useUser } from "./context/index";
import SocialLoginForm from "./pages/SocialLoginForm";

const App = () => {
  return (
    <div className="w-full h-screen flex flex-col">
      <UserProvider>
        <BrowserRouter>
          <Navbar />
          <div className="grow flex flex-col p-4">
            <Routes>
              <Route path="/" element={<PrivateRoute component={<Home />} />} />
              <Route path="/login" element={<SocialLoginForm />} />
            </Routes>
          </div>
        </BrowserRouter>
      </UserProvider>
    </div>
  );
};

const PrivateRoute = ({ component }) => {
  const { userData } = useUser();
  if (!userData) {
    return <Navigate to="/login" />;
  }
  return component;
};

export default App;
