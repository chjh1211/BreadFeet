import React, { useEffect, useState } from 'react';
import { Carousel } from 'react-bootstrap'; // 1. Carousel을 import 합니다.
import "./TopBakery.css";
import bakeryImage from "../../assets/bakery1.jpg"; 

const TopbakeryList = () => {
  const [bakeries, setBakeries] = useState([]); 

  useEffect(() => {
    fetch(`http://localhost:3001/bakery?_sort=reviewCount&_order=desc&_limit=3`)
      .then((res) => res.json()) 
      .then((data) => setBakeries(data)) 
      .catch((err) => console.error("❌ Top 빵집 로딩 실패:", err));
  }, []);

  return (
    <div className="TopBakeryList">
      <h2 className="TopBakeryTitle">이번 주 Top 3 빵집</h2>
      <h3 className="TopBakeryPhrase">
        현재 시각기준 가장 방문자가 많은 빵집이에요!
      </h3>

      {/* 2. [수정] <div className="TopBakeryContainer"> 대신 <Carousel> 사용 */}
      <Carousel 
        className="top-bakery-carousel"
        indicators={false} /* 아래쪽 ... 인디케이터 숨김 */
        interval={3000}    /* 3초마다 자동으로 넘어감 (null로 하면 자동 넘김 끔) */
      >
        {bakeries.length > 0 ? (
          bakeries.map((bakery) => (
            // 3. [수정] <div className="TopBakery"> 대신 <Carousel.Item> 사용
            <Carousel.Item key={bakery.id}>
              {/* 4. 슬라이드 이미지 (bootstrap 클래스 d-block w-100 추가) */}
              <img
                className="d-block w-100"
                src={bakeryImage} // db.json에 이미지가 있다면 bakery.image
                alt={bakery.name}
              />
              {/* 5. 슬라이드 캡션 (이미지 위에 텍스트 표시) */}
              <Carousel.Caption className="top-bakery-caption">
                <h4>{bakery.name}</h4>
                <h5>{bakery.address}</h5>
              </Carousel.Caption>
            </Carousel.Item>
          ))
        ) : (
          <Carousel.Item>
            {/* 로딩 중일 때 표시할 기본 슬라이드 */}
            <img
              className="d-block w-100"
              src={bakeryImage}
              alt="Loading"
            />
            <Carousel.Caption className="top-bakery-caption">
              <h4>Top 빵집을 불러오는 중입니다...</h4>
            </Carousel.Caption>
          </Carousel.Item>
        )}
      </Carousel>
    </div>
  );
};

export default TopbakeryList;