import { Link } from 'react-router-dom'
import Logo from './Logo'

export default function Footer() {
  return (
    <footer className="footer">
      <div className="container">
        <div className="footer-grid">
          <div className="footer-brand">
            <Link to="/" className="logo">
              <Logo className="logo-badge" />
            </Link>
            <p>
              Exportation et vente de solutions solaires en République de Guinée. Énergie
              propre pour tous, du particulier à l’industriel.
            </p>
          </div>

          <div>
            <h4>Navigation</h4>
            <div className="footer-col">
              <Link to="/">Accueil</Link>
              <a href="/#catalogue">Catalogue</a>
              <a href="/#solutions">Solutions</a>
              <a href="/#faq">FAQ</a>
            </div>
          </div>

          <div>
            <h4>Contact</h4>
            <div className="footer-col">
              <a href="tel:+224614658717">+224 614 65 87 17</a>
              <a href="mailto:contact@solargreen.com">contact@solargreen.com</a>
              <span>L50 Route Prince Lambanyi<br />Conakry — Guinée</span>
            </div>
          </div>

          <div>
            <h4>Suivez-nous</h4>
            <div className="footer-col">
              <a href="#" rel="noreferrer" onClick={(e) => e.preventDefault()}>Facebook</a>
              <a href="#" rel="noreferrer" onClick={(e) => e.preventDefault()}>Instagram</a>
              <a href="#" rel="noreferrer" onClick={(e) => e.preventDefault()}>LinkedIn</a>
              <a href="#" rel="noreferrer" onClick={(e) => e.preventDefault()}>TikTok</a>
            </div>
          </div>
        </div>

        <div className="footer-bottom">
          <span>© {new Date().getFullYear()} Solar Green S.A. — Tous droits réservés.</span>
          <span>Fait avec iBilium,inc en Guinée</span>
        </div>
      </div>
    </footer>
  )
}