// src/pages/MonthlyTracker.jsx
import { useEffect, useMemo, useState } from "react";
import { attendanceSessionStore, rosterStore } from "../lib/store";

export default function MonthlyTracker() {
  const [month, setMonth] = useState(monthStr(new Date())); // YYYY-MM
  const [sessions, setSessions] = useState([]);
  const [selectedId, setSelectedId] = useState(null);

  useEffect(() => {
    rosterStore.ensureSeed();
    setSessions(attendanceSessionStore.list());
  }, []);

  const roster = rosterStore.list();

  const days = useMemo(() => buildMonth(month), [month]);

  // 선택된 사람의 출석 기록 추출
  const filledSet = useMemo(() => {
    if (!selectedId) return new Set();
    const s = new Set();
    const prefix = month + "-"; // YYYY-MM-
    sessions.forEach((sess) => {
      if (!sess.date?.startsWith(prefix)) return;
      if (sess.checks && sess.checks[selectedId]) {
        s.add(sess.date);
      }
    });
    return s;
  }, [sessions, selectedId, month]);

  const filledCount = days.filter((d) => d && filledSet.has(d.iso)).length;

  return (
    <div style={{ padding: 16 }}>
      <header style={{ marginBottom: 16 }}>
        <h1 style={{ margin: 0 }}>월간 출석표</h1>
        <MonthPicker value={month} onChange={setMonth} />
      </header>

      <div style={{ display: "flex", gap: 24 }}>
        {/* 왼쪽: 이름 목록 */}
        <div style={{ minWidth: 200 }}>
          <h2 style={{ fontSize: 16, marginBottom: 8 }}>명단</h2>
          <ul style={{ listStyle: "none", padding: 0, margin: 0 }}>
            {roster.map((r) => (
              <li key={r.id}>
                <button
                  onClick={() => setSelectedId(r.id)}
                  style={{
                    ...nameBtn,
                    background: selectedId === r.id ? "#ddd" : "#fff",
                  }}
                >
                  {r.name}
                </button>
              </li>
            ))}
          </ul>
        </div>

        {/* 오른쪽: 선택된 사람의 달력 */}
        <div style={{ flex: 1 }}>
          {selectedId ? (
            <>
              <h2 style={{ fontSize: 18 }}>
                {roster.find((r) => r.id === selectedId)?.name} —{" "}
                {month} 출석 {filledCount}회
              </h2>
              <div style={grid}>
                {days.map((d, i) =>
                  d ? (
                    <Cell
                      key={d.iso}
                      iso={d.iso}
                      label={d.day}
                      on={filledSet.has(d.iso)}
                    />
                  ) : (
                    <div key={`empty-${i}`} />
                  )
                )}
              </div>
            </>
          ) : (
            <p style={{ color: "#666" }}>← 왼쪽에서 이름을 선택하세요</p>
          )}
        </div>
      </div>
    </div>
  );
}

function Cell({ iso, label, on }) {
  return (
    <div
      style={{
        ...cell,
        background: on ? "#7c3aed" : "#f3f4f6",
        color: on ? "#fff" : "#111827",
      }}
      title={iso}
    >
      {label}
    </div>
  );
}

function MonthPicker({ value, onChange }) {
  const onPrev = () => onChange(shiftMonth(value, -1));
  const onNext = () => onChange(shiftMonth(value, 1));
  return (
    <div style={{ display: "flex", alignItems: "center", gap: 8 }}>
      <button onClick={onPrev} style={btn}>◀ 이전달</button>
      <input
        type="month"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        style={monthInput}
      />
      <button onClick={onNext} style={btn}>다음달 ▶</button>
    </div>
  );
}

/* ===== 스타일 ===== */
const grid = {
  display: "grid",
  gridTemplateColumns: "repeat(7, 1fr)",
  gap: 10,
  marginTop: 16,
};
const cell = {
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
  height: 64,
  borderRadius: 999,
  fontWeight: 700,
};
const btn = { padding: "6px 12px", border: "1px solid #ddd", borderRadius: 8, background: "#fff" };
const monthInput = { padding: 6, border: "1px solid #ccc", borderRadius: 6 };
const nameBtn = {
  width: "100%",
  textAlign: "left",
  padding: "8px 12px",
  marginBottom: 4,
  border: "1px solid #ddd",
  borderRadius: 6,
  cursor: "pointer",
};

/* ===== 유틸 ===== */
function monthStr(d) {
  const y = d.getFullYear();
  const m = String(d.getMonth() + 1).padStart(2, "0");
  return `${y}-${m}`;
}
function buildMonth(ym) {
  const [y, m] = ym.split("-").map(Number);
  const first = new Date(y, m - 1, 1);
  const last = new Date(y, m, 0);
  const firstIdx = (first.getDay() + 6) % 7; // 월=0
  const total = last.getDate();
  const arr = [];
  for (let i = 0; i < firstIdx; i++) arr.push(null);
  for (let d = 1; d <= total; d++) {
    const iso = `${ym}-${String(d).padStart(2, "0")}`;
    arr.push({ iso, day: d });
  }
  return arr;
}
function shiftMonth(ym, delta) {
  const [y, m] = ym.split("-").map(Number);
  const d = new Date(y, m - 1 + delta, 1);
  return monthStr(d);
}
