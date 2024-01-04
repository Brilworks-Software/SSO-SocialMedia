import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Navbar from "./components/Navbar";
import GoogleLoginForm from "./components/GoogleLoginForm";
import Home from './pages/Home';


const App = () => {
  return (
    <div className="w-screen h-screen bg-gray-600 flex flex-col">
      <BrowserRouter>
        <Navbar />
        <div className="flex-1 p-4">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<GoogleLoginForm />} />
          </Routes>
        </div>
      </BrowserRouter>
    </div>
  );
};

export default App;
