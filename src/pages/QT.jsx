import { useState } from "react";
export default function QT() {
  const [list, setList] = useState([]);
  const [form, setForm] = useState({ title: "", content: "", weekOf: "" });

  const add = () => {
    const item = { ...form, id: crypto.randomUUID(), createdAt: new Date().toISOString() };
    setList((prev) => [item, ...prev]);
    setForm({ title: "", content: "", weekOf: "" });
  };

  return (
    <div style={{ display: "grid", gap: 12 }}>
      <h1>QT</h1>
      <input placeholder="제목" value={form.title} onChange={(e)=>setForm({...form, title:e.target.value})} style={input}/>
      <input placeholder="주차 (예: 2025W39)" value={form.weekOf} onChange={(e)=>setForm({...form, weekOf:e.target.value})} style={input}/>
      <textarea placeholder="내용" rows={5} value={form.content} onChange={(e)=>setForm({...form, content:e.target.value})} style={input}/>
      <div>
        <button onClick={add} style={btn}>추가</button>
      </div>

      <table style={table}>
        <thead><tr><th>제목</th><th>주차</th><th>작성일</th></tr></thead>
        <tbody>
          {list.map(it=>(
            <tr key={it.id}>
              <td>{it.title}</td>
              <td>{it.weekOf}</td>
              <td>{new Date(it.createdAt).toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
const input = { padding: 10, border: "1px solid #e5e7eb", borderRadius: 8 };
const btn = { padding: "10px 14px", borderRadius: 8, border: "1px solid #ddd", background: "#e0e7ff" };
const table = { width: "100%", borderCollapse: "collapse", border: "1px solid #eee" };
