import "./Home.css";
// import PopularChallenge from "../Reject/PopularChallenge";
// import TopBakery from "./TopBakery";
// import HomeRightLogin from "./HomeRightLogin";
import WeakChallenge from "./WeakChallenge";
import CTAnoLogin from "./CTAnoLogin";
import TopBakeries from "./TopBakeries";
import BestCourseCTA from "./bestCourseCTA";
import AiCTA from "./AiCTA";
import CTAlogin from "./CTAlogin";
import { Row, Col } from "react-bootstrap";

const Home = () => {
  return (
    <div className="homeWrapper">
      <Row className="m-4 justify-content-center">
        <Col md={6} className="m-3">
          {/* <TopBakery /> */}
          {/* <CTAnoLogin /> */}
          <CTAlogin />
        </Col>
        <Col md={4} className="m-3">
          <TopBakeries />
        </Col>
      </Row>
      <Row className="m-4 justify-content-center">
        <Col className="m-4">
          <WeakChallenge />
        </Col>
      </Row>
    </div>
  );
};

export default Home;
