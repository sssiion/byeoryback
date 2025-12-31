# Playtime Tracking API & Frontend Guidelines

## 1. Overview
사용자의 일일 접속 시간을 누적하고, 자정(00:00)을 기준으로 초기화하는 시스템입니다.
- **Heartbeat 방식**: 클라이언트가 주기적으로 서버에 신호를 보내 시간을 누적합니다.
- **예외 처리**: 브라우저 종료/비활성 상태에서는 시간이 누적되지 않도록 클라이언트에서 제어합니다.

---

## 2. API Endpoints

### 2.1 Heartbeat (시간 누적)
**POST** `/api/user/heartbeat`

- **설명**: 현재 날짜를 기준으로 접속 시간을 1분(60초) 누적합니다.
- **Header**: `Authorization: Bearer {Access_Token}`
- **Logic**:
    - 마지막 누적 날짜가 오늘과 다르면 `todayPlayTime`을 0으로 초기화 후 누적.
    - 접속 상태 유지를 위해 1분마다 호출 필요.

**Response**
- `200 OK`: 성공

### 2.2 Playtime 조회
**GET** `/api/user/playtime`

- **설명**: 현재까지 누적된 오늘의 접속 시간(초 단위)을 조회합니다.
- **Header**: `Authorization: Bearer {Access_Token}`

**Response**
- `200 OK`
```json
120  // (Long type, seconds)
```

---

## 3. Frontend Implementation Guide (React)

### 요구사항
1. **Heartbeat Loop**: 로그인 상태일 때 **60초마다** `/api/user/heartbeat`를 호출합니다.
2. **Real-time Display**: UI에서 시간을 실시간으로 보여주기 위해, Heartbeat와 별개로 **1초마다 로컬 state를 1씩 증가**시킵니다.
3. **Synchronization**: 페이지 로드/새로고침 시 `/api/user/playtime`을 호출하여 서버 시간으로 동기화합니다.
4. **Inactive Handling**:
    - `visibilitychange` 이벤트를 사용하여 탭이 백그라운드로 갔거나, 브라우저가 최소화되었을 때는 Heartbeat 호출을 중단하는 것을 권장합니다 (선택사항, 기획 의도에 따라 다름).
    - 사용자의 요청사항인 **"브라우저를 끄거나 로그아웃 시 시간 멈춤"**은 Heartbeat 방식의 특성상 자동 해결됩니다. (요청을 안 보내면 누적 안 됨)

### Example Code (Conceptual Hooks)

```typescript
import { useEffect, useState } from 'react';
import axios from 'axios';

export const usePlayTime = () => {
  const [playTime, setPlayTime] = useState(0);

  // 1. Initial Sync
  useEffect(() => {
    fetchPlayTime();
  }, []);

  const fetchPlayTime = async () => {
    try {
      const { data } = await axios.get('/api/user/playtime');
      setPlayTime(data);
    } catch (e) {
      console.error(e);
    }
  };

  // 2. Local Timer (UI only)
  useEffect(() => {
    const timer = setInterval(() => {
      setPlayTime(prev => prev + 1);
    }, 1000);
    return () => clearInterval(timer);
  }, []);

  // 3. Heartbeat (Server Sync)
  useEffect(() => {
    const heartbeat = setInterval(async () => {
      try {
        await axios.post('/api/user/heartbeat');
        // Optional: Re-sync just to be safe, or trust local
        // fetchPlayTime(); 
      } catch (e) {
        console.error("Heartbeat failed", e);
      }
    }, 60000); // 60 seconds

    return () => clearInterval(heartbeat);
  }, []);

  return playTime;
};
```
