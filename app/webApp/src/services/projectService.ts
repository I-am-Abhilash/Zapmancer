export interface ProjectItem {
  id: string;
  title: string;
  client: string;
  clientAvatar: string;
  projectType: 'Fixed' | 'Hourly';
  budgetValue: number;
  budgetDisplay: string;
  duration: string;
  level: string;
  proposals: number;
  postedHours: number;
  postedDisplay: string;
  verified: boolean;
  clientSpent: number;
  clientRating: number;
  description: string;
  skills: string[];
}

export interface ProjectDetailData extends ProjectItem {
  location: string;
  deliverables?: string[];
}

export interface CreateProjectPayload {
  title: string;
  category: string;
  description: string;
  budgetType: 'Fixed' | 'Hourly';
  budgetAmount: number;
  skills: string[];
}

const FALLBACK_PROJECTS: ProjectItem[] = [
  {
    id: '1',
    title: 'Compose Multiplatform Desktop App for Ktor Analytics',
    client: 'Acme AI Systems',
    clientAvatar: 'https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=120&q=80',
    projectType: 'Fixed',
    budgetValue: 3200,
    budgetDisplay: '$3,200',
    duration: '3 weeks',
    level: 'Intermediate',
    proposals: 8,
    postedHours: 2,
    postedDisplay: '2 hours ago',
    verified: true,
    clientSpent: 42000,
    clientRating: 4.9,
    description: 'Desktop management dashboard with Compose Multiplatform for Kotlin, integrating Ktor backend REST API and WebSocket streaming endpoints.',
    skills: ['Kotlin', 'Compose', 'Ktor', 'Desktop', 'SQLDelight'],
  },
  {
    id: '2',
    title: 'High-Concurrency PostgreSQL Exposed ORM Migration',
    client: 'Fintech Core',
    clientAvatar: 'https://images.unsplash.com/photo-1557804506-669a67965ba0?auto=format&fit=crop&w=120&q=80',
    projectType: 'Fixed',
    budgetValue: 1800,
    budgetDisplay: '$1,800',
    duration: '10 days',
    level: 'Expert',
    proposals: 14,
    postedHours: 5,
    postedDisplay: '5 hours ago',
    verified: true,
    clientSpent: 12500,
    clientRating: 4.8,
    description: 'Refactor legacy SQL queries into Exposed ORM DSL with HikariCP pooling, automated migrations, and multi-region read replica fallback.',
    skills: ['PostgreSQL', 'Exposed', 'Ktor', 'SQL', 'HikariCP'],
  },
  {
    id: '3',
    title: 'WebAssembly Component for Real-Time Audio Processing',
    client: 'AudioCraft Labs',
    clientAvatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=120&q=80',
    projectType: 'Fixed',
    budgetValue: 4500,
    budgetDisplay: '$4,500',
    duration: '1 month',
    level: 'Expert',
    proposals: 4,
    postedHours: 24,
    postedDisplay: '1 day ago',
    verified: true,
    clientSpent: 8200,
    clientRating: 5.0,
    description: 'High-performance Wasm module compiled from Kotlin Native to process web audio streams in real-time with zero latency drops.',
    skills: ['Wasm', 'Kotlin', 'WebAudio', 'Desktop'],
  },
  {
    id: '4',
    title: 'Native Android Material 3 Design Overhaul',
    client: 'HealthSync Mobile',
    clientAvatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=120&q=80',
    projectType: 'Hourly',
    budgetValue: 65,
    budgetDisplay: '$65 / hr',
    duration: '2–3 weeks',
    level: 'Entry',
    proposals: 19,
    postedHours: 48,
    postedDisplay: '2 days ago',
    verified: false,
    clientSpent: 1500,
    clientRating: 4.2,
    description: 'Modernize Android app views to Material 3, Jetpack Compose adaptive layouts, and dynamic theme switching support.',
    skills: ['Android', 'Jetpack Compose', 'Kotlin', 'Material 3'],
  },
];

