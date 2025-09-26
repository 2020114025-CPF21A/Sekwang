// src/pages/Albums.jsx
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { albumStore } from "../lib/store";

export default function Albums() {
  const [items, setItems] = useState([]);

  useEffect(() => { setItems(albumStore.list()); }, []);

  const remove = (id) => {
    if (!confirm("앨범을 삭제할까요? (사진도 함께 삭제)")) return;
    albumStore.remove(id);
    setItems(albumStore.list());
  };

  return (
    <div style={{ display: "grid", gap: 12 }}>
      <h1>사진첩</h1>
      <Link to="/albums/new"><button style={btn}>+ 새 앨범</button></Link>

      {items.length === 0 ? (
        <div style={empty}>등록된 앨범이 없습니다.</div>
      ) : (
        <div style={grid}>
          {items.map((a) => (
            <div key={a.id} style={card}>
              {a.coverUrl ? (
                <img src={a.coverUrl} alt="" style={cover} />
              ) : (
                <div style={{ ...cover, background: "#f3f4f6" }} />
              )}
              <div style={{ marginTop: 8 }}>
                <Link to={`/albums/${a.id}`} style={title}>{a.title || "(제목 없음)"}</Link>
                <div style={{ color: "#6b7280", fontSize: 12 }}>
                  {new Date(a.createdAt).toLocaleDateString("ko-KR")} · 사진 {a.photos?.length || 0}장
                </div>
                <div style={{ marginTop: 8, display: "flex", gap: 8 }}>
                  <Link to={`/albums/${a.id}`}><button>열기</button></Link>
                  <button onClick={() => remove(a.id)}>삭제</button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

const btn = { padding: "8px 12px", borderRadius: 8, border: "1px solid #ddd", background: "#fef3c7" };
const empty = { padding: 12, border: "1px dashed #ddd", borderRadius: 8, color: "#6b7280" };
const grid = { display: "grid", gap: 12, gridTemplateColumns: "repeat(auto-fill, minmax(220px, 1fr))" };
const card = { border: "1px solid #eee", borderRadius: 12, padding: 10, background: "#fff" };
const cover = { width: "100%", height: 140, objectFit: "cover", borderRadius: 8 };
const title = { textDecoration: "none", color: "#111827", fontWeight: 600 };
