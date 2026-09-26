import { useState } from 'react'
import Reveal from './Reveal'

export default function QuoteForm({ toast }) {
  const [form, setForm] = useState({
    nom: '',
    telephone: '',
    email: '',
    localite: '',
    produit: '',
    puissance: '',
    message: '',
  })
  const [sending, setSending] = useState(false)

  const update = (e) => setForm((f) => ({ ...f, [e.target.name]: e.target.value }))

  const onSubmit = async (e) => {
    e.preventDefault()
    setSending(true)
    try {
      const res = await fetch('https://formsubmit.co/ajax/contact@solargreen.com', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', Accept: 'application/json' },
        body: JSON.stringify({
          _subject: `Nouveau devis Solar Green — ${form.nom}`,
          ...form,
        }),
      })
      if (!res.ok) throw new Error('network')
      toast('✓ Votre demande a bien été envoyée. Nous vous recontactons sous 24 h.')
      setForm({
        nom: '',
        telephone: '',
        email: '',
        localite: '',
        produit: '',
        puissance: '',
        message: '',
      })
    } catch (err) {
      toast('⚠ Envoi impossible pour l’instant. Appelez-nous au +224 614 65 87 17.')
    } finally {
      setSending(false)
    }
  }

  return (
    <section className="quote" id="devis">
      <div className="container quote-grid">
        <Reveal className="quote-side">
          <div className="kicker" style={{ color: 'var(--accent)', fontSize: '0.78rem', fontWeight: 600, textTransform: 'uppercase', letterSpacing: '0.18em', marginBottom: 14 }}>
            Devis gratuit
          </div>
          <h2>Obtenez votre devis en 24 h</h2>
          <p>
            Décrivez votre besoin — maison, entreprise, forage ou village — et notre équipe
            vous rappelle avec une proposition technique et financière personnalisée.
          </p>

          <div className="contact-item">
            <span className="ic">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none"><path d="M4 6l8 5 8-5M4 6h16v12H4V6z" stroke="currentColor" strokeWidth="1.6" strokeLinejoin="round"/></svg>
            </span>
            <div><b>contact@solargreen.com</b><span>Réponse sous 24 h ouvrées</span></div>
          </div>
          <div className="contact-item">
            <span className="ic">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none"><path d="M5 4h4l2 5-2.5 1.5a11 11 0 005 5L15 13l5 2v4a2 2 0 01-2 2A16 16 0 013 6a2 2 0 012-2z" stroke="currentColor" strokeWidth="1.6" strokeLinejoin="round"/></svg>
            </span>
            <div><b>+224 614 65 87 17</b><span>WhatsApp 7j/7 de 8h à 20h</span></div>
          </div>
          <div className="contact-item">
            <span className="ic">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none"><path d="M12 21s-7-5.5-7-11a7 7 0 1114 0c0 5.5-7 11-7 11z" stroke="currentColor" strokeWidth="1.6" strokeLinejoin="round"/><circle cx="12" cy="10" r="2.5" stroke="currentColor" strokeWidth="1.6"/></svg>
            </span>
            <div><b>L50 Route Prince Lambanyi</b><span>Conakry — République de Guinée</span></div>
          </div>
        </Reveal>

        <Reveal delay={1}>
          <form className="form" onSubmit={onSubmit}>
            <div className="form-row">
              <div className="field">
                <label htmlFor="nom">Nom complet *</label>
                <input id="nom" name="nom" required value={form.nom} onChange={update} placeholder="Mamadou Diallo" />
              </div>
              <div className="field">
                <label htmlFor="telephone">Téléphone *</label>
                <input id="telephone" name="telephone" required value={form.telephone} onChange={update} placeholder="+224 6xx xx xx xx" />
              </div>
            </div>
            <div className="form-row">
              <div className="field">
                <label htmlFor="email">Email</label>
                <input id="email" name="email" type="email" value={form.email} onChange={update} placeholder="vous@exemple.com" />
              </div>
              <div className="field">
                <label htmlFor="localite">Localité *</label>
                <input id="localite" name="localite" required value={form.localite} onChange={update} placeholder="Conakry, Kindia, Kankan…" />
              </div>
            </div>
            <div className="form-row">
              <div className="field">
                <label htmlFor="produit">Produit souhaité</label>
                <select id="produit" name="produit" value={form.produit} onChange={update}>
                  <option value="">— Choisir —</option>
                  <option>Panneau solaire 550W</option>
                  <option>Panneau solaire 450W</option>
                  <option>Onduleur hybride 5kW</option>
                  <option>Batterie 10 kWh</option>
                  <option>Kit maison premium</option>
                  <option>Pompe solaire de forage</option>
                  <option>Autre / projet sur mesure</option>
                </select>
              </div>
              <div className="field">
                <label htmlFor="puissance">Puissance estimée</label>
                <select id="puissance" name="puissance" value={form.puissance} onChange={update}>
                  <option value="">— Choisir —</option>
                  <option>Moins de 1 kW</option>
                  <option>1 à 5 kW</option>
                  <option>5 à 20 kW</option>
                  <option>Plus de 20 kW</option>
                </select>
              </div>
            </div>
            <div className="field">
              <label htmlFor="message">Votre projet</label>
              <textarea id="message" name="message" value={form.message} onChange={update} placeholder="Ex : alimenter une maison de 5 personnes, 3 chambres + frigo…" />
            </div>
            <button className="btn btn-primary" disabled={sending}>
              {sending ? 'Envoi en cours…' : 'Envoyer ma demande'}
            </button>
            <p className="form-note">
              Vos données restent confidentielles. Aucun paiement n’est demandé pour établir un devis.
            </p>
          </form>
        </Reveal>
      </div>
    </section>
  )
}