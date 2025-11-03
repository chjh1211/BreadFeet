import "./Home.css";
// import PopularChallenge from "../Reject/PopularChallenge";
import TopBakery from "./TopBakery";
import HomeRightLogin from "./HomeRightLogin";
import WeakChallenge from "./WeakChallenge";

const Home = () => {
  return (
    <div className="HomeWrapper">
      <div className="HomeContents">
        {/* <PopularChallenge /> */}
        <WeakChallenge />
        <TopBakery />
        <HomeRightLogin />
      </div>
    </div>
  );
};

export default Home;
