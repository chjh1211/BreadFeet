import React from "react";
import { Container } from "react-bootstrap";
import EventCard from "../../components/EventCard";

const EventPage = () => {
  return (
    <Container className="my-5">
      <EventCard 
        title="획득한 PPANG STICKER"
        subtitle = "감자님께서 획득하신 스티커"
        url="http://localhost:3001/completed" // JSON 서버에서 완료 스티커 API
      />

      <EventCard 
        title="도전 중인 PPANG~ing STICKER"
        subtitle = "감자님께서 획득 중인 스티커"
        url="http://localhost:3001/ongoing" // 진행 중 스티커 API
      />

      <EventCard 
        title="추천 PPANG STICKER"
        subtitle = "감자님께서 획득하신 스티커"
        url="http://localhost:3001/recommended" // 추천 스티커 API
      />
    </Container>
  );
};

export default EventPage;
