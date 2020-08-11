'use strict'

const express = require('express')
const helmet = require('helmet')
const probe = require('kube-probe')
const path = require('path')
const exphbs = require('express-handlebars')
const boom = require('@hapi/boom')
const log = require('./lib/log')
const keycloak = require('./lib/keycloak')
const http = require('got')
const { session } = require('./lib/session')
const { json } = require('body-parser')
const { HTTP_PORT, ORDERS_REST_BASE_URL, ORDERS_EVENT_BASE_URL } = require('./lib/config')(process.env)

const app = express()

// Variables that will be available to all views
app.locals.site = {
  title: 'OpenShift Node.js Template',
  description: 'A template for creating a Node.js application that performs server-side rendering and can be secured using Keycloak.'
}

// Required when running behind a load balancer, e.g HAProxy
app.set('trust proxy', true)

// Apply some default web security headers
app.use(helmet())

// Expose static assets in public/
app.use('/', express.static(path.join(__dirname, 'public')))

// Configure server-side rendering
app.engine('handlebars', exphbs())
app.set('views', path.resolve(__dirname, 'views'))
app.set('view engine', 'handlebars')

// Add liveness/readiness probes for Kubernetes
probe(app)

// Apply keycloak middleware if it's configured. All routes defined
// below this line are protected if KEYCLOAK_CONFIG was set
keycloak(app)

// Apply session management middleware (uses cookies)
app.use(session)

// Render a default landing page
app.get('/', (req, res) => {
  log.trace('rendering the homepage')
  res.render('index.handlebars', { activePage: { home: true } })
})

// Render relevant product pages
app.get('/async', (req, res) => res.render('async.handlebars', { activePage: { async: true } }))
app.get('/product', (req, res) => res.render('product.handlebars', { activePage: { product: true } }))
app.get('/contact', (req, res) => res.render('contact.handlebars', { activePage: { contact: true } }))

app.post('/order/rest', json(), async (req, res, next) => {
  log.info('placing order with body: %j', req.body)

  // Uncomment this to easily test the ordering
  // return res.json({
  //   flavor: 'Kiwi',
  //   invoiceId: '276-g4-f34f',
  //   amount: '7.50'
  // })

  try {
    const response = await http.post(new URL('/place', ORDERS_REST_BASE_URL ? ORDERS_REST_BASE_URL : 'http://order:8080'), {
      headers: {
        'content-type': 'application/json'
      },
      body: JSON.stringify(req.body)
    })

    if (!req.session.orders) {
      req.session.orders = []
    }

    // Store the order result in the user session
    // Can be used to render order history or similar
    req.session.orders.push(response.body)

    res.json(response.body)
  } catch (e) {
    next(boom.internal('error placing order', e))
  }
})

app.post('/order/event', json(), async (req, res, next) => {
  log.info('placing order with body: %j', req.body)
  try {
    const response = await http.post(new URL('/place', ORDERS_EVENT_BASE_URL ? ORDERS_EVENT_BASE_URL : "http://i-events.fuse-user1.svc:8080"), {
      headers: {
        'content-type': 'application/json'
      },
      body: JSON.stringify(req.body)
    })

    if (!req.session.orders) {
      req.session.orders = []
    }

    // Store the order result in the user session
    // Can be used to render order history or similar
    req.session.orders.push(response.body)

    res.json(response.body)
  } catch (e) {
    next(boom.internal('error placing order', e))
  }
})

// Provide a friendly 404 page for all unknown routes
app.use((req, res, next) => {
  if (req.headers.accept && req.headers.accept.match(/text\/html|application\/xhtml+xml|application\/xml/ig)) {
    // User typed an invalid url or followed a bad link
    log.warn(`404 - user tried to access ${req.originalUrl}`)
    res.render('not-found.handlebars')
  } else {
    // A resource that we don't have was requested
    next(boom.notFound())
  }
})

// Log errors/exceptions to stderr and return a server error
app.use((err, req, res, next) => {
  log.error(err, `error processing a request ${req.method} ${req.originalUrl}`)

  if (boom.isBoom(err)) {
    res.status(err.output.statusCode).end(err.output.payload.message)
  } else {
    res.status(500).end('Internal Server Error')
  }
})

app.listen(HTTP_PORT, (err) => {
  if (err) {
    log.error(err, 'error starting application')
  } else {
    log.info(`🚀 started listening on port ${HTTP_PORT}`)
  }
})
