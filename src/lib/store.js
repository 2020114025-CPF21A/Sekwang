// src/lib/store.js
const get = (k, def = []) => JSON.parse(localStorage.getItem(k) || JSON.stringify(def));
const set = (k, v) => localStorage.setItem(k, JSON.stringify(v));

/** QT (이미 있으면 그대로 두기) */
const QT_KEY = "qt_entries";
export const qtStore = {
  list() { return get(QT_KEY).sort((a,b)=>(b.createdAt||"").localeCompare(a.createdAt||"")); },
  getById(id) { return get(QT_KEY).find(x=>x.id===id) || null; },
  upsert(item) {
    const arr = get(QT_KEY);
    if (item.id) {
      const i = arr.findIndex(x=>x.id===item.id);
      if (i>=0) arr[i]=item; else arr.push(item);
    } else {
      item.id = crypto.randomUUID(); item.createdAt = new Date().toISOString(); arr.push(item);
    }
    set(QT_KEY, arr); return item;
  },
  remove(id) { set(QT_KEY, get(QT_KEY).filter(x=>x.id!==id)); },
};

/** Notices */
const NOTICE_KEY = "notices";
export const noticeStore = {
  list() {
    const arr = get(NOTICE_KEY);
    return arr.sort((a,b)=>{
      if ((b.pinned?1:0)!==(a.pinned?1:0)) return (b.pinned?1:0)-(a.pinned?1:0);
      return (b.createdAt||"").localeCompare(a.createdAt||"");
    });
  },
  getById(id) { return get(NOTICE_KEY).find(x=>x.id===id) || null; },
  upsert(item) {
    const arr = get(NOTICE_KEY);
    if (item.id) {
      const i = arr.findIndex(x=>x.id===item.id);
      if (i>=0) arr[i]=item; else arr.push(item);
    } else {
      item.id = crypto.randomUUID();
      item.createdAt = new Date().toISOString();
      arr.push(item);
    }
    set(NOTICE_KEY, arr);
    return item;
  },
  remove(id) { set(NOTICE_KEY, get(NOTICE_KEY).filter(x=>x.id!==id)); },
};

/** Album */
const ALBUM_KEY = "albums";
export const albumStore = {
  list() {
    // 최신 생성일 순
    return get(ALBUM_KEY).sort((a, b) => (b.createdAt || "").localeCompare(a.createdAt || ""));
  },
  getById(id) {
    return get(ALBUM_KEY).find((x) => x.id === id) || null;
  },
  upsert(item) {
    const arr = get(ALBUM_KEY);
    if (item.id) {
      const i = arr.findIndex((x) => x.id === item.id);
      if (i >= 0) arr[i] = item;
      else arr.push(item);
    } else {
      item.id = crypto.randomUUID();
      item.createdAt = new Date().toISOString();
      item.photos = item.photos || [];
      arr.push(item);
    }
    set(ALBUM_KEY, arr);
    return item;
  },
  remove(id) {
    set(ALBUM_KEY, get(ALBUM_KEY).filter((x) => x.id !== id));
  },
  addPhoto(albumId, photoUrl, caption = "") {
    const arr = get(ALBUM_KEY);
    const a = arr.find((x) => x.id === albumId);
    if (!a) return null;
    a.photos = a.photos || [];
    a.photos.push({ id: crypto.randomUUID(), url: photoUrl, caption });
    set(ALBUM_KEY, arr);
    return a;
  },
  removePhoto(albumId, photoId) {
    const arr = get(ALBUM_KEY);
    const a = arr.find((x) => x.id === albumId);
    if (!a) return null;
    a.photos = (a.photos || []).filter((p) => p.id !== photoId);
    set(ALBUM_KEY, arr);
    return a;
  },
};

/** Bulletin (주보) */
const BULLETIN_KEY = "bulletins";
export const bulletinStore = {
  list() {
    const arr = get(BULLETIN_KEY);
    // 날짜 내림차순 → 생성일 내림차순
    return arr.sort((a, b) => (b.date || "").localeCompare(a.date || "") || (b.createdAt || "").localeCompare(a.createdAt || ""));
  },
  getById(id) {
    return get(BULLETIN_KEY).find((x) => x.id === id) || null;
  },
  upsert(item) {
    const arr = get(BULLETIN_KEY);
    if (item.id) {
      const i = arr.findIndex((x) => x.id === item.id);
      if (i >= 0) arr[i] = item;
      else arr.push(item);
    } else {
      item.id = crypto.randomUUID();
      item.createdAt = new Date().toISOString();
      arr.push(item);
    }
    set(BULLETIN_KEY, arr);
    return item;
  },
  remove(id) {
    set(BULLETIN_KEY, get(BULLETIN_KEY).filter((x) => x.id !== id));
  },
};

