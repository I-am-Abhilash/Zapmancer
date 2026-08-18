export type MemberType = 'Contractor' | 'Full-Time';
export type MemberStatus = 'Active' | 'In Review' | 'Onboarding';
export type TaskStatus = 'Open' | 'In Progress' | 'PR Review' | 'Completed';
export type BudgetType = 'Escrow' | 'Salary' | 'Bounty';

export interface TeamMember {
  id: string;
  name: string;
  role: string;
  type: MemberType;
  comp: string;
  task: string;
  hrs: number;
  status: MemberStatus;
  avatar: string;
}

export interface SprintTask {
  id: string;
  title: string;
  assignee: string;
  deadline: string;
  status: TaskStatus;
  budget: string;
  budgetType: BudgetType;
  hrs: number;
  bounty: boolean;
}

export interface Candidate {
  name: string;
  title: string;
  match: string;
  rating: string;
  rate: string;
  skills: string[];
}

export interface CompanyMetrics {
  totalTeam: number;
  fullTimeCount: number;
  contractorCount: number;
  activeSprints: number;
  totalTasks: number;
  hoursLogged: number;
  openBounties: number;
}

export interface CompanyDashboardData {
  companyName: string;
  website: string;
  location: string;
  isVerified: boolean;
  metrics: CompanyMetrics;
  team: TeamMember[];
  tasks: SprintTask[];
  candidates: Candidate[];
}

const DEFAULT_DASHBOARD: CompanyDashboardData = {
  companyName: 'Acme AI Systems',
  website: 'acme.ai',
  location: 'San Francisco, CA',
  isVerified: true,
  metrics: {
    totalTeam: 12,
    fullTimeCount: 8,
    contractorCount: 4,
    activeSprints: 3,
    totalTasks: 14,
    hoursLogged: 142,
    openBounties: 2,
  },
  team: [
    {
      id: '1',
      name: 'Elena Rostova',
      role: 'Full-Stack & Web Architect',
      type: 'Contractor',
      comp: '$85 / hr',
      task: 'Task #102: Custom RAG AI Agent Pipeline',
      hrs: 38,
      status: 'Active',
      avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&q=80',
    },
    {
      id: '2',
      name: 'Dr. Lucas Meyer',
      role: 'AI Agent Specialist',
      type: 'Full-Time',
      comp: '$14,500 / mo',
      task: 'Task #108: Multi-Modal Model Fine-Tuning',
      hrs: 42,
      status: 'Active',
      avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=150&q=80',
    },
    {
      id: '3',
      name: 'Sophia Al-Mansoor',
      role: 'Lead UI/UX Designer',
      type: 'Full-Time',
      comp: '$11,000 / mo',
      task: 'Task #112: Design System Tokens Redesign',
      hrs: 35,
      status: 'In Review',
      avatar: 'https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=150&q=80',
    },
    {
      id: '4',
      name: 'David Kim',
      role: 'DevOps & Infra Specialist',
      type: 'Contractor',
      comp: '$90 / hr',
      task: 'Task #115: Kubernetes Cluster Auto-Scaling',
      hrs: 27,
      status: 'Active',
      avatar: 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=150&q=80',
    },
  ],
  tasks: [
    {
      id: '102',
      title: 'Custom RAG AI Agent Pipeline Integration',
      assignee: 'Elena Rostova',
      deadline: 'Aug 20, 2026',
      status: 'In Progress',
      budget: '$4,500',
      budgetType: 'Escrow',
      hrs: 38,
      bounty: false,
    },
    {
      id: '108',
      title: 'Multi-Modal LLM Fine-Tuning & Vector Store',
      assignee: 'Dr. Lucas Meyer',
      deadline: 'Aug 25, 2026',
      status: 'PR Review',
      budget: '$3,800',
      budgetType: 'Salary',
      hrs: 42,
      bounty: false,
    },
    {
      id: '114',
      title: 'Mobile App Design System & Micro-Animations',
      assignee: 'Unassigned',
      deadline: 'Sep 01, 2026',
      status: 'Open',
      budget: '$2,800',
      budgetType: 'Bounty',
      hrs: 0,
      bounty: true,
    },
  ],
  candidates: [
    {
      name: 'Dr. Lucas Meyer',
      title: 'AI Agent & RAG Pipeline Specialist',
      match: '98% match',
      rating: '5.0',
      rate: '$95/hr',
      skills: ['AI Agent', 'Python', 'LangChain', 'OpenAI'],
    },
    {
      name: 'Sophia Al-Mansoor',
      title: 'Principal UI/UX & Design Systems Lead',
      match: '96% match',
      rating: '5.0',
      rate: '$80/hr',
      skills: ['Figma', 'UI/UX Design', 'Design Tokens'],
    },
    {
      name: 'Alex Rivera',
      title: 'Full-Stack Software Architect',
      match: '94% match',
      rating: '5.0',
      rate: '$90/hr',
      skills: ['TypeScript', 'React', 'Node.js', 'PostgreSQL'],
    },
  ],
};

const STORAGE_KEY = 'zapmancer_company_dashboard';

export const companyService = {
  async getDashboard(): Promise<CompanyDashboardData> {
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

  async inviteContractor(candidateName: string, taskLabel: string): Promise<void> {
    const current = await this.getDashboard();
    const candidate = current.candidates.find((c) => c.name === candidateName);
    const newMember: TeamMember = {
      id: `m_${Date.now()}`,
      name: candidateName,
      role: candidate?.title || 'Contractor Engineer',
      type: 'Contractor',
      comp: candidate?.rate || '$85 / hr',
      task: taskLabel,
      hrs: 0,
      status: 'Onboarding',
      avatar: 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=150&q=80',
    };

    current.team = [newMember, ...current.team];
    current.metrics.totalTeam += 1;
    current.metrics.contractorCount += 1;

    localStorage.setItem(STORAGE_KEY, JSON.stringify(current));
  },

  async updateTaskStatus(taskId: string, status: TaskStatus): Promise<void> {
    const current = await this.getDashboard();
    current.tasks = current.tasks.map((t) => (t.id === taskId ? { ...t, status } : t));
    localStorage.setItem(STORAGE_KEY, JSON.stringify(current));
  },
};
