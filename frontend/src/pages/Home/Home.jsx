import "./Home.css";
// import PopularChallenge from "../Reject/PopularChallenge";
import TopBakery from "./TopBakery";
import HomeRightLogin from "./HomeRightLogin";
import WeakChallenge from "./WeakChallenge";
import {Row, Col} from 'react-bootstrap'

const Home = () => { 
  return (
      <div>
      <Row className="m-4 justify-content-center">        
        <Col md={6} className="m-3"><TopBakery /></Col>
        <Col  md={4} className="m-3"><HomeRightLogin /></Col>
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
