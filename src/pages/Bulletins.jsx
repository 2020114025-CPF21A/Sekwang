// src/pages/Bulletins.jsx
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { bulletinStore } from "../lib/store";

export default function Bulletins() {
  const [items, setItems] = useState([]);

  useEffect(() => { setItems(bulletinStore.list()); }, []);

  const remove = (id) => {
    if (!confirm("주보를 삭제할까요?")) return;
    bulletinStore.remove(id);
    setItems(bulletinStore.list());
  };

  return (
    <div style={{ display: "grid", gap: 12 }}>
      <h1>주보</h1>
      <Link to="/bulletins/new"><button style={btn}>+ 업로드</button></Link>

      {items.length === 0 ? (
        <div style={empty}>등록된 주보가 없습니다.</div>
      ) : (
        <table style={table}>
          <thead><tr><th>날짜</th><th>파일</th><th></th></tr></thead>
          <tbody>
            {items.map((b) => (
              <tr key={b.id}>
                <td>{b.date || "-"}</td>
                <td>
                  {b.fileUrl ? (
                    <a href={b.fileUrl} target="_blank" rel="noreferrer">열기</a>
                  ) : "-"}
                </td>
                <td>
                  <Link to={`/bulletins/${b.id}`}><button>수정</button></Link>{" "}
                  <button onClick={() => remove(b.id)}>삭제</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

const btn = { padding: "8px 12px", borderRadius: 8, border: "1px solid #ddd", background: "#e5e7eb" };
const empty = { padding: 12, border: "1px dashed #ddd", borderRadius: 8, color: "#6b7280" };
const table = { width: "100%", borderCollapse: "collapse", border: "1px solid #eee" };
