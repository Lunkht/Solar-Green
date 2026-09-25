import { Link, useParams, Navigate } from 'react-router-dom'
import { products } from '../data/products'
import SolarScene from './SolarScene'
import Reveal from './Reveal'

export default function ProductDetail({ toast }) {
  const { id } = useParams()
  const p = products.find((x) => x.id === id)
  if (!p) return <Navigate to="/" replace />

  return (
    <main className="detail">
      <div className="container detail-grid">
        <Reveal className="detail-visual" style={{ ['--swatch']: p.swatch }}>
          {p.image ? <img src={p.image} alt={p.name} /> : <SolarScene variant={p.hero} swatch={p.swatch} />}
        </Reveal>

        <Reveal delay={1} className="detail-copy">
          <span className="tag" style={{ textTransform: 'uppercase', letterSpacing: '0.18em', fontSize: '0.78rem' }}>
            {p.category}
          </span>
          <h1>{p.name}</h1>
          <p>{p.description}</p>

<div className="price-box">
            <span className="now">{p.price}</span>
            {p.priceEur && <span className="eur">{p.priceEur}</span>}
          </div>

          <ul className="spec-list">
            {p.specs.map((s) => (
              <li key={s.label}><span>{s.label}</span><span>{s.value}</span></li>
            ))}
          </ul>

          <div className="detail-actions">
            <a href="/#devis" onClick={(e) => e.preventDefault()} className="btn btn-primary">
              Commander ce produit
            </a>
            <Link to="/#catalogue" className="btn btn-dark">← Tous les produits</Link>
          </div>
        </Reveal>
      </div>
    </main>
  )
}