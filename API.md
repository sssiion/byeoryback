로컬 회원가입
### POST /auth/join
**Local Join**
```json
{
  "email": "user@example.com",
  "password": "password",
  "provider": "LOCAL"
} 
```

소셜 회원가입
**Social Join (Google)**
```json
{
  "email": "user@gmail.com",
  "provider": "GOOGLE",
  "providerId": "google_unique_id_123"
}
```

로그인
### POST /auth/login
```json
{
  "email": "user@example.com",
  "password": "password"
}
```

소셜 로그인
### POST /auth/social-login
```json
{
  "email": "user@gmail.com",
  "provider": "GOOGLE",
  "providerId": "google_unique_id_123"
}
```

전체 설정 조회
### GET /api/setting/all

테마 수정(설정)
### PUT /api/setting/theme
(JWT 토큰 받아야 함)
```json
{
  "mode": "dark",
  "font": {
    "family": "'Noto Sans KR', sans-serif",
    "size": "16px"
  }
}
```

(테마 수동 설정)
```json
{
  "mode": "manual",
  "font": {
    "family": "Pretendard",
    "size": "14px"
  },
  "manualConfig": {
    "text": {
      "color": "#333333",
      "intensity": 100
    },
    "background": {
      "isGradient": true,
      "color": "#ffffff",
      "intensity": 100,
      "gradientDirection": "to bottom right",
      "gradientStart": "#ff9a9e",
      "gradientEnd": "#fecfef",
      "image": "",
      "size": "cover"
    },
    "component": {
      "cardColor": "rgba(255, 255, 255, 0.8)",
      "btnColor": "#6200ea",
      "btnTextColor": "#ffffff"
    }
  }
}
```

테마 값 불러오기
### GET /api/setting/theme

메뉴 순서 변경
### PUT /api/setting/menu
```json
{
  "menuOrder": [
    "/community",
    "/market",
    "/home",
    "/post"
  ]
}
```

메뉴 순서 조회 확인
### GET /api/setting/menu
```json 예상 응답
{
  "menuOrder": [
    "/community",
    "/market",
    "/home",
    "/post"
  ]
}
```