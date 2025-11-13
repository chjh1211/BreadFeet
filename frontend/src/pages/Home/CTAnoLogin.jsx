import { useCallback } from "react";
import { useNavigate } from "react-router-dom";
import { Carousel } from "react-bootstrap";
import "./CTAnoLogin.css";

export default function CTAnoLogin() {
  const navigate = useNavigate();

  const goTaste = useCallback(() => {
    navigate("/taste");
  }, [navigate]);

  return (
    <div
      className="cta-no-login mt-2 mt-md-5"
      data-bs-theme="light"
      aria-label="AI 빵지순례 CTA"
    >
      <Carousel
        fade
        controls={false}
        indicators={false}
        touch
        pause="hover"
        interval={8000}
        className="cta-carousel"
      >
        <Carousel.Item>
          <div className="cta-hero">
            <div className="cta-lines">
              <p className="line-sm">전국 빵순이들을</p>
              <p className="line-sm">
                위한 <strong>AI 빵지순례</strong>
              </p>
            </div>

            <button
              type="button"
              className="cta-button"
              onClick={goTaste}
              aria-label="당신의 취향 테스트 하러 가기"
            >
              <span className="cta-button-text">당신의 취향은?</span>
              <span className="cta-arrow" aria-hidden>
                →
              </span>
            </button>
          </div>
        </Carousel.Item>
      </Carousel>
    </div>
  );
}
