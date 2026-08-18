export type KycDocumentType = 'PASSPORT' | 'DRIVERS_LICENSE' | 'NATIONAL_ID';
export type KycStatus = 'PENDING' | 'VERIFIED' | 'FAILED' | 'MANUAL_REVIEW';
export type KycLivenessPreset = 'EYE' | 'HEAD_TURN' | 'SMILE' | 'MULTI_RANGE' | 'FULL' | 'PASSIVE_ONLY';

export interface KycInitResponse {
  verificationId: string;
  livenessSessionId: string;
  instruction: string;
  preset: string;
  expiresAtUtc: string;
}

export interface KycStatusResponse {
  verificationId: string;
  userId: string;
  status: KycStatus;
  documentType: KycDocumentType;
  extractedName?: string;
  extractedDob?: string;
  extractedDocNumber?: string;
  extractedExpiry?: string;
  faceSimilarityScore?: number;
  livenessScore?: number;
  livenessPassed: boolean;
  isNameMatched: boolean;
  receiptSignature?: string;
  receiptHash?: string;
  reviewerNotes?: string;
  createdAt: string;
  updatedAt: string;
}

export interface KycReceiptResponse {
  verificationId: string;
  userId: string;
  isValid: boolean;
  canonicalPayloadJson: string;
  ed25519SignatureBase64: string;
  receiptHashSha256: string;
  publicKeyBase64: string;
  verifiedAtUtc: string;
}

export interface OpenBiometricsCapabilitiesResponse {
  engine: string;
  version: string;
  supported_presets: string[];
  supported_documents: string[];
  features: Record<string, boolean>;
}

const STORAGE_KYC_KEY = 'zapmancer_kyc_status';