/** Music Sheets (찬양 악보) */
const MUSIC_KEY = "music_sheets";
export const musicStore = {
  list() {
    const arr = get(MUSIC_KEY);
    return arr.sort(
      (a, b) => (b.createdAt || "").localeCompare(a.createdAt || "") || (a.title || "").localeCompare(b.title || "")
    );
  },
  getById(id) {
    return get(MUSIC_KEY).find((x) => x.id === id) || null;
  },
  upsert(item) {
    const arr = get(MUSIC_KEY);
    if (item.id) {
      const i = arr.findIndex((x) => x.id === item.id);
      if (i >= 0) arr[i] = item;
      else arr.push(item);
    } else {
      item.id = crypto.randomUUID();
      item.createdAt = new Date().toISOString();
      arr.push(item);
    }
    set(MUSIC_KEY, arr);
    return item;
  },
  remove(id) {
    set(MUSIC_KEY, get(MUSIC_KEY).filter((x) => x.id !== id));
  },
};

// === 출석: 명단(로스터) ===
// === 출석: 명단(로스터) — 버전드 시드 ===
const ROSTER_KEY = "attendance_roster";
const ROSTER_META_KEY = "attendance_roster_meta";
const ROSTER_VERSION = 2; // ← 코드로 명단 바꿨으면 숫자만 올리면 자동 업데이트

const ROSTER_SEED = [
    { id: "u1", name: "류영희" }, { id: "u2", name: "오세현" }, { id: "u3", name: "이대연" },
    { id: "u4", name: "권오윤" }, { id: "u5", name: "권오윤" }, { id: "u6", name: "김리우" },
    { id: "u7", name: "김정헌" }, { id: "u8", name: "김한결" }, { id: "u9", name: "임지환" },
    { id: "u10", name: "조선우" }, { id: "u11", name: "권예나" }, { id: "u12", name: "권창하" },
    { id: "u13", name: "김나윤" }, { id: "u14", name: "김태이" }, { id: "u15", name: "양하민" },
    { id: "u16", name: "이유준" }, { id: "u17", name: "전솔빈" }, { id: "u18", name: "조태율" },
    { id: "u19", name: "김진우" }, { id: "u20", name: "배주윤" }, { id: "u21", name: "송인우" },
    { id: "u22", name: "이도윤" }, { id: "u23", name: "장윤지" }, { id: "u24", name: "최주혁" },
    { id: "u25", name: "한은총" }, { id: "u26", name: "이채은" }, { id: "u27", name: "김지유" },
    { id: "u28", name: "김하린" }, { id: "u29", name: "김하윤" }
  ];

  export const rosterStore = {
    ensureSeed() {
      const meta = JSON.parse(localStorage.getItem(ROSTER_META_KEY) || "{}");
      const current = get(ROSTER_KEY, null);
  
      // 1) 없으면 시드
      if (!current) {
        set(ROSTER_KEY, ROSTER_SEED);
        localStorage.setItem(ROSTER_META_KEY, JSON.stringify({ version: ROSTER_VERSION }));
        return;
      }
  
      // 2) 버전 달라지면 덮어쓰기(마이그레이션)
      if ((meta.version ?? 0) !== ROSTER_VERSION) {
        set(ROSTER_KEY, ROSTER_SEED);
        localStorage.setItem(ROSTER_META_KEY, JSON.stringify({ version: ROSTER_VERSION }));
      }
    },
    list() { return get(ROSTER_KEY, []); },
    replace(list) {
      set(ROSTER_KEY, list);
      localStorage.setItem(ROSTER_META_KEY, JSON.stringify({ version: ROSTER_VERSION }));
    },
    clear() {
      localStorage.removeItem(ROSTER_KEY);
      localStorage.removeItem(ROSTER_META_KEY);
    }
  };

// === 출석: 세션(날짜별 체크) ===
const SESS_KEY = "attendance_sessions";
/*
세션 구조 예시:
{
  id: "2025-09-26",        // 날짜(키)
  date: "2025-09-26",
  checks: { "u1": true, "u2": false, ... },  // id → 출석여부
  createdAt: "ISO", updatedAt: "ISO"
}
*/
export const attendanceSessionStore = {
  list() { return get(SESS_KEY).sort((a,b)=>(b.date||"").localeCompare(a.date||"")); },
  get(date) { return get(SESS_KEY).find(s => s.date === date) || null; },
  upsert(session) {
    const arr = get(SESS_KEY);
    const i = arr.findIndex(s => s.date === session.date);
    session.updatedAt = new Date().toISOString();
    if (i >= 0) arr[i] = session;
    else { session.id = session.date; session.createdAt = new Date().toISOString(); arr.push(session); }
    set(SESS_KEY, arr);
    return session;
  },
  toggle(date, userId) {
    const roster = rosterStore.list();
    let sess = this.get(date);
    if (!sess) sess = { id: date, date, checks: {}, createdAt: null, updatedAt: null };
    // 존재하지 않는 id 방지
    if (!roster.some(r => r.id === userId)) return sess;
    sess.checks = sess.checks || {};
    sess.checks[userId] = !sess.checks[userId];
    return this.upsert(sess);
  },
};