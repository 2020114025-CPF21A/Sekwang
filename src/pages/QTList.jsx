// src/pages/QTList.jsx
import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import { qtStore } from "../lib/store";

export default function QTList() {
  const [items, setItems] = useState([]);

  useEffect(() => {
    setItems(qtStore.list());
  }, []);

  const remove = (id) => {
    if (!confirm("삭제할까요?")) return;
    qtStore.remove(id);
    setItems(qtStore.list());
  };

  return (
    <div style={{ display: "grid", gap: 12 }}>
      <h1>QT</h1>
      <Link to="/qt/new"><button style={btn}>+ 새 QT</button></Link>

      {items.length === 0 ? (
        <div style={empty}>아직 작성된 QT가 없습니다.</div>
      ) : (
        <table style={table}>
          <thead>
            <tr><th>제목</th><th>주차</th><th>작성일</th><th></th></tr>
          </thead>
          <tbody>
            {items.map(it => (
              <tr key={it.id}>
                <td><Link to={`/qt/${it.id}`}>{it.title}</Link></td>
                <td>{it.weekOf}</td>
                <td>{new Date(it.createdAt).toLocaleString()}</td>
                <td><button onClick={() => remove(it.id)}>삭제</button></td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

const btn = { padding: "8px 12px", borderRadius: 8, border: "1px solid #ddd", background: "#e0e7ff" };
const table = { width: "100%", borderCollapse: "collapse", border: "1px solid #eee" };
const empty = { padding: 12, border: "1px dashed #ddd", borderRadius: 8, color: "#6b7280" };
