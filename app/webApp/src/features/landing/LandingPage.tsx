import React, { useState } from 'react';
import './landing.css';
import { Link } from 'react-router-dom';
import { Header } from '../../components/layout/Header';
import { Footer } from '../../components/layout/Footer';
import { ShieldCheck, ArrowRight, Code, Cpu, Smartphone, Layers, CheckCircle2, Lock, Sparkles, Scale, GitBranch, ChevronDown, ChevronUp, Palette, Terminal, Globe, Monitor, Copy, Check, Sliders, HelpCircle } from 'lucide-react';

export const LandingPage: React.FC = () => {
  const [openFaq, setOpenFaq] = useState<number | null>(0);
  const [calcAmount, setCalcAmount] = useState<number>(5000);
  const [activeCodeTab, setActiveCodeTab] = useState<'kmp' | 'ktor' | 'escrow'>('kmp');
  const [copiedCode, setCopiedCode] = useState<boolean>(false);

  // Fee calculation logic
  const zapmancerPayout = calcAmount;
  const upworkPayout = calcAmount * 0.8; // 20% fee
  const freelancerPayout = calcAmount * 0.76; // 24% fee
  const developerSavings = calcAmount - upworkPayout;

  const categories = [
    { name: 'Kotlin & KMP Core', count: '142 Open Projects', icon: Code },
    { name: 'Mobile (Android & iOS)', count: '215 Open Projects', icon: Smartphone },
    { name: 'Full-Stack & Ktor', count: '189 Open Projects', icon: Layers },
    { name: 'AI Systems & Gorse', count: '94 Open Projects', icon: Cpu },
    { name: 'WebAssembly (Wasm)', count: '86 Open Projects', icon: Globe },
    { name: 'Desktop & Compose UI', count: '112 Open Projects', icon: Monitor },
    { name: 'UI/UX & Design Tokens', count: '78 Open Projects', icon: Palette },
    { name: 'DevOps & Backend Services', count: '65 Open Projects', icon: Terminal },
  ];

  const featuredProjects = [
    {
      id: '1',
      title: 'Compose Multiplatform Desktop App for Ktor Analytics',
      budget: '$3,200',
      budgetType: 'Fixed Milestone',
      client: 'Acme AI Systems',
      posted: '2 hours ago',
      tags: ['Compose', 'Ktor', 'Desktop', 'SQLDelight']
    },
    {
      id: '2',
      title: 'High-Concurrency PostgreSQL Exposed ORM Migration',
      budget: '$1,800',
      budgetType: 'Fixed Milestone',
      client: 'Fintech Core',
      posted: '5 hours ago',
      tags: ['PostgreSQL', 'Exposed', 'Ktor', 'HikariCP']
    },
    {
      id: '3',
      title: 'WebAssembly Wasm Component for Audio Processing',
      budget: '$4,500',
      budgetType: 'Fixed Milestone',
      client: 'AudioCraft Labs',
      posted: '1 day ago',
      tags: ['Wasm', 'Kotlin', 'WebAudio', 'C++']
    },
  ];

  const paymentSteps = [
    {
      step: '01',
      title: 'Client Milestone Escrow Lock',
      desc: 'Before engineering work starts, the client deposits the milestone budget into a secure escrow account (Stripe / USDC).'
    },
    {
      step: '02',
      title: 'Code Delivery & Review',
      desc: 'The engineer builds the feature, submits PR deliverables, and provides live demonstration build artifacts.'
    },
    {
      step: '03',
      title: 'Instant Release & IP Transfer',
      desc: 'Upon client sign-off, funds release immediately to the developer with automated legal IP copyright transfer.'
    }
  ];

  const codeSnippets = {
    kmp: `// Shared Kotlin Multiplatform Wallet Repository
expect class PlatformWalletContext {
    val platformName: String
}

class KmpWalletEngine(
    private val database: SQLDelightDriver,
    private val ktorClient: HttpClient
) {
    suspend fun releaseEscrowMilestone(contractId: String): PayoutResult {
        return ktorClient.post("https://api.zapmancer.io/v1/escrow/release") {
            setBody(EscrowReleaseRequest(contractId, autoIpTransfer = true))
        }.body()
    }
}`,
    ktor: `// Ktor 3.0 High-Throughput Server WebSockets Router
fun Application.configureEscrowSockets() {
    routing {
        webSocket("/ws/escrow/{contractId}") {
            val contractId = call.parameters["contractId"]
            val liveSession = EscrowSessionManager.register(contractId)
            
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val event = Json.decodeFromString<MilestoneEvent>(frame.readText())
                    liveSession.broadcastEvent(event)
                }
            }
        }
    }
}`,
    escrow: `// Automated Legal Copyright & IP Transfer Contract
data class LegalIpTransferReceipt(
    val contractId: String,
    val clientLegalEntity: String,
    val engineerWalletAddress: String,
    val escrowPayoutAmount: Double,
    val copyrightLicense: String = "Exclusive Work-For-Hire Copyright Transfer (MIT/Proprietary)",
    val timestampUtc: Long = System.currentTimeMillis()
)`
  };

  const testimonials = [
    {
      name: 'Elena Rostova',
      role: 'KMP & Wasm Lead Engineer',
      avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80',
      earned: '$68,000+ Earned',
      quote: 'Zapmancer saved me over $12,000 in platform fees compared to Upwork. The milestone escrow releases are instant and the KMP project quality is unmatched.'
    },
    {
      name: 'Marcus Vance',
      role: 'CTO at Acme AI Systems',
      avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=150&q=80',
      earned: '14 Bounties Funded',
      quote: 'Finding vetted Kotlin Multiplatform and Ktor backend talent used to take weeks. With Zapmancer’s Gorse AI matching, we awarded our first milestone in 24 hours.'
    }
  ];

  const landingFaqs = [
    {
      q: 'How does Zapmancer handle Intellectual Property (IP) ownership?',
      a: 'Every completed milestone includes an automated, legally binding Work-for-Hire copyright transfer agreement. Upon escrow funds release, 100% of code IP, patents, and assets transfer exclusively to the client under standard software license terms (or custom NDA).'
    },
    {
      q: 'Why is Zapmancer Open Source and how does it prevent legal lock-in?',
      a: 'The core Zapmancer platform is 100% open-source under the Apache 2.0 license. This guarantees total auditability of escrow logic, eliminates vendor lock-in, and allows enterprise teams to self-host private marketplace nodes.'
    },
    {
      q: 'What are the exact platform pricing fees?',
      a: 'Unlike traditional platforms taking a 20% cut from engineers, Zapmancer charges 0% commission to developers. Clients pay a flat 3% to 5% operational cost recovery fee at escrow deposit time.'
    },
    {
      q: 'How are dispute resolution and escrow refunds managed?',
      a: 'If a milestone deliverable does not match agreed contract specs, either party can initiate automated dispute resolution backed by code repository audit logs and milestone verification windows.'
    },
    {
      q: 'Which payment methods are supported for escrow deposits & payouts?',
      a: 'Zapmancer supports automated Stripe Direct Bank Deposit, Credit Card, and Web3 USDC stablecoin payments directly into developer wallets with zero holding delays.'
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

      {/* Hero Section */}
      <section className="landing-hero-section">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center relative z-10 space-y-8">
          
          {/* Overline Badge */}
          <div className="landing-hero-badge">
            <Sparkles className="w-3.5 h-3.5" /> Open-Source KMP Freelance Platform
          </div>

          {/* Hero Title */}
          <h1 className="landing-hero-title">
            Fair, Transparent Escrow Freelancing Built for <span className="text-brand-green">Engineers & Creators</span>
          </h1>

          {/* Subtitle */}
          <p className="landing-hero-subtitle">
            Zapmancer eliminates high fee extractions (0% developer commission). Work directly with client escrow protection powered by Kotlin Multiplatform.
          </p>

          {/* Action Buttons */}
          <div className="flex flex-col sm:flex-row items-center justify-center gap-4 pt-4">
            <Link
              to="/signup"
              className="landing-btn-primary"
            >
              Get Started Free <ArrowRight className="w-4 h-4" />
            </Link>
            <Link
              to="/projects"
              className="landing-btn-secondary"
            >
              Browse Open Bounties
            </Link>
          </div>

          {/* Quick Metrics */}
          <div className="pt-12 grid grid-cols-2 md:grid-cols-4 gap-4 max-w-4xl mx-auto mt-12">
            <div className="landing-metric-card">
              <div className="text-3xl font-extrabold text-brand-green">$1.2M+</div>
              <div className="text-xs font-bold text-mute uppercase tracking-[0.05em] mt-1.5">Escrow Bounties Paid</div>
            </div>
            <div className="landing-metric-card">
              <div className="text-3xl font-extrabold text-ink">100%</div>
              <div className="text-xs font-bold text-mute uppercase tracking-[0.05em] mt-1.5">Open Source Codebase</div>
            </div>
            <div className="landing-metric-card">
              <div className="text-3xl font-extrabold text-brand-green">0%</div>
              <div className="text-xs font-bold text-mute uppercase tracking-[0.05em] mt-1.5">Developer Fees</div>
            </div>
            <div className="landing-metric-card">
              <div className="text-3xl font-extrabold text-ink">3,400+</div>
              <div className="text-xs font-bold text-mute uppercase tracking-[0.05em] mt-1.5">Verified Engineers</div>
            </div>
          </div>

        </div>
      </section>

      {/* Interactive Fee & Payout Calculator Widget */}
      <section className="py-20 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 w-full space-y-12">
        <div className="text-center space-y-2 max-w-2xl mx-auto">
          <div className="text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">Interactive Payout Estimator</div>
          <h2 className="text-3xl font-extrabold tracking-[-0.01em] text-ink">Calculate Your Freelancer Savings</h2>
          <p className="text-mute text-sm leading-relaxed">
            Drag the slider to see how much more money you keep on Zapmancer compared to legacy 20% fee platforms.
          </p>
        </div>

        <div className="bg-surface p-8 sm:p-10 rounded-xl border border-hairline shadow-sm dark:shadow-none max-w-4xl mx-auto space-y-8">
          
          {/* Slider Control */}
          <div className="space-y-4">
            <div className="flex items-center justify-between">
              <label className="text-xs font-bold uppercase tracking-wider text-mute flex items-center gap-2">
                <Sliders className="w-4 h-4 text-brand-green" /> Contract Bounty Amount
              </label>
              <span className="text-2xl font-extrabold text-brand-green">${calcAmount.toLocaleString()}</span>
            </div>

            <input
              type="range"
              min="500"
              max="20000"
              step="500"
              value={calcAmount}
              onChange={(e) => setCalcAmount(Number(e.target.value))}
              className="w-full accent-brand-green cursor-pointer h-2 bg-surface-elevated rounded-lg"
            />
            <div className="flex justify-between text-[11px] text-mute font-bold">
              <span>$500</span>
              <span>$5,000</span>
              <span>$10,000</span>
              <span>$20,000</span>
            </div>
          </div>

          {/* Comparison Cards Grid */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 pt-4 border-t border-hairline">
            <div className="p-6 rounded-xl bg-brand-green/10 border border-brand-green/30 space-y-2">
              <span className="text-[11px] font-extrabold text-brand-green uppercase tracking-wider">Zapmancer (0% Fee)</span>
              <p className="text-3xl font-extrabold text-brand-green">${zapmancerPayout.toLocaleString()}.00</p>
              <p className="text-xs text-brand-green font-bold flex items-center gap-1">
                <CheckCircle2 className="w-3.5 h-3.5" /> 100% Payout Retained
              </p>
            </div>

            <div className="p-6 rounded-xl bg-surface-elevated border border-hairline space-y-2 opacity-75">
              <span className="text-[11px] font-bold text-mute uppercase tracking-wider">Upwork (20% Cut)</span>
              <p className="text-2xl font-extrabold text-mute">${upworkPayout.toLocaleString()}.00</p>
              <p className="text-xs text-red-400 font-semibold">Loss: -${(calcAmount - upworkPayout).toLocaleString()}</p>
            </div>

            <div className="p-6 rounded-xl bg-surface-elevated border border-hairline space-y-2 opacity-60">
              <span className="text-[11px] font-bold text-mute uppercase tracking-wider">Freelancer.com (24% Cut)</span>
              <p className="text-2xl font-extrabold text-mute">${freelancerPayout.toLocaleString()}.00</p>
              <p className="text-xs text-red-400 font-semibold">Loss: -${(calcAmount - freelancerPayout).toLocaleString()}</p>
            </div>
          </div>

          <div className="bg-surface-elevated p-4 rounded-xl border border-hairline flex flex-col sm:flex-row items-center justify-between gap-4 text-center sm:text-left">
            <div className="space-y-0.5">
              <p className="text-xs font-bold text-ink">Extra earnings kept on this project:</p>
              <p className="text-sm font-extrabold text-brand-green">+${developerSavings.toLocaleString()}.00 into your wallet</p>
            </div>
            <Link to="/signup" className="landing-btn-primary py-2.5 px-6 shrink-0">
              Start Earning 100% Now
            </Link>
          </div>

        </div>
      </section>

      {/* Developer Architecture & Code Terminal Carousel */}
      <section className="py-20 bg-surface border-y border-hairline">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-12">
          
          <div className="text-center space-y-2 max-w-2xl mx-auto">
            <div className="text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">Developer Experience</div>
            <h2 className="text-3xl font-extrabold tracking-[-0.01em] text-ink">Built for Kotlin & Multiplatform Engineering</h2>
            <p className="text-mute text-sm leading-relaxed">
              Explore clean, auditable KMP and Ktor backend integration templates powered by JetBrains Mono.
            </p>
          </div>

          {/* Code Window Container */}
          <div className="max-w-4xl mx-auto bg-canvas rounded-xl border border-hairline shadow-2xl overflow-hidden">
            
            {/* Terminal Header Bar */}
            <div className="bg-surface-elevated px-4 py-3 border-b border-hairline flex items-center justify-between">
              <div className="flex items-center gap-2">
                <span className="w-3 h-3 rounded-full bg-red-500/80"></span>
                <span className="w-3 h-3 rounded-full bg-yellow-500/80"></span>
                <span className="w-3 h-3 rounded-full bg-green-500/80"></span>
                <span className="text-xs font-mono text-mute ml-2">zapmancer-kmp-engine &bull; bash</span>
              </div>

              <div className="flex items-center gap-2">
                <button
                  onClick={() => setActiveCodeTab('kmp')}
                  className={`px-3 py-1 rounded-md text-xs font-mono transition-colors ${
                    activeCodeTab === 'kmp' ? 'bg-brand-green text-white font-bold' : 'text-mute hover:text-ink'
                  }`}
                >
                  WalletRepo.kt
                </button>
                <button
                  onClick={() => setActiveCodeTab('ktor')}
                  className={`px-3 py-1 rounded-md text-xs font-mono transition-colors ${
                    activeCodeTab === 'ktor' ? 'bg-brand-green text-white font-bold' : 'text-mute hover:text-ink'
                  }`}
                >
                  Router.kt
                </button>
                <button
                  onClick={() => setActiveCodeTab('escrow')}
                  className={`px-3 py-1 rounded-md text-xs font-mono transition-colors ${
                    activeCodeTab === 'escrow' ? 'bg-brand-green text-white font-bold' : 'text-mute hover:text-ink'
                  }`}
                >
                  LegalIpContract.kt
                </button>

                <button
                  onClick={handleCopyCode}
                  className="p-1.5 rounded-md text-mute hover:text-brand-green transition-colors"
                  title="Copy Code"
                >
                  {copiedCode ? <Check className="w-4 h-4 text-brand-green" /> : <Copy className="w-4 h-4" />}
                </button>
              </div>
            </div>

            {/* Code Body */}
            <pre className="p-6 font-mono text-xs text-brand-green bg-canvas overflow-x-auto leading-relaxed">
              <code>{codeSnippets[activeCodeTab]}</code>
            </pre>

          </div>

        </div>
      </section>

      {/* Featured Categories / Skill Ecosystems */}
      <section className="py-20 bg-canvas border-b border-hairline">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-10">
          <div className="flex flex-col sm:flex-row sm:items-end justify-between gap-4 border-b border-hairline pb-6">
            <div>
              <div className="text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">Engineering Ecosystems</div>
              <h2 className="text-3xl font-extrabold tracking-tight text-ink mt-1">Explore Skill Ecosystems</h2>
              <p className="text-sm text-mute">Specialized talent pools and contract bounties across 8 core engineering disciplines.</p>
            </div>
            <Link to="/projects" className="text-brand-green hover:text-brand-green-hover text-xs font-bold uppercase tracking-[0.05em] transition-colors shrink-0">
              View All Categories &rarr;
            </Link>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-6">
            {categories.map((cat) => {
              const Icon = cat.icon;
              return (
                <Link
                  key={cat.name}
                  to="/projects"
                  className="p-6 rounded-xl bg-surface border border-hairline hover:bg-surface-elevated hover:scale-[1.03] transition-all duration-200 shadow-sm dark:shadow-none group flex flex-col justify-between"
                >
                  <div>
                    <div className="w-10 h-10 rounded-full bg-surface-elevated text-brand-green flex items-center justify-center mb-4 group-hover:bg-brand-green group-hover:text-white transition-colors border border-hairline">
                      <Icon className="w-5 h-5" />
                    </div>
                    <h3 className="font-bold text-base text-ink group-hover:text-brand-green transition-colors">{cat.name}</h3>
                  </div>
                  <p className="text-xs text-mute mt-3 font-semibold">{cat.count}</p>
                </Link>
              );
            })}
          </div>
        </div>
      </section>

      {/* Community Testimonials */}
      <section className="py-20 bg-surface border-y border-hairline">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-12">
          
          <div className="text-center space-y-2 max-w-2xl mx-auto">
            <div className="text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">Vetted Community</div>
            <h2 className="text-3xl font-extrabold tracking-[-0.01em] text-ink">Trusted by Engineers & Founders</h2>
            <p className="text-mute text-sm leading-relaxed">
              Read how developers and enterprise teams build products faster with zero fee extractions.
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
                    <img src={t.avatar} alt={t.name} className="w-12 h-12 rounded-full object-cover border-2 border-brand-green/40" />
                    <div>
                      <h4 className="font-bold text-ink text-base flex items-center gap-1.5">
                        {t.name} <ShieldCheck className="w-4 h-4 text-brand-green" />
                      </h4>
                      <p className="text-xs text-mute font-medium">{t.role}</p>
                    </div>
                  </div>

                  <span className="text-xs font-bold text-brand-green bg-brand-green/10 px-3 py-1 rounded-full border border-brand-green/20">
                    {t.earned}
                  </span>
                </div>
              </div>
            ))}
          </div>

        </div>
      </section>

      {/* How Payment & Escrow Works Section */}
      <section className="py-20 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 w-full space-y-12">
        <div className="text-center space-y-2 max-w-2xl mx-auto">
          <div className="text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">Transparent Payments</div>
          <h2 className="text-3xl font-extrabold tracking-[-0.01em] text-ink">How Escrow & Payouts Work</h2>
          <p className="text-mute text-sm leading-relaxed">
            Zero surprise fees. Funds are protected in milestone escrow before coding begins, and released instantly upon sign-off.
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {paymentSteps.map((step) => (
            <div key={step.step} className="bg-surface border border-hairline p-8 rounded-xl space-y-4 shadow-sm dark:shadow-none hover:bg-surface-elevated transition-colors">
              <div className="text-2xl font-extrabold text-brand-green font-mono">{step.step}</div>
              <h3 className="text-lg font-bold text-ink">{step.title}</h3>
              <p className="text-xs text-mute leading-relaxed">{step.desc}</p>
            </div>
          ))}
        </div>
      </section>

      {/* Legal & Open Source Compliance Section */}
      <section className="py-20 bg-surface border-y border-hairline">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-12">
          
          <div className="text-center space-y-2 max-w-2xl mx-auto">
            <div className="text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">Open Source & Legal Safety</div>
            <h2 className="text-3xl font-extrabold tracking-[-0.01em] text-ink">Auditable Code, Guaranteed IP Transfer</h2>
            <p className="text-mute text-sm leading-relaxed">
              Addressing legal challenges, IP rights, and open-source transparency for enterprise teams and developers.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
            <div className="p-8 rounded-xl bg-surface-elevated border border-hairline space-y-4">
              <div className="w-10 h-10 rounded-full bg-brand-green/10 text-brand-green flex items-center justify-center border border-brand-green/20">
                <Scale className="w-5 h-5" />
              </div>
              <h3 className="text-xl font-bold text-ink">Work-for-Hire IP Transfer</h3>
              <p className="text-xs text-mute leading-relaxed">
                All contract submissions include automated legal IP copyright transfer. Clients retain 100% exclusive ownership of custom code deliverables, patents, and design assets upon milestone funds release.
              </p>
              <div className="flex items-center gap-2 text-xs font-bold text-brand-green pt-1">
                <ShieldCheck className="w-4 h-4" /> Standardized Legal Contracts Attached
              </div>
            </div>

            <div className="p-8 rounded-xl bg-surface-elevated border border-hairline space-y-4">
              <div className="w-10 h-10 rounded-full bg-brand-green/10 text-brand-green flex items-center justify-center border border-brand-green/20">
                <GitBranch className="w-5 h-5" />
              </div>
              <h3 className="text-xl font-bold text-ink">Why 100% Open Source?</h3>
              <p className="text-xs text-mute leading-relaxed">
                By keeping the platform core open-source, escrow logic and matching algorithms are completely auditable. No hidden black-box algorithms or proprietary vendor lock-in.
              </p>
              <div className="flex items-center gap-2 text-xs font-bold text-brand-green pt-1">
                <CheckCircle2 className="w-4 h-4" /> Apache 2.0 Audited Marketplace Core
              </div>
            </div>
          </div>

        </div>
      </section>

      {/* Live Marketplace — Active Project Bounties Section */}
      <section className="py-20 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 w-full space-y-8">
        <div className="flex flex-col sm:flex-row sm:items-end justify-between gap-4 border-b border-hairline pb-6">
          <div className="space-y-1">
            <div className="inline-flex items-center gap-1.5 text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">
              <Sparkles className="w-3.5 h-3.5" /> Live Marketplace
            </div>
            <h2 className="text-3xl font-extrabold tracking-tight text-ink">Active Project Bounties</h2>
            <p className="text-sm text-mute">Explore verified contract bounties with funded milestone escrows.</p>
          </div>
          <Link to="/projects" className="text-brand-green hover:text-brand-green-hover text-xs font-bold uppercase tracking-[0.05em] transition-colors shrink-0">
            Explore All Projects &rarr;
          </Link>
        </div>

        {/* Clean Project Cards List */}
        <div className="space-y-4">
          {featuredProjects.map((p) => (
            <div
              key={p.id}
              className="bg-surface p-6 rounded-xl border border-hairline hover:bg-surface-elevated hover:scale-[1.005] transition-all duration-200 flex flex-col md:flex-row md:items-center justify-between gap-6 shadow-sm dark:shadow-none"
            >
              <div className="space-y-2 flex-1">
                <div className="flex items-center gap-2 text-xs">
                  <span className="font-bold text-brand-green uppercase tracking-wider">{p.client}</span>
                  <span className="text-mute">&bull;</span>
                  <span className="text-mute flex items-center gap-1"><ShieldCheck className="w-3.5 h-3.5 text-brand-green" /> Verified Escrow</span>
                  <span className="text-mute">&bull;</span>
                  <span className="text-mute">{p.posted}</span>
                </div>

                <h3 className="font-extrabold text-lg text-ink hover:text-brand-green transition-colors">
                  <Link to={`/projects/${p.id}`}>{p.title}</Link>
                </h3>

                <div className="flex flex-wrap gap-2 pt-1">
                  {p.tags.map((t) => (
                    <span key={t} className="px-3.5 py-1 rounded-full bg-surface-elevated text-brand-green text-xs font-bold border border-hairline">
                      {t}
                    </span>
                  ))}
                </div>
              </div>

              <div className="flex items-center justify-between md:justify-end gap-6 border-t md:border-t-0 border-hairline pt-4 md:pt-0 shrink-0">
                <div className="text-left md:text-right">
                  <div className="text-xl font-extrabold text-brand-green tracking-tight">{p.budget}</div>
                  <div className="text-xs text-mute font-bold uppercase tracking-wider">{p.budgetType}</div>
                </div>

                <Link
                  to={`/projects/${p.id}/apply`}
                  className="bg-brand-green hover:bg-brand-green-hover text-white font-bold text-xs uppercase tracking-[0.05em] px-6 py-3 rounded-full hover:scale-[1.04] transition-all shadow-md shadow-brand-green/20"
                >
                  Apply Now
                </Link>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* Native Q&A / FAQ Section (Moved to Bottom) */}
      <section className="py-20 max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 w-full space-y-10 border-t border-hairline">
        <div className="text-center space-y-2 max-w-xl mx-auto">
          <div className="inline-flex items-center gap-1.5 text-brand-green text-[11px] font-bold tracking-[0.1em] uppercase">
            <HelpCircle className="w-3.5 h-3.5" /> Frequently Asked Questions
          </div>
          <h2 className="text-3xl font-extrabold tracking-tight text-ink">Everything You Need to Know</h2>
          <p className="text-sm text-mute">Clear answers regarding contract escrow, 0% developer fees, IP ownership, and open-source compliance.</p>
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
                    ? 'bg-surface-elevated border-brand-green/40 shadow-sm'
                    : 'bg-surface border-hairline hover:bg-surface-elevated'
                }`}
              >
                <div className="flex items-center justify-between gap-4">
                  <h3 className="text-base font-bold text-ink leading-snug flex items-center gap-3">
                    <span className="w-6 h-6 rounded-full bg-brand-green/10 text-brand-green font-mono text-xs flex items-center justify-center shrink-0">
                      ?
                    </span>
                    {faq.q}
                  </h3>
                  {isOpen ? (
                    <ChevronUp className="w-5 h-5 text-brand-green shrink-0" />
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
            <div className="flex items-center gap-2 text-brand-green font-bold text-[11px] uppercase tracking-[0.1em]">
              <Lock className="w-4 h-4" /> Built-in Escrow Security
            </div>
            <h2 className="text-3xl font-extrabold tracking-tight text-ink">Zero Risk Milestone Escrow Protection</h2>
            <p className="text-mute text-sm leading-relaxed">
              Client funds are safely locked in escrow prior to milestone commencement. Freelancers deliver clean code, and payments release seamlessly upon milestone approval.
            </p>
          </div>
          <Link to="/signup" className="bg-brand-green hover:bg-brand-green-hover text-white font-bold text-xs uppercase tracking-[0.05em] px-8 py-4 rounded-full hover:scale-[1.04] transition-all shrink-0 shadow-md shadow-brand-green/20">
            Join Marketplace Now
          </Link>
        </div>
      </section>

      <Footer />
    </div>
  );
};
