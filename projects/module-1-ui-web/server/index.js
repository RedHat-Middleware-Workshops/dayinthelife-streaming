'use strict'

require('make-promises-safe')

const http = require('http')
const exphbs = require('express-handlebars')
const express = require('express')
const env = require('env-var')
const probe = require('kube-probe')
const { join } = require('path')
const wssOutgoing = require('./ws-outgoing')
const log = require('./log')
const kafka = require('./kafka')

const PORT = env.get('PORT', 8080).asPortNumber()
const WS_CONNECTION_STRING = env.get('WS_CONNECTION_STRING').asUrlString()
const WS_MOCK_DATA_ENABLED = env.get('WS_MOCK_DATA_ENABLED', 'false').asBool()

// Create an express app, attach it to a http server,
// and bind a websocket server to the same socket
const app = express()
const server = http.createServer(app)

// Kubernetes liveness readiness probes
probe(app)

app.engine('handlebars', exphbs())
app.set('views', join(__dirname, '/views'))
app.set('view engine', 'handlebars')

app.get('/', (req, res) => {
  res.render('home.handlebars', {
    WS_CONNECTION_STRING
  })
})

app.use('/static', express.static(join(__dirname, '../static')))

server.listen(PORT, async (err) => {
  if (err) {
    throw err
  }

  log.info(`application started on port ${PORT}`)

  if (WS_MOCK_DATA_ENABLED === false) {
    await kafka()
  }

  wssOutgoing.configureClientWebSocketServer(server)
})
