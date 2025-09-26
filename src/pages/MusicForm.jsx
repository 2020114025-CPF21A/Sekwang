// src/pages/MusicForm.jsx
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { musicStore } from "../lib/store";

export default function MusicForm() {
  const nav = useNavigate();
  const { id } = useParams();
  const [form, setForm] = useState({ title: "", fileUrl: "" });

  useEffect(() => {
    if (id) {
      const data = musicStore.getById(id);
      if (data) setForm(data);
    }
  }, [id]);

  const save = () => {
    if (!form.title.trim()) return alert("제목을 입력하세요.");
    if (!form.fileUrl.trim()) return alert("파일 URL을 입력하세요. (이미지 또는 PDF)");
    musicStore.upsert(form);
    nav("/music");
  };

  return (
    <div style={{ display: "grid", gap: 12 }}>
      <h1>{id ? "악보 수정" : "악보 업로드"}</h1>
      <input
        placeholder="제목"
        value={form.title}
        onChange={(e) => setForm({ ...form, title: e.target.value })}
        style={input}
      />
      <input
        placeholder="파일 URL (이미지/PDF)"
        value={form.fileUrl}
        onChange={(e) => setForm({ ...form, fileUrl: e.target.value })}
        style={input}
      />
      <div style={{ display: "flex", gap: 8 }}>
        <button onClick={save} style={btnPrimary}>저장</button>
        <button onClick={() => nav(-1)} style={btn}>취소</button>
      </div>

      {/* 이미지면 미리보기 */}
      {form.fileUrl && /\.(png|jpg|jpeg|gif|webp)$/i.test(form.fileUrl) && (
        <div style={{ marginTop: 8 }}>
          <img src={form.fileUrl} alt="" style={{ maxWidth: "100%", borderRadius: 8, border: "1px solid #eee" }} />
        </div>
      )}
    </div>
  );
}

const input = { padding: 10, border: "1px solid #e5e7eb", borderRadius: 8 };
const btn = { padding: "10px 14px", borderRadius: 8, border: "1px solid #ddd", background: "#f3f4f6" };
const btnPrimary = { ...btn, background: "#e0e7ff" };
