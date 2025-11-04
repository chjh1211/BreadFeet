import "./App.css";
import { BrowserRouter, Routes, Route, useLocation } from "react-router-dom";

import Navbar from "./components/Navbar";
import Home from "./pages/Home/Home";
import Map from "./pages/Map/Map";
import Login from "./pages/Login/Login";
import Event from "./pages/Event/EventPage";
import BakeryDetail from "./components/BakeryDetail";
import EventDetail from "./pages/Event/EventDetail";

function AppShell() {
  // detail에서 navBar 안띄우려고 useLocation 사용(자식에서만 사용가능해서 함수 만들어줌)
  const { pathname } = useLocation();
  const hideNav = pathname.startsWith("/bakery/");

  return (
    <>
      {!hideNav && <Navbar />}

      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/map" element={<Map />} />
        <Route path="/login" element={<Login />} />
        <Route path="/event" element={<Event/>}/>
        <Route path="/event/:eventType/:eventId" element={<EventDetail />}/>
        <Route path="/bakery/:bakeryId" element={<BakeryDetail />} />
      </Routes>
    </>
  );
}

function App() {
  return (
    <BrowserRouter>
      <AppShell />
    </BrowserRouter>
  );
}

export default App;
