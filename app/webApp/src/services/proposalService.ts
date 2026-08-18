export interface ProposalPayload {
  projectId: string;
  bidAmount: number;
  deliveryDays: number;
  coverLetter: string;
  milestones: { title: string; amount: number }[];
}

export interface ProposalItem {
  id: string;
  name: string;
  title: string;
  avatar: string;
  bid: string;
  rating: number;
  reviews: number;
  pitch: string;
  skills: string[];
  status?: 'PENDING' | 'ACCEPTED' | 'REJECTED';
}

export interface MilestoneDeliverable {
  id: string;
  projectTitle: string;
  developerName: string;
  developerAvatar: string;
  developerRole: string;
  milestoneBudget: string;
  prUrl: string;
  commitHash: string;
  demoUrl: string;
  testSuiteStatus: string;
  coverage: string;
  submittedDate: string;
  status: 'Pending Client Approval' | 'Escrow Released & IP Transferred';
}

const FALLBACK_PROPOSALS: ProposalItem[] = [
  {
    id: 'pr1',
    name: 'Elena Rostova',
    title: 'Senior KMP & WebAssembly Lead',
    avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80',
    bid: '$3,200',
    rating: 5.0,
    reviews: 42,
    pitch: 'I have architected over 15 Compose Multiplatform desktop and mobile clients connected to Ktor backends. Ready to deliver milestone 1 within 5 days with full test coverage.',
    skills: ['Kotlin', 'Wasm', 'Ktor', 'Compose'],
    status: 'PENDING',
  },
  {
    id: 'pr2',
    name: 'Marcus Vance',
    title: 'Kubernetes & Backend Systems Engineer',
    avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=150&q=80',
    bid: '$2,800',
    rating: 4.9,
    reviews: 29,
    pitch: 'Deep experience with high throughput WebSocket clustering and Exposed ORM connection pool optimization. Ready to start immediately.',
    skills: ['Ktor', 'Docker', 'PostgreSQL', 'WebSockets'],
    status: 'PENDING',
  },
];

const FALLBACK_MILESTONES: MilestoneDeliverable[] = [
  {
    id: 'm-102',
    projectTitle: 'Task #102 · Compose Wasm WebAudio Processing Engine',
    developerName: 'Elena Rostova',
    developerAvatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80',
    developerRole: 'KMP & Wasm Lead',
    milestoneBudget: '$4,500.00',
    prUrl: 'https://github.com/acme-ai/kmp-core/pull/42',
    commitHash: 'b8f9a2e',
    demoUrl: 'https://demo.acme.ai/wasm-preview',
    testSuiteStatus: '14/14 unit tests passing',
    coverage: '94% coverage',
    submittedDate: '2 hours ago',
    status: 'Pending Client Approval',
  },
  {
    id: 'm-108',
    projectTitle: 'Task #108 · High-Concurrency Ktor WebSockets Clustering',
    developerName: 'Marcus Vance',
    developerAvatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=150&q=80',
    developerRole: 'Backend Architect',
    milestoneBudget: '$3,200.00',
    prUrl: 'https://github.com/acme-ai/ktor-server/pull/84',
    commitHash: 'f4a19c3',
    demoUrl: 'https://demo.acme.ai/ktor-ws',
    testSuiteStatus: '28/28 integration tests passing',
    coverage: '98% coverage',
    submittedDate: '1 day ago',
    status: 'Pending Client Approval',
  },
];

const STORAGE_MILESTONES_KEY = 'zapmancer_client_milestones';
const STORAGE_PROPOSALS_KEY = 'zapmancer_client_proposals';

