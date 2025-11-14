# Breadfeet API 명세서

이 문서는 Breadfeet 백엔드 API의 명세서입니다.

## 1. 빵집 (Bakery)

### 1.1. 빵집 목록 조회

- **Endpoint:** `GET /api/v1/bakeries`
- **설명:** 빵집 목록을 검색하고 페이징하여 조회합니다.
- **Query Parameters:**
    - `search` (optional, string): 검색어
    - `page` (optional, integer): 페이지 번호 (0부터 시작)
    - `size` (optional, integer): 페이지 당 항목 수
- **성공 응답 (200 OK):**
    - `BakeryPageResponseDto`
        - `bakeries` (List<`BakeryListResponseDto`>): 빵집 목록
        - `currentPage` (integer): 현재 페이지
        - `totalPages` (integer): 전체 페이지 수
        - `totalElements` (long): 전체 빵집 수

### 1.2. 빵집 상세 조회

- **Endpoint:** `GET /api/v1/bakeries/{bakeryId}`
- **설명:** 특정 빵집의 상세 정보를 조회합니다.
- **Path Parameters:**
    - `bakeryId` (required, long): 빵집 ID
- **성공 응답 (200 OK):**
    - `BakeryDetailResponseDto`

## 2. 챌린지 (Challenge)

### 2.1. 전체 챌린지 목록 조회

- **Endpoint:** `GET /api/challenges/all`
- **설명:** 사용자의 달성 여부를 포함한 전체 챌린지 목록을 조회합니다.
- **인증:** 필요 (JWT)
- **성공 응답 (200 OK):**
    - `ChallengeListResponseDto`

### 2.2. 나의 챌린지 조회

- **Endpoint:** `GET /api/challenges/my`
- **설명:** 사용자가 진행 중인 챌린지 목록을 조회합니다.
- **인증:** 필요 (JWT)
- **성공 응답 (200 OK):**
    - `List<UserChallengeResponseDto>`

### 2.3. 내가 달성한 챌린지 조회

- **Endpoint:** `GET /api/challenges/my-achieved`
- **설명:** 사용자가 달성한 챌린지 목록을 조회합니다.
- **인증:** 필요 (JWT)
- **성공 응답 (200 OK):**
    - `List<MyChallengeResponseDto>`

## 3. 즐겨찾기 (Favorite)

### 3.1. 즐겨찾기 추가

- **Endpoint:** `POST /api/v1/bakeries/{bakeryId}/favorite`
- **설명:** 특정 빵집을 즐겨찾기에 추가합니다.
- **인증:** 필요 (JWT)
- **Path Parameters:**
    - `bakeryId` (required, long): 빵집 ID
- **성공 응답 (201 Created):**
    - `FavoriteResponseDto`

### 3.2. 즐겨찾기 삭제

- **Endpoint:** `DELETE /api/v1/bakeries/{bakeryId}/favorite`
- **설명:** 특정 빵집을 즐겨찾기에서 삭제합니다.
- **인증:** 필요 (JWT)
- **Path Parameters:**
    - `bakeryId` (required, long): 빵집 ID
- **성공 응답 (204 No Content):**

### 3.3. 내 즐겨찾기 목록 조회

- **Endpoint:** `GET /api/v1/bakeries/favorites/my`
- **설명:** 내가 즐겨찾기한 빵집 목록을 조회합니다.
- **인증:** 필요 (JWT)
- **성공 응답 (200 OK):**
    - `List<FavoriteListResponseDto>`

## 4. 메뉴 (Menu)

### 4.1. 빵집 메뉴 목록 조회

- **Endpoint:** `GET /api/v1/bakeries/{bakeryId}/menus`
- **설명:** 특정 빵집의 메뉴 목록을 조회합니다.
- **Path Parameters:**
    - `bakeryId` (required, long): 빵집 ID
- **성공 응답 (200 OK):**
    - `List<MenuResponseDto>`

## 5. 마이페이지 (MyPage)

### 5.1. 내 프로필 조회

- **Endpoint:** `GET /api/mypage/profile`
- **설명:** 내 프로필 정보를 조회합니다.
- **인증:** 필요 (JWT)
- **성공 응답 (200 OK):**
    - `MyPageResponseDto`

### 5.2. 내 프로필 수정

- **Endpoint:** `PATCH /api/mypage/profile`
- **설명:** 내 프로필 정보를 수정합니다.
- **인증:** 필요 (JWT)
- **Request Body:**
    - `MyProfileUpdateRequestDto`
- **성공 응답 (200 OK):**
    - `MyPageResponseDto`

### 5.3. 내가 쓴 리뷰 조회

- **Endpoint:** `GET /api/mypage/reviews`
- **설명:** 내가 작성한 리뷰 목록을 조회합니다.
- **인증:** 필요 (JWT)
- **성공 응답 (200 OK):**
    - `List<MyReviewResponseDto>`

## 6. 리뷰 (Review)

### 6.1. 빵집 리뷰 목록 조회

- **Endpoint:** `GET /api/bakeries/{bakeryId}/reviews`
- **설명:** 특정 빵집의 리뷰 목록을 조회합니다.
- **Path Parameters:**
    - `bakeryId` (required, long): 빵집 ID
- **성공 응답 (200 OK):**
    - `List<ReviewResponseDto>`

### 6.2. 리뷰 작성

- **Endpoint:** `POST /api/reviews/{bakeryId}/reviews`
- **설명:** 특정 빵집에 리뷰를 작성합니다.
- **인증:** 필요 (JWT)
- **Path Parameters:**
    - `bakeryId` (required, long): 빵집 ID
- **Request Body:**
    - `ReviewCreateRequestDto`
- **성공 응답 (201 Created):**
    - `ReviewCreateResponseDto`

### 6.3. 리뷰 수정

- **Endpoint:** `PUT /api/reviews/{reviewId}`
- **설명:** 특정 리뷰를 수정합니다.
- **인증:** 필요 (JWT)
- **Path Parameters:**
    - `reviewId` (required, long): 리뷰 ID
- **Request Body:**
    - `ReviewUpdateRequestDto`
- **성공 응답 (200 OK):**
    - "리뷰가 성공적으로 수정되었습니다." (string)

## 7. 리뷰 반응 (Review Reaction)

### 7.1. 리뷰 '좋아요'

- **Endpoint:** `POST /api/reviews/{reviewId}/like`
- **설명:** 특정 리뷰에 '좋아요'를 표시합니다.
- **인증:** 필요 (JWT)
- **Path Parameters:**
    - `reviewId` (required, long): 리뷰 ID
- **성공 응답 (200 OK):**

### 7.2. 리뷰 '싫어요'

- **Endpoint:** `POST /api/reviews/{reviewId}/dislike`
- **설명:** 특정 리뷰에 '싫어요'를 표시합니다.
- **인증:** 필요 (JWT)
- **Path Parameters:**
    - `reviewId` (required, long): 리뷰 ID
- **성공 응답 (200 OK):**

## 8. 테스트 (Test)

### 8.1. DB 연결 테스트

- **Endpoint:** `GET /testdb`
- **설명:** 데이터베이스 연결 및 기본적인 CRUD 동작을 테스트합니다.
- **성공 응답 (200 OK):**
    - "✅ DB 저장 및 조회 성공! 닉네임: {nickname}" (string)
- **실패 응답 (200 OK):**
    - "❌ DB 저장 실패! ..." (string)
    - "❌ DB 저장 중 에러 발생: ..." (string)
