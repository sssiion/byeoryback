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

이메일 인증 번호 발송
### POST /auth/email/send
**Email Send**
```json
{
  "email": "your_email@gmail.com"
}
```
**Response**
```text
(6자리 인증번호, 예: 123456)
```

---

이메일 인증 번호 확인
### POST /auth/email/check
**Email Check**
```json
{
  "email": "your_email@gmail.com",
  "authNum": "123456"
}
```
**Response**
```text
true / false
```