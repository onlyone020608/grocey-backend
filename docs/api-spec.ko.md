# 📑 Grocey API 명세서

본 문서는 **Grocey 백엔드**의 REST API 명세서입니다.
도메인별로 구분하여 주요 엔드포인트와 요청/응답 예시를 정리합니다.
---

<div align="center">
<h4><b> 도메인 </b></h4>
<p align="center">
  <a href="#-auth-도메인">Auth</a> •
  <a href="#-users-api">User</a> •
  <a href="#-fridge-api">Fridge</a> •
  <a href="#-product-api">Product</a>
</p>
</div>

---

## 🔐 Auth 도메인

### 1. 회원가입
**Endpoint**: `POST /api/auth/signup`

**Request**
```json
{
  "name": "test9876",
  "email": "test9876@example.com",
  "password": "98769876"
}
```
**Response (201 Created)**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

### 2. 로그인
**Endpoint**: `POST /api/auth/login`

**Request**
```json
{
  "email": "test9876@example.com",
  "password": "98769876"
}
```
**Response (200 OK)**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

### 3. 비밀번호 변경
**Endpoint**: `PATCH /api/auth/password`

**Request**
```json
{
  "currentPassword": "98769876",
  "newPassword": "10191019"
}
```
**Response (200 OK)**
- 본문 없음

---

### 4. 로그아웃
**Endpoint**: `POST /api/auth/logout`

**Request**
- 본문 없음

**Response (200 OK)**
- 본문 없음

---

### 5. 회원탈퇴
**Endpoint**: `DELETE /api/auth/withdraw`

**Request**
- 본문 없음

**Response (204 No Content)**
- 본문 없음

---

## 👤 Users API

### 1. 성별 설정
**Endpoint**: `PATCH /api/users/me/gender`

**Request**
```json
{
  "gender": "MALE"
}
```
**Response (200 OK)**
- 본문 없음

---

### 2. 연령대 설정
**Endpoint**: `PATCH /api/users/me/age-group`

**Request**
```json
{
  "ageValue": 20
}
```
**Response (200 OK)**
- 본문 없음

---

### 3. 알러지 설정
**Endpoint**: `PATCH /api/users/me/allergies`

**Request**
```json
{
  "allergyIds": [1, 3, 5]
}
```
**Response (200 OK)**
- 본문 없음

---

### 4. 비건 여부 설정
**Endpoint**: `PATCH /api/users/me/vegan`

**Request**
```json
{
  "vegan": false
}
```
**Response (200 OK)**
- 본문 없음

---

### 5. 음식/재료 선호도 설정
**Endpoint**: `PATCH /api/users/me/preferences`

**Request**
```json
{
  "foodPreferenceIds": [1, 3],
  "preferredIngredientIds": [2, 4, 6],
  "dislikedIngredientIds": [7, 8]
}
```
**Response (200 OK)**
- 본문 없음

---

### 6. 프로필 완료 여부 확인
**Endpoint**: `GET /api/users/me/status`

**Request**
- 본문 없음

**Response (200 OK)**
```json
{
  "profileCompleted": true
}
```

---

### 7. 사용자 이름 요약 조회
**Endpoint**: `GET /api/users/me/summary`

**Request**
- 본문 없음

**Response (200 OK)**
```json
{
  "name": "test9876"
}
```

---

### 8. 사용자 프로필 조회
**Endpoint**: `GET /api/users/me`

**Request**
- 본문 없음

**Response (200 OK)**
```json
{
  "userName": "test9876",
  "email": "test9876@example.com"
}
```

---

### 9. 사용자 프로필 수정
**Endpoint**: `PATCH /api/users/me`

**Request**
```json
{
  "userName": "test1019",
  "email": "test1019@example.com"
}
```
**Response (200 OK)**
- 본문 없음

---

## 🧊 Fridge API

### 1. 냉장고 정보 조회
**Endpoint**: `GET /api/fridge`

**Request**
- 본문 없음

