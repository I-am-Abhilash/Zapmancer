import { useState } from 'react';
import { useSharedState } from './useSharedState';

export interface AuthBridgeState {
  email: string;
  isLoading: boolean;
  error: string | null;
  isSuccess: boolean;
}

const initialAuthBridgeState: AuthBridgeState = {
  email: '',
  isLoading: false,
  error: null,
  isSuccess: false,
};

export function useAuthViewModel(sharedViewModel?: any) {
  const state = useSharedState<AuthBridgeState>(
    sharedViewModel?.uiState,
    initialAuthBridgeState
  );

  const onEmailChanged = (email: String) => {
    sharedViewModel?.onEmailChanged?.(email);
  };

  const submitLogin = () => {
    sharedViewModel?.submitLogin?.();
  };

  return {
    state,
    onEmailChanged,
    submitLogin,
  };
}
