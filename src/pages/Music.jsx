// src/pages/Music.jsx
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { musicStore } from "../lib/store";

export default function Music() {
  const [items, setItems] = useState([]);

  useEffect(() => { setItems(musicStore.list()); }, []);

  const remove = (id) => {
    if (!confirm("악보를 삭제할까요?")) return;
    musicStore.remove(id);
    setItems(musicStore.list());
  };

  return (
    <div style={{ display: "grid", gap: 12 }}>
      <h1>찬양 악보</h1>
      <Link to="/music/new"><button style={btn}>+ 업로드</button></Link>

      {items.length === 0 ? (
        <div style={empty}>등록된 악보가 없습니다.</div>
      ) : (
        <table style={table}>
          <thead><tr><th>제목</th><th>파일</th><th></th></tr></thead>
          <tbody>
            {items.map((m) => (
              <tr key={m.id}>
                <td>{m.title || "-"}</td>
                <td>{m.fileUrl ? <a href={m.fileUrl} target="_blank" rel="noreferrer">열기</a> : "-"}</td>
                <td>
                  <Link to={`/music/${m.id}`}><button>수정</button></Link>{" "}
                  <button onClick={() => remove(m.id)}>삭제</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

const btn = { padding: "8px 12px", borderRadius: 8, border: "1px solid #ddd", background: "#f1f5f9" };
const empty = { padding: 12, border: "1px dashed #ddd", borderRadius: 8, color: "#6b7280" };
const table = { width: "100%", borderCollapse: "collapse", border: "1px solid #eee" };