**Response (200 OK)**
```json
{
  "fridgeTemperature": 3.5,
  "freezerTemperature": -18.0
}
```

---

### 2. 냉장고 재료 목록 조회
**Endpoint**: `GET /api/fridge/ingredients`

**Query Parameters**
- `isFreezer` (boolean, optional): freezer 여부 필터

**Request**
- 본문 없음

**Response (200 OK)**
```json
[
  {
    "ingredientId": 1,
    "ingredientName": "Orange",
    "imageUrl": "https://example.com/orange.png"
  },
  {
    "ingredientId": 2,
    "ingredientName": "Cherry Tomato",
    "imageUrl": "https://example.com/tomato.png"
  }
]
```

---

### 3. 재료 상세 조회
**Endpoint**: `GET /api/fridge/ingredients/{ingredientId}`

**Request**
- 본문 없음

**Response (200 OK)**
```json
{
  "ingredientName": "Pork",
  "imageUrl": "https://example.com/pork.png",
  "quantity": 2,
  "isFreezer": true,
  "expirationDate": "2025-09-30"
}
```

---

## 🛒 Product API

### 1. 탭별 상품 목록 조회
**Endpoint**: `GET /api/products`

**Query Parameters**
- `tab` (String, default=`NEW`): `NEW`, `BEST`, `DEAL`, `NOBRAND`, `SSG_ONLY`

**Request**
- 본문 없음

**Response (200 OK)**
```json
{
  "products": [
    {
      "productId": 1,
      "brandName": "No Brand",
      "productName": "Orange Juice",
      "price": 3.99,
      "imageUrl": "https://example.com/orange-juice.png"
    },
    {
      "productId": 2,
      "brandName": "SSG",
      "productName": "Cherry Tomato Pack",
      "price": 2.49,
      "imageUrl": "https://example.com/cherry-tomato.png"
    }
  ]
}
```

---

### 2. 상품 상세 조회
**Endpoint**: `GET /api/products/{productId}`

**Request**
- 본문 없음

**Response (200 OK)**
```json
{
  "productId": 1,
  "brandName": "No Brand",
  "productName": "Orange Juice",
  "price": 3.99,
  "imageUrl": "https://example.com/orange-juice.png"
}
```
---

## 🧺 Cart API

### 1. 장바구니 조회
**Endpoint**: `GET /api/cart`

**Request**
- 본문 없음

**Response (200 OK)**
```json
{
  "cartId": 1,
  "items": [
    {
      "cartItemId": 10,
      "productId": 101,
      "productName": "Orange Juice",
      "imageUrl": "https://example.com/orange-juice.png",
      "price": 3.99,
      "quantity": 2
    },
    {
      "cartItemId": 11,
      "productId": 202,
      "productName": "Cherry Tomato Pack",
      "imageUrl": "https://example.com/cherry-tomato.png",
      "price": 2.49,
      "quantity": 1
    }
  ]
}
```

---

### 2. 장바구니 아이템 추가
**Endpoint**: `POST /api/cart/items`

**Request**
```json
{
  "productId": 101,
  "quantity": 2
}
```

**Response (201 Created)**
- 본문 없음

---

### 3. 장바구니 아이템 수량 변경
**Endpoint**: `PATCH /api/cart/items`

**Request**
```json
{
  "cartItemId": 10,
  "quantity": 3
}
```

**Response (200 OK)**
- 본문 없음

---

### 4. 장바구니 아이템 삭제 (복수)
**Endpoint**: `DELETE /api/cart/items`

**Request**
```json
[10, 11, 12]
```

**Response (204 No Content)**
- 본문 없음

---

### 5. 장바구니 아이템 일괄 추가 (배치)
**Endpoint**: `POST /api/cart/items/batch`

**Request**
```json
[
  { "productId": 101, "quantity": 2 },
  { "productId": 202, "quantity": 1 },
  { "productId": 303, "quantity": 5 }
]
```

