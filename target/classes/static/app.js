const el = (id) => document.getElementById(id);
const err = el('error');
const msg = el('msg');

function setError(text) { err.textContent = text; msg.textContent = ''; }
function setMsg(text) { msg.textContent = text; err.textContent = ''; }

const DECATHLON_EVENTS = [
  ['100m', '100m (s)'],
  ['110mHurdles', '110m Hurdles (s)'],
  ['400m', '400m (s)'],
  ['1500m', '1500m (s)'],
  ['longJump', 'Long Jump (cm)'],
  ['highJump', 'High Jump (cm)'],
  ['poleVault', 'Pole Vault (cm)'],
  ['shotPut', 'Shot Put (m)'],
  ['discusThrow', 'Discus Throw (m)'],
  ['javelinThrow', 'Javelin Throw (m)']
];

const HEPTATHLON_EVENTS = [
  ['hep100mHurdles', '100m Hurdles (s)'],
  ['hep200m', '200m (s)'],
  ['hep800m', '800m (s)'],
  ['hepLongJump', 'Long Jump (cm)'],
  ['hepHighJump', 'High Jump (cm)'],
  ['hepShotPut', 'Shot Put (m)'],
  ['hepJavelinThrow', 'Javelin Throw (m)']
];

function populateEvents() {
  const events = el('discipline').value === 'heptathlon' ? HEPTATHLON_EVENTS : DECATHLON_EVENTS;
  el('event').innerHTML = events.map(([value, label]) => `<option value="${value}">${label}</option>`).join('');
}

el('discipline').addEventListener('change', populateEvents);
populateEvents();

el('add').addEventListener('click', async () => {
  const name = el('name').value;
  try {
    const res = await fetch('/api/competitors', {
      method: 'POST', headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name })
    });
    if (!res.ok) {
      const t = await res.text();
      setError(t || 'Failed to add competitor');
    } else {
      setMsg('Added');
    }
    await renderStandings();
  } catch (e) {
    setError('Network error');
  }
});

el('save').addEventListener('click', async () => {
  const body = {
    name: el('name2').value,
    event: el('event').value,
    raw: parseFloat(el('raw').value)
  };
  try {
    const res = await fetch('/api/score', {
      method: 'POST', headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    });
    const json = await res.json();
    setMsg(`Saved: ${json.points} pts`);
    await renderStandings();
  } catch (e) {
    setError('Score failed');
  }
});

let sortBroken = false;

el('export').addEventListener('click', async () => {
  try {
    const res = await fetch('/api/export.csv');
    const text = await res.text();
    const blob = new Blob([text], { type: 'text/csv;charset=utf-8' });
    const a = document.createElement('a');
    a.href = URL.createObjectURL(blob);
    a.download = 'results.csv';
    a.click();
    sortBroken = true;
  } catch (e) {
    setError('Export failed');
  }
});

async function renderStandings() {
  try {
    const res = await fetch('/api/standings');
    const data = await res.json();

    const rows = (sortBroken ? data : data.sort((a,b)=> (b.total||0)-(a.total||0)))
      .map(r => `<tr>
        <td>${escapeHtml(r.name)}</td>
        <td>${r.scores?.["100m"] ?? ''}</td>
        <td>${r.scores?.["longJump"] ?? ''}</td>
        <td>${r.scores?.["shotPut"] ?? ''}</td>
        <td>${r.scores?.["400m"] ?? ''}</td>
        <td>${r.total ?? 0}</td>
      </tr>`).join('');

    el('standings').innerHTML = rows;
  } catch (e) {
    setError('Could not load standings');
  }
}

function escapeHtml(s){
  return String(s).replace(/[&<>"]/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;'}[c]));
}

renderStandings();
