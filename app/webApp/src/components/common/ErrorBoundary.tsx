import { Component, ErrorInfo, ReactNode } from 'react';

interface Props {
  children: ReactNode;
}

interface State {
  hasError: boolean;
  error: Error | null;
}

export class ErrorBoundary extends Component<Props, State> {
  public state: State = {
    hasError: false,
    error: null,
  };

  public static getDerivedStateFromError(error: Error): State {
    return { hasError: true, error };
  }

  public componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    console.error('Zapmancer UI Error caught by boundary:', error, errorInfo);
  }

  public render() {
    if (this.state.hasError) {
      return (
        <div className="min-h-screen flex items-center justify-center bg-canvas text-ink p-8 font-sans">
          <div className="max-w-md w-full bg-surface border border-hairline p-8 rounded-xl space-y-4 shadow-xl">
            <h2 className="text-2xl font-extrabold text-ink">Application Error</h2>
            <p className="text-sm text-mute leading-relaxed">
              Something went wrong while rendering this component. Please refresh the browser.
            </p>
            <button
              onClick={() => window.location.reload()}
              className="w-full py-3 rounded-full bg-brand-green hover:bg-brand-green-hover text-white text-xs font-bold uppercase tracking-[0.05em] transition-all"
            >
              Refresh Zapmancer
            </button>
          </div>
        </div>
      );
    }

    return this.props.children;
  }
}
