import { Link } from "react-router-dom";

export default function Home() {
  const today = new Date().toLocaleDateString("ko-KR");
  const cards = [
    { to: "/attendance/board", label: "출석보드" },
    { to: "/qt",         label: "QT" },
    { to: "/notices",    label: "공지사항" },
    { to: "/albums",     label: "사진첩" },
    { to: "/bulletins",  label: "주보" },
    { to: "/music",      label: "찬양 악보" },
  ];

  return (
    <div style={{ display: "grid", gap: 12 }}>
      <h1 style={{ margin: 0 }}>홈</h1>
      <p style={{ color: "#6b7280" }}>{today} 기준</p>
      <div style={{ display: "grid", gap: 12, gridTemplateColumns: "repeat(auto-fill, minmax(180px, 1fr))" }}>
        {cards.map(c => (
          <Link key={c.to} to={c.to} style={card}>{c.label}</Link>
        ))}
      </div>
    </div>
  );
}
const card = {
  padding: 16,
  border: "1px solid #e5e7eb",
  borderRadius: 12,
  background: "#fff",
  textDecoration: "none",
  color: "#111827",
  boxShadow: "0 1px 2px rgba(0,0,0,0.04)",
};
