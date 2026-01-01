
## PIN Security & Lockout System

> [!NOTE]
> All endpoints require a valid Access Token headers.
> The PIN is locked automatically after 5 consecutive failed attempts.

### 1. Check PIN Status
Retrieve the current failure count and lock status.
- **URL**: `/api/pin/status`
- **Method**: `GET`
- **Response**: `200 OK`
    ```json
    {
      "registered": true,  // Recently added: true if user has a PIN set
      "failureCount": 0,   // Current failed attempts (0-5)
      "locked": false      // true if failureCount >= 5
    }
    ```
    *If not registered, returns `registered: false` (no more 400 errors).*

### 2. Verify PIN (Test Lockout)
Attempt to verify PIN. 
- **URL**: `/api/pin/verify`
- **Method**: `POST`
- **Request Body**:
    ```json
    {
      "pin": "123456"
    }
    ```
- **Response**:
    - **Success (Match)**: `200 OK` - `true` (failureCount resets to 0)
    - **Failure (Mismatch)**: `200 OK` - `false` (failureCount increments)
    - **Locked**: `200 OK` with Error or `400/500` depending on global handler (Currently throws `IllegalArgumentException: PIN blocked...`)

### 3. Request Unlock Code
Request a 6-digit verification code to be sent to the user's email.
- **URL**: `/api/pin/unlock-request`
- **Method**: `POST`
- **Response**: `200 OK` (Email sent)

### 4. Unlock PIN (Delete PIN)
Enter the code received via email. **Upon success, the PIN is completely removed/disabled.** The user will no longer have a PIN.
- **URL**: `/api/pin/unlock`
- **Method**: `POST`
- **Request Body**:
    ```json
    {
      "code": "123456" // The 6-digit code from email
    }
    ```
- **Response**: `200 OK` (PIN deleted)

---

### Recommended Postman Test Flow

1. **Check Initial Status**
    - `GET /api/pin/status` -> Expect `failureCount: 0`, `locked: false`
2. **Intentionally Fail 5 Times**
    - `POST /api/pin/verify` with wrong PIN x5
    - Check `GET /api/pin/status` after each -> Count increases.
    - After 5th fail -> Expect `locked: true`.
3. **Verify Lockout**
    - `POST /api/pin/verify` with **CORRECT** or **WRONG** PIN.
    - Expect Error: "PIN blocked due to too many failed attempts".
4. **Request Unlock**
    - `POST /api/pin/unlock-request`
    - Check Console/Logs (or Mock) for the generated code if email is not working locally, or check actual email.
5. **Unlock (Delete PIN)**
    - `POST /api/pin/unlock` with the code.
6. **Verify Deletion**
    - `GET /api/pin/check` -> Expect `false`.
    - `GET /api/pin/status` -> Expect Error (PIN not set for this user).
