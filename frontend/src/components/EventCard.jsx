import axios from 'axios';
import { Row, Col, Card } from "react-bootstrap";
import { useEffect, useState } from 'react';
import './EventCard.css'

const EventCard = ({ title, subtitle, url }) => {
  const [badges, setBadges] = useState([]);

  const getBadges = async () => {
    try {
      const res = await axios.get(url);
      setBadges(res.data);
    } catch (err) {
      console.error("❌ 뱃지 데이터를 불러오지 못했습니다:", err);
    }
  };

  useEffect(() => {
    getBadges();
  }, [url]);

  return (
    <section className="text-start mb-5">
      <h3 className="mb-2">{title}</h3>
      <p className='mb-4'>{subtitle}</p>
      <Row className="justify-content">
        {badges.length > 0 ? (
  badges.map((badge) => {
    const progressPercent =Math.ceil(
      badge.progress && badge.total
        ? Math.min((Number(badge.progress) / Number(badge.total)) * 100, 100)
        : 0);
    console.log(progressPercent);
    return (
      <Col
        key={badge.id}
        xs={6}
        md={4}
        className="d-flex justify-content-center mb-3"
      >
        <Card className="text-center hover-card" style={{ width: "14rem" }}>
          <Card.Img variant="top" src={badge.img} />
          <Card.Body>
            <Card.Title>{badge.title}</Card.Title>
            <div className="badge-body">{badge.body}</div>

            {/* progress가 있으면 ongoing */}
            {badge.progress !== undefined && badge.total !== undefined && (
              <div className="progress-container m-1">
                <div
                  className="progress-bar"
                  style={{ width: `${progressPercent}%` }}
                >
                {progressPercent}% 달성
                </div>
              </div>
            )}
          </Card.Body>
        </Card>
      </Col>
    );
  })
) : (
  <p className="text-center">불러올 스티커가 없습니다.</p>
)}

      </Row>
    </section>
  );
};

export default EventCard;
