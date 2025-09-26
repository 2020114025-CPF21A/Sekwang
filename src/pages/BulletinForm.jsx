// src/pages/BulletinForm.jsx
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { bulletinStore } from "../lib/store";

export default function BulletinForm() {
  const nav = useNavigate();
  const { id } = useParams();
  const [form, setForm] = useState({ date: "", fileUrl: "" });

  useEffect(() => {
    if (id) {
      const data = bulletinStore.getById(id);
      if (data) setForm(data);
    }
  }, [id]);

  const save = () => {
    if (!form.date.trim()) return alert("날짜(YYYY-MM-DD)를 입력하세요.");
    if (!form.fileUrl.trim()) return alert("파일 URL을 입력하세요. (이미지 또는 PDF)");
    bulletinStore.upsert(form);
    nav("/bulletins");
  };

  return (
    <div style={{ display: "grid", gap: 12 }}>
      <h1>{id ? "주보 수정" : "주보 업로드"}</h1>
      <input
        placeholder="날짜 (YYYY-MM-DD)"
        value={form.date}
        onChange={(e) => setForm({ ...form, date: e.target.value })}
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

      {/* 간단 미리보기 (이미지 URL일 때만) */}
      {form.fileUrl && isImage(form.fileUrl) && (
        <div style={{ marginTop: 8 }}>
          <img src={form.fileUrl} alt="" style={{ maxWidth: "100%", borderRadius: 8, border: "1px solid #eee" }} />
        </div>
      )}
    </div>
  );
}

function isImage(url) {
  return /\.(png|jpg|jpeg|gif|webp)$/i.test(url);
}

const input = { padding: 10, border: "1px solid #e5e7eb", borderRadius: 8 };
const btn = { padding: "10px 14px", borderRadius: 8, border: "1px solid #ddd", background: "#f3f4f6" };
const btnPrimary = { ...btn, background: "#e0e7ff" };
