export interface WorkHistoryItem {
  project: string;
  client: string;
  rating: number;
  earned: string;
  date: string;
  review: string;
  milestones: { name: string; amount: string }[];
}

export interface PortfolioItem {
  name: string;
  stars: string;
  desc: string;
  url: string;
}

export interface TalentProfile {
  id: string;
  name: string;
  title: string;
  specialty: 'Dev' | 'AI' | 'Design' | 'DevOps' | 'Growth';
  avatar: string;
  rating: number;
  reviews: number;
  rateNum: number;
  rateDisplay: string;
  successRate: string;
  location: string;
  region: string;
  availableNow: boolean;
  talentType: string;
  totalEarned: number;
  earnedDisplay: string;
  contracts: number;
  onTime: string;
  availability: string;
  bio: string;
  skills: string[];
  workHistory: WorkHistoryItem[];
  portfolio: PortfolioItem[];
}

const FALLBACK_TALENTS: TalentProfile[] = [
  {
    id: 't1',
    name: 'Elena Rostova',
    title: 'Senior Full-Stack & WebAssembly Lead',
    specialty: 'Dev',
    avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80',
    rating: 5.0,
    reviews: 42,
    rateNum: 85,
    rateDisplay: '$85 / hr',
    successRate: '100% Success',
    location: 'Berlin, Germany',
    region: 'Europe',
    availableNow: true,
    talentType: 'Individual',
    totalEarned: 68000,
    earnedDisplay: '$68,000+',
    contracts: 18,
    onTime: '100%',
    availability: 'Available · 30 hrs/wk',
    bio: 'Architected high-throughput backend microservices, React web applications, and WebAssembly audio processing components for enterprise clients.',
    skills: ['TypeScript', 'React', 'Node.js', 'Wasm', 'PostgreSQL', 'Docker'],
    workHistory: [
      {
        project: 'Compose Multiplatform Mobile Wallet Core',
        client: 'Fintech Core Ltd',
        rating: 5.0,
        earned: '$12,500',
        date: 'Jan – Mar 2026',
        review: 'Elena delivered top-tier code with 100% test coverage ahead of schedule.',
        milestones: [
          { name: 'Shared Logic & Ktor Integration', amount: '$4,500' },
          { name: 'Reactive Desktop & Mobile Views', amount: '$4,500' },
          { name: 'CI Pipeline & Security Audit', amount: '$3,500' },
        ],
      },
    ],
    portfolio: [
      { name: 'KMP Multiplatform Wallet', stars: '1.2k', desc: 'Compose Mobile & Web Wasm wallet with SQLDelight persistence', url: 'https://github.com' },
      { name: 'Ktor Exposed Microservice Template', stars: '840', desc: 'High-concurrency starter with HikariCP & WebSockets', url: 'https://github.com' },
    ],
  },
  {
    id: 't2',
    name: 'Dr. Lucas Meyer',
    title: 'AI Agent & RAG Pipeline Specialist',
    specialty: 'AI',
    avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=150&q=80',
    rating: 5.0,
    reviews: 38,
    rateNum: 95,
    rateDisplay: '$95 / hr',
    successRate: '100% Success',
    location: 'Zurich, Switzerland',
    region: 'Europe',
    availableNow: true,
    talentType: 'Individual',
    totalEarned: 84000,
    earnedDisplay: '$84,000+',
    contracts: 22,
    onTime: '100%',
    availability: 'Available · 40 hrs/wk',
    bio: 'Specialized in vector embeddings, LangChain RAG pipelines, LLM fine-tuning, and Gorse AI recommendation clusters for enterprise-scale deployments.',
    skills: ['Python', 'OpenAI', 'LangChain', 'PGVector', 'Gorse AI', 'PyTorch'],
    workHistory: [
      {
        project: 'Vector Recommendation Engine Pipeline',
        client: 'Gorse Labs',
        rating: 5.0,
        earned: '$18,000',
        date: 'Oct – Dec 2025',
        review: 'Dr. Lucas is one of the brightest AI systems minds we have hired.',
        milestones: [
          { name: 'PGVector Schema & Embeddings', amount: '$8,000' },
          { name: 'Real-Time Re-ranking Pipeline', amount: '$10,000' },
        ],
      },
    ],
    portfolio: [
      { name: 'PGVector LangChain Bridge', stars: '2.1k', desc: 'High throughput semantic search vector store for Ktor/Python', url: 'https://github.com' },
    ],
  },
];

