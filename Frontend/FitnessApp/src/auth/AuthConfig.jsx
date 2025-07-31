export const authConfig = {
  clientId: 'oAuth2-pkce-client',//from the keycloak dashboard
  authorizationEndpoint: 'http://localhost:8181/realms/fitnessApp-oAuth2/protocol/openid-connect/auth',
  tokenEndpoint: 'http://localhost:8181/realms/fitnessApp-oAuth2/protocol/openid-connect/token',
  redirectUri: 'http://localhost:5173',
  scope: 'openid profile email offline_access',
  onRefreshTokenExpire: (event) => event.logIn(),
}