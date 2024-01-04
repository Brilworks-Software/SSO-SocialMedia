import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import GoogleLoginForm from './pages/GoogleLoginForm';
import Home from './pages/Home';
import { UserProvider, useUser } from './context/index';

const App = () => {
  return (
    <div className="w-screen h-screen bg-gray-600 flex flex-col">
      <UserProvider>
        <BrowserRouter>
          <Navbar />
          <div className="flex-1 p-4">
            <Routes>
              <Route
                path="/"
                element={<PrivateRoute component={<Home />} />}
              />
              <Route path="/login" element={<GoogleLoginForm />} />
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
