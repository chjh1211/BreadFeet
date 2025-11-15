import { useEffect, useState } from "react";
import { AuthContext } from "./AuthContext";
import axios from "axios";

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);

  useEffect(() => {
    // Check for user session with the backend
    const checkUserSession = async () => {
      try {
        // 백엔드에 현재 로그인된 사용자 정보를 요청합니다.
        // 세션 쿠키가 유효하다면 사용자 정보를 반환합니다.
        const response = await axios.get("/api/members/me");
        const userData = response.data;
        
        if (userData) {
          setUser(userData);
          localStorage.setItem("kakaoUser", JSON.stringify(userData));
        }
      } catch (error) {
        // 세션이 없거나 유효하지 않은 경우 에러가 발생합니다.
        console.log("User is not authenticated with the server.");
        localStorage.removeItem("kakaoUser");
        setUser(null);
      }
    };

    checkUserSession();
  }, []);

  const login = (profile) => {
    // 이 함수는 이제 수동으로 로그인 상태를 설정할 때 사용될 수 있습니다.
    setUser(profile);
    localStorage.setItem("kakaoUser", JSON.stringify(profile));
  };

  const logout = async () => {
    try {
      // 백엔드의 로그아웃 엔드포인트를 호출합니다.
      await axios.post("/logout"); 
    } catch (error) {
      console.error("Logout failed:", error);
    } finally {
      // 백엔드 요청 성공 여부와 관계없이 클라이언트 상태를 초기화합니다.
      setUser(null);
      localStorage.removeItem("kakaoUser");
      // 필요하다면 로그인 페이지로 리디렉션합니다.
      window.location.href = "/login";
    }
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
