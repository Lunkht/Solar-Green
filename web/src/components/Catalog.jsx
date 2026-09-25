import { Link } from 'react-router-dom'
import { products } from '../data/products'
import SolarScene from './SolarScene'
import Reveal from './Reveal'

export default function Catalog() {
  return (
    <section className="catalog" id="catalogue">
      <div className="container">
        <Reveal className="section-head">
          <div className="kicker">Catalogue</div>
          <h2>Tout ce qu’il faut pour passer au solaire</h2>
          <p>
            Panneaux monocristallins, onduleurs hybrides, batteries lithium et kits clés en
            main. Sélectionnés selon les standards internationaux et adaptés au climat de Guinée.
          </p>
        </Reveal>

        <div className="catalog-grid">
          {products.map((p, i) => (
            <Reveal key={p.id} delay={i % 3} className="card" style={{ ['--swatch']: p.swatch }}>
              <Link to={`/produit/${p.id}`} className="card-visual">
                <span className="type-chip">{p.type}</span>
                {p.image ? <img src={p.image} alt={p.name} loading="lazy" /> : <SolarScene variant={p.hero} swatch={p.swatch} />}
              </Link>
              <div className="card-body">
                <h3>
                  <Link to={`/produit/${p.id}`}>{p.name}</Link>
                </h3>
                <div className="tag">{p.tagline}</div>
                <p>{p.description}</p>
                <div className="card-foot">
                  <div className="price">
                    {p.price}
                    <small>{p.category}</small>
                  </div>
                  <Link to={`/produit/${p.id}`} className="btn btn-outline">
                    Détails →
                  </Link>
                </div>
              </div>
            </Reveal>
          ))}
        </div>
      </div>
    </section>
  )
}