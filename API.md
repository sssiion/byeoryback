### POST /auth/join
**Local Join**
```json
{
  "email": "user@example.com",
  "password": "password",
  "provider": "LOCAL"
} 
```

**Social Join (Google)**
```json
{
  "email": "user@gmail.com",
  "provider": "GOOGLE",
  "providerId": "google_unique_id_123"
}
```

### POST /auth/login
```json
{
  "email": "user@example.com",
  "password": "password"
}
```

### POST /auth/social-login
```json
{
  "email": "user@gmail.com",
  "provider": "GOOGLE",
  "providerId": "google_unique_id_123"
}
```
