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

---

회원 상세 정보 입력 (프로필 완성)
### POST /api/user/profile
**Complete User Profile**
*Header*
`Authorization: Bearer {Access_Token}`

**Request Body**
```json
{
  "profilePhoto": "https://example.com/profile.jpg",
  "name": "홍길동",
  "nickname": "StarUser",
  "birthDate": "1995-12-25",
  "phone": "010-1234-5678",
  "gender": "MALE",
  "bio": "안녕하세요! 별이 돌아왔다 입니다."
}
```
* gender options: `MALE`, `FEMALE`, `PRIVATE`

**Response**
Status: 200 OK

---

회원 프로필 조회
### GET /api/user/profile
**Get User Profile**
*Header*
`Authorization: Bearer {Access_Token}`

**Response Body**
```json
{
  "profilePhoto": "https://example.com/profile.jpg",
  "name": "홍길동",
  "nickname": "StarUser",
  "birthDate": "1995-12-25",
  "phone": "010-1234-5678",
  "gender": "MALE",
  "bio": "안녕하세요! 별이 돌아왔다 입니다."
}
```

---

회원 프로필 수정
### PUT /api/user/profile
**Update User Profile**
*Header*
`Authorization: Bearer {Access_Token}`

**Note**: PUT 방식이므로 **모든 필드**를 보내야 합니다. 값을 보내지 않는 필드(null)는 DB에서도 null로 초기화될 수 있습니다. (name, nickname은 필수 값으로 유지 필요)

**Request Body**
```json
{
  "profilePhoto": "https://example.com/new_profile.jpg",
  "name": "홍길동",
  "nickname": "NewNickname",
  "birthDate": "1995-12-25",
  "phone": "010-9876-5432",
  "gender": "MALE",
  "bio": "자기소개가 변경되었습니다."
}
```

**Response**
Status: 200 OK
