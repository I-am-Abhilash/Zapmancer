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
  },
];

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
