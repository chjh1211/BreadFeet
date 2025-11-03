import "./Login.css";
import KakaoLoginWide from "../../assets/kakao_login_large_wide.png";
import KakaoLoginNarrow from "../../assets/kakao_login_large_narrow.png";
import KakaoLoginSort from "../../assets/kakao_login_medium_narrow.png";

const Login = () => {
  return (
    <div className="LoginBackGround">

      {/* 1. 왼쪽 베이지색 원 */}
      <div className="Shape Shape-1"></div>
      {/* 2. 오른쪽 상단 곡선 */}
      <div className="Shape Shape-2"></div>
      {/* 3. 오른쪽 하단 곡선 */}
      <div className="Shape Shape-3"></div>
      <div className="Shape Shape-4"></div>


      <div className="Wrapper">
        <div className="Logo">BreadFeet</div>
        <div className="Phrase">빵지순례를 위한 단 하나의 준비물</div>
      </div>
      <a href="#">
        <picture>
          <source srcSet={KakaoLoginSort} media="(max-width: 480px)" />{" "}
          {/* 480px 이하일때 sort ver로 변경 */}
          <source srcSet={KakaoLoginNarrow} media="(max-width: 1024px)" />{" "}
          {/* 1024px 이하일때 narrow ver로 변경 */}
          <img src={KakaoLoginWide} alt="카카오 로그인" />
          {/* 기본은 wide ver */}
        </picture>
      </a>
    </div>
  );
};

export default Login;
