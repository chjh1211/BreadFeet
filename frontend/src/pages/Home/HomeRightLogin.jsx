import "./HomeRightLogin.css";
import MatchRateBar from "../../components/MatchRateBar";
import { useState } from "react";

const mockData = [
  { id: 1, name: "감자빵집", rate: 93 },
  { id: 2, name: "대구빵집", rate: 85 },
  { id: 3, name: "수성빵집", rate: 70 },
  { id: 4, name: "북구빵집", rate: 60 },
  { id: 5, name: "경북빵집", rate: 50 },
  { id: 6, name: "한국빵집", rate: 30 },
];

const HomeRightLogin = () => {
  const [isLoggedIn, _setIsLoggedIn] = useState(true); // NoLogin 컴포넌트 확인용

  return (
    <div className="HomeRightWrapper">
      <div className="HomeRightLogin">
        <h2 className="Title">이런 빵집은 어때요?</h2>
        <div className="MatchList">
          {mockData.map((item) => (
            <MatchRateBar
              key={item.id}
              bakeryId={item.id}
              name={item.name}
              rate={item.rate}
            />
          ))}
        </div>
      </div>
      {!isLoggedIn && (
        <div className="NoLoginOverLay">
          <h3>로그인하고 빵자취 AI가 추천하는 나만의 빵집을 찾아보세요</h3>
          <p>취향 매칭률과 추천 빵집 기능은 로그인 후 이용할 수 있어요.</p>
        </div>
      )}
    </div>
  );
};

export default HomeRightLogin;
