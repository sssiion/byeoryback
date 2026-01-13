-- 위젯 양식
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('welcome', '환영 메시지', '사용자에게 환영 인사를 전하는 위젯입니다.', 'Utility', '2x1', '[[2, 1], [2, 2]]', '{}', true, '["인사", "welcome", "hello"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('formula-block', '수식 블록', '간단한 수식을 계산하고 결과를 보여줍니다.', 'Data & Logic', '2x2', '[[2, 2]]', '{}', true, '["계산", "math", "calc"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('time-machine', '타임머신', '미래의 나에게 편지를 보내거나 과거를 회상합니다.', 'Utility', '2x2', '[[2, 2]]', '{}', true, '["letter", "future", "past", "편지"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('todo-list', '할 일 목록', '해야 할 일들을 목록으로 관리하고 체크합니다.', 'Utility', '2x2', '[[1, 2], [2, 2], [2, 3]]', '{}', true, '["todo", "list", "check", "투두", "리스트", "체크", "할일"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('weather', '날씨', '현재 위치의 실시간 날씨 정보를 제공합니다.', 'Utility', '2x2', '[[2, 2]]', '{}', true, '["weather", "temp", "온도", "기온"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('clock', '시계', '다양한 스타일의 시계로 현재 시간을 확인합니다.', 'Utility', '2x2', '[[2, 2]]', '{}', true, '["time", "watch", "시간"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('streak', '연속 기록', '글 작성 연속 기간을 측정합니다.', 'Utility', '2x2', '[[2, 2]]', '{}', true, '["habit", "goal", "track", "습관", "목표"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('battery', '내 에너지', '나의 현재 에너지 레벨을 배터리 모양으로 표시합니다.', 'Utility', '1x1', '[[1, 1]]', '{}', true, '["energy", "power", "status", "상태"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('worry-shredder', '근심 파쇄기', '걱정거리를 적고 파쇄기에 넣어 없애버리세요.', 'Utility', '2x2', '[[2, 2]]', '{}', true, '["stress", "delete", "remove", "걱정", "삭제", "파쇄"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('scrap-note', '찢어진 노트', '간단한 메모를 남길 수 있는 빈티지 노트입니다.', 'Utility', '2x2', '[[2, 2]]', '{}', true, '["memo", "note", "write", "메모"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('recipe-card', '레시피 카드', '좋아하는 요리 레시피를 카드 형태로 보관합니다.', 'Utility', '2x2', '[[2, 2]]', '{}', true, '["cook", "food", "kitchen", "요리", "음식"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('worry-doll', '걱정 인형', '걱정 인형에게 고민을 털어놓고 마음을 비우세요.', 'Utility', '2x2', '[[2, 2]]', '{}', true, '["doll", "listen", "comfort", "위로"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('photo-gallery', '사진 갤러리', '소중한 추억이 담긴 사진들을 갤러리로 꾸밉니다.', 'Decoration', '2x2', '[[2, 2], [3, 2], [4, 2]]', '{}', true, '["image", "picture", "album", "앨범"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('polaroid', '폴라로이드', '감성적인 폴라로이드 사진으로 화면을 장식합니다.', 'Decoration', '2x2', '[[2, 2]]', '{"date": "2023.12.25", "src": "https://images.unsplash.com/photo-1511895426328-dc8714191300?w=400"}', true, '["photo", "retro", "camera", "사진"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('instant-booth', '인생네컷', '네 컷 사진으로 특별한 순간을 기록합니다.', 'Decoration', '2x2', '[[2, 2]]', '{"date": "2023.12.25", "images": ["https://images.unsplash.com/photo-1511895426328-dc8714191300?w=400", "https://images.unsplash.com/photo-1511895426328-dc8714191300?w=400", "https://images.unsplash.com/photo-1511895426328-dc8714191300?w=400", "https://images.unsplash.com/photo-1511895426328-dc8714191300?w=400"]}', true, '["photo", "4cut", "sticker", "사진"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('film-strip', '필름 스트립', '영화 필름처럼 사진을 나열하여 보여줍니다.', 'Decoration', '2x2', '[[2, 2]]', '{"images": ["https://images.unsplash.com/photo-1511895426328-dc8714191300?w=400", "https://images.unsplash.com/photo-1511895426328-dc8714191300?w=400"]}', true, '["movie", "cinema", "photo", "영화"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('ocean-wave', '바다 (파도)', '잔잔한 파도 소리와 함께 바다 풍경을 감상합니다.', 'Decoration', '2x2', '[[2, 2]]', '{}', true, '["sea", "water", "relax", "바다", "물"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('movie-ticket', '영화 티켓', '관람한 영화 티켓을 모아두는 컬렉션입니다.', 'Collection', '2x2', '[[2, 2]]', '{}', true, '["ticket", "cinema", "record", "영화"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('bookshelf', '책장', '읽은 책이나 읽고 싶은 책을 책장에 정리합니다.', 'Collection', '2x2', '[[2, 2]]', '{}', true, '["book", "read", "library", "도서"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('stamp-collection', '우표 수집', '다양한 디자인의 우표를 수집하고 감상합니다.', 'Collection', '2x2', '[[2, 2]]', '{}', true, '["stamp", "post", "mail", "우표"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('sky-map', '하늘 지도', '현재 밤하늘의 별자리와 천체를 보여줍니다.', 'Decoration', '2x2', '[[2, 2]]', '{}', true, '["star", "constellation", "space", "별", "우주"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('birth-flower', '탄생화', '나의 탄생화와 꽃말을 알려주는 위젯입니다.', 'Decoration', '2x2', '[[2, 2]]', '{}', true, '["flower", "birthday", "plant", "꽃"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('receipt-printer', '영수증', '소비 내역을 감각적인 영수증 형태로 정리합니다.', 'Decoration', '2x2', '[[2, 2]]', '{}', true, '["bill", "cost", "money", "돈", "지출"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('weather-stickers', '날씨 스티커', '귀여운 스티커로 날씨를 표현하고 꾸밉니다.', 'Decoration', '2x2', '[[2, 2]]', '{}', true, '["weather", "deco", "cute", "꾸미기"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('unit-converter', '단위 변환기', '길이, 무게, 온도 등 다양한 단위를 변환합니다.', 'Utility', '2x2', '[[2, 2]]', '{}', true, '["convert", "measure", "scale", "변환"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('calculator', '계산기', '간단한 사칙연산을 수행할 수 있는 계산기입니다.', 'Utility', '2x2', '[[2, 2]]', '{}', true, '["math", "plus", "minus", "더하기"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('random-picker', '랜덤 뽑기', '무작위 추첨이나 제비뽑기를 할 때 유용합니다.', 'Utility', '2x2', '[[2, 2]]', '{}', true, '["lottery", "luck", "choice", "추첨"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('map-pin', '지도 핀', '지도 위에 주요 장소를 핀으로 표시합니다.', 'Utility', '2x2', '[[2, 2]]', '{}', true, '["location", "place", "gps", "위치"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('rss-reader', '기사 모음', '구독한 RSS 피드의 최신 글을 모아두는 뷰어입니다.', 'Utility', '2x2', '[[2, 2]]', '{}', true, '["news", "feed", "blog", "뉴스"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('community', '커뮤니티', '다른 사용자들과 소통할 수 있는 커뮤니티 공간입니다.', 'Decoration', '2x2', '[[2, 2]]', '{}', true, '["chat", "talk", "social", "소통"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('moon-phase', '달의 위상', '오늘 밤 달의 모양(위상)을 보여줍니다.', 'Decoration', '2x2', '[[2, 2]]', '{}', true, '["moon", "night", "sky", "달"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('switch-board', '스위치', '딸깍거리는 소리와 함께 켜고 끄는 재미가 있는 스위치입니다.', 'Decoration', '2x2', '[[2, 2]]', '{}', true, '["click", "toggle", "button", "버튼"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('fortune-cookie', '포춘 쿠키', '쿠키를 깨서 오늘의 운세를 점쳐보세요.', 'Decoration', '2x2', '[[2, 2]]', '{}', true, '["luck", "future", "snack", "운세"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('ootd', 'OOTD', '오늘의 복장(Outfit Of The Day)을 그리고 기록합니다.', 'Decoration', '2x2', '[[2, 2]]', '{}', true, '["fashion", "clothes", "style", "옷", "패션"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('book-cover', '읽는 책', '현재 읽고 있는 책의 표지를 장식해두세요.', 'Decoration', '2x2', '[[2, 2]]', '{}', true, '["book", "reading", "cover", "독서"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('bubble-wrap', '뽁뽁이', '무한 뽁뽁이로 스트레스를 해소하세요.', 'Decoration', '2x2', '[[2, 2]]', '{}', true, '["pop", "stress", "toy", "장난감"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('transparent', '투명 (공백)', '화면에 여백을 주고 싶을 때 사용하는 투명 위젯입니다.', 'Decoration', '2x2', '[[2, 2]]', '{}', true, '["empty", "space", "blank", "여백"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('favorite-char', '최애 캐릭터', '가장 좋아하는 캐릭터 이미지를 띄워두세요.', 'Decoration', '2x2', '[[2, 2]]', '{"name": "Kirby", "src": "https://api.dicebear.com/7.x/fun-emoji/svg?seed=Kirby"}', true, '["image", "avatar", "love", "캐릭터"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('color-chip', '컬러칩', '영감을 주는 색상을 저장해두는 컬러칩입니다.', 'Decoration', '2x2', '[[2, 2]]', '{"color": "#FFD700", "name": "Golden", "code": "#FFD700"}', true, '["color", "paint", "code", "색상"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('color-palette', '컬러 팔레트', '나만의 색상 팔레트를 만들고 조합해보세요.', 'Tool', '2x2', '[[2, 2]]', '{}', true, '["colors", "mix", "art", "미술"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('movie-scene', '영화 명장면', '영화 속 명장면과 명대사를 기록합니다.', 'Decoration', '2x2', '[[2, 2]]', '{"src": "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=400", "quote": "Here''s looking at you, kid."}', true, '["movie", "scene", "quote", "명대사"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('ticket', '티켓', '공연이나 영화 티켓을 보관하는 티켓북입니다.', 'Decoration', '2x2', '[[2, 2]]', '{"title": "Movie Night", "date": "24.12.24", "seat": "H12"}', true, '["ticket", "stub", "entry", "입장권"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('neon', '네온 사인', '화려하게 빛나는 네온 사인으로 문구를 꾸밉니다.', 'Decoration', '2x2', '[[2, 2]]', '{"text": "DREAM", "color": "#ff00ff"}', true, '["light", "sign", "glow", "조명"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('candle', '양초', '흔들리는 촛불을 보며 힐링하는 위젯입니다.', 'Decoration', '2x2', '[[2, 2]]', '{}', true, '["fire", "light", "relax", "불"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('text-scroller', '텍스트 전광판', '메시지가 흐르는 LED 전광판 효과를 냅니다.', 'Decoration', '2x2', '[[2, 2]]', '{"text": "HELLO WORLD"}', true, '["led", "sign", "scroll", "글자"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('typewriter', '타자기', '타닥타닥 소리가 나는 타자기로 글을 써보세요.', 'Tool', '2x2', '[[2, 2]]', '{}', true, '["write", "retro", "text", "글"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('dessert-case', '간식 진열대', '맛있는 간식들을 진열해놓고 하나씩 꺼내보세요.', 'Interactive', '2x2', '[[2, 2]]', '{}', true, '["food", "sweet", "cake", "간식"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('cat-chaser', '따라오는 고양이', '마우스 커서를 따라다니는 귀여운 고양이입니다.', 'Interactive', '2x2', '[[2, 2]]', '{}', true, '["pet", "animal", "mouse", "고양이"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('snow-globe', '스노우볼', '흔들면 눈이 내리는 겨울 감성의 스노우볼입니다.', 'Interactive', '2x2', '[[2, 2]]', '{}', true, '["winter", "shake", "snow", "눈", "겨울"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('lp-player', '턴테이블', 'LP판을 올려 음악을 재생하는 턴테이블입니다.', 'Interactive', '2x2', '[[2, 2]]', '{}', true, '["music", "record", "play", "음악", "노래"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('bonfire', '모닥불', '타닥타닥 타오르는 모닥불을 보며 불멍을 즐기세요.', 'Interactive', '2x2', '[[2, 2]]', '{}', true, '["fire", "camp", "relax", "불"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES
('chat-diary', '나와의 채팅', '메신저 형식으로 나 자신과 대화를 나눕니다.', 'Diary & Emotion', '2x2', '[[2, 2]]', '{}', true, '["talk", "message", "chat", "대화"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES
('mood-analytics', 'Mood Analytics', '나의 감정 분포를 차트로 확인합니다.', 'Diary & Emotion', '2x1', '[[1, 1], [2, 1]]', '{}', true, '["mood", "chart", "analysis", "감정"]', NOW(), NOW()),
('word-mind-map', 'Word Mind Map', '나의 기록에서 자주 등장하는 키워드를 확인합니다.', 'Diary & Emotion', '2x1', '[[1, 1], [2, 1]]', '{}', true, '["word", "cloud", "analysis", "keyword"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES
('tag-cloud', '태그 구름', '자주 사용하는 태그들을 구름 모양으로 보여줍니다.', 'Utility', '2x2', '[[2, 2]]', '{}', true, '["keyword", "cloud", "text", "단어"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('scratch-card', '복권 긁기', '은박을 긁어서 당첨 결과를 확인하는 재미를 느껴보세요.', 'Interactive', '2x2', '[[2, 2]]', '{}', true, '["lottery", "luck", "scrtach", "복권"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('switches', '스위치 & 레버', '기계식 스위치와 레버를 조작하는 손맛을 느껴보세요.', 'Interactive', '2x2', '[[2, 2]]', '{}', true, '["toggle", "lever", "machinic", "스위치"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('cursor-trail', '커서 트레일', '마우스 커서 뒤에 화려한 효과가 따라다닙니다.', 'Global', '1x1', '[[1, 1]]', '{}', true, '["mouse", "effect", "pointer", "마우스"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('highlighter', '형광펜 모드', '화면의 중요 부분을 형광펜으로 칠하듯 강조합니다.', 'Global', '1x1', '[[1, 1]]', '{}', true, '["paint", "draw", "mark", "펜"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('physics-box', '물리 상자', '위젯들이 중력에 의해 서로 부딪히고 튀어 오릅니다.', 'Global', '1x1', '[[1, 1]]', '{}', true, '["gravity", "bounce", "game", "물리"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('magnifier', '돋보기', '화면의 특정 부분을 크게 확대해서 볼 수 있습니다.', 'Global', '1x1', '[[1, 1]]', '{}', true, '["zoom", "big", "view", "확대"]', NOW(), NOW());
INSERT IGNORE INTO widget_definitions (widget_type, label, description, category, default_size, valid_sizes, default_props, is_system, keywords, created_at, updated_at) VALUES 
('ruby-text', '루비 문자', '텍스트 위에 발음이나 설명을 작게 달아줍니다.', 'Global', '1x1', '[[1, 1]]', '{}', true, '["text", "annotation", "font", "문자"]', NOW(), NOW());

-- 위젯 사이즈표
UPDATE widget_definitions SET default_size = '4x1', valid_sizes = '[[1, 1], [4, 1]]', updated_at = NOW() WHERE widget_type = 'welcome';
UPDATE widget_definitions SET default_size = '2x1', valid_sizes = '[[1, 1], [2, 1], [2, 2]]', updated_at = NOW() WHERE widget_type = 'formula-block';
UPDATE widget_definitions SET default_size = '2x2', valid_sizes = '[[2, 2], [2, 3], [3, 3]]', updated_at = NOW() WHERE widget_type = 'chat-diary';
UPDATE widget_definitions SET default_size = '2x2', valid_sizes = '[[1, 1], [1, 2], [2, 1], [2, 2], [2, 3]]', updated_at = NOW() WHERE widget_type = 'todo-list';
UPDATE widget_definitions SET default_size = '2x1', valid_sizes = '[[1, 1], [2, 1], [2, 2]]', updated_at = NOW() WHERE widget_type = 'weather';
UPDATE widget_definitions SET default_size = '2x1', valid_sizes = '[[1, 1], [2, 1], [2, 2], [4, 1]]', updated_at = NOW() WHERE widget_type = 'clock';
UPDATE widget_definitions SET default_size = '2x1', valid_sizes = '[[1, 1], [2, 1]]', description = '글 작성 연속 기간을 측정합니다.', updated_at = NOW() WHERE widget_type = 'streak';
UPDATE widget_definitions SET default_size = '1x1', valid_sizes = '[[1, 1]]', updated_at = NOW() WHERE widget_type = 'battery';
UPDATE widget_definitions SET default_size = '1x1', valid_sizes = '[[1, 1], [2, 1]]', updated_at = NOW() WHERE widget_type = 'worry-shredder';
UPDATE widget_definitions SET default_size = '2x2', valid_sizes = '[[1, 1], [2, 1], [2, 2], [3, 2]]', updated_at = NOW() WHERE widget_type = 'scrap-note';
UPDATE widget_definitions SET default_size = '2x3', valid_sizes = '[[1, 2], [2, 2], [2, 3]]', updated_at = NOW() WHERE widget_type = 'recipe-card';
UPDATE widget_definitions SET default_size = '1x1', valid_sizes = '[[1, 1], [1, 2], [2, 1], [2, 2]]', updated_at = NOW() WHERE widget_type = 'worry-doll';
UPDATE widget_definitions SET default_size = '2x2', valid_sizes = '[[1, 2], [2, 1], [2, 2]]', updated_at = NOW() WHERE widget_type = 'unit-converter';
UPDATE widget_definitions SET default_size = '2x2', valid_sizes = '[[2, 2], [2, 3]]', updated_at = NOW() WHERE widget_type = 'calculator';
UPDATE widget_definitions SET default_size = '2x2', valid_sizes = '[[1, 1], [2, 2], [2, 3], [3, 3]]', updated_at = NOW() WHERE widget_type = 'random-picker';
UPDATE widget_definitions SET default_size = '2x2', valid_sizes = '[[1, 1], [2, 2]]', updated_at = NOW() WHERE widget_type = 'map-pin';
UPDATE widget_definitions SET default_size = '2x2', valid_sizes = '[[2, 2], [3, 2], [3, 3], [4, 2]]', updated_at = NOW() WHERE widget_type = 'rss-reader';
UPDATE widget_definitions SET default_size = '2x2', valid_sizes = '[[2, 1], [2, 2], [3, 2]]', updated_at = NOW() WHERE widget_type = 'tag-cloud';
UPDATE widget_definitions SET default_size = '2x2', valid_sizes = '[[2, 2], [3, 2], [3, 3]]', updated_at = NOW() WHERE widget_type = 'color-palette';
UPDATE widget_definitions SET default_size = '2x1', valid_sizes = '[[2, 1], [3, 1]]', updated_at = NOW() WHERE widget_type = 'typewriter';
UPDATE widget_definitions SET default_size = '1x1', valid_sizes = '[[1, 1], [1, 2], [2, 2]]', updated_at = NOW() WHERE widget_type = 'polaroid';
UPDATE widget_definitions SET default_size = '2x2', valid_sizes = '[[1, 1], [2, 2], [3, 2]]', updated_at = NOW() WHERE widget_type = 'photo-gallery';
UPDATE widget_definitions SET default_size = '1x2', valid_sizes = '[[1, 2], [2, 3]]', updated_at = NOW() WHERE widget_type = 'instant-booth';
UPDATE widget_definitions SET default_size = '4x1', valid_sizes = '[[4, 1], [4, 2]]', updated_at = NOW() WHERE widget_type = 'film-strip';
UPDATE widget_definitions SET default_size = '1x1', valid_sizes = '[[1, 1], [2, 2]]', updated_at = NOW() WHERE widget_type = 'moon-phase';
UPDATE widget_definitions SET default_size = '1x1', valid_sizes = '[[1, 1], [2, 1]]', updated_at = NOW() WHERE widget_type = 'switch-board';
UPDATE widget_definitions SET default_size = '1x1', valid_sizes = '[[1, 1]]', updated_at = NOW() WHERE widget_type = 'fortune-cookie';
UPDATE widget_definitions SET default_size = '2x2', valid_sizes = '[[1, 2], [2, 2], [2, 3]]', updated_at = NOW() WHERE widget_type = 'ootd';
UPDATE widget_definitions SET default_size = '1x2', valid_sizes = '[[1, 2], [2, 3]]', updated_at = NOW() WHERE widget_type = 'book-cover';
UPDATE widget_definitions SET default_size = '2x2', valid_sizes = '[[1, 1], [2, 2], [3, 3]]', updated_at = NOW() WHERE widget_type = 'bubble-wrap';
UPDATE widget_definitions SET default_size = '1x1', valid_sizes = '[[1, 1], [1, 2], [2, 1], [2, 2], [4, 1]]', updated_at = NOW() WHERE widget_type = 'transparent';
UPDATE widget_definitions SET default_size = '1x1', valid_sizes = '[[1, 1], [1, 2], [2, 1], [2, 2]]', updated_at = NOW() WHERE widget_type = 'favorite-char';
UPDATE widget_definitions SET default_size = '1x1', valid_sizes = '[[1, 1], [2, 1]]', updated_at = NOW() WHERE widget_type = 'color-chip';
UPDATE widget_definitions SET default_size = '2x1', valid_sizes = '[[2, 1], [3, 2]]', updated_at = NOW() WHERE widget_type = 'movie-scene';
UPDATE widget_definitions SET default_size = '2x1', valid_sizes = '[[2, 1]]', updated_at = NOW() WHERE widget_type = 'ticket';
UPDATE widget_definitions SET default_size = '2x1', valid_sizes = '[[1, 1], [2, 1], [3, 1]]', updated_at = NOW() WHERE widget_type = 'neon';
UPDATE widget_definitions SET default_size = '1x1', valid_sizes = '[[1, 1], [1, 2]]', updated_at = NOW() WHERE widget_type = 'candle';
UPDATE widget_definitions SET default_size = '2x1', valid_sizes = '[[1, 1], [2, 1], [3, 1], [4, 1]]', updated_at = NOW() WHERE widget_type = 'text-scroller';
UPDATE widget_definitions SET default_size = '2x1', valid_sizes = '[[1, 1], [2, 1], [4, 1]]', updated_at = NOW() WHERE widget_type = 'ocean-wave';
UPDATE widget_definitions SET default_size = '2x1', valid_sizes = '[[2, 1]]', updated_at = NOW() WHERE widget_type = 'movie-ticket';
UPDATE widget_definitions SET default_size = '2x2', valid_sizes = '[[2, 2], [3, 3]]', updated_at = NOW() WHERE widget_type = 'bookshelf';
UPDATE widget_definitions SET default_size = '2x2', valid_sizes = '[[2, 2], [2, 3]]', updated_at = NOW() WHERE widget_type = 'stamp-collection';
UPDATE widget_definitions SET default_size = '2x2', valid_sizes = '[[2, 2]]', updated_at = NOW() WHERE widget_type = 'sky-map';
UPDATE widget_definitions SET default_size = '1x1', valid_sizes = '[[1, 1], [1, 2]]', updated_at = NOW() WHERE widget_type = 'birth-flower';
UPDATE widget_definitions SET default_size = '2x2', valid_sizes = '[[1, 2], [2, 2]]', updated_at = NOW() WHERE widget_type = 'receipt-printer';
UPDATE widget_definitions SET default_size = '1x1', valid_sizes = '[[1, 1], [1, 2]]', updated_at = NOW() WHERE widget_type = 'weather-stickers';
UPDATE widget_definitions SET default_size = '2x1', valid_sizes = '[[2, 1]]', updated_at = NOW() WHERE widget_type = 'dessert-case';
UPDATE widget_definitions SET default_size = '1x1', valid_sizes = '[[1, 1], [2, 1]]', updated_at = NOW() WHERE widget_type = 'cat-chaser';
UPDATE widget_definitions SET default_size = '1x1', valid_sizes = '[[1, 1], [2, 2]]', updated_at = NOW() WHERE widget_type = 'snow-globe';
UPDATE widget_definitions SET default_size = '2x2', valid_sizes = '[[1, 1], [2, 2]]', updated_at = NOW() WHERE widget_type = 'lp-player';
UPDATE widget_definitions SET default_size = '2x2', valid_sizes = '[[2, 2]]', updated_at = NOW() WHERE widget_type = 'bonfire';
UPDATE widget_definitions SET default_size = '2x2', valid_sizes = '[[2, 2]]', updated_at = NOW() WHERE widget_type = 'community';
UPDATE widget_definitions SET default_size = '2x2', valid_sizes = '[[2, 1], [2, 2], [3, 2]]', updated_at = NOW() WHERE widget_type = 'time-machine';
UPDATE widget_definitions SET default_size = '2x2', valid_sizes = '[[1, 1], [1, 2], [2, 1], [2, 2]]', updated_at = NOW() WHERE widget_type = 'scratch-card';
UPDATE widget_definitions SET default_size = '2x2', valid_sizes = '[[1, 2], [2, 1], [2, 2]]', updated_at = NOW() WHERE widget_type = 'switches';
UPDATE widget_definitions SET default_size = '1x1', valid_sizes = '[[1, 1]]', updated_at = NOW() WHERE widget_type = 'cursor-trail';
UPDATE widget_definitions SET default_size = '1x1', valid_sizes = '[[1, 1]]', updated_at = NOW() WHERE widget_type = 'highlighter';
UPDATE widget_definitions SET default_size = '1x1', valid_sizes = '[[1, 1]]', updated_at = NOW() WHERE widget_type = 'physics-box';
UPDATE widget_definitions SET default_size = '1x1', valid_sizes = '[[1, 1], [1, 2], [2, 1], [2, 2]]', updated_at = NOW() WHERE widget_type = 'magnifier';
UPDATE widget_definitions SET default_size = '1x1', valid_sizes = '[[1, 1]]', updated_at = NOW() WHERE widget_type = 'ruby-text';
UPDATE widget_definitions SET thumbnail = '/thumbnails/emotion-analysis.png' WHERE widget_type = 'mood-analytics';
UPDATE widget_definitions SET thumbnail = '/thumbnails/tag-cloud.png' WHERE widget_type = 'word-mind-map';
UPDATE widget_definitions SET thumbnail = CONCAT('/thumbnails/', widget_type, '.png') WHERE thumbnail IS NULL;
