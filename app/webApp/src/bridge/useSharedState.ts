import { useState, useEffect } from 'react';

/**
 * Hook to observe a Kotlin Coroutines Flow or StateFlow from KMP sharedLogic.
 */
export function useSharedState<T>(stateFlow: any, initialState: T): T {
  const [state, setState] = useState<T>(() => {
    if (stateFlow && typeof stateFlow.value !== 'undefined') {
      return stateFlow.value;
    }
    return initialState;
  });

  useEffect(() => {
    if (!stateFlow) return;

    // Kotlin StateFlow / Flow listener bridge
    if (typeof stateFlow.collect === 'function') {
      const job = stateFlow.collect({
        emit: (value: T) => setState(value),
      });
      return () => {
        if (job && typeof job.cancel === 'function') {
          job.cancel();
        }
      };
    } else if (typeof stateFlow.subscribe === 'function') {
      const unsubscribe = stateFlow.subscribe((val: T) => setState(val));
      return () => {
        if (typeof unsubscribe === 'function') {
          unsubscribe();
        } else if (unsubscribe && typeof unsubscribe.unsubscribe === 'function') {
          unsubscribe.unsubscribe();
        }
      };
    }
  }, [stateFlow]);

  return state;
}