export const proposalService = {
  async submitProposal(payload: ProposalPayload): Promise<void> {
    const token = localStorage.getItem('zapmancer_jwt_token');
    const res = await fetch('http://localhost:8080/proposals', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
      },
      body: JSON.stringify({
        projectId: payload.projectId,
        bidAmount: payload.bidAmount,
        estimatedDays: payload.deliveryDays,
        coverLetter: payload.coverLetter,
        milestones: payload.milestones,
      }),
    });

    if (!res.ok) {
      const err = await res.json().catch(() => null);
      throw new Error(err?.error?.message || `Failed to submit proposal: ${res.status}`);
    }
  },

  async getPendingMilestones(): Promise<MilestoneDeliverable[]> {
    const stored = localStorage.getItem(STORAGE_MILESTONES_KEY);
    if (stored) {
      try {
        return JSON.parse(stored);
      } catch {
        // Fallback
      }
    }
    return FALLBACK_MILESTONES;
  },

  async approveMilestone(milestoneId: string): Promise<void> {
    const current = await this.getPendingMilestones();
    const updated = current.map((m) =>
      m.id === milestoneId ? { ...m, status: 'Escrow Released & IP Transferred' as const } : m
    );
    localStorage.setItem(STORAGE_MILESTONES_KEY, JSON.stringify(updated));
  },

  async getClientProposals(): Promise<ProposalItem[]> {
    try {
      const token = localStorage.getItem('zapmancer_jwt_token');
      const headers: Record<string, string> = { 'Accept': 'application/json' };
      if (token) headers['Authorization'] = `Bearer ${token}`;

      const res = await fetch('http://localhost:8080/proposals/my', { headers });
      if (res.ok) {
        const body = await res.json();
        const items = body.data || body;
        if (Array.isArray(items) && items.length > 0) {
          return items.map((p: any) => ({
            id: String(p.id),
            name: p.freelancerName || 'Candidate Developer',
            title: p.freelancerRole || 'Senior Engineer',
            avatar: p.avatar || 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80',
            bid: `$${Number(p.budget || p.bidAmount || 0).toLocaleString()}`,
            rating: p.rating || 5.0,
            reviews: p.reviews || 20,
            pitch: p.pitchContent || p.coverLetter || '',
            skills: Array.isArray(p.skills) ? p.skills : ['Kotlin', 'Wasm', 'Compose'],
            status: p.status || 'PENDING',
          }));
        }
      }
    } catch {
      // Offline fallback
    }

    const stored = localStorage.getItem(STORAGE_PROPOSALS_KEY);
    if (stored) {
      try {
        return JSON.parse(stored);
      } catch {
        // Fallback
      }
    }

    return FALLBACK_PROPOSALS;
  },

  async acceptProposal(proposalId: string): Promise<void> {
    try {
      const token = localStorage.getItem('zapmancer_jwt_token');
      await fetch(`http://localhost:8080/proposals/${proposalId}/accept`, {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
        },
      });
    } catch {
      // Offline fallback
    }

    const current = await this.getClientProposals();
    const updated = current.map((p) =>
      p.id === proposalId ? { ...p, status: 'ACCEPTED' as const } : p
    );
    localStorage.setItem(STORAGE_PROPOSALS_KEY, JSON.stringify(updated));
  },

  async rejectProposal(proposalId: string): Promise<void> {
    try {
      const token = localStorage.getItem('zapmancer_jwt_token');
      await fetch(`http://localhost:8080/proposals/${proposalId}/reject`, {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
        },
      });
    } catch {
      // Offline fallback
    }

    const current = await this.getClientProposals();
    const updated = current.map((p) =>
      p.id === proposalId ? { ...p, status: 'REJECTED' as const } : p
    );
    localStorage.setItem(STORAGE_PROPOSALS_KEY, JSON.stringify(updated));
  },

  async getProposalsForProject(projectId: string): Promise<ProposalItem[]> {
    try {
      const token = localStorage.getItem('zapmancer_jwt_token');
      const headers: Record<string, string> = { 'Accept': 'application/json' };
      if (token) headers['Authorization'] = `Bearer ${token}`;

      const res = await fetch(`http://localhost:8080/projects/${projectId}/proposals`, { headers });
      if (res.ok) {
        const body = await res.json();
        const items = body.data || body;
        if (Array.isArray(items) && items.length > 0) {
          return items.map((p: any) => ({
            id: String(p.id),
            name: p.developerName || 'Elena Rostova',
            title: p.developerRole || 'Senior KMP Engineer',
            avatar: p.developerAvatar || 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80',
            bid: `$${Number(p.bidAmount || p.bid || 0).toLocaleString()}`,
            rating: p.rating || 5.0,
            reviews: p.reviews || 42,
            pitch: p.coverLetter || p.pitch || '',
            skills: Array.isArray(p.skills) ? p.skills : ['Kotlin', 'Wasm', 'Compose'],
          }));
        }
      }
    } catch {
      // Offline fallback
    }

    return FALLBACK_PROPOSALS;
  },
};
