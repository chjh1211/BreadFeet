import React from "react";
import './Review.css';

// onReviewSuccess를 props로 받도록 추가
const Review = ({ bakeryName, onClose, bakeryId, onReviewSuccess }) => {
    const [rating, setRating] = React.useState(0);
    const [text, setText] = React.useState("");
    const [isSubmitting, setIsSubmitting] = React.useState(false); 
    
    // 선택된 이미지 파일 상태    
    const [selectedImage, setSelectedImage] = React.useState(null); 
    // 이미지 미리보기 URL 상태
    const [imagePreviewUrl, setImagePreviewUrl] = React.useState(null); 

    // 이미지 파일 변경 핸들러
    const handleImageChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            setSelectedImage(file);
            setImagePreviewUrl(URL.createObjectURL(file)); // 미리보기 URL 생성
        } else {
            setSelectedImage(null);
            setImagePreviewUrl(null);
        }
    };
    
    // 이미지 제거 핸들러
    const handleImageRemove = () => {
        setSelectedImage(null);
        if (imagePreviewUrl) {
            URL.revokeObjectURL(imagePreviewUrl); // 메모리 해제
            setImagePreviewUrl(null);
        }
        // 파일 input 초기화
        const fileInput = document.getElementById('review-image-input');
        if (fileInput) fileInput.value = '';
    };

    const handleSubmit = async (e) => {
        e.preventDefault(); 
        
        if (rating === 0) {
            alert("별점을 선택해주세요.");
            return;
        }

        setIsSubmitting(true);

        // FormData를 사용하여 파일과 텍스트 데이터를 함께 전송
        const formData = new FormData();
        formData.append("bakeryId", bakeryId);
        formData.append("rating", rating);
        formData.append("text", text);
        formData.append("date", new Date().toISOString());
        formData.append("likes", 0);
        if (selectedImage) {
            formData.append("image", selectedImage); // 이미지 파일 추가, 필드명은 'image'
        }

        try {
            const response = await fetch("http://localhost:3001/reviews", {
                method: "POST",
                // Content-Type은 FormData 사용 시 설정하지 않음
                body: formData, 
            });

            if (!response.ok) {
                throw new Error("리뷰 등록에 실패했습니다.");
            }

            alert("✅ 리뷰가 성공적으로 등록되었습니다!");
            
            // 등록 성공 후, 부모 컴포넌트의 새로고침 함수 호출
            if (onReviewSuccess) {
                onReviewSuccess();
            }

            // 모달 닫기
            onClose();

        } catch (error) {
            console.error("리뷰 등록 오류:", error);
            alert(`리뷰 등록 중 오류가 발생했습니다: ${error.message}`);
        } finally {
            setIsSubmitting(false);
        }
    }

    // ESC로 닫기 및 이미지 미리보기 URL 클린업
    React.useEffect(()=>{
        const onEsc = (e) => e.key === "Escape" && onClose();
        document.addEventListener("keydown", onEsc);
        
        // 클린업 함수
        return () => {
            document.removeEventListener("keydown", onEsc); 
            if (imagePreviewUrl) {
                URL.revokeObjectURL(imagePreviewUrl); 
            }
        }
    }, [onClose, imagePreviewUrl]);


    return (
        <div className="review-overlay" onClick={onClose}>
      <div className="review-modal" onClick={(e) => e.stopPropagation()}>
        <header className="review-header">
          <h2>{bakeryName}에 대한 리뷰 작성</h2>
          <button className="review-close" onClick={onClose}>
            ✕
          </button>
        </header>

        <form onSubmit={handleSubmit}>
          {/* 별점 */}
          <div className="review-rating">
            {[1, 2, 3, 4, 5].map((n) => (
              <span
                key={n}
                className={n <= rating ? "star active" : "star"}
                onClick={() => setRating(n)}
              >
                ★
              </span>
            ))}
          </div>

          {/* 리뷰 텍스트 영역 */}
          <textarea
            className="review-textarea"
            placeholder="소중한 리뷰를 작성해주세요..."
            value={text}
            onChange={(e) => setText(e.target.value)}
            required
          />

          {/* 사진 첨부 UI */}
          <div className="review-image-upload">
            {!imagePreviewUrl && (
              <label htmlFor="review-image-input" className="image-upload-label">
              📸 사진 추가
              <input
                id="review-image-input"
                type="file"
                accept="image/*" 
                onChange={handleImageChange}
                style={{ display: "none" }} 
              />
              </label>
            )}


            {imagePreviewUrl && (
              <div className="image-preview-container">
                <img
                  src={imagePreviewUrl}
                  alt="미리보기"
                  className="image-preview"
                />
                <button
                  type="button"
                  className="remove-image-btn"
                  onClick={handleImageRemove}
                >
                  ✕
                </button>
              </div>
            )}
          </div>

          {/* 등록 버튼 */}
          <div>
            <button 
              type="submit" 
              className="review-submit"
              disabled={isSubmitting || rating === 0} 
            >
              {isSubmitting ? "등록 중..." : "등록하기"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Review;