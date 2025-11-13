import "./Navbar.css";
import { NavLink } from "react-router-dom";

const Navbar = () => {
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
          <li>
            <NavLink to="/Login">LOGIN</NavLink>
          </li>
        </ul>
      </nav>
    </header>
  );
};

export default Navbar;
