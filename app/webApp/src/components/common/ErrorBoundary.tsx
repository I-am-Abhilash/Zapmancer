import React, { Component, ErrorInfo, ReactNode } from 'react';
import { AlertTriangle, RefreshCw } from 'lucide-react';

interface Props {
  children?: ReactNode;
}

interface State {
  hasError: boolean;
  error?: Error;
}

export class ErrorBoundary extends Component<Props, State> {
  public state: State = {
    hasError: false
  };

  public static getDerivedStateFromError(error: Error): State {
    return { hasError: true, error };
  }

  public componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    console.error('ScriptSide UI Error caught by boundary:', error, errorInfo);
  }

  public render() {
    if (this.state.hasError) {
      return (
        <div className="min-h-screen bg-[var(--color-canvas)] text-[var(--color-ink)] flex items-center justify-center p-6">
          <div className="editorial-card max-w-md w-full p-8 text-center bg-[var(--color-soft-cloud)] border-none">
            <AlertTriangle size={48} className="mx-auto text-[var(--color-sale)] mb-4" />
            <h2 className="editorial-headline text-2xl font-bold font-serif mb-2">Something went wrong</h2>
            <p className="text-sm text-[var(--color-mute)] mb-6">
              An unhandled error occurred. Our team has been notified.
            </p>
            <button
              onClick={() => window.location.reload()}
              className="btn-primary py-2.5 px-6 text-sm"
            >
              <RefreshCw size={16} /> Reload Page
            </button>
          </div>
        </div>
      );
    }

    return this.props.children;
  }
}
