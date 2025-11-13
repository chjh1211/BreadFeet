import React from 'react';
import { Container, Row, Col, Card, Image, Dropdown } from 'react-bootstrap';
import './MyPage.css'; // 2. 스타일을 위한 CSS 파일
import EventCard from '../../components/EventCard';

// 1. 프로필 사진, 리뷰 이미지 등 (실제 경로로 변경 필요)
//import profilePic from '../../assets/profile-pic.png'; // 랄프 이미지
//import breadImg from '../../assets/bakery1.jpg'; // 리뷰용 빵 이미지

import profilePic from '/img/profile/profile.png'

const MyPage = () => {
  return (
    <Container className="my-5">
      <Row className='g-5'>

        {/* =================================
              1. 왼쪽 열 (프로필 + 피드)
        ================================== */}
        <Col>
          {/* 1.1 프로필 카드 */}
          <h2 className='mb-3'>감자님의 프로필</h2>
          <Card className="mycard-profile text-center p-3">
            <Card.Body>
                <Image className='card-image'
                src={profilePic} 
                roundedCircle 
                
                />
              <Card.Title className="mt-3 mb-1">감자돌이</Card.Title>
              <Card.Subtitle className="mb-2 text-muted">
                대표칭호: <strong>대청호</strong>
              </Card.Subtitle>
              <div className="profile-stats mb-3">
                <span>지역</span>
              </div>
              <Card.Text className="profile-bio">
                대구 빵순이. 빵지순례 3년차. 소금빵만 먹는 사람. 후기 100회 이상 작성
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>

    
        <Col md={7}>
          
            {/* 2. 획득 스티커 카드 */}
            <Col>
                <EventCard
                    subtitle = "감자님께서 획득하신 스티커"
                    type ="completed"
                    url="http://localhost:3001/completed" // JSON 서버에서 완료 스티커 API
                />            
            </Col>

            {/* 3. AI 빵 취향 분석*/}
            <Col>
              <Card className="mycard-frame mb-4">
                <Card.Body>
                  <Card.Title>AI 빵 취향 분석</Card.Title>
                  <Card.Text>당신의 빵bti는?</Card.Text>
                  {/* (간략한 구현을 위해 차트 자리 표시) */}
                  <div className="chart-placeholder">
                    [차트 영역]
                  </div>
                </Card.Body>
              </Card>
            </Col>
          

          {/* 2.3 AI 취향 분석 */}
          <Row>
            <Col>
              <Card className="mycard-frame mb-4">
                <Card.Body>
                  <Card.Title>AI 취향 분석</Card.Title>
                  <Card.Text>당신을 위한 AI 취향 분석</Card.Text>
                  {/* (간략한 구현을 위해 차트 자리 표시) */}
                  <div className="chart-placeholder">
                    [차트 영역]
                  </div>
                </Card.Body>
              </Card>
            </Col>
          </Row>
        </Col>

      </Row>
    </Container>
  );
};

export default MyPage;