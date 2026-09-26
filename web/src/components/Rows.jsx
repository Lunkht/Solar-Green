import Reveal from './Reveal'
import SolarScene from './SolarScene'

const rows = [
  {
    title: 'De l’export à votre toit',
    text: 'Nous importons directement des usines certifiées IEC et TÜV, sans intermédiaires. Chaque panneau est contrôlé au port de Conakry avant mise à disposition.',
    points: ['Contrôle qualité au débarquement', 'Documents douaniers et factures conformes', 'Suivi de commande en temps réel'],
    variant: '550w',
    swatch: 'hsl(42 100% 55%)',
  },
  {
    title: 'Une installation pensée pour la Guinée',
    text: 'Climat chaud et humide, coupures EGB, zones sans réseau… Nos équipes dimensionnent chaque système pour vos réalités. Installations homologuées et mises en service testées.',
    points: ['Étude technique gratuite de votre site', 'Installation en 2 à 4 jours (Conakry)', 'Formation de votre opérateur'],
    variant: 'kit',
    swatch: 'hsl(20 90% 55%)',
    image: '/installation-guinea.png',
  },
  {
    title: 'Une énergie pilotable, même au téléphone',
    text: 'Avec nos onduleurs connectés, suivez production et consommation sur votre smartphone, où que vous soyez. Optimisez vos charges et revendez votre surplus.',
    points: ['Application mobile de suivi', 'Alertes et historique de production', 'Mise à jour du firmware à distance'],
    variant: 'inverter',
    swatch: 'hsl(160 70% 45%)',
    image: '/control-interface.png',
  },
]

export default function Rows() {
  return (
    <section className="rows" id="solutions">
      <div className="container">
        {rows.map((r, i) => (
          <Reveal key={r.title} className={`row ${i % 2 === 1 ? 'reverse' : ''}`}>
            <div className="row-media" style={{ ['--swatch']: r.swatch }}>
              {r.image ? <img src={r.image} alt={r.title} loading="lazy" /> : <SolarScene variant={r.variant} swatch={r.swatch} className="big-solar" />}
            </div>
            <div className="row-copy">
              <h3>{r.title}</h3>
              <p>{r.text}</p>
              <ul>
                {r.points.map((pt) => (
                  <li key={pt}>{pt}</li>
                ))}
              </ul>
              <a href="#devis" className="btn btn-dark">
                Demander une étude →
              </a>
            </div>
          </Reveal>
        ))}
      </div>
    </section>
  )
}