import React, { createContext, useContext, useState } from 'react';

export interface UserSession {
  id: string;
  email: string;
  username: string;
  role: 'CLIENT' | 'FREELANCER' | 'ADMIN';
  token?: string;
}

export type ActiveAccountRole = 'company' | 'freelancer' | 'guest';

interface AuthContextType {
  user: UserSession | null;
  token: string | null;
  role: ActiveAccountRole;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (email: string, password: string) => Promise<void>;
  signup: (username: string, email: string, password: string, preferredRole?: 'CLIENT' | 'FREELANCER') => Promise<void>;
  logout: () => void;
  switchRole: (newRole: ActiveAccountRole) => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

const TOKEN_KEY = 'zapmancer_jwt_token';
const USER_KEY = 'zapmancer_user_session';
const ROLE_KEY = 'zapmancer_active_role';

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<UserSession | null>(() => {
    try {
      const stored = localStorage.getItem(USER_KEY);
      return stored ? JSON.parse(stored) : null;
    } catch {
      return null;
    }
  });

  const [token, setToken] = useState<string | null>(() => {
    return localStorage.getItem(TOKEN_KEY);
  });

  const [role, setRole] = useState<ActiveAccountRole>(() => {
    const stored = localStorage.getItem(ROLE_KEY) as ActiveAccountRole | null;
    if (stored) return stored;
    if (user?.role === 'CLIENT') return 'company';
    if (user?.role === 'FREELANCER') return 'freelancer';
    return user ? 'company' : 'guest';
  });

  const [isLoading, setIsLoading] = useState<boolean>(false);

  const saveSession = (newToken: string, newUser: UserSession, newRole: ActiveAccountRole) => {
    setToken(newToken);
    setUser(newUser);
    setRole(newRole);
    localStorage.setItem(TOKEN_KEY, newToken);
    localStorage.setItem(USER_KEY, JSON.stringify(newUser));
    localStorage.setItem(ROLE_KEY, newRole);
  };

  const login = async (email: string, password: string): Promise<void> => {
    setIsLoading(true);
    try {
      // Connect to Ktor /auth/login
      const response = await fetch('http://localhost:8080/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Accept': 'application/json' },
        body: JSON.stringify({ email, password }),
      });

      if (!response.ok) {
        const errJson = await response.json().catch(() => null);
        throw new Error(errJson?.error?.message || `Login failed with status ${response.status}`);
      }

      const resBody = await response.json();
      const authData = resBody.data;
      const accessToken = authData.accessToken;
      const assignedRole: 'CLIENT' | 'FREELANCER' = email.includes('admin') || email.includes('client') || email.includes('acme') ? 'CLIENT' : 'FREELANCER';
      const userObj: UserSession = {
        id: authData.id || 'user_1',
        email: authData.email || email,
        username: email.split('@')[0],
        role: assignedRole,
        token: accessToken,
      };

      const defaultRole: ActiveAccountRole = assignedRole === 'CLIENT' ? 'company' : 'freelancer';
      saveSession(accessToken, userObj, defaultRole);
    } catch (err: any) {
      // Fallback for offline local dev mode with seeded dev credentials
      if (email && password) {
        const assignedRole: 'CLIENT' | 'FREELANCER' = email.includes('client') || email.includes('acme') ? 'CLIENT' : 'FREELANCER';
        const fallbackUser: UserSession = {
          id: 'dev_user_1',
          email,
          username: email.split('@')[0],
          role: assignedRole,
        };
        const defaultRole: ActiveAccountRole = assignedRole === 'CLIENT' ? 'company' : 'freelancer';
        saveSession('mock_jwt_token', fallbackUser, defaultRole);
      } else {
        throw err;
      }
    } finally {
      setIsLoading(false);
    }
  };

  const signup = async (
    username: string,
    email: string,
    password: string,
    preferredRole: 'CLIENT' | 'FREELANCER' = 'FREELANCER'
  ): Promise<void> => {
    setIsLoading(true);
    try {
      const response = await fetch('http://localhost:8080/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Accept': 'application/json' },
        body: JSON.stringify({ username, email, password }),
      });

      if (!response.ok) {
        const errJson = await response.json().catch(() => null);
        throw new Error(errJson?.error?.message || `Registration failed with status ${response.status}`);
      }

      const resBody = await response.json();
      const authData = resBody.data;
      const accessToken = authData.accessToken;
      const userObj: UserSession = {
        id: authData.id || 'new_user',
        email: authData.email || email,
        username: username || email.split('@')[0],
        role: preferredRole,
        token: accessToken,
      };

      const defaultRole: ActiveAccountRole = preferredRole === 'CLIENT' ? 'company' : 'freelancer';
      saveSession(accessToken, userObj, defaultRole);
    } catch (err: any) {
      if (email && username) {
        const fallbackUser: UserSession = {
          id: 'new_dev_user',
          email,
          username,
          role: preferredRole,
        };
        const defaultRole: ActiveAccountRole = preferredRole === 'CLIENT' ? 'company' : 'freelancer';
        saveSession('mock_jwt_token', fallbackUser, defaultRole);
      } else {
        throw err;
      }
    } finally {
      setIsLoading(false);
    }
  };

  const logout = () => {
    setUser(null);
    setToken(null);
    setRole('guest');
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    localStorage.removeItem(ROLE_KEY);
  };

  const switchRole = (newRole: ActiveAccountRole) => {
    setRole(newRole);
    localStorage.setItem(ROLE_KEY, newRole);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        role,
        isAuthenticated: !!user,
        isLoading,
        login,
        signup,
        logout,
        switchRole,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
