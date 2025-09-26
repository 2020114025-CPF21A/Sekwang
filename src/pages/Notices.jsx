import { useState } from "react";
export default function Notices() {
  const [list, setList] = useState([]);
  const [form, setForm] = useState({ title: "", content: "", pinned: false });

  const add = () => {
    const item = { ...form, id: crypto.randomUUID(), createdAt: new Date().toISOString() };
    setList((prev) => [item, ...prev]);
    setForm({ title: "", content: "", pinned: false });
  };

  return (
    <div style={{ display: "grid", gap: 12 }}>
      <h1>공지사항</h1>
      <input placeholder="제목" value={form.title} onChange={(e)=>setForm({...form, title:e.target.value})} style={input}/>
      <textarea placeholder="내용" rows={4} value={form.content} onChange={(e)=>setForm({...form, content:e.target.value})} style={input}/>
      <label><input type="checkbox" checked={form.pinned} onChange={(e)=>setForm({...form, pinned:e.target.checked})}/> 핀 고정</label>
      <button onClick={add} style={btn}>등록</button>

      <ul style={{ padding: 0, listStyle: "none" }}>
        {list.map((n) => (
          <li key={n.id} style={noticeItem}>
            <strong>{n.title}</strong> {n.pinned ? "📌" : ""} <span style={{ color: "#6b7280" }}>{new Date(n.createdAt).toLocaleString()}</span>
            <p style={{ margin: "6px 0 0" }}>{n.content}</p>
          </li>
        ))}
      </ul>
    </div>
  );
}
const input = { padding: 10, border: "1px solid #e5e7eb", borderRadius: 8 };
const btn = { padding: "10px 14px", borderRadius: 8, border: "1px solid #ddd", background: "#dcfce7" };
const noticeItem = { border: "1px solid #eee", borderRadius: 8, padding: 12, marginTop: 10, background: "#fff" };
