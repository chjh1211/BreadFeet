import "./HomeRightLogin.css";
import MatchRateBar from "../../components/MatchRateBar";
import { useState, useEffect, useMemo } from "react";

const HomeRightLogin = () => {
  const [isLoggedIn, _setIsLoggedIn] = useState(true); // NoLogin 컴포넌트 확인용
  const [bakerys, setBakerys] = useState([]);
  const [userMatchRates, setUserMatchRates] = useState([]);
  const userId = 1001;

  useEffect(() => {
    fetch(`http://localhost:3001/bakery`)
      .then((res) => res.json())
      .then((data) => setBakerys(data))
      .catch(() => setBakerys([]));
  }, []);

  useEffect(() => {
    fetch(`http://localhost:3001/matchRates?userId=${userId}`)
      .then((res) => res.json())
      .then((data) => setUserMatchRates(data))
      .catch(() => setUserMatchRates([]));
  }, []);

  // userRate + bakery data
  const bakeryMap = useMemo(
    () => new Map(bakerys.map((b) => [String(b.id), b])),
    [bakerys]
  );

  const joined = useMemo(() => {
    const merged = userMatchRates.map((r) => {
      const b = bakeryMap.get(String(r.bakeryId));
      return {
        ...r,
        bakery: b || null,
        name: b?.name ?? "",
      };
    });

    return merged;
  }, [userMatchRates, bakeryMap]);

  return (
    <div className="HomeRightWrapper">
      <div className="HomeRightLogin">
        <h2 className="Title">이런 빵집은 어때요?</h2>
        <div className="MatchList">
          {joined.map((item) => (
            <MatchRateBar
              key={item.id}
              bakeryId={item.id}
              name={item.name}
              rate={item.matchScore}
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
