const items = [
  { b: 'Exportation directe', t: 'fabricants certifiés IEC/TÜV' },
  { b: 'Installation', t: 'par techniciens agréés' },
  { b: 'Garantie', t: 'jusqu’à 25 ans' },
  { b: 'Livraison', t: 'Conakry & 33 préfectures' },
  { b: 'Mobile Money', t: 'Orange', extra: ' · MTN · OMA' },
  { b: '+224 614 65 87 17', t: 'support 7j/7' },
  { b: 'L50, Route Prince Lambanyi', t: 'Conakry' },
  { b: 'Maintien à distance', t: 'suivi via app mobile' },
]

export default function Ticker() {
  const row = items.map((it, i) => (
    <span className="ticker-item" key={i}>
      <b>{it.b}</b>
      <span>{it.t}{it.extra || ''}</span>
      <span style={{ color: 'rgba(238,248,0,0.6)' }}>◆</span>
    </span>
  ))
  return (
    <div className="ticker" aria-hidden="true">
      <div className="ticker-track">
        {row}
        {row}
      </div>
    </div>
  )
}