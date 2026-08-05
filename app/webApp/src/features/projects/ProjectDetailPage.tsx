import React from 'react';
import { Link, useParams } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { ShieldCheck, Clock, DollarSign, Award, CheckCircle2, ArrowLeft, MessageSquare, FileText, Lock } from 'lucide-react';

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
    <div className="min-h-screen flex flex-col bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-100 transition-colors">
      <Header isLoggedIn={true} />

      <main className="flex-1 max-w-5xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
        
        {/* Back Link */}
        <Link to="/projects" className="inline-flex items-center gap-2 text-sm font-semibold text-slate-500 hover:text-indigo-600 transition-colors">
          <ArrowLeft className="w-4 h-4" /> Back to Project Listings
        </Link>

        {/* Main Content Layout */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          
          {/* Project Details (Left 2 cols) */}
          <div className="lg:col-span-2 space-y-6">
            <div className="bg-white dark:bg-slate-900 p-8 rounded-3xl border border-slate-200 dark:border-slate-800 shadow-sm space-y-6">
              
              <div className="space-y-2">
                <div className="flex items-center gap-2 text-xs font-semibold text-slate-500">
                  <span>Posted {project.posted}</span>
                  <span>&bull;</span>
                  <span className="text-emerald-500 flex items-center gap-1"><ShieldCheck className="w-4 h-4" /> Verified Client</span>
                </div>

                <h1 className="text-2xl sm:text-3xl font-extrabold tracking-tight text-slate-900 dark:text-white">
                  {project.title}
                </h1>
              </div>

              {/* Key Specs */}
              <div className="grid grid-cols-3 gap-4 p-4 rounded-2xl bg-slate-50 dark:bg-slate-800/50 border border-slate-100 dark:border-slate-800 text-center">
                <div>
                  <p className="text-xs text-slate-400 font-medium">Budget</p>
                  <p className="text-lg font-extrabold text-emerald-600 dark:text-emerald-400 mt-0.5">{project.budget}</p>
                </div>
                <div>
                  <p className="text-xs text-slate-400 font-medium">Type</p>
                  <p className="text-sm font-bold text-slate-900 dark:text-white mt-0.5">{project.budgetType}</p>
                </div>
                <div>
                  <p className="text-xs text-slate-400 font-medium">Est. Duration</p>
                  <p className="text-sm font-bold text-slate-900 dark:text-white mt-0.5">{project.duration}</p>
                </div>
              </div>

              {/* Scope Description */}
              <div className="space-y-4">
                <h3 className="text-lg font-bold">Project Scope & Specifications</h3>
                <div className="text-sm text-slate-600 dark:text-slate-300 leading-relaxed whitespace-pre-line">
                  {project.description}
                </div>
              </div>

              {/* Required Skills */}
              <div className="space-y-3 pt-4 border-t border-slate-100 dark:border-slate-800">
                <h3 className="text-sm font-bold uppercase tracking-wider text-slate-500">Required Skills</h3>
                <div className="flex flex-wrap gap-2">
                  {project.skills.map((skill) => (
                    <span key={skill} className="px-3 py-1.5 rounded-xl bg-indigo-50 dark:bg-indigo-950/60 text-indigo-700 dark:text-indigo-300 text-xs font-semibold">
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
            <div className="bg-white dark:bg-slate-900 p-6 rounded-3xl border border-slate-200 dark:border-slate-800 shadow-sm space-y-4">
              <Link
                to={`/projects/${project.id}/apply`}
                className="w-full flex items-center justify-center gap-2 bg-indigo-600 hover:bg-indigo-700 text-white font-bold text-base py-3.5 rounded-xl shadow-md shadow-indigo-500/20 transition-all"
              >
                Submit Proposal <FileText className="w-5 h-5" />
              </Link>

              <div className="flex items-center justify-between text-xs text-slate-500 dark:text-slate-400 pt-2 border-t border-slate-100 dark:border-slate-800">
                <span>{project.proposalsCount} Proposals Submitted</span>
                <span className="flex items-center gap-1 text-emerald-500"><Lock className="w-3.5 h-3.5" /> Escrow Protected</span>
              </div>
            </div>

            {/* Client Info Card */}
            <div className="bg-white dark:bg-slate-900 p-6 rounded-3xl border border-slate-200 dark:border-slate-800 shadow-sm space-y-4">
              <h3 className="font-bold text-slate-900 dark:text-white">About the Client</h3>

              <div className="space-y-3 text-sm">
                <div>
                  <p className="font-bold text-slate-900 dark:text-white">{project.client}</p>
                  <p className="text-xs text-slate-500">{project.location}</p>
                </div>

                <div className="flex items-center gap-2 text-amber-500 text-sm font-bold">
                  <Award className="w-4 h-4" /> {project.clientRating} ★ Rating
                </div>

                <div className="text-xs text-slate-500">
                  Total Spent: <strong className="text-slate-900 dark:text-white">{project.clientSpent}</strong>
                </div>

                <div className="pt-2 border-t border-slate-100 dark:border-slate-800">
                  <Link to="/messages" className="flex items-center justify-center gap-2 text-xs font-bold text-indigo-600 dark:text-indigo-400 hover:underline">
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
