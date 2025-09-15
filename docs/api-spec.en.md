# 📑 Grocey API Specification

This document describes the REST API endpoints for the **Grocey Backend**.  
Endpoints are grouped by domain for clarity.  
All requests and responses use the `application/json` media type.

---

<div align="center">
<h4><b> Domain Index </b></h4>
<p align="center">
  <a href="#-auth">Auth</a> •
  <a href="#-users">Users</a> •
  <a href="#-fridge">Fridge</a> •
  <a href="#-product">Product</a> •
  <a href="#-cart">Cart</a> •
  <a href="#-order">Order</a> •
  <a href="#-recommendation">Recommendation</a> •
  <a href="#-recipe">Recipe</a>
</p>
</div>

---

## 🔐 Auth

### 1) Sign Up
**Endpoint**: `POST /api/auth/signup`

**Request Body**
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

### 2) Login
**Endpoint**: `POST /api/auth/login`

**Request Body**
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

### 3) Change Password
**Endpoint**: `PATCH /api/auth/password`

**Request Body**
```json
{
  "currentPassword": "98769876",
  "newPassword": "10191019"
}
```

**Response (200 OK)**
- No body

---

### 4) Logout
**Endpoint**: `POST /api/auth/logout`

**Request Body**
- No body

**Response (200 OK)**
- No body

---

### 5) Delete Account
**Endpoint**: `DELETE /api/auth/withdraw`

**Request Body**
- No body

**Response (204 No Content)**
- No body

---

## 👤 Users

### 1) Set Gender
**Endpoint**: `PATCH /api/users/me/gender`

**Request Body**
```json
{
  "gender": "MALE"
}
```

**Response (200 OK)**
- No body

---

### 2) Set Age Group
**Endpoint**: `PATCH /api/users/me/age-group`

**Request Body**
```json
{
  "ageValue": 20
}
```

**Response (200 OK)**
- No body

---

### 3) Set Allergies
**Endpoint**: `PATCH /api/users/me/allergies`

**Request Body**
```json
{
  "allergyIds": [1, 3, 5]
}
```

**Notes**
- `allergyIds` accepts only predefined IDs (1–12).

**Response (200 OK)**
- No body

---

### 4) Set Vegan Status
**Endpoint**: `PATCH /api/users/me/vegan`

**Request Body**
```json
{
  "vegan": false
}
```

**Response (200 OK)**
- No body

---

### 5) Set Food & Ingredient Preferences
**Endpoint**: `PATCH /api/users/me/preferences`

**Request Body**
```json
{
  "foodPreferenceIds": [1, 3],
  "preferredIngredientIds": [2, 4, 6],
  "dislikedIngredientIds": [7, 8]
}
```

**Notes**
- `foodPreferenceIds`: 1–7
- `preferredIngredientIds` & `dislikedIngredientIds`: 1–17

**Response (200 OK)**
- No body

---

### 6) Check Profile Completion
**Endpoint**: `GET /api/users/me/status`

**Response (200 OK)**
```json
{
  "profileCompleted": true
}
```

---

### 7) Get User Name Summary
**Endpoint**: `GET /api/users/me/summary`

**Response (200 OK)**
```json
{
  "name": "test9876"
}
```

---

### 8) Get User Profile
**Endpoint**: `GET /api/users/me`

**Response (200 OK)**
```json
{
  "userName": "test9876",
  "email": "test9876@example.com"
}
```

---

### 9) Update User Profile
**Endpoint**: `PATCH /api/users/me`

**Request Body**
```json
{
  "userName": "test1019",
  "email": "test1019@example.com"
}
```

**Response (200 OK)**
- No body

---

## 🧊 Fridge

### 1) Get Fridge Info
**Endpoint**: `GET /api/fridge`

**Response (200 OK)**
```json
{
  "fridgeTemperature": 3.5,
  "freezerTemperature": -18.0
}
```

---

### 2) List Fridge Ingredients
**Endpoint**: `GET /api/fridge/ingredients`