export const projectService = {
  async getProjects(params?: {
    query?: string;
    category?: string;
    sortBy?: string;
  }): Promise<ProjectItem[]> {
    try {
      const url = new URL('http://localhost:8080/projects');
      if (params?.query) url.searchParams.append('query', params.query);
      if (params?.category && params.category !== 'All') url.searchParams.append('category', params.category);
      if (params?.sortBy) url.searchParams.append('sortBy', params.sortBy);

      const token = localStorage.getItem('zapmancer_jwt_token');
      const headers: Record<string, string> = { 'Accept': 'application/json' };
      if (token) headers['Authorization'] = `Bearer ${token}`;

      const res = await fetch(url.toString(), { headers });
      if (res.ok) {
        const body = await res.json();
        const items = body.data || body;
        if (Array.isArray(items) && items.length > 0) {
          return items.map((p: any) => ({
            id: String(p.id || p.bountyId),
            title: p.title,
            client: p.clientName || p.client || 'Acme AI Systems',
            clientAvatar: p.clientAvatar || 'https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=120&q=80',
            projectType: p.budgetType === 'Hourly' ? 'Hourly' : 'Fixed',
            budgetValue: Number(p.budget || p.budgetValue || 0),
            budgetDisplay: p.budgetType === 'Hourly' ? `$${p.budget || p.budgetValue} / hr` : `$${Number(p.budget || p.budgetValue || 0).toLocaleString()}`,
            duration: p.duration || '2–4 weeks',
            level: p.experienceLevel || p.level || 'Intermediate',
            proposals: p.proposalsCount || p.proposals || 0,
            postedHours: 2,
            postedDisplay: 'Recently posted',
            verified: p.isVerified ?? true,
            clientSpent: p.clientSpent || 42000,
            clientRating: p.clientRating || 4.9,
            description: p.description || '',
            skills: Array.isArray(p.skills) ? p.skills : ['Kotlin', 'TypeScript', 'Ktor'],
          }));
        }
      }
    } catch {
      // Offline fallback
    }

    return FALLBACK_PROJECTS;
  },

  async getProjectDetail(id: string): Promise<ProjectDetailData> {
    try {
      const token = localStorage.getItem('zapmancer_jwt_token');
      const headers: Record<string, string> = { 'Accept': 'application/json' };
      if (token) headers['Authorization'] = `Bearer ${token}`;

      const res = await fetch(`http://localhost:8080/projects/${id}`, { headers });
      if (res.ok) {
        const body = await res.json();
        const p = body.data || body;
        return {
          id: String(p.id || id),
          title: p.title,
          client: p.clientName || 'Acme AI Systems',
          clientAvatar: p.clientAvatar || 'https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=120&q=80',
          projectType: p.budgetType === 'Hourly' ? 'Hourly' : 'Fixed',
          budgetValue: Number(p.budget || 3200),
          budgetDisplay: p.budgetType === 'Hourly' ? `$${p.budget} / hr` : `$${Number(p.budget || 3200).toLocaleString()}`,
          duration: p.duration || '3–4 weeks',
          level: p.experienceLevel || 'Intermediate',
          proposals: p.proposalsCount || 8,
          postedHours: 2,
          postedDisplay: '2 hours ago',
          verified: true,
          clientSpent: 42000,
          clientRating: 4.9,
          location: p.location || 'San Francisco, CA',
          description: p.description || 'Project deliverables and milestone specifications.',
          skills: Array.isArray(p.skills) ? p.skills : ['Kotlin Multiplatform', 'Compose Desktop', 'Ktor Client'],
        };
      }
    } catch {
      // Offline fallback
    }

    const fallback = FALLBACK_PROJECTS.find((p) => p.id === id) || FALLBACK_PROJECTS[0];
    return {
      ...fallback,
      location: 'San Francisco, CA',
    };
  },

  async postProject(payload: CreateProjectPayload): Promise<void> {
    const token = localStorage.getItem('zapmancer_jwt_token');
    const res = await fetch('http://localhost:8080/projects', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
      },
      body: JSON.stringify({
        title: payload.title,
        description: payload.description,
        category: payload.category,
        budget: payload.budgetAmount,
        budgetType: payload.budgetType,
        skills: payload.skills,
      }),
    });

    if (!res.ok) {
      const err = await res.json().catch(() => null);
      throw new Error(err?.error?.message || `Failed to create project: ${res.status}`);
    }
  },
};
