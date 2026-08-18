export type TaskStatus = 'In Progress' | 'PR Submitted' | 'Approved';

export interface AssignedTask {
  id: string;
  workspaceId: string;
  title: string;
  companyName: string;
  deadline: string;
  budget: string;
  budgetType: string;
  status: TaskStatus;
  prUrl?: string;
  commitHash?: string;
  demoUrl?: string;
  loggedHours: number;
}

export interface PayoutTransaction {
  id: string;
  date: string;
  description: string;
  amount: string;
  method: string;
  status: 'Completed' | 'Escrow Locked';
}

export interface FreelancerWorkspace {
  id: string;
  name: string;
  role: string;
  type: string;
  count: number;
}

export interface FreelancerMetrics {
  activeMilestones: number;
  inProgressCount: number;
  underReviewCount: number;
  escrowLocked: string;
  lifetimePayouts: string;
}

export interface FreelancerDashboardData {
  freelancerName: string;
  workspaces: FreelancerWorkspace[];
  tasks: AssignedTask[];
  payouts: PayoutTransaction[];
  metrics: FreelancerMetrics;
  activeTimerTaskId?: string;
}

const DEFAULT_DASHBOARD: FreelancerDashboardData = {
  freelancerName: 'Alex Morgan',
  workspaces: [
    { id: 'all', name: 'All Workspaces', role: 'All Roles', type: 'Combined', count: 3 },
    { id: 'w1', name: 'Acme AI Systems', role: 'KMP & Wasm Lead', type: 'Contractor', count: 2 },
    { id: 'w2', name: 'Fintech Core', role: 'Backend Specialist', type: 'Full-Time', count: 1 },
  ],
  tasks: [
    {
      id: '102',
      workspaceId: 'w1',
      title: 'Task #102: Compose Wasm WebAudio Processing Engine',
      companyName: 'Acme AI Systems',
      deadline: 'Aug 20, 2026',
      budget: '$4,500.00',
      budgetType: 'Escrow',
      status: 'In Progress',
      loggedHours: 38,
    },
    {
      id: '108',
      workspaceId: 'w1',
      title: 'Task #108: High-Concurrency Ktor WebSockets Clustering',
      companyName: 'Acme AI Systems',
      deadline: 'Aug 25, 2026',
      budget: '$3,200.00',
      budgetType: 'Milestone',
      status: 'PR Submitted',
      prUrl: 'https://github.com/acme-ai/kmp-core/pull/84',
      commitHash: 'f4a19c3',
      demoUrl: 'https://demo.acme.ai/ktor-ws',
      loggedHours: 42,
    },
    {
      id: '112',
      workspaceId: 'w2',
      title: 'Task #112: SKie Swift Interop & Memory Leak Resolution',
      companyName: 'Fintech Core',
      deadline: 'Aug 28, 2026',
      budget: '$2,800.00',
      budgetType: 'Bounty',
      status: 'In Progress',
      loggedHours: 15,
    },
  ],
  payouts: [
    {
      id: 'tx-881',
      date: 'Aug 10, 2026',
      description: 'Milestone: Ktor Exposed ORM Migration',
      amount: '+$3,500.00',
      method: 'Stripe Direct Deposit',
      status: 'Completed',
    },
    {
      id: 'tx-882',
      date: 'Aug 02, 2026',
      description: 'Milestone: Android Compose WearOS Component',
      amount: '+$2,800.00',
      method: 'USDC Wallet',
      status: 'Completed',
    },
    {
      id: 'tx-883',
      date: 'Pending',
      description: 'Escrow: Compose Wasm Audio Engine',
      amount: '$4,500.00',
      method: 'Stripe Direct Deposit',
      status: 'Escrow Locked',
    },
  ],
  metrics: {
    activeMilestones: 3,
    inProgressCount: 2,
    underReviewCount: 1,
    escrowLocked: '$4,500',
    lifetimePayouts: '$32,400',
  },
};

const STORAGE_KEY = 'zapmancer_freelancer_dashboard';

export const homeService = {
  async getDashboard(): Promise<FreelancerDashboardData> {
    try {
      const token = localStorage.getItem('zapmancer_jwt_token');
      const res = await fetch('http://localhost:8080/home/dashboard', {
        headers: {
          Accept: 'application/json',
          ...(token ? { Authorization: `Bearer ${token}` } : {}),
        },
      });
      if (res.ok) {
        const json = await res.json();
        const data = json.data || json;
        if (data && data.tasks) {
          localStorage.setItem(STORAGE_KEY, JSON.stringify(data));
          return data;
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

    return DEFAULT_DASHBOARD;
  },

  async submitPr(
    taskId: string,
    prUrl: string,
    commitHash: string,
    demoUrl: string
  ): Promise<void> {
    const current = await this.getDashboard();
    current.tasks = current.tasks.map((t) =>
      t.id === taskId
        ? {
            ...t,
            status: 'PR Submitted' as const,
            prUrl,
            commitHash,
            demoUrl,
          }
        : t
    );
    current.metrics.underReviewCount += 1;
    current.metrics.inProgressCount = Math.max(0, current.metrics.inProgressCount - 1);
    localStorage.setItem(STORAGE_KEY, JSON.stringify(current));
  },

  async exportTimeLogs(): Promise<void> {
    const csvContent =
      'Date,Task ID,Task Title,Company,Hours Logged,Status\n' +
      '2026-08-18,102,Compose Wasm WebAudio Processing Engine,Acme AI Systems,38,In Progress\n' +
      '2026-08-17,108,High-Concurrency Ktor WebSockets Clustering,Acme AI Systems,42,PR Submitted\n' +
      '2026-08-16,112,SKie Swift Interop & Memory Leak Resolution,Fintech Core,15,In Progress\n';

    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.setAttribute('href', url);
    link.setAttribute('download', `zapmancer_time_logs_${new Date().toISOString().slice(0, 10)}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  },
};
