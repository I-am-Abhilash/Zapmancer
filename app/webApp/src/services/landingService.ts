export interface MarketplaceCategory {
  id: string;
  name: string;
  count: string;
  iconName: string;
  tint: string;
}

export interface HowItWorksStep {
  n: string;
  title: string;
  desc: string;
}

export interface FaqItem {
  q: string;
  a: string;
}

export interface HeroStat {
  value: string;
  label: string;
}

export interface LandingData {
  headline: string;
  subtitle: string;
  stats: HeroStat[];
  categories: MarketplaceCategory[];
  steps: HowItWorksStep[];
  faqs: FaqItem[];
}

const DEFAULT_LANDING_DATA: LandingData = {
  headline: 'Run your company and hire developers, designers, and AI builders.',
  subtitle: 'Manage your full-time team alongside on-demand freelancers — all in one workspace, with milestone escrow and 0% worker commission.',
  stats: [
    { value: '$1.2M+', label: 'escrow paid' },
    { value: '0%', label: 'worker fee' },
    { value: '3,400+', label: 'verified members' },
    { value: '100%', label: 'auto IP transfer' },
  ],
  categories: [
    { id: 'c1', name: 'Full-Stack & Web Dev', count: '342 open contracts', iconName: 'Code', tint: 'landing-feat-card-mint' },
    { id: 'c2', name: 'AI Builders & Agents', count: '215 open contracts', iconName: 'Bot', tint: 'landing-feat-card-lavender' },
    { id: 'c3', name: 'UI/UX & Product Design', count: '189 open contracts', iconName: 'Palette', tint: 'landing-feat-card-sky' },
    { id: 'c4', name: 'Mobile (iOS & Android)', count: '194 open contracts', iconName: 'Smartphone', tint: 'landing-feat-card-peach' },
    { id: 'c5', name: 'DevOps & Cloud', count: '126 open contracts', iconName: 'Terminal', tint: 'landing-feat-card-rose' },
    { id: 'c6', name: 'Data & Analytics', count: '112 open contracts', iconName: 'Database', tint: 'landing-feat-card-yellow' },
    { id: 'c7', name: 'Technical Writing', count: '78 open contracts', iconName: 'PenTool', tint: 'landing-feat-card-mint' },
    { id: 'c8', name: 'Growth & Marketing', count: '95 open contracts', iconName: 'TrendingUp', tint: 'landing-feat-card-sky' },
  ],
  steps: [
    {
      n: '1',
      title: 'Post a project or hire directly',
      desc: 'Create a Company Workspace, post a scoped contract, and set a fixed or hourly budget. Receive proposals from vetted workers within hours.',
    },
    {
      n: '2',
      title: 'Deposit into milestone escrow',
      desc: 'Funds are held in secure escrow (Stripe or USDC stablecoin) before any work begins — protecting both sides of the contract.',
    },
    {
      n: '3',
      title: 'Review deliverables and release payment',
      desc: 'Sign off on completed work and funds transfer instantly. An automated Work-for-Hire IP agreement executes at the same moment.',
    },
  ],
  faqs: [
    {
      q: 'Who can join Zapmancer?',
      a: 'Anyone. Companies register a Workspace to manage teams and contract freelancers. Developers, designers, AI builders, marketers, and creators join to find paid work.',
    },
    {
      q: 'What are the platform fees?',
      a: 'Workers pay 0% commission. Companies pay a flat 3–5% escrow processing fee at milestone deposit time — no hidden charges, no sliding scale.',
    },
    {
      q: 'How does IP ownership work?',
      a: 'Every completed milestone triggers an automated, legally-binding Work-for-Hire copyright transfer. All deliverables — code, design, content — transfer 100% to the client on payment release.',
    },
    {
      q: 'What payment methods are supported?',
      a: 'Stripe bank transfer (USD, EUR, GBP), credit card, and USDC stablecoin. Payouts reach worker accounts with no holding period.',
    },
    {
      q: 'Can companies manage a full-time team on Zapmancer?',
      a: 'Yes. The Company Workspace includes a sprint board, team roster, time logs, payroll, and an escrow vault — covering both permanent staff and contract workers in one place.',
    },
  ],
};

const STORAGE_KEY = 'zapmancer_landing_data';

export const landingService = {
  async getLandingData(): Promise<LandingData> {
    try {
      const res = await fetch('http://localhost:8080/landing-page/data', {
        headers: { Accept: 'application/json' },
      });
      if (res.ok) {
        const json = await res.json();
        const data = json.data || json;
        if (data && data.hero) {
          const mapped: LandingData = {
            headline: data.hero.headline || DEFAULT_LANDING_DATA.headline,
            subtitle: data.hero.subtext || DEFAULT_LANDING_DATA.subtitle,
            stats: DEFAULT_LANDING_DATA.stats,
            categories: (data.marketplaceCategories || []).map((c: any, i: number) => ({
              id: `cat_${i}`,
              name: c.name || c.title || 'Category',
              count: `${c.projectCount || 100}+ contracts`,
              iconName: c.iconName || 'Code',
              tint: DEFAULT_LANDING_DATA.categories[i % DEFAULT_LANDING_DATA.categories.length].tint,
            })),
            steps: DEFAULT_LANDING_DATA.steps,
            faqs: DEFAULT_LANDING_DATA.faqs,
          };
          localStorage.setItem(STORAGE_KEY, JSON.stringify(mapped));
          return mapped;
        }
      }
    } catch {
      // Fallback
    }

    const stored = localStorage.getItem(STORAGE_KEY);
    if (stored) {
      try {
        return JSON.parse(stored);
      } catch {
        // Fallback
      }
    }

    return DEFAULT_LANDING_DATA;
  },
};