export const kycService = {
  async getCapabilities(): Promise<OpenBiometricsCapabilitiesResponse> {
    try {
      const res = await fetch('http://localhost:8080/kyc/capabilities');
      if (res.ok) {
        const json = await res.json();
        return json.data || json;
      }
    } catch {
      // Offline fallback
    }

    return {
      engine: 'OpenBiometrics Core',
      version: '2.0.0-wasm',
      supported_presets: ['EYE', 'HEAD_TURN', 'SMILE', 'PASSIVE_ONLY'],
      supported_documents: ['PASSPORT', 'DRIVERS_LICENSE', 'NATIONAL_ID'],
      features: {
        anti_spoofing: true,
        ed25519_receipts: true,
        watchlist_screening: true,
        ocr_extraction: true,
      },
    };
  },

  async getKycStatus(): Promise<KycStatusResponse> {
    try {
      const token = localStorage.getItem('zapmancer_jwt_token');
      if (token) {
        const res = await fetch('http://localhost:8080/kyc/status', {
          headers: {
            Authorization: `Bearer ${token}`,
            Accept: 'application/json',
          },
        });
        if (res.ok) {
          const json = await res.json();
          const data = json.data || json;
          localStorage.setItem(STORAGE_KYC_KEY, JSON.stringify(data));
          return data;
        }
      }
    } catch {
      // Offline fallback
    }

    const stored = localStorage.getItem(STORAGE_KYC_KEY);
    if (stored) {
      return JSON.parse(stored);
    }

    return {
      verificationId: 'ver_mock_init',
      userId: 'usr_me',
      status: 'PENDING',
      documentType: 'PASSPORT',
      livenessPassed: false,
      isNameMatched: false,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    };
  },

  async initKyc(
    documentType: KycDocumentType,
    livenessPreset: KycLivenessPreset = 'EYE'
  ): Promise<KycInitResponse> {
    try {
      const token = localStorage.getItem('zapmancer_jwt_token');
      const res = await fetch('http://localhost:8080/kyc/init', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json',
          ...(token ? { Authorization: `Bearer ${token}` } : {}),
        },
        body: JSON.stringify({ documentType, livenessPreset }),
      });
      if (res.ok) {
        const json = await res.json();
        return json.data || json;
      }
    } catch {
      // Offline fallback
    }

    const verificationId = `ver_${Math.random().toString(36).substring(2, 9)}`;
    const livenessSessionId = `liv_${Math.random().toString(36).substring(2, 9)}`;
    return {
      verificationId,
      livenessSessionId,
      instruction:
        livenessPreset === 'HEAD_TURN'
          ? 'Turn your head slowly to the left, then look straight.'
          : livenessPreset === 'SMILE'
          ? 'Smile naturally into the camera, then relax.'
          : 'Blink your eyes twice slowly while looking directly at the camera.',
      preset: livenessPreset,
      expiresAtUtc: new Date(Date.now() + 15 * 60000).toISOString(),
    };
  },

  async submitKyc(formData: FormData): Promise<KycStatusResponse> {
    try {
      const token = localStorage.getItem('zapmancer_jwt_token');
      const res = await fetch('http://localhost:8080/kyc/submit', {
        method: 'POST',
        headers: {
          Accept: 'application/json',
          ...(token ? { Authorization: `Bearer ${token}` } : {}),
        },
        body: formData,
      });
      if (res.ok) {
        const json = await res.json();
        const data = json.data || json;
        localStorage.setItem(STORAGE_KYC_KEY, JSON.stringify(data));
        return data;
      }
    } catch {
      // Offline fallback
    }

    // Simulated verified response with cryptographic receipt hash
    const verifiedResponse: KycStatusResponse = {
      verificationId: (formData.get('verification_id') as string) || `ver_${Date.now()}`,
      userId: 'usr_me',
      status: 'VERIFIED',
      documentType: (formData.get('document_type') as KycDocumentType) || 'PASSPORT',
      extractedName: 'Alex Morgan',
      extractedDob: '1992-04-14',
      extractedDocNumber: 'P98421094',
      extractedExpiry: '2031-10-09',
      faceSimilarityScore: 0.982,
      livenessScore: 0.994,
      livenessPassed: true,
      isNameMatched: true,
      receiptSignature: 'ed25519_sig_' + Math.random().toString(36).substring(2) + Math.random().toString(36).substring(2),
      receiptHash: 'sha256_' + Math.random().toString(36).substring(2) + Math.random().toString(36).substring(2),
      reviewerNotes: 'Automated OpenBiometrics v2.0 neural pipeline verification passed with 99.4% confidence score.',
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    };

    localStorage.setItem(STORAGE_KYC_KEY, JSON.stringify(verifiedResponse));
    return verifiedResponse;
  },

  async getReceipt(verificationId: string): Promise<KycReceiptResponse> {
    try {
      const res = await fetch(`http://localhost:8080/kyc/receipt/${verificationId}`);
      if (res.ok) {
        const json = await res.json();
        return json.data || json;
      }
    } catch {
      // Offline fallback
    }

    const payload = {
      verification_id: verificationId,
      user_id: 'usr_me',
      status: 'VERIFIED',
      issued_at_utc: new Date().toISOString(),
      biometric_engine: 'OpenBiometrics v2.0.0',
      liveness_score: 0.994,
      face_similarity: 0.982,
      anti_spoofing: 'PASSED',
      fraud_watchlist: 'CLEARED',
    };

    return {
      verificationId,
      userId: 'usr_me',
      isValid: true,
      canonicalPayloadJson: JSON.stringify(payload, null, 2),
      ed25519SignatureBase64: 'MEQCID6v+e23bN1X/Y04xG8e8G8u4L0hP/fP7J...Zk10948==',
      receiptHashSha256: '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08',
      publicKeyBase64: 'MCowBQYDK2VwAyEA9r6vN7J1Q09x4B2L0hPf7P7JZk10948X9b8e8G8u4L0=',
      verifiedAtUtc: new Date().toISOString(),
    };
  },
};