**Response (201 Created)**
- 본문 없음

---

## 📦 Order API

### 1. 최근 주문 요약 목록 조회
**Endpoint**: `GET /api/orders/summary`

**Request**
- 본문 없음

**Response (200 OK)**
```json
[
  {
    "orderId": 1201,
    "createdAt": "2025-09-14T10:23:45",
    "orderStatus": "PAID",
    "items": [
      {
        "orderItemId": 5011,
        "productName": "Orange Juice",
        "quantity": 2,
        "price": 3.99,
        "imageUrl": "https://example.com/orange-juice.png"
      },
      {
        "orderItemId": 5012,
        "productName": "Cherry Tomato Pack",
        "quantity": 1,
        "price": 2.49,
        "imageUrl": "https://example.com/cherry-tomato.png"
      }
    ]
  },
  {
    "orderId": 1199,
    "createdAt": "2025-09-12T19:05:10",
    "orderStatus": "DELIVERING",
    "items": [
      {
        "orderItemId": 4988,
        "productName": "Pork Belly",
        "quantity": 1,
        "price": 8.50,
        "imageUrl": "https://example.com/pork.png"
      }
    ]
  }
]
```

---

### 2. 주문 상세 조회
**Endpoint**: `GET /api/orders/{orderId}`

**Path Variables**
- `orderId` (Long): 주문 ID

**Request**
- 본문 없음

**Response (200 OK)**
```json
{
  "orderId": 1201,
  "createdAt": "2025-09-14T10:23:45",
  "orderStatus": "PAID",
  "paymentMethod": "CARD",
  "shippingAddress": "Seoul, Gangnam-gu, Teheran-ro 123",
  "items": [
    {
      "orderItemId": 5011,
      "productName": "Orange Juice",
      "quantity": 2,
      "price": 3.99,
      "imageUrl": "https://example.com/orange-juice.png"
    },
    {
      "orderItemId": 5012,
      "productName": "Cherry Tomato Pack",
      "quantity": 1,
      "price": 2.49,
      "imageUrl": "https://example.com/cherry-tomato.png"
    }
  ]
}
```

---

### 3. 전체 주문 목록 조회 (페이지네이션)
**Endpoint**: `GET /api/orders`

**Query Parameters**
- `page` (integer, default=`0`): 0-based page index
- `size` (integer, default=`10`): page size
- `sort` (string, default=`createdAt,desc`): 정렬 필드 및 방향

**Request**
- 본문 없음

**Response (200 OK)**
```json
{
  "content": [
    {
      "orderId": 1201,
      "createdAt": "2025-09-14T10:23:45",
      "orderStatus": "PAID",
      "items": [
        {
          "orderItemId": 5011,
          "productName": "Orange Juice",
          "quantity": 2,
          "price": 3.99,
          "imageUrl": "https://example.com/orange-juice.png"
        }
      ]
    },
    {
      "orderId": 1199,
      "createdAt": "2025-09-12T19:05:10",
      "orderStatus": "DELIVERING",
      "items": [
        {
          "orderItemId": 4988,
          "productName": "Pork Belly",
          "quantity": 1,
          "price": 8.50,
          "imageUrl": "https://example.com/pork.png"
        }
      ]
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 25,
  "totalPages": 3,
  "sort": "createdAt,desc",
  "first": true,
  "last": false,
  "numberOfElements": 2,
  "empty": false
}
```

---

### 4. 주문 생성 (장바구니 기반)
**Endpoint**: `POST /api/orders`

**Request**
```json
{
  "cartItemIds": [10, 11, 12],
  "address": "Seoul, Gangnam-gu, Teheran-ro 123",
  "paymentMethod": "CARD"
}
```
> `paymentMethod` 는 서버의 `PaymentMethod` enum 값과 일치해야 합니다. (예: `"CARD"`, `"BANK_TRANSFER"` 등 실제 값에 맞게 사용)

**Response (201 Created)**
```json
{
  "orderId": 1202
}
```

