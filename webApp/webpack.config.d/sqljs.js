// webApp/webpack.config.d/sqljs.js
config.resolve = config.resolve || {};
config.resolve.fallback = config.resolve.fallback || {};

config.resolve.fallback.fs = false;
config.resolve.fallback.path = false;
config.resolve.fallback.crypto = false;
config.resolve.fallback.os = false;