**Query Parameters**
- `isFreezer` (boolean, optional): Filter by freezer vs. fridge

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

### 3) Get Ingredient Detail
**Endpoint**: `GET /api/fridge/ingredients/{ingredientId}`

**Path Variables**
- `ingredientId` (Long)

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

## 🛒 Product

### 1) List Products by Tab
**Endpoint**: `GET /api/products`

**Query Parameters**
- `tab` (string, default=`NEW`): one of `NEW`, `BEST`, `DEAL`, `NOBRAND`, `SSG_ONLY`

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

### 2) Get Product Detail
**Endpoint**: `GET /api/products/{productId}`

**Path Variables**
- `productId` (Long)

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

## 🧺 Cart

### 1) Get Cart
**Endpoint**: `GET /api/cart`

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

### 2) Add Cart Item
**Endpoint**: `POST /api/cart/items`

**Request Body**
```json
{
  "productId": 101,
  "quantity": 2
}
```

**Response (201 Created)**
- No body

---

### 3) Update Cart Item Quantity
**Endpoint**: `PATCH /api/cart/items`

**Request Body**
```json
{
  "cartItemId": 10,
  "quantity": 3
}
```

**Response (200 OK)**
- No body

---

### 4) Delete Cart Items (Batch)
**Endpoint**: `DELETE /api/cart/items`

**Request Body**
```json
[10, 11, 12]
```

**Response (204 No Content)**
- No body

---

### 5) Add Cart Items (Batch)
**Endpoint**: `POST /api/cart/items/batch`

**Request Body**
```json
[
  { "productId": 101, "quantity": 2 },
  { "productId": 202, "quantity": 1 },
  { "productId": 303, "quantity": 5 }
]
```

**Response (201 Created)**
- No body

---

## 📦 Order

### 1) Get Recent Order Summaries
**Endpoint**: `GET /api/orders/summary`

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

### 2) Get Order Detail
**Endpoint**: `GET /api/orders/{orderId}`

**Path Variables**
- `orderId` (Long)

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

### 3) List Orders (Paginated)
**Endpoint**: `GET /api/orders`

**Query Parameters**
- `page` (integer, default `0`): zero-based page index
- `size` (integer, default `10`): page size
- `sort` (string, default `createdAt,desc`): sort field and direction

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

### 4) Place Order (from Cart)
**Endpoint**: `POST /api/orders`

**Request Body**
```json
{
  "cartItemIds": [10, 11, 12],
  "address": "Seoul, Gangnam-gu, Teheran-ro 123",
  "paymentMethod": "CARD"
}
```

**Notes**
- `paymentMethod` must match your server’s `PaymentMethod` enum (e.g., `"CARD"`, `"BANK_TRANSFER"`).

**Response (201 Created)**
```json
{
  "orderId": 1202
}
```

---

## 🤖 Recommendation

### 1) Fridge-based Product Recommendations (Latest)
**Endpoint**: `GET /api/recommendations/fridge`

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

### 2) Personalized Recipe Recommendations
**Endpoint**: `GET /api/recipes/recommendations/personal`

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

### 3) Fridge-based Recipe Recommendations
**Endpoint**: `GET /api/recipes/recommendations/fridge`

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

## 🍳 Recipe

### 1) Get Recipe Detail
**Endpoint**: `GET /api/recipes/{recipeId}`

**Path Variables**
- `recipeId` (Long)

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

### 2) List Saved Recipes
**Endpoint**: `GET /api/users/me/recipes`

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

### 3) Save Recipe
**Endpoint**: `POST /api/users/me/recipes/{recipeId}`

**Path Variables**
- `recipeId` (Long)

**Request Body**
- No body

**Response (201 Created)**
- No body

---

### 4) Remove Saved Recipe
**Endpoint**: `DELETE /api/users/me/recipes/{recipeId}`

**Path Variables**
- `recipeId` (Long)

**Request Body**
- No body

**Response (204 No Content)**
- No body
