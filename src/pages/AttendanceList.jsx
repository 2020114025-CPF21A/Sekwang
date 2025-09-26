// src/pages/AttendanceList.jsx
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { attendanceSessionStore, rosterStore } from "../lib/store";

export default function AttendanceList() {
  const [items, setItems] = useState([]);

  useEffect(() => {
    rosterStore.ensureSeed?.();         // 안전장치 (있으면 호출)
    setItems(attendanceSessionStore.list?.() || []);
  }, []);

  if (!items.length) {
    return (
      <div style={{ padding:16 }}>
        <h1>출석 현황</h1>
        <div style={{ padding:12, border:"1px dashed #ddd", borderRadius:8, color:"#6b7280" }}>
          저장된 출석이 없습니다. <Link to="/attendance/board">출석 보드</Link>에서 먼저 저장하세요.
        </div>
      </div>
    );
  }

  const total = rosterStore.list?.().length || 0;

  return (
    <div style={{ padding:16 }}>
      <h1>출석 현황</h1>
      <table style={{ width:"100%", borderCollapse:"collapse", border:"1px solid #eee" }}>
        <thead><tr><th>날짜</th><th>출석</th><th>미출석</th><th>수정</th></tr></thead>
        <tbody>
          {items.map(s => {
            const checks = s.checks || {};
            const present = Object.values(checks).filter(Boolean).length;
            const absent = Math.max(total - present, 0);
            return (
              <tr key={s.id}>
                <td>{s.date}</td>
                <td>{present}</td>
                <td>{absent}</td>
                <td><Link to={`/attendance/board?date=${s.date}`}><button>열기</button></Link></td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
