// src/pages/AlbumForm.jsx
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { albumStore } from "../lib/store";

export default function AlbumForm() {
  const nav = useNavigate();
  const { id } = useParams();
  const [form, setForm] = useState({ title: "", coverUrl: "", photos: [] });
  const [photoUrl, setPhotoUrl] = useState("");
  const [caption, setCaption] = useState("");

  useEffect(() => {
    if (id) {
      const data = albumStore.getById(id);
      if (data) setForm(data);
    }
  }, [id]);

  const save = () => {
    if (!form.title.trim()) return alert("앨범 제목을 입력하세요.");
    albumStore.upsert(form);
    nav("/albums");
  };

  const addPhoto = () => {
    if (!photoUrl.trim()) return;
    if (!form.id) {
      // 새 앨범 먼저 저장해서 id 확보
      const saved = albumStore.upsert(form);
      setForm(saved);
    }
    const updated = albumStore.addPhoto(form.id, photoUrl, caption);
    setForm({ ...updated });
    setPhotoUrl("");
    setCaption("");
  };

  const removePhoto = (pid) => {
    const updated = albumStore.removePhoto(form.id, pid);
    setForm({ ...updated });
  };

  return (
    <div style={{ display: "grid", gap: 12 }}>
      <h1>{id ? "앨범 수정" : "앨범 생성"}</h1>

      <input
        placeholder="앨범 제목"
        value={form.title}
        onChange={(e) => setForm({ ...form, title: e.target.value })}
        style={input}
      />
      <input
        placeholder="커버 이미지 URL"
        value={form.coverUrl}
        onChange={(e) => setForm({ ...form, coverUrl: e.target.value })}
        style={input}
      />

      <div style={photoBox}>
        <h3 style={{ margin: 0 }}>사진 추가</h3>
        <input
          placeholder="사진 URL"
          value={photoUrl}
          onChange={(e) => setPhotoUrl(e.target.value)}
          style={input}
        />
        <input
          placeholder="캡션 (선택)"
          value={caption}
          onChange={(e) => setCaption(e.target.value)}
          style={input}
        />
        <button onClick={addPhoto} style={btnAdd}>+ 사진 추가</button>

        <div style={thumbGrid}>
          {(form.photos || []).map((p) => (
            <div key={p.id} style={thumbCard}>
              <img src={p.url} alt="" style={thumbImg} />
              <div style={{ fontSize: 12, color: "#374151", marginTop: 4 }}>{p.caption || ""}</div>
              <button onClick={() => removePhoto(p.id)} style={btnDel}>삭제</button>
            </div>
          ))}
        </div>
      </div>

      <div style={{ display: "flex", gap: 8 }}>
        <button onClick={save} style={btnPrimary}>저장</button>
        <button onClick={() => nav(-1)} style={btn}>취소</button>
      </div>
    </div>
  );
}

const input = { padding: 10, border: "1px solid #e5e7eb", borderRadius: 8 };
const btn = { padding: "10px 14px", borderRadius: 8, border: "1px solid #ddd", background: "#f3f4f6" };
const btnPrimary = { ...btn, background: "#dcfce7" };
const btnAdd = { padding: "8px 12px", borderRadius: 8, border: "1px solid #ddd", background: "#e0e7ff", marginTop: 6 };
const btnDel = { padding: "6px 10px", borderRadius: 8, border: "1px solid #ddd", background: "#fee2e2", marginTop: 6 };
const photoBox = { border: "1px solid #eee", borderRadius: 12, padding: 12, background: "#fff" };
const thumbGrid = { display: "grid", gap: 10, gridTemplateColumns: "repeat(auto-fill, minmax(140px, 1fr))", marginTop: 10 };
const thumbCard = { border: "1px solid #eee", borderRadius: 10, padding: 8, background: "#fafafa" };
const thumbImg = { width: "100%", height: 100, objectFit: "cover", borderRadius: 6 };
