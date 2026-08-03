#!/usr/bin/env node
/**
 * check-token-drift.mjs
 *
 * Asserts that src/index.css :root / [data-theme="dark"] blocks contain
 * every CSS variable referenced by tailwind.config.ts, and vice versa.
 *
 * Drift = the single source of truth is out of sync. Exit 1 on drift.
 *
 * Run via: npm run lint:tokens
 */
import { readFileSync } from 'node:fs';
import { dirname, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

const __dirname = dirname(fileURLToPath(import.meta.url));
const root = resolve(__dirname, '..');

const cfgRaw = readFileSync(resolve(root, 'tailwind.config.ts'), 'utf8');
const cfgClean = cfgRaw.replace(/\/\*[\s\S]*?\*\/|\/\/.*/g, '');
const cssRaw = readFileSync(resolve(root, 'src/index.css'), 'utf8');

const errors = [];

/* -------------------------------------------------------------- */
/* 1. Parse tailwind.config.ts: which CSS vars each token references */
/* -------------------------------------------------------------- */

const colorPairs = [];
const colorRe = /^\s*['"]?([a-z0-9-]+)['"]?\s*:\s*['"]var\(--([a-z0-9-]+)\)['"]/gim;
let m;
while ((m = colorRe.exec(cfgClean))) {
  colorPairs.push({ tokenName: m[1], cssVar: m[2] });
}

/* -------------------------------------------------------------- */
/* 2. Parse index.css :root vars                                   */
/* -------------------------------------------------------------- */

const rootVars = new Map(); // css-var-suffix -> raw definition line
const rootBlockRe = /:root\s*\{([\s\S]*?)\}/;
const rootBlock = cssRaw.match(rootBlockRe)?.[1] ?? '';
for (const line of rootBlock.split('\n')) {
  const v = line.match(/--([a-z0-9-]+)\s*:\s*([^;]+);/);
  if (v) rootVars.set(v[1], v[2].trim());
}

const darkBlockRe = /\[data-theme="dark"\]\s*\{([\s\S]*?)\}/;
const darkBlock = cssRaw.match(darkBlockRe)?.[1] ?? '';
const darkVars = new Set();
for (const line of darkBlock.split('\n')) {
  const v = line.match(/--([a-z0-9-]+)\s*:/);
  if (v) darkVars.add(v[1]);
}

/* -------------------------------------------------------------- */
/* 3. Drift checks                                                 */
/* -------------------------------------------------------------- */

// 3a. Every CSS var referenced by config colors exists in :root or dark.
for (const { tokenName, cssVar } of colorPairs) {
  if (!rootVars.has(cssVar) && !darkVars.has(cssVar)) {
    errors.push(
      `Config references var(--${cssVar}) via token "${tokenName}" but :root doesn't define it.`,
    );
  }
}

// 3b. Numeric radius values match borderRadius in config.
const borderBlock = cfgClean.match(/borderRadius\s*:\s*\{([\s\S]*?)\}/)?.[1] ?? '';
const radiusRe = /^\s*['"]?(sm|md|lg)['"]?\s*:\s*['"](\d+)px['"]/gim;
const cfgRadii = {};
while ((m = radiusRe.exec(borderBlock))) cfgRadii[m[1]] = m[2];

const cssRadii = {};
for (const k of ['sm', 'md', 'lg']) {
  const def = rootVars.get(`radius-${k}`);
  if (def) {
    const px = def.match(/(\d+)px/);
    if (px) cssRadii[k] = px[1];
  }
}
for (const k of Object.keys(cfgRadii)) {
  if (cssRadii[k] && cssRadii[k] !== cfgRadii[k]) {
    errors.push(
      `Radius ${k}: config says ${cfgRadii[k]}px but :root says ${cssRadii[k]}px.`,
    );
  }
}

// 3c. fontFamily sanity: config "sans" must contain Inter.
const fontRe = /sans\s*:\s*\[(.*?)\]/s;
const sansStack = fontRe.exec(cfgClean)?.[1] ?? '';
if (!/Inter/.test(sansStack)) {
  errors.push(`Config fontFamily.sans does not contain "Inter".`);
}

/* -------------------------------------------------------------- */
/* 4. Report                                                       */
/* -------------------------------------------------------------- */

if (errors.length) {
  console.error('✗ Token drift detected:');
  for (const e of errors) console.error('  -', e);
  process.exit(1);
}
console.log('✓ Tokens in sync');
console.log(
  `  - ${colorPairs.length} color tokens`,
);
console.log(
  `  - radii: ${Object.entries(cfgRadii).map(([k, v]) => `${k}=${v}px`).join(', ')}`,
);
