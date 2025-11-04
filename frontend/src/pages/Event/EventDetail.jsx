import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom'; 
import axios from 'axios';
// 1. Row와 Col을 import 목록에 추가합니다.
import { Container, Card, Row, Col } from 'react-bootstrap'; 

const EventDetail = () => {
  const { eventId, eventType } = useParams(); 
  
  const [badge, setBadge] = useState(null); 

  useEffect(() => {
    const getBadgeDetail = async () => {
      try {
        const res = await axios.get(`http://localhost:3001/${eventType}/${eventId}`); 
        setBadge(res.data);
      } catch (err) {
        console.error("❌ 상세 데이터를 불러오지 못했습니다:", err);
      }
    };
    
    getBadgeDetail();
  }, [eventId, eventType]); // (eventType도 의존성에 추가하는 것이 좋습니다)

  // 로딩 중 표시
  if (!badge) {
    return <Container>로딩 중...</Container>;
  }

  // 4. 받아온 데이터로 상세 페이지 렌더링
  return (
    <div className='m-5'>
    <Row>
        {/* 1. 첫 번째 카드 (뱃지 정보) */}
        <Col md={6} className="mb-4"> {/* mb-4를 Col로 옮겨서 모바일에서 간격을 줍니다. */}
            <Card className='event-section-frame h-100'> {/* h-100은 높이를 맞추기 위함 */}
            <Card.Img variant="top" src={badge.img} />
            <Card.Body>
                <Card.Title>{badge.title}</Card.Title>
                <Card.Text>{badge.body}</Card.Text>
            </Card.Body>
            </Card>
        </Col>

        {/* 2. 두 번째 카드 (빵집 정보) */}
        <Col md={6} className="mb-4">
            <Card className='event-section-frame h-100'> {/* h-100은 높이를 맞추기 위함 */}
            <Card.Body>
                <Card.Title>빵집 이름</Card.Title>
                <Card.Text>빵집 정보</Card.Text>
            </Card.Body>
            </Card>
        </Col>
    </Row>
     <Row>
        <Col md={6}>
            <Card className='event-section-frame mb-4'>
            <Card.Body> 
                <Card.Title>"챌린지 장소를 한눈에!"</Card.Title>
                <Card.Text>(장소 찾기)</Card.Text>
            </Card.Body>
            </Card>
        </Col>

        <Col md={6}>
            <Card className='event-section-frame mb-4'>
            <Card.Body> 
                <Card.Title>"진행률 한눈에!"</Card.Title>
                <Card.Text>(진행률)</Card.Text>
            </Card.Body>
            </Card>
        </Col>
    </Row>
    </div>
  );
};

export default EventDetail;