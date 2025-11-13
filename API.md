### **BreadFeet 프로젝트 API 명세서**

#### **1. 인증 (Test)**

*   **`GET /testdb`**
    *   **설명**: 데이터베이스 연결 및 User 테이블 CRUD를 테스트합니다.
    *   **응답**: 성공 또는 실패 메시지 (String)

#### **2. 빵집 (Bakery)**

*   **`GET /api/v1/bakeries`**
    *   **설명**: 전체 빵집 목록을 페이지네이션하여 조회합니다. 빵집 이름으로 검색할 수 있습니다.
    *   **쿼리 파라미터**: `search` (String, 옵션), `page`, `size`, `sort` (Pageable)
    *   **응답**: `BakeryPageResponseDto`

*   **`GET /api/v1/bakeries/{bakeryId}`**
    *   **설명**: 특정 빵집의 상세 정보를 조회합니다.
    *   **경로 변수**: `bakeryId` (Long)
    *   **응답**: `BakeryDetailResponseDto`

#### **3. 챌린지 (Challenge)**

*   **`GET /api/challenges/my`**
    *   **설명**: 현재 로그인한 사용자의 챌린지 참여 현황을 조회합니다.
    *   **인증**: 필요 (JWT)
    *   **응답**: `List<UserChallengeResponseDto>`

*   **`GET /api/challenges/my-achieved`**
    *   **설명**: 현재 로그인한 사용자가 달성한 챌린지 목록을 조회합니다.
    *   **인증**: 필요 (JWT)
    *   **응답**: `List<MyChallengeResponseDto>`

#### **4. 즐겨찾기 (Favorite)**

*   **`POST /api/v1/bakeries/{bakeryId}/favorite`**
    *   **설명**: 특정 빵집을 즐겨찾기에 추가합니다.
    *   **인증**: 필요 (JWT)
    *   **경로 변수**: `bakeryId` (Long)
    *   **응답**: `FavoriteResponseDto`

*   **`DELETE /api/v1/bakeries/{bakeryId}/favorite`**
    *   **설명**: 특정 빵집을 즐겨찾기에서 삭제합니다.
    *   **인증**: 필요 (JWT)
    *   **경로 변수**: `bakeryId` (Long)
    *   **응답**: 없음 (204 No Content)

*   **`GET /api/v1/bakeries/favorites/my`**
    *   **설명**: 현재 로그인한 사용자의 즐겨찾기 목록을 조회합니다.
    *   **인증**: 필요 (JWT)
    *   **응답**: `List<FavoriteListResponseDto>`

#### **5. 메뉴 (Menu)**

*   **`GET /api/v1/bakeries/{bakeryId}/menus`**
    *   **설명**: 특정 빵집의 메뉴 목록을 조회합니다.
    *   **경로 변수**: `bakeryId` (Long)
    *   **응답**: `List<MenuResponseDto>`

#### **6. 마이페이지 (MyPage)**

*   **`GET /api/mypage/profile`**
    *   **설명**: 현재 로그인한 사용자의 프로필 정보를 조회합니다.
    *   **인증**: 필요 (JWT)
    *   **응답**: `MyPageResponseDto`

*   **`PATCH /api/mypage/profile`**
    *   **설명**: 현재 로그인한 사용자의 프로필 정보(닉네임, 프로필 이미지)를 수정합니다.
    *   **인증**: 필요 (JWT)
    *   **요청 본문**: `MyProfileUpdateRequestDto`
    *   **응답**: `MyPageResponseDto`

*   **`GET /api/mypage/reviews`**
    *   **설명**: 현재 로그인한 사용자가 작성한 리뷰 목록을 조회합니다.
    *   **인증**: 필요 (JWT)
    *   **응답**: `List<MyReviewResponseDto>`

#### **7. 리뷰 (Review)**

*   **`GET /api/bakeries/{bakeryId}/reviews`**
    *   **설명**: 특정 빵집에 작성된 모든 리뷰를 조회합니다.
    *   **경로 변수**: `bakeryId` (Long)
    *   **응답**: `List<ReviewResponseDto>`

*   **`POST /api/reviews/{bakeryId}/reviews`**
    *   **설명**: 특정 빵집에 새로운 리뷰를 작성합니다.
    *   **인증**: 필요 (JWT)
    *   **경로 변수**: `bakeryId` (Long)
    *   **요청 본문**: `ReviewCreateRequestDto`
    *   **응답**: `ReviewCreateResponseDto`

*   **`PUT /api/reviews/{reviewId}`**
    *   **설명**: 특정 리뷰를 수정합니다. (작성자 본인만 가능)
    *   **인증**: 필요 (JWT)
    *   **경로 변수**: `reviewId` (Long)
    *   **요청 본문**: `ReviewUpdateRequestDto`
    *   **응답**: 성공 메시지 (String)

*   **`POST /api/reviews/{reviewId}/like`**
    *   **설명**: 특정 리뷰에 '좋아요' 반응을 추가하거나, 기존 반응을 '좋아요'로 변경하거나, '좋아요' 반응을 취소합니다.
    *   **인증**: 필요 (JWT)
    *   **경로 변수**: `reviewId` (Long)
    *   **응답**: 없음 (200 OK)

*   **`POST /api/reviews/{reviewId}/dislike`**
    *   **설명**: 특정 리뷰에 '싫어요' 반응을 추가하거나, 기존 반응을 '싫어요'로 변경하거나, '싫어요' 반응을 취소합니다.
    *   **인증**: 필요 (JWT)
    *   **경로 변수**: `reviewId` (Long)
    *   **응답**: 없음 (200 OK)