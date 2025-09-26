// src/pages/NoticeForm.jsx
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { noticeStore } from "../lib/store";

export default function NoticeForm() {
  const nav = useNavigate();
  const { id } = useParams();
  const [form, setForm] = useState({ title:"", content:"", pinned:false });

  useEffect(()=>{
    if (id) {
      const data = noticeStore.getById(id);
      if (data) setForm(data);
    }
  },[id]);

  const save = () => {
    if (!form.title.trim()) return alert("제목을 입력하세요.");
    noticeStore.upsert(form);
    nav("/notices");
  };

  return (
    <div style={{ display:"grid", gap:12 }}>
      <h1>{id ? "공지 수정" : "공지 작성"}</h1>
      <input
        placeholder="제목"
        value={form.title}
        onChange={(e)=>setForm({ ...form, title:e.target.value })}
        style={input}
      />
      <textarea
        placeholder="내용"
        rows={8}
        value={form.content}
        onChange={(e)=>setForm({ ...form, content:e.target.value })}
        style={input}
      />
      <label><input type="checkbox" checked={form.pinned} onChange={(e)=>setForm({...form, pinned:e.target.checked})}/> 핀 고정</label>
      <div style={{ display:"flex", gap:8 }}>
        <button onClick={save} style={btnPrimary}>저장</button>
        <button onClick={()=>nav(-1)} style={btn}>취소</button>
      </div>
    </div>
  );
}
const input = { padding:10, border:"1px solid #e5e7eb", borderRadius:8 };
const btn = { padding:"10px 14px", borderRadius:8, border:"1px solid #ddd", background:"#f3f4f6" };
const btnPrimary = { ...btn, background:"#e0e7ff" };
