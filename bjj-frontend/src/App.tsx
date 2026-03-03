/*
BJJ Progression Tracker - Single-file React app (TypeScript)
Save this file as: src/App.tsx inside a Vite + React + TypeScript project.
*/

import React, { useEffect, useState } from 'react'
import './App.css'

// --- Types (match your backend DTOs) ---
type Technique = {
  id: number
  name: string
  position: string
  numFinishes: number
  numTaps: number
}

type TechniqueCount = {
  rollID: number
  technique: Technique
  count: number
}

type Roll = {
  id: number
  lengthMinutes: number
  partner: string
  numRounds: number
  subs: TechniqueCount[]
  taps: TechniqueCount[]
}

type Session = {
  id: number
  date: string // ISO date
  time: string // HH:mm
  isGi: boolean
  instructor: string
  currentBelt: string
  rolls: Roll[]
}

// --- Helper: API client with configurable base URL ---
const STORAGE_KEY = 'http://localhost:8080/'

function useApiBaseUrl() {
  const [baseUrl, setBaseUrl] = useState<string>(() => {
    return localStorage.getItem(STORAGE_KEY) || ''
  })
  useEffect(() => {
    localStorage.setItem(STORAGE_KEY, baseUrl)
  }, [baseUrl])
  return { baseUrl, setBaseUrl }
}

async function apiFetch<T>(baseUrl: string, path: string, opts: RequestInit = {}): Promise<T> {
  const res = await fetch(baseUrl.replace(/\/$/, '') + path, {
    headers: { 'Content-Type': 'application/json' },
    ...opts,
  })
  if (!res.ok) {
    const text = await res.text()
    throw new Error(`API ${res.status} ${res.statusText}: ${text}`)
  }
  const contentType = res.headers.get('content-type') || ''
  if (contentType.includes('application/json')) return (await res.json()) as T
  return (null as unknown) as T
}

// --- Components ---

function Header({ baseUrl, setBaseUrl }: { baseUrl: string; setBaseUrl: (s: string) => void }) {
  const [value, setValue] = useState(baseUrl)
  return (
    <header className="app-header">
      <h1>BJJ Progression Tracker</h1>
      <div className="header-controls">
        <input className="input" placeholder="API Base URL (e.g. http://localhost:8080)" value={value} onChange={(e) => setValue(e.target.value)} />
        <button className="btn" onClick={() => setBaseUrl(value)}>Save</button>
      </div>
    </header>
  )
}

