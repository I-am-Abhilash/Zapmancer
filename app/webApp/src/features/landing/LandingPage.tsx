import React, { useState } from 'react';
import './landing.css';
import { Link } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { ShieldCheck, ArrowRight, Code, Cpu, Smartphone, Layers, CheckCircle2, Lock, Sparkles, Scale, GitBranch, ChevronDown, ChevronUp, Palette, Terminal, Globe, Monitor, Copy, Check, Sliders, HelpCircle, Download, Server, Laptop, PenTool, Megaphone, TrendingUp, Paintbrush, Database, Bot, Users, Building2, Clock, CheckSquare, Zap } from 'lucide-react';

export const LandingPage: React.FC = () => {
  const [openFaq, setOpenFaq] = useState<number | null>(0);
  const [calcAmount, setCalcAmount] = useState<number>(5000);
  const [activeCodeTab, setActiveCodeTab] = useState<'code' | 'ai' | 'design'>('code');
  const [copiedCode, setCopiedCode] = useState<boolean>(false);

  // Fee calculation logic
  const zapmancerPayout = calcAmount;
  const upworkPayout = calcAmount * 0.8; // 20% fee
  const freelancerPayout = calcAmount * 0.76; // 24% fee
  const developerSavings = calcAmount - upworkPayout;

  const categories = [
    { name: 'Full-Stack & Web Dev', count: '342 Open Bounties', icon: Code, tint: 'landing-card-mint' },
    { name: 'AI Builders & Agents', count: '215 Open Bounties', icon: Bot, tint: 'landing-card-lavender' },
    { name: 'UI/UX & Product Design', count: '189 Open Bounties', icon: Palette, tint: 'landing-card-sky' },
    { name: 'Mobile (iOS & Android)', count: '194 Open Bounties', icon: Smartphone, tint: 'landing-card-peach' },
    { name: 'DevOps & Cloud Systems', count: '126 Open Bounties', icon: Terminal, tint: 'landing-card-rose' },
    { name: 'Data Engineering & Analytics', count: '112 Open Bounties', icon: Database, tint: 'landing-card-mint' },
    { name: 'Technical Writing & Content', count: '78 Open Bounties', icon: PenTool, tint: 'landing-card-sky' },
    { name: 'Growth & Digital Marketing', count: '95 Open Bounties', icon: TrendingUp, tint: 'landing-card-peach' },
  ];

  const paymentSteps = [
    {
      step: '01',
      title: 'Company Milestone Escrow Deposit',
      desc: 'Before project work starts, the company deposits the milestone budget into a secure escrow account (Stripe / USDC).'
    },
    {
      step: '02',
      title: 'Deliverable Submission & Review',
      desc: 'The freelancer or employee builds the deliverable, submits work artifacts, and provides live demonstration links.'
    },
    {
      step: '03',
      title: 'Instant Payout & Legal IP Transfer',
      desc: 'Upon company sign-off, funds release immediately to the worker with automated legal Work-for-Hire copyright transfer.'
    }
  ];

  const codeSnippets = {
    code: `// Universal API Microservice & WebSocket Event Router
export async function releaseEscrowMilestone(req: MilestoneRequest) {
  const contract = await db.contracts.findUnique({ id: req.contractId });
  
  if (contract.status === 'DELIVERABLE_SUBMITTED') {
    await stripe.transfers.create({
      amount: contract.amount,
      currency: 'usd',
      destination: contract.freelancerStripeId,
    });
    
    return { status: 'FUNDS_RELEASED', ipTransferred: true };
  }
}`,
    ai: `# AI Agent RAG Pipeline & Multi-Modal Matching Engine
from langchain.vectorstores import PGVector
from langchain.embeddings import OpenAIEmbeddings

def match_talent_for_task(company_task_description: str):
    embeddings = OpenAIEmbeddings()
    talent_index = PGVector.from_existing_index(embeddings)
    
    matched_freelancers = talent_index.similarity_search(
        query=company_task_description,
        k=5,
        filter={"kyc_verified": True}
    )
    return [dev.metadata for dev in matched_freelancers]`,
    design: `/* Notion Central Design Token Specification */
:root {
  --color-primary: #5645d4; /* Signature Notion Purple */
  --color-brand-navy: #0a1530;
  --font-family-sans: 'Inter', sans-serif;
  --radius-[md]: 8px; /* Sober Editorial Geometry */
}`
  };

  const testimonials = [
    {
      name: 'Elena Rostova',
      role: 'Full-Stack & AI Builder',
      avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80',
      earned: '$68,000+ Earned',
      quote: 'Zapmancer saved me over $12,000 in platform fees compared to Upwork. The milestone escrow releases are instant and I work with top global companies.'
    },
    {
      name: 'Marcus Vance',
      role: 'CTO at Acme AI Systems',
      avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=150&q=80',
      earned: '14 Bounties Funded',
      quote: 'Managing our core internal team and instant-hiring expert designers and AI engineers in 1 click has completely streamlined our company operations.'
    }
  ];

  const landingFaqs = [
    {
      q: 'Who can join Zapmancer and create a company workspace?',
      a: 'Zapmancer is open to everyone! Any company, startup, or agency can register a Company Workspace to manage employees and hire freelancers. Any developer, designer, AI builder, marketer, or creator can join to find work or contract with companies.'
    },
    {
      q: 'How does Zapmancer handle Intellectual Property (IP) ownership?',
      a: 'Every completed milestone includes an automated, legally binding Work-for-Hire copyright transfer agreement. Upon escrow funds release, 100% of project deliverables, design assets, and code IP transfer exclusively to the client company.'
    },
    {
      q: 'What are the exact platform pricing fees?',
      a: 'Unlike traditional platforms taking a 20% cut from freelancers, Zapmancer charges 0% commission to workers. Companies pay a flat 3% to 5% operational cost recovery fee at milestone deposit time.'
    },
    {
      q: 'How does Instant Dev & Creator Hiring work for companies?',
      a: 'Inside your Company Workspace task board, company admins can click "＋ Add Fellow Dev/Creator" to instantly invite top AI-matched talent directly into active project tasks.'
    },
    {
      q: 'Which payment methods are supported for escrow deposits & payouts?',
      a: 'Zapmancer supports automated Stripe Direct Bank Deposit (USD/EUR/GBP), Credit Card, and Web3 USDC stablecoin payments directly into worker accounts with zero holding delays.'
    }
  ];

  const handleCopyCode = () => {
    navigator.clipboard.writeText(codeSnippets[activeCodeTab]);
    setCopiedCode(true);
    setTimeout(() => setCopiedCode(false), 2000);
  };

  return (
    <div className="landing-page">
      <Header isLoggedIn={false} />

      {/* Notion Navy Hero Band */}
      <section className="landing-hero-section">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center relative z-10 space-y-8">
          
          {/* Overline Badge */}
          <div className="landing-hero-badge">
            <Sparkles className="w-3.5 h-3.5" /> All-in-One Company OS & Universal Freelance Network
          </div>

          {/* Notion Hero Display Title */}
          <h1 className="landing-hero-title">
            Run your company & hire top developers, designers & AI builders.
          </h1>

          {/* Subtitle */}
          <p className="landing-hero-subtitle">
            Manage your full-time team tasks and instant-hire expert freelancers with 0% worker commission and 100% milestone escrow protection.
          </p>

          {/* Signature Notion Purple Pill Primary CTA */}
          <div className="flex flex-col sm:flex-row items-center justify-center gap-4 pt-2">
            <Link
              to="/signup"
              className="landing-btn-primary"
            >
              Get Zapmancer free <ArrowRight className="w-4 h-4" />
            </Link>
            <Link
              to="/projects"
              className="landing-btn-secondary"
            >
              Request a demo
            </Link>
          </div>

          {/* Real Embedded Workspace UI Mockup Card (notion-DESIGN.md Spec) */}
          <div className="pt-8">
            <div className="landing-workspace-mockup">
              
              {/* Mockup Header Bar */}
              <div className="bg-surface-elevated px-4 py-3 border-b border-hairline flex items-center justify-between">
                <div className="flex items-center gap-3">
                  <div className="flex items-center gap-1.5">
                    <span className="w-3 h-3 rounded-full bg-red-400"></span>
                    <span className="w-3 h-3 rounded-full bg-yellow-400"></span>
                    <span className="w-3 h-3 rounded-full bg-green-400"></span>
                  </div>
                  <span className="text-xs font-semibold text-mute flex items-center gap-1.5">
                    <Building2 className="w-3.5 h-3.5 text-primary" /> Acme AI Systems Workspace &bull; Sprint Board
                  </span>
                </div>

                <div className="flex items-center gap-2 text-xs font-semibold text-primary">
                  <ShieldCheck className="w-4 h-4 text-primary" /> Verified Entity &bull; 0% Fee Escrow Vault Active
                </div>
              </div>

              {/* Mockup Content Grid */}
              <div className="p-6 grid grid-cols-1 md:grid-cols-3 gap-4">
                
                {/* Column 1 */}
                <div className="p-4 rounded-lg bg-surface border border-hairline space-y-3">
                  <div className="flex items-center justify-between text-xs font-semibold text-mute">
                    <span>Task #102 &bull; AI Agent RAG</span>
                    <span className="text-primary font-bold">$4,500 Escrow</span>
                  </div>
                  <h4 className="font-bold text-sm text-ink">Custom RAG AI Pipeline Integration</h4>
                  <div className="flex items-center justify-between text-xs text-mute pt-2 border-t border-hairline">
                    <span className="flex items-center gap-1"><Users className="w-3 h-3 text-primary" /> Elena Rostova</span>
                    <span className="text-primary font-semibold">In Progress</span>
                  </div>
                </div>

                {/* Column 2 */}
                <div className="p-4 rounded-lg bg-surface border border-hairline space-y-3">
                  <div className="flex items-center justify-between text-xs font-semibold text-mute">
                    <span>Task #108 &bull; Fine-Tuning</span>
                    <span className="text-primary font-bold">$3,800 Salary</span>
                  </div>
                  <h4 className="font-bold text-sm text-ink">Multi-Modal Model Vector Store</h4>
                  <div className="flex items-center justify-between text-xs text-mute pt-2 border-t border-hairline">
                    <span className="flex items-center gap-1"><Users className="w-3 h-3 text-primary" /> Dr. Lucas Meyer</span>
                    <span className="text-primary font-semibold">PR Review</span>
                  </div>
                </div>

                {/* Column 3 */}
                <div className="p-4 rounded-lg bg-surface border border-hairline space-y-3">
                  <div className="flex items-center justify-between text-xs font-semibold text-mute">
                    <span>Task #114 &bull; UI Design</span>
                    <span className="text-primary font-bold">$2,800 Bounty</span>
                  </div>
                  <h4 className="font-bold text-sm text-ink">Design System Micro-Animations</h4>
                  <div className="flex items-center justify-between text-xs text-mute pt-2 border-t border-hairline">
                    <span className="flex items-center gap-1"><Users className="w-3 h-3 text-primary" /> Sophia Al-Mansoor</span>
                    <span className="text-primary font-semibold">Needs Designer</span>
                  </div>
                </div>

              </div>

            </div>
          </div>

          {/* Quick Metrics */}
          <div className="pt-8 grid grid-cols-2 md:grid-cols-4 gap-4 max-w-4xl mx-auto">
            <div className="landing-metric-card">
              <div className="metric-val">$1.2M+</div>
              <div className="metric-lbl">Escrow Bounties Paid</div>
            </div>
            <div className="landing-metric-card">
              <div className="metric-val">100%</div>
              <div className="metric-lbl">Open Source Core</div>
            </div>
            <div className="landing-metric-card">
              <div className="metric-val">0%</div>
              <div className="metric-lbl">Freelancer Fees</div>
            </div>
            <div className="landing-metric-card">
              <div className="metric-val">3,400+</div>
              <div className="metric-lbl">Verified Workers & Companies</div>
            </div>
          </div>

        </div>
      </section>

      {/* Notion Bold Yellow High-Emphasis Banner Section (notion-DESIGN.md Spec) */}
      <section className="py-16 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 w-full">
        <div className="landing-card-yellow-bold flex flex-col md:flex-row items-center justify-between gap-6">
          <div className="space-y-2 max-w-2xl">
            <div className="inline-flex items-center gap-1.5 text-xs font-bold uppercase tracking-wider">
              <Zap className="w-4 h-4 fill-current text-primary" /> On-Demand Talent Matching
            </div>
            <h2 className="text-2xl sm:text-3xl font-extrabold tracking-tight">
              On-demand talent matching & instant milestone escrow payouts 24/7.
            </h2>
            <p className="text-xs sm:text-sm leading-relaxed opacity-90">
              Zapmancer matches your company's active task specifications with top pre-vetted engineers, UI/UX designers, and AI builders in under 60 seconds.
            </p>
          </div>

          <Link
            to="/projects/new"
            className="landing-btn-primary py-3 px-6 shrink-0"
          >
            Post a Project Now <ArrowRight className="w-4 h-4" />
          </Link>
        </div>
      </section>

      {/* Notion Pastel Feature Cards Grid (notion-DESIGN.md Spec) */}
      <section className="py-16 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 w-full space-y-10">
        <div className="text-center space-y-2 max-w-2xl mx-auto">
          <div className="text-primary text-[11px] font-semibold tracking-[0.1em] uppercase">Universal Discipline Ecosystems</div>
          <h2 className="text-3xl font-extrabold tracking-[-0.01em] text-ink">Built for Every Discipline & Industry</h2>
          <p className="text-mute text-sm leading-relaxed">
            Explore specialized talent pools and contract bounties rendered in Notion's signature pastel card property palette.
          </p>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-6">
          {categories.map((cat) => {
            const Icon = cat.icon;
            return (
              <Link
                key={cat.name}
                to="/projects"
                className={`${cat.tint} block space-y-4 hover:scale-[1.02] transition-all cursor-pointer shadow-sm`}
              >
                <div className="w-10 h-10 rounded-lg bg-white/40 dark:bg-black/20 flex items-center justify-center border border-current/20">
                  <Icon className="w-5 h-5" />
                </div>
                <div className="space-y-1">
                  <h3 className="font-extrabold text-base">{cat.name}</h3>
                  <p className="text-xs opacity-80 font-medium">{cat.count}</p>
                </div>
              </Link>
            );
          })}
        </div>
      </section>

      {/* Interactive Fee & Payout Calculator Widget */}
      <section className="py-20 bg-surface border-y border-hairline">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 w-full space-y-12">
          <div className="text-center space-y-2 max-w-2xl mx-auto">
            <div className="text-primary text-[11px] font-semibold tracking-[0.1em] uppercase">Interactive Payout Estimator</div>
            <h2 className="text-3xl font-extrabold tracking-[-0.01em] text-ink">Calculate Your Worker Savings</h2>
            <p className="text-mute text-sm leading-relaxed">
              Drag the slider to see how much more money you keep on Zapmancer compared to legacy 20% fee platforms.
            </p>
          </div>

          <div className="bg-surface-elevated p-8 sm:p-10 rounded-xl border border-hairline shadow-sm dark:shadow-none max-w-4xl mx-auto space-y-8">
            
            {/* Slider Control */}
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <label className="text-xs font-semibold uppercase tracking-wider text-mute flex items-center gap-2">
                  <Sliders className="w-4 h-4 text-primary" /> Contract Project Amount
                </label>
                <span className="text-2xl font-extrabold text-primary">${calcAmount.toLocaleString()}</span>
              </div>

              <input
                type="range"
                min="500"
                max="20000"
                step="500"
                value={calcAmount}
                onChange={(e) => setCalcAmount(Number(e.target.value))}
                className="w-full accent-primary cursor-pointer h-2 bg-surface rounded-lg"
              />
              <div className="flex justify-between text-[11px] text-mute font-medium">
                <span>$500</span>
                <span>$5,000</span>
                <span>$10,000</span>
                <span>$20,000</span>
              </div>
            </div>

            {/* Comparison Cards Grid */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 pt-4 border-t border-hairline">
              <div className="p-6 rounded-xl bg-primary/10 border border-primary/30 space-y-2">
                <span className="text-[11px] font-extrabold text-primary uppercase tracking-wider">Zapmancer (0% Fee)</span>
                <p className="text-3xl font-extrabold text-primary">${zapmancerPayout.toLocaleString()}.00</p>
                <p className="text-xs text-primary font-semibold flex items-center gap-1">
                  <CheckCircle2 className="w-3.5 h-3.5" /> 100% Payout Retained
                </p>
              </div>

              <div className="p-6 rounded-xl bg-surface border border-hairline space-y-2 opacity-75">
                <span className="text-[11px] font-semibold text-mute uppercase tracking-wider">Upwork (20% Cut)</span>
                <p className="text-2xl font-extrabold text-mute">${upworkPayout.toLocaleString()}.00</p>
                <p className="text-xs text-red-500 font-semibold">Loss: -${(calcAmount - upworkPayout).toLocaleString()}</p>
              </div>

              <div className="p-6 rounded-xl bg-surface border border-hairline space-y-2 opacity-60">
                <span className="text-[11px] font-semibold text-mute uppercase tracking-wider">Freelancer.com (24% Cut)</span>
                <p className="text-2xl font-extrabold text-mute">${freelancerPayout.toLocaleString()}.00</p>
                <p className="text-xs text-red-500 font-semibold">Loss: -${(calcAmount - freelancerPayout).toLocaleString()}</p>
              </div>
            </div>

            <div className="bg-surface p-4 rounded-xl border border-hairline flex flex-col sm:flex-row items-center justify-between gap-4 text-center sm:text-left">
              <div className="space-y-0.5">
                <p className="text-xs font-semibold text-ink">Extra earnings kept on this project:</p>
                <p className="text-sm font-extrabold text-primary">+${developerSavings.toLocaleString()}.00 into your wallet</p>
              </div>
              <Link to="/signup" className="landing-btn-primary py-2.5 px-6 shrink-0">
                Start Earning 100% Now
              </Link>
            </div>

          </div>
        </div>
      </section>

      {/* Code, AI & Design Terminal Carousel */}
      <section className="py-20 bg-canvas border-b border-hairline">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-12">
          
          <div className="text-center space-y-2 max-w-2xl mx-auto">
            <div className="text-primary text-[11px] font-semibold tracking-[0.1em] uppercase">Engineering & Creative Showcase</div>
            <h2 className="text-3xl font-extrabold tracking-[-0.01em] text-ink">Built for Code, AI & Product Design</h2>
            <p className="text-mute text-sm leading-relaxed">
              Explore clean integration templates and design token specifications powering modern tech companies.
            </p>
          </div>

          {/* Code Window Container */}
          <div className="max-w-4xl mx-auto bg-surface rounded-xl border border-hairline shadow-2xl overflow-hidden">
            
            {/* Terminal Header Bar */}
            <div className="bg-surface-elevated px-4 py-3 border-b border-hairline flex items-center justify-between">
              <div className="flex items-center gap-2">
                <span className="w-3 h-3 rounded-full bg-red-500/80"></span>
                <span className="w-3 h-3 rounded-full bg-yellow-500/80"></span>
                <span className="w-3 h-3 rounded-full bg-green-500/80"></span>
                <span className="text-xs font-mono text-mute ml-2">zapmancer-platform &bull; bash</span>
              </div>

              <div className="flex items-center gap-2">
                <button
                  onClick={() => setActiveCodeTab('code')}
                  className={`px-3 py-1 rounded-md text-xs font-mono transition-colors cursor-pointer ${
                    activeCodeTab === 'code' ? 'bg-primary text-white font-bold' : 'text-mute hover:text-ink'
                  }`}
                >
                  SoftwareApi.ts
                </button>
                <button
                  onClick={() => setActiveCodeTab('ai')}
                  className={`px-3 py-1 rounded-md text-xs font-mono transition-colors cursor-pointer ${
                    activeCodeTab === 'ai' ? 'bg-primary text-white font-bold' : 'text-mute hover:text-ink'
                  }`}
                >
                  AiAgent.py
                </button>
                <button
                  onClick={() => setActiveCodeTab('design')}
                  className={`px-3 py-1 rounded-md text-xs font-mono transition-colors cursor-pointer ${
                    activeCodeTab === 'design' ? 'bg-primary text-white font-bold' : 'text-mute hover:text-ink'
                  }`}
                >
                  DesignTokens.css
                </button>

                <button
                  onClick={handleCopyCode}
                  className="p-1.5 rounded-md text-mute hover:text-primary transition-colors cursor-pointer"
                  title="Copy Code"
                >
                  {copiedCode ? <Check className="w-4 h-4 text-primary" /> : <Copy className="w-4 h-4" />}
                </button>
              </div>
            </div>

            {/* Code Body */}
            <pre className="p-6 font-mono text-xs text-primary bg-surface overflow-x-auto leading-relaxed">
              <code>{codeSnippets[activeCodeTab]}</code>
            </pre>

          </div>

        </div>
      </section>

      {/* Community Testimonials */}
      <section className="py-20 bg-surface border-y border-hairline">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-12">
          
          <div className="text-center space-y-2 max-w-2xl mx-auto">
            <div className="text-primary text-[11px] font-semibold tracking-[0.1em] uppercase">Vetted Community</div>
            <h2 className="text-3xl font-extrabold tracking-[-0.01em] text-ink">Trusted by Workers & Founders</h2>
            <p className="text-mute text-sm leading-relaxed">
              Read how freelancers and company teams build products faster with zero fee extractions.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
            {testimonials.map((t, idx) => (
              <div key={idx} className="p-8 rounded-xl bg-surface-elevated border border-hairline space-y-4 flex flex-col justify-between">
                <p className="text-sm text-mute leading-relaxed italic">
                  "{t.quote}"
                </p>

                <div className="flex items-center justify-between border-t border-hairline pt-4">
                  <div className="flex items-center gap-4">
                    <img src={t.avatar} alt={t.name} className="w-12 h-12 rounded-full object-cover border-2 border-primary/40" />
                    <div>
                      <h4 className="font-bold text-ink text-base flex items-center gap-1.5">
                        {t.name} <ShieldCheck className="w-4 h-4 text-primary" />
                      </h4>
                      <p className="text-xs text-mute font-medium">{t.role}</p>
                    </div>
                  </div>

                  <span className="text-xs font-bold text-primary bg-primary/10 px-3 py-1 rounded-full border border-primary/20">
                    {t.earned}
                  </span>
                </div>
              </div>
            ))}
          </div>

        </div>
      </section>

      {/* Native Q&A / FAQ Section */}
      <section className="py-20 max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 w-full space-y-10 border-t border-hairline">
        <div className="text-center space-y-2 max-w-xl mx-auto">
          <div className="inline-flex items-center gap-1.5 text-primary text-[11px] font-semibold tracking-[0.1em] uppercase">
            <HelpCircle className="w-3.5 h-3.5" /> Frequently Asked Questions
          </div>
          <h2 className="text-3xl font-extrabold tracking-tight text-ink">Everything You Need to Know</h2>
          <p className="text-sm text-mute">Clear answers regarding company workspace management, 0% freelancer fees, IP ownership, and open-source compliance.</p>
        </div>

        <div className="space-y-4">
          {landingFaqs.map((faq, idx) => {
            const isOpen = openFaq === idx;
            return (
              <div
                key={idx}
                onClick={() => setOpenFaq(isOpen ? null : idx)}
                className={`p-6 rounded-xl border transition-all duration-200 cursor-pointer ${
                  isOpen
                    ? 'bg-surface-elevated border-primary/40 shadow-sm'
                    : 'bg-surface border-hairline hover:bg-surface-elevated'
                }`}
              >
                <div className="flex items-center justify-between gap-4">
                  <h3 className="text-base font-bold text-ink leading-snug flex items-center gap-3">
                    <span className="w-6 h-6 rounded-full bg-primary/10 text-primary font-mono text-xs flex items-center justify-center shrink-0">
                      ?
                    </span>
                    {faq.q}
                  </h3>
                  {isOpen ? (
                    <ChevronUp className="w-5 h-5 text-primary shrink-0" />
                  ) : (
                    <ChevronDown className="w-5 h-5 text-mute shrink-0" />
                  )}
                </div>

                {isOpen && (
                  <p className="text-sm text-mute leading-relaxed pt-4 border-t border-hairline mt-4 pl-9">
                    {faq.a}
                  </p>
                )}
              </div>
            );
          })}
        </div>
      </section>

      {/* Security Banner */}
      <section className="py-16 bg-surface border-t border-hairline text-ink">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex flex-col md:flex-row items-center justify-between gap-8">
          <div className="space-y-3 max-w-2xl">
            <div className="flex items-center gap-2 text-primary font-semibold text-[11px] uppercase tracking-[0.1em]">
              <Lock className="w-4 h-4" /> Built-in Escrow Security
            </div>
            <h2 className="text-3xl font-extrabold tracking-tight text-ink">Zero Risk Milestone Escrow Protection</h2>
            <p className="text-mute text-sm leading-relaxed">
              Company funds are safely locked in escrow prior to project commencement. Freelancers and creators deliver clean work, and payments release seamlessly upon milestone sign-off.
            </p>
          </div>
          <Link to="/signup" className="landing-btn-primary shrink-0">
            Join Marketplace Now
          </Link>
        </div>
      </section>

      <Footer />
    </div>
  );
};
