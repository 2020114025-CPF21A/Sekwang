// src/App.jsx
import { NavLink, Routes, Route, Navigate } from "react-router-dom";

import Home from "./pages/Home.jsx";
import AttendanceBoard from "./pages/AttendanceBoard.jsx";
import AttendanceList from "./pages/AttendanceList.jsx";

import QTList from "./pages/QTList.jsx";
import QTForm from "./pages/QTForm.jsx";
import Notices from "./pages/Notices.jsx";
import NoticeForm from "./pages/NoticeForm.jsx";
import Albums from "./pages/Albums.jsx";
import AlbumForm from "./pages/AlbumForm.jsx";
import Bulletins from "./pages/Bulletins.jsx";
import BulletinForm from "./pages/BulletinForm.jsx";
import Music from "./pages/Music.jsx";
import MusicForm from "./pages/MusicForm.jsx";
import MonthlyTracker from "./pages/MonthlyTracker.jsx";

export default function App() {
  return (
    <div style={app}>
      <header style={topbar}>
        <h1 style={title}>⛪ 교회 행정 웹</h1>
        <nav style={nav}>
          <MenuLink to="/" end>홈</MenuLink>
          <MenuLink to="/attendance/board">출석 보드</MenuLink>
          <MenuLink to="/attendance/list">출석 현황</MenuLink>
          <MenuLink to="/qt">QT</MenuLink>
          <MenuLink to="/notices">공지사항</MenuLink>
          <MenuLink to="/albums">사진첩</MenuLink>
          <MenuLink to="/bulletins">주보</MenuLink>
          <MenuLink to="/music">찬양 악보</MenuLink>
          <MenuLink to="/tracker">월간 출석표</MenuLink>
        </nav>
      </header>

      <main style={main}>
        <Routes>
          <Route path="/" element={<Home />} />

          {/* Attendance (정식 경로) */}
          <Route path="/attendance/board" element={<AttendanceBoard />} />
          <Route path="/attendance/list" element={<AttendanceList />} />


          {/* QT */}
          <Route path="/qt" element={<QTList />} />
          <Route path="/qt/new" element={<QTForm />} />
          <Route path="/qt/:id" element={<QTForm />} />

          {/* Notices */}
          <Route path="/notices" element={<Notices />} />
          <Route path="/notices/new" element={<NoticeForm />} />
          <Route path="/notices/:id" element={<NoticeForm />} />

          {/* Albums */}
          <Route path="/albums" element={<Albums />} />
          <Route path="/albums/new" element={<AlbumForm />} />
          <Route path="/albums/:id" element={<AlbumForm />} />

          {/* Bulletins */}
          <Route path="/bulletins" element={<Bulletins />} />
          <Route path="/bulletins/new" element={<BulletinForm />} />
          <Route path="/bulletins/:id" element={<BulletinForm />} />

          {/* Music */}
          <Route path="/music" element={<Music />} />
          <Route path="/music/new" element={<MusicForm />} />
          <Route path="/music/:id" element={<MusicForm />} />

          {/* Monthly Tracker */}
          <Route path="/tracker" element={<MonthlyTracker />} />

          {/* 404 */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      </main>
    </div>
  );
}

/* — 네비 링크 — */
function MenuLink({ to, children, end }) {
  return (
    <NavLink
      to={to}
      end={end}
      style={({ isActive }) => ({
        whiteSpace: "nowrap",
        padding: "8px 12px",
        borderRadius: 10,
        textDecoration: "none",
        fontWeight: 600,
        color: isActive ? "#fff" : "#111827",
        background: isActive ? "#6d28d9" : "#eef2ff",
        border: isActive ? "1px solid #5b21b6" : "1px solid #e5e7eb",
      })}
    >
      {children}
    </NavLink>
  );
}

function NotFound() {
  return (
    <div style={{ padding: 16 }}>
      <h2>페이지를 찾을 수 없습니다</h2>
      <p style={{ color: "#6b7280" }}>주소를 확인해주세요.</p>
    </div>
  );
}

/* — 스타일 — */
const app = { fontFamily: "system-ui, -apple-system, Segoe UI, Roboto, sans-serif" };
const topbar = { position: "sticky", top: 0, zIndex: 50, background: "#fff", borderBottom: "1px solid #eee", padding: "12px 16px" };
const title = { margin: 0, fontSize: 20, lineHeight: 1.2 };
const nav = { display: "flex", gap: 8, marginTop: 10, overflowX: "auto", paddingBottom: 4 };
const main = { padding: 16 };