function SessionsList({ baseUrl, onSelect }: { baseUrl: string; onSelect: (s: Session | null) => void }) {
  const [sessions, setSessions] = useState<Session[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  async function load() {
    if (!baseUrl) return
    setLoading(true)
    setError(null)
    try {
      const data = await apiFetch<Session[]>(baseUrl, '/api/sessions')
      setSessions(data)
    } catch (e: any) {
      setError(e.message)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    load()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [baseUrl])

  return (
    <div className="card sessions-list">
      <div style={{display:'flex', justifyContent:'space-between', alignItems:'center', marginBottom:8}}>
        <h2 style={{margin:0}}>Sessions</h2>
        <div>
          <button className="btn success" style={{marginRight:8}} onClick={() => onSelect({ id: 0, date: new Date().toISOString().slice(0, 10), time: '18:00', isGi: true, instructor: '', currentBelt: '', rolls: [] })}>New</button>
          <button className="btn secondary" onClick={load}>Refresh</button>
        </div>
      </div>

      {loading && <div>Loading...</div>}
      {error && <div style={{color:'red'}}>{error}</div>}

      <ul>
        {sessions.map((s) => (
          <li key={s.id} className="session-item">
            <div>
              <div style={{fontWeight:600}}>{s.date} {s.time}</div>
              <div className="session-meta">Instructor: {s.instructor || '—'} | Belt: {s.currentBelt || '—'}</div>
            </div>
            <div style={{display:'flex', gap:8}}>
              <button className="btn" onClick={() => onSelect(s)}>View</button>
              <button className="btn danger" onClick={async () => {
                if (!confirm('Delete session?')) return
                try {
                  await apiFetch<void>(baseUrl, `/api/sessions/${s.id}`, { method: 'DELETE' })
                  setSessions((prev) => prev.filter((x) => x.id !== s.id))
                  onSelect(null)
                } catch (e: any) { alert('Delete failed: ' + e.message) }
              }}>Delete</button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  )
}

function SessionEditor({ baseUrl, session, onSaved }: { baseUrl: string; session: Session | null; onSaved: (s: Session | null) => void }) {
  const [local, setLocal] = useState<Session | null>(session)
  const [saving, setSaving] = useState(false)

  useEffect(() => setLocal(session), [session])

  if (!local) return <div className="card" style={{padding:12}}>Select a session to view/edit or create a new one.</div>

  async function save() {
    if (!baseUrl) return alert('Set API base URL first')
    if (!local) return
    setSaving(true)
    try {
      if (local.id === 0) {
        const created = await apiFetch<Session>(baseUrl, '/api/sessions', { method: 'POST', body: JSON.stringify(local) })
        alert('Created')
        onSaved(created)
      } else {
        const updated = await apiFetch<Session>(baseUrl, `/api/sessions/${local.id}`, { method: 'PUT', body: JSON.stringify(local) })
        alert('Updated')
        onSaved(updated)
      }
    } catch (e: any) {
      alert('Save failed: ' + e.message)
    } finally { setSaving(false) }
  }

  function updateField<K extends keyof Session>(key: K, value: Session[K]) {
    setLocal((prev) => (prev ? { ...prev, [key]: value } : prev))
  }

  function addRoll() {
    const newRoll: Roll = { id: 0, lengthMinutes: 5, partner: '', numRounds: 1, subs: [], taps: [] }
    setLocal((prev) => (prev ? { ...prev, rolls: [...prev.rolls, newRoll] } : prev))
  }

  function updateRoll(idx: number, patch: Partial<Roll>) {
    setLocal((prev) => {
      if (!prev) return prev
      const newRolls = prev.rolls.map((r, i) => (i === idx ? { ...r, ...patch } : r))
      return { ...prev, rolls: newRolls }
    })
  }

  function removeRoll(idx: number) {
    setLocal((prev) => (prev ? { ...prev, rolls: prev.rolls.filter((_, i) => i !== idx) } : prev))
  }

  return (
    <div className="card">
      <h3 style={{marginTop:0}}>Session Editor</h3>

      <div className="editor-row">
        <div className="label">Date</div>
        <input className="input" type="date" value={local.date} onChange={(e) => updateField('date', e.target.value)} />
        <div className="label">Time</div>
        <input className="input" type="time" value={local.time} onChange={(e) => updateField('time', e.target.value)} />
      </div>

      <div className="editor-row">
        <div className="label">Gi?</div>
        <input type="checkbox" checked={local.isGi} onChange={(e) => updateField('isGi', e.target.checked)} />
        <div className="label">Instructor</div>
        <input className="input" value={local.instructor} onChange={(e) => updateField('instructor', e.target.value)} />
        <div className="label">Belt</div>
        <input className="input" value={local.currentBelt} onChange={(e) => updateField('currentBelt', e.target.value)} />
      </div>

      <div style={{marginTop:8}}>
        <div style={{display:'flex', justifyContent:'space-between', alignItems:'center', marginBottom:8}}>
          <div style={{fontWeight:600}}>Rolls ({local.rolls.length})</div>
          <div>
            <button className="btn success" style={{marginRight:8}} onClick={addRoll}>Add Roll</button>
          </div>
        </div>

        <div>
          {local.rolls.map((r, idx) => (
            <div key={idx} className="roll-card">
              <div className="editor-row">
                <div className="label">Length (min)</div>
                <input className="input" type="number" value={r.lengthMinutes} onChange={(e) => updateRoll(idx, { lengthMinutes: Number(e.target.value) })} />
                <div className="label">Partner</div>
                <input className="input" value={r.partner} onChange={(e) => updateRoll(idx, { partner: e.target.value })} />
                <div className="label">#Rounds</div>
                <input className="input" type="number" value={r.numRounds} onChange={(e) => updateRoll(idx, { numRounds: Number(e.target.value) })} />
              </div>

              <div style={{display:'flex', gap:8}}>
                <button className="btn secondary" onClick={() => alert('TechniqueCount editing will be added later as requested')}>Manage subs/taps</button>
                <button className="btn danger" onClick={() => removeRoll(idx)}>Remove roll</button>
              </div>
            </div>
          ))}
        </div>
      </div>

      <div style={{marginTop:12, display:'flex', gap:8}}>
        <button className="btn" onClick={save} disabled={saving}>{saving ? 'Saving...' : 'Save Session'}</button>
        <button className="btn secondary" onClick={() => onSaved(null)}>Close</button>
      </div>

    </div>
  )
}

export default function App() {
  const { baseUrl, setBaseUrl } = useApiBaseUrl()
  const [selectedSession, setSelectedSession] = useState<Session | null>(null)

  return (
    <div>
      <Header baseUrl={baseUrl} setBaseUrl={setBaseUrl} />
      <main className="container">
        <section className="card">
          <SessionsList baseUrl={baseUrl} onSelect={(s) => setSelectedSession(s)} />
        </section>
        <section className="card">
          <SessionEditor baseUrl={baseUrl} session={selectedSession} onSaved={(s) => setSelectedSession(s)} />
        </section>
      </main>
      <div className="footer">Tip: make sure your backend allows CORS for this origin (http://localhost:5173 by default for Vite).</div>
    </div>
  )
}