---

## 🤖 Recommendation API

### 1. 냉장고 기반 상품 추천 (최신)
**Endpoint**: `GET /api/recommendations/fridge`

**Request**
- 본문 없음

**Response (200 OK)**
```json
{
  "recommendationId": 42,
  "createdAt": "2025-09-15T11:20:00",
  "products": [
    {
      "productId": 101,
      "productName": "Orange Juice",
      "brand": "No Brand",
      "price": 3.99,
      "imageUrl": "https://example.com/orange-juice.png"
    },
    {
      "productId": 202,
      "productName": "Cherry Tomato Pack",
      "brand": "SSG",
      "price": 2.49,
      "imageUrl": "https://example.com/cherry-tomato.png"
    }
  ]
}
```

---

### 2. 개인화 레시피 추천
**Endpoint**: `GET /api/recipes/recommendations/personal`

**Request**
- 본문 없음

**Response (200 OK)**
```json
[
  {
    "recipeId": 9001,
    "recipeName": "Orange Pork Stir-fry",
    "recipeImageUrl": "https://example.com/recipes/orange-pork.png"
  },
  {
    "recipeId": 9002,
    "recipeName": "Tomato Basil Pasta",
    "recipeImageUrl": "https://example.com/recipes/tomato-pasta.png"
  }
]
```

---

### 3. 냉장고 기반 레시피 추천
**Endpoint**: `GET /api/recipes/recommendations/fridge`

**Request**
- 본문 없음

**Response (200 OK)**
```json
[
  {
    "recipeId": 9101,
    "recipeName": "Pork & Cherry Tomato Skillet",
    "recipeImageUrl": "https://example.com/recipes/pork-tomato.png"
  },
  {
    "recipeId": 9102,
    "recipeName": "Citrus Salad",
    "recipeImageUrl": "https://example.com/recipes/citrus-salad.png"
  }
]
```
---

## 🍳 Recipe API

### 1. 레시피 상세 조회
**Endpoint**: `GET /api/recipes/{recipeId}`

**Path Variables**
- `recipeId` (Long): 레시피 ID

**Request**
- 본문 없음

**Response (200 OK)**
```json
{
  "recipeName": "Tomato Basil Pasta",
  "descriptionSteps": [
    "Boil pasta in salted water for 8–10 minutes.",
    "Sauté garlic and cherry tomatoes until softened.",
    "Add basil and toss with pasta."
  ],
  "cookingTime": 20,
  "servings": 2,
  "recipeImageUrl": "https://example.com/recipes/tomato-basil-pasta.png",
  "ingredients": [
    { "name": "Spaghetti", "quantity": "200g" },
    { "name": "Cherry Tomato", "quantity": "150g" },
    { "name": "Basil", "quantity": "a handful" },
    { "name": "Garlic", "quantity": "2 cloves" }
  ],
  "saved": true
}
```

---

### 2. 저장한 레시피 목록 조회
**Endpoint**: `GET /api/users/me/recipes`

**Request**
- 본문 없음

**Response (200 OK)**
```json
[
  {
    "recipeId": 9001,
    "recipeName": "Orange Pork Stir-fry",
    "imageUrl": "https://example.com/recipes/orange-pork.png"
  },
  {
    "recipeId": 9002,
    "recipeName": "Tomato Basil Pasta",
    "imageUrl": "https://example.com/recipes/tomato-basil-pasta.png"
  }
]
```

---

### 3. 레시피 저장
**Endpoint**: `POST /api/users/me/recipes/{recipeId}`

**Path Variables**
- `recipeId` (Long): 레시피 ID

**Request**
- 본문 없음

**Response (201 Created)**
- 본문 없음

---

### 4. 저장한 레시피 삭제
**Endpoint**: `DELETE /api/users/me/recipes/{recipeId}`

**Path Variables**
- `recipeId` (Long): 레시피 ID

**Request**
- 본문 없음

**Response (204 No Content)**
- 본문 없음


