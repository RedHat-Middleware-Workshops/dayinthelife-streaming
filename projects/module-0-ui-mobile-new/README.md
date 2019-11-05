# Module 0 UI Mobile Ordering

## Requirements

* Node.js 10+

## Local Development Server

Run the following commands in the project directory to start the development
server:

```
npm install
npm run start:dev
```

## Deployment to OpenShift

```
npm run nodeshift
```


### Environment Variables

* HTTP_PORT - (Default: 8080) port the server listens on
* NODE_ENV - (Default: undefined) set this to `production` when in production
* LOG_LEVEL - (Default: info) set to trace, debug, or info, etc.
* KEYCLOAK_CONFIG - (Default: undefined) can be set to Keycloak OIDC JSON to protect the app with login
* SESSION_SECRET - (Default: random) can be set to a specific value to sign cookies
* ORDERS_REST_BASE_URL - (Required) set this to the *base URL* of order API, e.g http://orders.svc:8080


## Enable Keycloak/Red Hat SSO

Create a public client in your Red Hat SSO/Keycloak instance and export the
OIDC JSON. Remove all whitespace and run the application using one of the start
commands:

```
export KEYCLAOK_CONFIG=$YOUR_CONFIG_HERE
npm start
```

Accessing the application will require you to login now. The UI will also
display a logout button and your username.
