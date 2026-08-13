import React, { useEffect } from 'react';

interface SEOProps {
  title?: string;
  description?: string;
}

export const SEO: React.FC<SEOProps> = ({
  title = "Zapmancer — KMP Freelance Marketplace",
  description = "Zapmancer is the open-source Kotlin Multiplatform & Compose freelance marketplace with transparent milestone escrow protection and zero platform fees."
}) => {
  useEffect(() => {
    document.title = `${title} — Zapmancer`;
    const metaDescription = document.querySelector('meta[name="description"]');
    if (metaDescription) {
      metaDescription.setAttribute('content', description);
    }
  }, [title, description]);

  return null;
};
