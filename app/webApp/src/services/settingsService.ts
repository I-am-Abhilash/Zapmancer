export interface UserSettings {
  email: string;
  username: string;
  twoFactorEnabled: boolean;
  payoutMethod: 'stripe' | 'crypto' | 'bank';
  payoutEmail: string;
  emailNotifications: boolean;
  pushNotifications: boolean;
  publicProfile: boolean;
}

const DEFAULT_SETTINGS: UserSettings = {
  email: 'alex.morgan@zapmancer.io',
  username: 'alexmorgan',
  twoFactorEnabled: true,
  payoutMethod: 'stripe',
  payoutEmail: 'payouts@alexmorgan.dev',
  emailNotifications: true,
  pushNotifications: true,
  publicProfile: true,
};

const SETTINGS_KEY = 'zapmancer_user_settings';

export const settingsService = {
  async getUserSettings(): Promise<UserSettings> {
    try {
      const stored = localStorage.getItem(SETTINGS_KEY);
      if (stored) return JSON.parse(stored);

      const token = localStorage.getItem('zapmancer_jwt_token');
      const headers: Record<string, string> = { 'Accept': 'application/json' };
      if (token) headers['Authorization'] = `Bearer ${token}`;

      const res = await fetch('http://localhost:8080/settings', { headers });
      if (res.ok) {
        const body = await res.json();
        const data = body.data || body;
        return { ...DEFAULT_SETTINGS, ...data };
      }
    } catch {
      // Offline fallback
    }

    return DEFAULT_SETTINGS;
  },

  async updateSettings(payload: Partial<UserSettings>): Promise<UserSettings> {
    const current = await this.getUserSettings();
    const updated = { ...current, ...payload };
    localStorage.setItem(SETTINGS_KEY, JSON.stringify(updated));

    try {
      const token = localStorage.getItem('zapmancer_jwt_token');
      await fetch('http://localhost:8080/settings', {
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
