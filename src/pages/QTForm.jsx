// src/pages/QTForm.jsx
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { qtStore } from "../lib/store";

export default function QTForm() {
  const nav = useNavigate();
  const { id } = useParams();
  const [form, setForm] = useState({ title: "", content: "", weekOf: "" });

  useEffect(() => {
    if (id) {
      const data = qtStore.getById(id);
      if (data) setForm(data);
    }
  }, [id]);

  const save = () => {
    if (!form.title.trim()) return alert("제목을 입력하세요.");
    if (!form.weekOf.trim()) return alert("주차(예: 2025W39)를 입력하세요.");
    qtStore.upsert(form);
    nav("/qt");
  };

  return (
    <div style={{ display: "grid", gap: 12 }}>
      <h1>{id ? "QT 수정" : "QT 작성"}</h1>
      <input
        placeholder="제목"
        value={form.title}
        onChange={(e) => setForm({ ...form, title: e.target.value })}
        style={input}
      />
      <input
        placeholder="주차 (예: 2025W39)"
        value={form.weekOf}
        onChange={(e) => setForm({ ...form, weekOf: e.target.value })}
        style={input}
      />
      <textarea
        placeholder="본문"
        rows={8}
        value={form.content}
        onChange={(e) => setForm({ ...form, content: e.target.value })}
        style={input}
      />
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
