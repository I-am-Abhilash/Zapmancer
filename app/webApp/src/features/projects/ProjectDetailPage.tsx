import React from 'react';
import { Link, useParams } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { ShieldCheck, ArrowLeft, MessageSquare, FileText, Lock, Star, MapPin, Clock, DollarSign } from 'lucide-react';

const field: React.CSSProperties = {
  fontSize: 11, fontWeight: 600, color: 'var(--color-steel)',
  textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: 4,
};

const value: React.CSSProperties = {
  fontSize: 15, fontWeight: 700, color: 'var(--color-ink)',
};

export const ProjectDetailPage: React.FC = () => {
  const { id } = useParams();

  const project = {
    id: id || '1',
    title: 'Compose Multiplatform Desktop App for Ktor Analytics',
    client: 'Acme AI Systems',
    clientRating: 4.9,
    clientSpent: '$42,000+',
    location: 'San Francisco, CA',
    budget: '$3,200',
    budgetType: 'Fixed-price',
    duration: '3–4 weeks',
    experience: 'Intermediate',
    posted: '2 hours ago',
    proposalsCount: 8,
    description: `We are seeking an experienced Kotlin Multiplatform engineer to develop a high-performance desktop application targeting macOS and Windows using Compose Multiplatform.

Key deliverables:
1. Setup KMP module sharing architecture with JVM desktop target.
2. Connect to existing Ktor REST backend and WebSockets stream.
3. Implement interactive charts, metrics grid, and CSV export.
4. Provide automated UI tests and build pipeline configuration.`,
    skills: ['Kotlin Multiplatform', 'Compose Desktop', 'Ktor Client', 'Coroutines', 'SQLDelight'],
  };

  return (
    <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column', backgroundColor: 'var(--color-canvas)', color: 'var(--color-ink)', fontFamily: 'var(--font-sans)', WebkitFontSmoothing: 'antialiased' }}>
      <Header isLoggedIn={true} />

      <main style={{ flex: 1, maxWidth: 960, width: '100%', margin: '0 auto', padding: '40px 24px 64px', display: 'flex', flexDirection: 'column', gap: 20 }}>

        {/* Back */}
        <Link to="/projects" style={{ display: 'inline-flex', alignItems: 'center', gap: 6, fontSize: 13, color: 'var(--color-steel)', textDecoration: 'none', fontWeight: 500 }}>
          <ArrowLeft size={15} /> Back to listings
        </Link>

        {/* 2-col layout */}
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 280px', gap: 20, alignItems: 'start' }}>

          {/* LEFT — main content */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>

            {/* Header card */}
            <div style={{ border: '1px solid var(--color-hairline)', borderRadius: 12, padding: '20px 24px', backgroundColor: 'var(--color-canvas)' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 8, fontSize: 12, color: 'var(--color-steel)', marginBottom: 10 }}>
                <span style={{ display: 'flex', alignItems: 'center', gap: 4 }}><Clock size={12} /> Posted {project.posted}</span>
                <span>·</span>
                <span style={{ display: 'flex', alignItems: 'center', gap: 4, color: '#1aae39', fontWeight: 600 }}>
                  <ShieldCheck size={12} /> Verified client
                </span>
              </div>
              <h1 style={{ fontSize: 22, fontWeight: 700, letterSpacing: '-0.3px', color: 'var(--color-ink)', lineHeight: 1.3, marginBottom: 16 }}>
                {project.title}
              </h1>

              {/* Spec chips */}
              <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: 1, border: '1px solid var(--color-hairline)', borderRadius: 8, overflow: 'hidden', backgroundColor: 'var(--color-hairline)', textAlign: 'center' }}>
                {[
                  { label: 'Budget', val: project.budget },
                  { label: 'Type', val: project.budgetType },
                  { label: 'Duration', val: project.duration },
                ].map((s) => (
                  <div key={s.label} style={{ backgroundColor: 'var(--color-canvas)', padding: '12px 8px' }}>
                    <p style={field}>{s.label}</p>
                    <p style={value}>{s.val}</p>
                  </div>
                ))}
              </div>
            </div>

            {/* Description */}
            <div style={{ border: '1px solid var(--color-hairline)', borderRadius: 12, overflow: 'hidden' }}>
              <div style={{ padding: '14px 20px', borderBottom: '1px solid var(--color-hairline)', fontWeight: 600, fontSize: 14, color: 'var(--color-ink)' }}>
                Project scope & specifications
              </div>
              <div style={{ padding: '16px 20px', fontSize: 14, lineHeight: 1.7, color: 'var(--color-steel)', whiteSpace: 'pre-line' }}>
                {project.description}
              </div>
            </div>

            {/* Skills */}
            <div style={{ border: '1px solid var(--color-hairline)', borderRadius: 12, overflow: 'hidden' }}>
              <div style={{ padding: '14px 20px', borderBottom: '1px solid var(--color-hairline)', fontWeight: 600, fontSize: 14, color: 'var(--color-ink)' }}>
                Required skills
              </div>
              <div style={{ padding: '16px 20px', display: 'flex', flexWrap: 'wrap', gap: 6 }}>
                {project.skills.map((s) => (
                  <span key={s} style={{ fontSize: 12, fontWeight: 600, padding: '3px 10px', borderRadius: 6, backgroundColor: 'var(--color-card-tint-sky)', color: 'var(--color-link-blue)' }}>
                    {s}
                  </span>
                ))}
              </div>
            </div>

          </div>

          {/* RIGHT — sidebar */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: 12, position: 'sticky', top: 80 }}>

            {/* Apply CTA */}
            <div style={{ border: '1px solid var(--color-hairline)', borderRadius: 12, padding: 20, display: 'flex', flexDirection: 'column', gap: 14 }}>
              <Link
                to={`/projects/${project.id}/apply`}
                style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 8, backgroundColor: 'var(--color-primary)', color: '#ffffff', fontSize: 14, fontWeight: 500, padding: '10px 0', borderRadius: 8, textDecoration: 'none' }}
              >
                <FileText size={14} /> Submit proposal
              </Link>
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', paddingTop: 10, borderTop: '1px solid var(--color-hairline)', fontSize: 12, color: 'var(--color-steel)' }}>
                <span>{project.proposalsCount} proposals</span>
                <span style={{ display: 'flex', alignItems: 'center', gap: 4, color: '#1aae39', fontWeight: 600 }}>
                  <Lock size={12} /> Escrow protected
                </span>
              </div>
            </div>

            {/* Client info */}
            <div style={{ border: '1px solid var(--color-hairline)', borderRadius: 12, padding: 20, display: 'flex', flexDirection: 'column', gap: 12 }}>
              <p style={{ fontSize: 13, fontWeight: 600, color: 'var(--color-ink)' }}>About the client</p>
              <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
                <p style={{ fontSize: 14, fontWeight: 600, color: 'var(--color-ink)' }}>{project.client}</p>
                <p style={{ fontSize: 12, color: 'var(--color-steel)', display: 'flex', alignItems: 'center', gap: 4 }}><MapPin size={11} /> {project.location}</p>
                <p style={{ fontSize: 12, color: 'var(--color-steel)', display: 'flex', alignItems: 'center', gap: 4 }}>
                  <Star size={11} fill="#f59e0b" style={{ color: '#f59e0b' }} /> {project.clientRating} rating
                </p>
                <p style={{ fontSize: 12, color: 'var(--color-steel)', display: 'flex', alignItems: 'center', gap: 4 }}>
                  <DollarSign size={11} /> <strong style={{ color: 'var(--color-ink)' }}>{project.clientSpent}</strong> total spent
                </p>
              </div>
              <div style={{ paddingTop: 10, borderTop: '1px solid var(--color-hairline)' }}>
                <Link to="/messages" style={{ display: 'flex', alignItems: 'center', gap: 6, fontSize: 13, fontWeight: 500, color: 'var(--color-primary)', textDecoration: 'none' }}>
                  <MessageSquare size={13} /> Send inquiry
                </Link>
              </div>
            </div>

          </div>
        </div>
      </main>

      <Footer />
    </div>
  );
};
