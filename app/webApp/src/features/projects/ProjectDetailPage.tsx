import React from 'react';
import { Link, useParams } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { ShieldCheck, Award, ArrowLeft, MessageSquare, FileText, Lock, Sparkles } from 'lucide-react';

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
    duration: '3 - 4 weeks',
    experience: 'Intermediate',
    posted: '2 hours ago',
    proposalsCount: 8,
    description: `We are seeking an experienced Kotlin Multiplatform engineer to develop a high-performance desktop application targeting macOS and Windows using Compose Multiplatform.
    
    Key Deliverables:
    1. Setup KMP module sharing architecture with JVM desktop target.
    2. Connect to existing Ktor REST backend and WebSockets stream.
    3. Implement interactive charts, metrics grid, and CSV export.
    4. Provide automated UI tests and build pipeline configuration.`,
    skills: ['Kotlin Multiplatform', 'Compose Desktop', 'Ktor Client', 'Coroutines', 'SQLDelight'],
  };

  return (
    <div className="min-h-screen flex flex-col bg-canvas text-ink transition-colors duration-200 antialiased font-sans">
      <Header isLoggedIn={true} />

      <main className="flex-1 max-w-5xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
        
        {/* Back Link */}
        <Link to="/projects" className="inline-flex items-center gap-2 text-xs font-bold uppercase tracking-[0.05em] text-mute hover:text-ink transition-colors">
          <ArrowLeft className="w-4 h-4 text-brand-green" /> Back to Project Listings
        </Link>

        {/* Main Content Layout */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          
          {/* Project Details (Left 2 cols) */}
          <div className="lg:col-span-2 space-y-6">
            <div className="bg-surface p-8 rounded-xl border border-hairline shadow-sm dark:shadow-none space-y-6">
              
              <div className="space-y-2">
                <div className="flex items-center gap-2 text-xs font-semibold text-mute">
                  <span>Posted {project.posted}</span>
                  <span>&bull;</span>
                  <span className="text-brand-green flex items-center gap-1"><ShieldCheck className="w-4 h-4" /> Verified Client</span>
                </div>

                <h1 className="text-2xl sm:text-3xl font-extrabold tracking-tight text-ink">
                  {project.title}
                </h1>
              </div>

              {/* Key Specs */}
              <div className="grid grid-cols-3 gap-4 p-4 rounded-xl bg-surface-elevated border border-hairline text-center">
                <div>
                  <p className="text-xs text-mute font-bold uppercase tracking-wider">Budget</p>
                  <p className="text-lg font-extrabold text-brand-green mt-0.5">{project.budget}</p>
                </div>
                <div>
                  <p className="text-xs text-mute font-bold uppercase tracking-wider">Type</p>
                  <p className="text-sm font-bold text-ink mt-0.5">{project.budgetType}</p>
                </div>
                <div>
                  <p className="text-xs text-mute font-bold uppercase tracking-wider">Est. Duration</p>
                  <p className="text-sm font-bold text-ink mt-0.5">{project.duration}</p>
                </div>
              </div>

              {/* Scope Description */}
              <div className="space-y-4">
                <h3 className="text-lg font-bold text-ink">Project Scope & Specifications</h3>
                <div className="text-sm text-mute leading-relaxed whitespace-pre-line">
                  {project.description}
                </div>
              </div>

              {/* Required Skills */}
              <div className="space-y-3 pt-4 border-t border-hairline">
                <h3 className="text-xs font-bold uppercase tracking-[0.05em] text-mute">Required Skills</h3>
                <div className="flex flex-wrap gap-2">
                  {project.skills.map((skill) => (
                    <span key={skill} className="px-3.5 py-1.5 rounded-full bg-surface-elevated text-brand-green text-xs font-bold border border-hairline">
                      {skill}
                    </span>
                  ))}
                </div>
              </div>

            </div>
          </div>

          {/* Right Sidebar: Actions & Client Info */}
          <div className="space-y-6">
            
            {/* Apply CTA Card */}
            <div className="bg-surface p-6 rounded-xl border border-hairline shadow-sm dark:shadow-none space-y-4">
              <Link
                to={`/projects/${project.id}/apply`}
                className="w-full flex items-center justify-center gap-2 bg-brand-green hover:bg-brand-green-hover text-white font-bold text-xs uppercase tracking-[0.05em] py-3.5 rounded-full shadow-md shadow-brand-green/20 hover:scale-[1.04] transition-all"
              >
                Submit Proposal <FileText className="w-4 h-4" />
              </Link>

              <div className="flex items-center justify-between text-xs text-mute pt-2 border-t border-hairline">
                <span>{project.proposalsCount} Proposals Submitted</span>
                <span className="flex items-center gap-1 text-brand-green font-bold"><Lock className="w-3.5 h-3.5" /> Escrow Protected</span>
              </div>
            </div>

            {/* Client Info Card */}
            <div className="bg-surface p-6 rounded-xl border border-hairline shadow-sm dark:shadow-none space-y-4">
              <h3 className="font-bold text-ink text-base">About the Client</h3>

              <div className="space-y-3 text-sm">
                <div>
                  <p className="font-bold text-ink">{project.client}</p>
                  <p className="text-xs text-mute">{project.location}</p>
                </div>

                <div className="flex items-center gap-2 text-brand-green text-sm font-bold">
                  <Award className="w-4 h-4" /> {project.clientRating} ★ Rating
                </div>

                <div className="text-xs text-mute">
                  Total Spent: <strong className="text-ink">{project.clientSpent}</strong>
                </div>

                <div className="pt-2 border-t border-hairline">
                  <Link to="/messages" className="flex items-center justify-center gap-2 text-xs font-bold text-brand-green hover:text-brand-green-hover uppercase tracking-[0.05em] transition-colors">
                    <MessageSquare className="w-4 h-4" /> Send Direct Inquiry
                  </Link>
                </div>
              </div>
            </div>

          </div>

        </div>

      </main>

      <Footer />
    </div>
  );
};
