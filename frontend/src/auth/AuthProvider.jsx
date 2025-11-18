import { useEffect, useState } from "react";
import { AuthContext } from "./AuthContext";
import Cookies from "js-cookie";
import { jwtDecode } from "jwt-decode";

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);

  useEffect(() => {
    // On component mount, check for the JWT in the cookies
    const token = Cookies.get("breadfeet-token"); // 쿠키 이름을 'breadfeet-token'으로 변경
    console.log("Found token:", token);

    if (token) {
      try {
        // Decode the token to get user information
        const decodedUser = jwtDecode(token);
        console.log("Decoded user from token:", decodedUser);

        // Check if the token is expired
        const currentTime = Date.now() / 1000; // Current time in seconds
        if (decodedUser.exp < currentTime) {
          console.warn("Token is expired. Logging out.");
          setUser(null);
          Cookies.remove("breadfeet-token"); // Remove expired token
        } else {
          setUser(decodedUser);
        }
      } catch (error) {
        console.error("Failed to decode token or token is invalid:", error);
        setUser(null);
        Cookies.remove("breadfeet-token"); // Remove invalid token
      }
    } else {
      console.log("No breadfeet-token found in cookies.");
    }
  }, []);

  // This function can be used if you manually need to set the user
  // For example, after a login API call that returns the user profile
  const login = (profile) => {
    setUser(profile);
  };

  const logout = () => {
    // Remove the token from cookies
    Cookies.remove("breadfeet-token"); // 쿠키 이름을 'breadfeet-token'으로 변경
    // Clear the user state
    setUser(null);
    // Redirect to the login page to ensure a clean state
    window.location.href = "/login";
    console.log("Logged out and removed breadfeet-token.");
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        isLoggedIn: !!user,
        login,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}
