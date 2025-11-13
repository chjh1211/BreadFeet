import { useEffect, useState } from "react";
import { AuthContext } from "./AuthContext";

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const saved = localStorage.getItem("kakaoUser");
    if (saved) {
      setUser(JSON.parse(saved));
    }
  }, []);

  const login = (profile) => {
    setUser(profile);
    localStorage.setItem("kakaoUser", JSON.stringify(profile));
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem("kakaoUser");
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
