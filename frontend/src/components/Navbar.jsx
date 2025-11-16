import "./Navbar.css";
import { NavLink } from "react-router-dom";
import { useContext } from "react";
import { AuthContext } from "../auth/AuthContext";

const Navbar = () => {
  const { isLoggedIn, logout } = useContext(AuthContext);

  return (
    <header className="Navbar">
      <nav>
        <ul className="NavMenu">
          <li>
            <NavLink to="/">HOME</NavLink>
          </li>
          <li>
            <NavLink to="/Map">MAP</NavLink>
          </li>
          <li>
            <NavLink to="/Event">EVENT</NavLink>
          </li>
          {isLoggedIn ? (
            <>
              <li>
                <NavLink to="/mypage">MYPAGE</NavLink>
              </li>
              <li>
                <button onClick={logout} className="logout-button">
                  LOGOUT
                </button>
              </li>
            </>
          ) : (
            <li>
              <NavLink to="/Login">LOGIN</NavLink>
            </li>
          )}
        </ul>
      </nav>
    </header>
  );
};

export default Navbar;
