// src/pages/AttendanceBoard.jsx
import { useEffect, useMemo, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { rosterStore, attendanceSessionStore } from "../lib/store";

// 태블릿 가로 기준 열 개수
const COLS = 6;

export default function AttendanceBoard() {
  const nav = useNavigate();
  const [sp, setSp] = useSearchParams();

  const [date, setDate] = useState(sp.get("date") || todayStr());
  const [checks, setChecks] = useState({});
  const [roster, setRoster] = useState([]);

  // 최초 로스터 준비(버전 체크 포함)
  useEffect(() => {
    rosterStore.ensureSeed();
    setRoster(rosterStore.list());
  }, []);

  // 날짜 변경 시 세션 로드 + URL 동기화
  useEffect(() => {
    const sess = attendanceSessionStore.get(date);
    setChecks(sess?.checks || {});
    setSp({ date }, { replace: true });
  }, [date, setSp]);

  const presentCount = useMemo(
    () => Object.values(checks).filter(Boolean).length,
    [checks]
  );

  const toggle = (id) => {
    setChecks((prev) => ({ ...prev, [id]: !prev[id] }));
  };

  const submit = () => {
    attendanceSessionStore.upsert({ date, checks: checks || {} });
    alert(`저장 완료: ${date} · 출석 ${presentCount}명`);
  };

  // 보기 좋게 N열로 분할
  const columns = useMemo(
    () => chunk(roster, Math.ceil((roster?.length || 0) / COLS)),
    [roster]
  );

  // (선택) 명단 초기화 버튼: 로컬 저장된 명단을 지우고 시드로 재적용
  const resetRoster = () => {
    rosterStore.clear();
    rosterStore.ensureSeed();
    setRoster(rosterStore.list());
  };

  return (
    <div style={{ padding: 16 }}>
      <header style={{ display: "flex", alignItems: "center", gap: 12, marginBottom: 12 }}>
        <h1 style={{ margin: 0 }}>출석 보드</h1>
        <input type="date" value={date} onChange={(e) => setDate(e.target.value)} style={dateInput} />
        <button onClick={() => nav("/attendance/list")} style={btn}>현황 리스트</button>
        <button onClick={resetRoster} style={btn}>명단 초기화</button>
        <div style={{ marginLeft: "auto", fontWeight: 600 }}>
          출석 {presentCount} / {roster.length}
        </div>
      </header>

      <div style={boardWrap}>
        {columns.map((col, ci) => (
          <div key={ci} style={colStyle}>
            {col.map((s) => (
              <StudentRow
                key={s.id}
                student={s}
                checked={!!checks[s.id]}
                onToggle={() => toggle(s.id)}
              />
            ))}
          </div>
        ))}
      </div>

      <footer style={{ marginTop: 16, display: "flex", gap: 8 }}>
        <button onClick={submit} style={btnPrimary}>제출(저장)</button>
        <button onClick={() => setChecks({})} style={btn}>전체 초기화</button>
      </footer>
    </div>
  );
}

function StudentRow({ student, checked, onToggle }) {
  return (
    <div style={row}>
      <div style={{ whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis" }}>
        {student.name}
      </div>
      <button
        onClick={onToggle}
        style={{
          ...chip,
          background: checked ? "#22c55e" : "#e5e7eb",
          color: checked ? "#fff" : "#111827",
        }}
        aria-pressed={checked}
      >
        {checked ? "출석" : "미출석"}
      </button>
    </div>
  );
}

/* ===== 스타일 ===== */
const dateInput = { padding: 8, border: "1px solid #e5e7eb", borderRadius: 8 };
const btn = { padding: "10px 14px", borderRadius: 8, border: "1px solid #ddd", background: "#f3f4f6", cursor: "pointer" };
const btnPrimary = { ...btn, background: "#dcfce7" };
const boardWrap = { display: "grid", gridTemplateColumns: `repeat(${COLS}, 1fr)`, gap: 8 };
const colStyle = { display: "grid", gap: 8 };
const row = {
  display: "flex",
  alignItems: "center",
  justifyContent: "space-between",
  gap: 8,
  padding: "10px 12px",
  border: "1px solid #e5e7eb",
  borderRadius: 10,
  background: "#fff",
};
const chip = { padding: "6px 10px", border: "none", borderRadius: 999, cursor: "pointer", fontWeight: 600 };

/* ===== 유틸 ===== */
function todayStr() {
  return new Date().toISOString().slice(0, 10);
}
function chunk(arr, size) {
  const out = [];
  for (let i = 0; i < arr.length; i += size) out.push(arr.slice(i, i + size));
  return out;
}
