import React, { useState } from "react";
import { Container, Row, Col, Card, ProgressBar } from "react-bootstrap";
import "./EventPage.css"; // hover 효과용 CSS


const EventPage = () => {
  const [activeSection, setActiveSection] = useState("main"); 

  // 클릭 시 해당 섹션만 보이게
  const handleSectionClick = (section) => {
    setActiveSection(section);
  };

  // 상단 뱃지 데이터
  const completedEvents = [
    { id: 1, title: "열정적인 붕어빵", img: "/img/badges/badge1.png" },
    { id: 2, title: "붕어빵 마스터", img: "/img/badges/badge2.png" },
  ];

  // 수행 중 이벤트
  const ongoingEvents = [
    { id: 1, title: "3회 연속 방문",img:"/img/badges/badge3.png", progress: 70 },
    { id: 2, title: "한달 내 10회 방문",img:"/img/badges/badge4.png", progress: 70 },
    { id: 3, title: "영상 리뷰 3회", img:"/img/badges/badge5.png",progress: 70 },
  ];

  // 추천 이벤트
  const recommendedEvents = [
    { id: 1, title: "대구 빵탐방 3회 방문", img:"/img/badges/badge6.png", desc: "지역 챌린지" },
    { id: 2, title: "전국 빵탐방 10곳 방문", img:"/img/badges/badge7.png",desc: "전국 챌린지" },
  ];

  return (
    <Container className="my-5">
      {activeSection === "main" && (
        <>
          {/* 1. 수행 완료 이벤트(수행도 100) */}
          <section className="mb-5">
            <div>
            <h3 className="text-start mb-5">획득한 PPANG STICKER</h3>
            <Row className="justify-content">
              {completedEvents.map((item) => (
                <Col
                  key={item.id}
                  xs={6}
                  md={4}
                  className="d-flex justify-content-center mb-3 hover-card"
                  onClick={() => handleSectionClick("completed")}
                >
                  <Card className="text-center" style={{ width: "14rem" }}>
                    <Card.Img variant="top" src={item.img} />
                    <Card.Body>
                      <Card.Title>{item.title}</Card.Title>
                    </Card.Body>
                  </Card>
                </Col>
              ))}
            </Row>
            </div>
          </section>

          {/* 2. 도전 중 이벤트(수행도 0초과) */}
          <section className="mb-5">
            <h3 className="text-start mb-5">PPANG~ing STICKER</h3>
            <Row className="justify-content">
              {ongoingEvents.map((item) => (
                <Col
                  key={item.id}
                  xs={6}
                  md={4}
                  className="d-flex justify-content-center mb-3 hover-card"
                  onClick={() => handleSectionClick("ongoing")}
                >
                  <Card className="text-center" style={{ width: "14rem" }}>
                    <Card.Img variant="top" src={item.img} />
                    <Card.Body>
                      <Card.Title>{item.title}</Card.Title>
                      <ProgressBar now={item.progress} label={`${item.progress}%`} />
                    </Card.Body>
                  </Card>
                </Col>
              ))}
            </Row>
          </section>

          {/* 3. 추천 이벤트 (수행도 0)*/}
          <section>
            <h3 className="text-start mb-5">추천 PPANG STICKER</h3>
            <Row className="justify-content">
              {recommendedEvents.map((item) => (
                <Col
                  key={item.id}
                  xs={6}
                  md={4}
                  className="d-flex justify-content-center mb-3 hover-card"
                  onClick={() => handleSectionClick("recommended")}
                >
                  <Card className="text-center" style={{ width: "14rem" }}>
                     <Card.Img variant="top" src={item.img} />
                    <Card.Body>
                      <Card.Title>{item.title}</Card.Title>
                      <Card.Text>{item.desc}</Card.Text>
                    </Card.Body>
                  </Card>
                </Col>
              ))}
            </Row>
          </section>
        </>
      )}

      {/* 🎯 세부 페이지 (각 섹션 클릭 시) */}
      {activeSection === "completed" && (
        <div className="text-center">
          <h3>획득한 PPANG STICKER</h3>
          <p>내가 획득한 모든 뱃지 목록을 보여줍니다.</p>
          <button className="btn btn-outline-secondary" onClick={() => setActiveSection("main")}>
            ← 돌아가기
          </button>
        </div>
      )}

      {activeSection === "ongoing" && (
        <div className="text-center">
          <h3>도전 중인 PPANG~ing STICKER</h3>
          <p>진행 중인 챌린지 수행도를 확인하세요.</p>
          <button className="btn btn-outline-secondary" onClick={() => setActiveSection("main")}>
            ← 돌아가기
          </button>
        </div>
      )}

      {activeSection === "recommended" && (
        <div className="text-center">
          <h3>추천 PPANG STICKER</h3>
          <p>지금 참여할 수 있는 이벤트 목록입니다.</p>
          <button className="btn btn-outline-secondary" onClick={() => setActiveSection("main")}>
            ← 돌아가기
          </button>
        </div>
      )}
    </Container>
  );
};

export default EventPage;
