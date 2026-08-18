import React from 'react';
import { Navigate, useLocation, Outlet } from 'react-router-dom';
import { useAuth, ActiveAccountRole } from '../../context/AuthContext';

interface ProtectedRouteProps {
  allowedRoles?: ActiveAccountRole[];
  children?: React.ReactNode;
}

export const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ allowedRoles, children }) => {
  const { isAuthenticated, isLoading, role } = useAuth();
  const location = useLocation();

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-surface">
        <div className="flex flex-col items-center gap-3">
          <div className="w-8 h-8 border-2 border-primary border-t-transparent rounded-full animate-spin"></div>
          <span className="text-sm font-medium text-mute">Verifying session...</span>
        </div>
      </div>
    );
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  if (allowedRoles && allowedRoles.length > 0 && !allowedRoles.includes(role)) {
    const fallbackDestination = role === 'company' ? '/company/dashboard' : '/home';
    return <Navigate to={fallbackDestination} replace />;
  }

  return children ? <>{children}</> : <Outlet />;
};
