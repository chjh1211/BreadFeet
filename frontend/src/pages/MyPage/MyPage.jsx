import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Image, Spinner, Alert } from 'react-bootstrap';
import './MyPage.css'; // 2. 스타일을 위한 CSS 파일
import EventCard from '../../components/EventCard';

const MyPage = () => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/mypage/profile', {
          credentials: 'include' 
        });
        if (!response.ok) {
          throw new Error('서버에서 데이터를 가져오는 데 실패했습니다.');
        }
        const result = await response.json();
        setData(result);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  if (loading) {
    return (
      <Container className="d-flex justify-content-center my-5">
        <Spinner animation="border" role="status">
          <span className="visually-hidden">Loading...</span>
        </Spinner>
      </Container>
    );
  }

  if (error) {
    return (
      <Container className="my-5">
        <Alert variant="danger">
          오류가 발생했습니다: {error}
        </Alert>
      </Container>
    );
  }

  if (!data) {
    return null;
  }

  const { profile, tasteAnalysis } = data;

  return (
    <Container className="my-5">
      <Row className='g-5'>

        {/* =================================
              1. 왼쪽 열 (프로필)
        ================================== */}
        <Col>
          <h2 className='mb-3'>{profile.nickname}님의 프로필</h2>
          <Card className="mycard-profile text-center p-3">
            <Card.Body>
                <Image className='card-image'
                src={profile.profileImage}
                roundedCircle 
                />
              <Card.Title className="mt-3 mb-1">{profile.nickname}</Card.Title>
              <Card.Subtitle className="mb-2 text-muted">
                대표칭호: <strong>{profile.title}</strong>
              </Card.Subtitle>
              <div className="profile-stats mb-3">
                <span>{profile.location}</span>
              </div>
              <Card.Text className="profile-bio">
                {profile.bio}
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>

    
        <Col md={7}>
          
            {/* 2. 획득 스티커 카드 */}
            <Col>
                <EventCard
                    subtitle = {`${profile.nickname}님께서 획득하신 스티커`}
                    type ="completed"
                    url="http://localhost:3001/completed" // JSON 서버에서 완료 스티커 API
                />            
            </Col>

            {/* 3. AI 빵 취향 분석*/}
            <Col>
              <Card className="mycard-frame mb-4">
                <Card.Body>
                  <Card.Title>AI 빵 취향 분석</Card.Title>
                  <Card.Text>
                    당신의 빵BTI는? <strong>{tasteAnalysis.breadBTI}</strong>
                  </Card.Text>
                  <hr />
                  <p className='mb-2'><strong>Top 3 선호 빵</strong></p>
                  <ul className='taste-list'>
                    {tasteAnalysis.top3.map((bread, index) => (
                      <li key={index}>{bread}</li>
                    ))}
                  </ul>
                </Card.Body>
              </Card>
            </Col>
        </Col>

      </Row>
    </Container>
  );
};

export default MyPage;