const DEFAULT_ME_PROFILE: TalentProfile = {
  id: 'me',
  name: 'Alex Morgan',
  title: 'Senior KMP & Full-Stack Architect',
  specialty: 'Dev',
  avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80',
  rating: 4.98,
  reviews: 34,
  rateNum: 85,
  rateDisplay: '$85 / hr',
  successRate: '100% Success',
  location: 'San Francisco, USA',
  region: 'Americas',
  availableNow: true,
  talentType: 'Individual',
  totalEarned: 68000,
  earnedDisplay: '$68,000+',
  contracts: 18,
  onTime: '100%',
  availability: 'Available · 30 hrs/wk',
  bio: 'Kotlin Multiplatform engineer with 6+ years architecting cross-platform desktop and mobile apps connected to high-throughput Ktor backend microservices. Focused on clean architecture, test coverage, and on-time delivery.',
  skills: ['Kotlin Multiplatform', 'Compose UI', 'Ktor Server', 'PostgreSQL', 'WebAssembly', 'Docker'],
  workHistory: [
    {
      project: 'Compose Multiplatform Mobile Wallet Core',
      client: 'Fintech Core Ltd',
      rating: 5.0,
      earned: '$12,500',
      date: 'Jan – Mar 2026',
      review: 'Alex delivered top-tier KMP code with 100% test coverage ahead of schedule.',
      milestones: [
        { name: 'Shared KMP Wallet Logic', amount: '$4,500' },
        { name: 'Compose Mobile Views', amount: '$4,500' },
        { name: 'CI Pipeline & Security Audit', amount: '$3,500' },
      ],
    },
  ],
  portfolio: [
    { name: 'KMP Multiplatform Wallet', stars: '1.2k', desc: 'Compose Mobile & Web Wasm wallet with SQLDelight persistence', url: 'https://github.com' },
    { name: 'Ktor Exposed Microservice Template', stars: '840', desc: 'High-concurrency starter with HikariCP & WebSockets', url: 'https://github.com' },
  ],
};

const PROFILE_KEY = 'zapmancer_me_profile';

export const userService = {
  async searchTalent(params?: {
    query?: string;
    category?: string;
    skills?: string[];
  }): Promise<TalentProfile[]> {
    try {
      const url = new URL('http://localhost:8080/users/search');
      if (params?.query) url.searchParams.append('query', params.query);
      if (params?.category && params.category !== 'All') url.searchParams.append('category', params.category);

      const token = localStorage.getItem('zapmancer_jwt_token');
      const headers: Record<string, string> = { 'Accept': 'application/json' };
      if (token) headers['Authorization'] = `Bearer ${token}`;

      const res = await fetch(url.toString(), { headers });
      if (res.ok) {
        const body = await res.json();
        const items = body.data || body;
        if (Array.isArray(items) && items.length > 0) {
          return items.map((u: any) => ({
            id: String(u.id),
            name: u.name || u.username,
            title: u.title || 'Senior Software Engineer',
            specialty: u.specialty || 'Dev',
            avatar: u.avatar || 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80',
            rating: u.rating || 5.0,
            reviews: u.reviewsCount || 20,
            rateNum: u.hourlyRate || 85,
            rateDisplay: `$${u.hourlyRate || 85} / hr`,
            successRate: '100% Success',
            location: u.location || 'Remote',
            region: 'Global',
            availableNow: true,
            talentType: 'Individual',
            totalEarned: u.totalEarned || 50000,
            earnedDisplay: `$${Number(u.totalEarned || 50000).toLocaleString()}+`,
            contracts: u.contractsCount || 10,
            onTime: '100%',
            availability: 'Available',
            bio: u.bio || '',
            skills: Array.isArray(u.skills) ? u.skills : ['Kotlin', 'TypeScript'],
            workHistory: [],
            portfolio: [],
          }));
        }
      }
    } catch {
      // Offline fallback
    }

    return FALLBACK_TALENTS;
  },

  async getUserProfile(userId: string): Promise<TalentProfile> {
    if (userId === 'me' || userId === 'current') {
      const stored = localStorage.getItem(PROFILE_KEY);
      if (stored) return JSON.parse(stored);
      return DEFAULT_ME_PROFILE;
    }

    const found = FALLBACK_TALENTS.find((t) => t.id === userId);
    if (found) return found;

    try {
      const token = localStorage.getItem('zapmancer_jwt_token');
      const headers: Record<string, string> = { 'Accept': 'application/json' };
      if (token) headers['Authorization'] = `Bearer ${token}`;

      const res = await fetch(`http://localhost:8080/users/${userId}`, { headers });
      if (res.ok) {
        const body = await res.json();
        const u = body.data || body;
        return {
          ...DEFAULT_ME_PROFILE,
          id: String(u.id || userId),
          name: u.name || u.username,
          title: u.title || DEFAULT_ME_PROFILE.title,
          bio: u.bio || DEFAULT_ME_PROFILE.bio,
          location: u.location || DEFAULT_ME_PROFILE.location,
          rateNum: u.hourlyRate || DEFAULT_ME_PROFILE.rateNum,
          rateDisplay: `$${u.hourlyRate || DEFAULT_ME_PROFILE.rateNum} / hr`,
        };
      }
    } catch {
      // Offline fallback
    }

    return DEFAULT_ME_PROFILE;
  },

  async updateUserProfile(payload: Partial<TalentProfile>): Promise<TalentProfile> {
    const current = await this.getUserProfile('me');
    const updated: TalentProfile = {
      ...current,
      ...payload,
      rateDisplay: payload.rateNum ? `$${payload.rateNum} / hr` : current.rateDisplay,
    };

    localStorage.setItem(PROFILE_KEY, JSON.stringify(updated));

    try {
      const token = localStorage.getItem('zapmancer_jwt_token');
      await fetch('http://localhost:8080/users/profile', {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
        },
        body: JSON.stringify(payload),
      });
    } catch {
      // Offline fallback
    }

    return updated;
  },
};
