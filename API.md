# PIN Authentication API

## 1. PIN Setup (설정)
**URI**: `POST /api/pin/setup`
**JSON**:
```json
{
  "pin": "1234"
}
```
**Role**: PIN 번호를 처음 설정하거나 변경합니다.

## 2. PIN Verify (검증)
**URI**: `POST /api/pin/verify`
**JSON**:
```json
{
  "pin": "1234"
}
```
**Role**: 입력된 PIN 번호가 맞는지 검증합니다. 5회 실패 시 계정이 잠깁니다.

## 3. Unlock with Code (이메일 인증으로 잠금 해제)
**URI**: `POST /api/pin/lock/verify`
**JSON**:
```json
{
  "code": "123456"
}
```
**Role**: 계정이 잠겼을 때 이메일로 전송된 인증 코드를 입력하여 잠금을 해제합니다.

## 4. Resend Unlock Code (인증 코드 재전송)
**URI**: `POST /api/pin/lock/resend`
**JSON**: (Empty Body)
**Role**: 잠금 해제 인증 코드를 이메일로 재전송합니다.

## 5. Toggle PIN (PIN 사용 여부 토글)
**URI**: `POST /api/pin/toggle?enable=true` (or `false`)
**JSON**: (Empty Body)
**Role**: PIN 기능을 켜거나 끕니다.

## 6. Change PIN (PIN 변경)
**URI**: `POST /api/pin/change`
**JSON**:
```json
{
  "pin": "5678"
}
```
**Role**: 로그인 상태에서(잠기지 않은 경우) PIN 번호를 변경합니다. Setup과 역할이 비슷하지만 명시적으로 변경 의도를 가집니다.
