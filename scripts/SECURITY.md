# Security

Report suspected vulnerabilities privately to the repository owner through the hosting provider's private security reporting feature when available. Do not include credentials, tokens, or personal data in public issues.

The `dev` and `academic` profiles are for local learning. Demo credentials are public. Production rejects these profiles, requires external database configuration, disables API docs, and never seeds demo users.

Use HTTPS, a trusted reverse proxy, and managed secrets for deployment. Enable `jwt` for identity-provider-issued tokens with issuer, audience, expiry, signature, and role validation. Session mode keeps CSRF enabled. This template does not issue JWTs or provide password reset, email verification, MFA, or account enrollment.

Dependency versions are pinned for repeatable builds; review Dependabot updates and the Spring support policy before deployment. The Framework 6 requirement keeps this project on the Boot 3 line. Do not interpret a successful build as a security certification.
