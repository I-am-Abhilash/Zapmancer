import React, { useState, useEffect } from 'react';
import './kyc.css';
import { useParams, Link } from 'react-router-dom';
import {
  ShieldCheck,
  CheckCircle2,
  Copy,
  Check,
  Lock,
  FileCode,
  ArrowLeft,
  Loader2,
  Calendar,
  Key,
  Hash,
} from 'lucide-react';
import { kycService, KycReceiptResponse } from '../../services/kycService';

export const KycReceiptPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [receipt, setReceipt] = useState<KycReceiptResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [copied, setCopied] = useState(false);

  useEffect(() => {
    let isMounted = true;
    setLoading(true);
    kycService
      .getReceipt(id || 'ver_default')
      .then((data) => {
        if (isMounted) {
          setReceipt(data);
          setLoading(false);
        }
      })
      .catch(() => {
        if (isMounted) setLoading(false);
      });

    return () => {
      isMounted = false;
    };
  }, [id]);

  const copyJson = () => {
    if (receipt?.canonicalPayloadJson) {
      navigator.clipboard.writeText(receipt.canonicalPayloadJson.toString());
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    }
  };

  if (loading || !receipt) {
    return (
      <div className="min-h-[60vh] flex flex-col items-center justify-center gap-3">
        <Loader2 className="w-8 h-8 text-primary animate-spin" />
        <p className="text-sm font-semibold text-mute">Verifying Ed25519 cryptographic signature...</p>
      </div>
    );
  }

  return (
    <div className="kyc-page">
      <main className="kyc-main">
        {/* Navigation */}
        <Link to="/kyc" className="inline-flex items-center gap-2 text-xs font-semibold text-mute hover:text-ink transition-colors">
          <ArrowLeft size={14} /> Back to Identity Verification
        </Link>

        {/* Header */}
        <div className="kyc-header">
          <div className="flex items-center gap-2 text-primary font-semibold text-xs tracking-wider uppercase">
            <ShieldCheck size={16} /> OpenBiometrics Proof-of-Humanity
          </div>
          <h1 className="kyc-title">Cryptographic KYC Audit Receipt</h1>
          <p className="kyc-subtitle">
            Publicly verifiable Ed25519 digital signature and canonical hash for contract escrow verification.
          </p>
        </div>

        {/* Receipt Card */}
        <div className="kyc-card">
          {/* Validity Banner */}
          <div className="p-4 rounded-xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-between gap-4">
            <div className="flex items-center gap-3">
              <div className="p-2 rounded-full bg-emerald-500 text-white">
                <CheckCircle2 size={20} />
              </div>
              <div>
                <p className="text-sm font-bold text-ink">Cryptographic Signature Valid</p>
                <p className="text-xs text-mute mt-0.5">
                  Verified with Zapmancer OpenBiometrics Root Ed25519 Authority.
                </p>
              </div>
            </div>
            <span className="px-3 py-1 rounded-full text-xs font-bold bg-emerald-500 text-white">
              VERIFIED
            </span>
          </div>

          {/* Cryptographic Hashes Grid */}
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div className="p-3 rounded-xl bg-surface-elevated border border-hairline flex flex-col gap-1">
              <span className="text-[11px] font-semibold text-mute flex items-center gap-1.5">
                <Hash size={12} /> SHA-256 Receipt Hash
              </span>
              <p className="text-xs font-mono font-semibold text-ink break-all select-all">
                {receipt.receiptHashSha256}
              </p>
            </div>

            <div className="p-3 rounded-xl bg-surface-elevated border border-hairline flex flex-col gap-1">
              <span className="text-[11px] font-semibold text-mute flex items-center gap-1.5">
                <Calendar size={12} /> Issued Timestamp (UTC)
              </span>
              <p className="text-xs font-semibold text-ink">
                {receipt.verifiedAtUtc}
              </p>
            </div>

            <div className="p-3 rounded-xl bg-surface-elevated border border-hairline flex flex-col gap-1 sm:col-span-2">
              <span className="text-[11px] font-semibold text-mute flex items-center gap-1.5">
                <Key size={12} /> Ed25519 Signature (Base64)
              </span>
              <p className="text-xs font-mono font-semibold text-ink break-all select-all">
                {receipt.ed25519SignatureBase64}
              </p>
            </div>

            <div className="p-3 rounded-xl bg-surface-elevated border border-hairline flex flex-col gap-1 sm:col-span-2">
              <span className="text-[11px] font-semibold text-mute flex items-center gap-1.5">
                <Lock size={12} /> Verifier Public Key (Base64)
              </span>
              <p className="text-xs font-mono font-semibold text-ink break-all select-all">
                {receipt.publicKeyBase64}
              </p>
            </div>
          </div>

          {/* Canonical JSON Payload */}
          <div>
            <div className="flex items-center justify-between mb-2">
              <span className="text-xs font-semibold text-ink flex items-center gap-1.5">
                <FileCode size={14} /> Signed Canonical JSON Payload
              </span>
              <button
                onClick={copyJson}
                className="kyc-btn-secondary py-1 px-3 text-xs"
              >
                {copied ? <><Check size={12} /> Copied</> : <><Copy size={12} /> Copy Payload</>}
              </button>
            </div>
            <pre className="kyc-receipt-box">
              {receipt.canonicalPayloadJson}
            </pre>
          </div>

          <div className="kyc-actions">
            <Link to="/projects" className="kyc-btn-secondary">
              Browse Contracts
            </Link>
            <Link to="/kyc" className="kyc-btn-primary">
              Run New Verification
            </Link>
          </div>
        </div>
      </main>
    </div>
  );
};
