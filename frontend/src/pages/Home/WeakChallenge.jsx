import EventCard from "../../components/EventCard";

const WeakChallenge = () => {
   

    return (
        <div className="">
        <h2 className="">이번주 Top 3 챌린지</h2>
        <div className="ChallengeList">
           <EventCard 
            url="http://localhost:3001/recommended" // 추천 스티커 API
            />
        </div>
        </div>
  );
};

export default WeakChallenge;
