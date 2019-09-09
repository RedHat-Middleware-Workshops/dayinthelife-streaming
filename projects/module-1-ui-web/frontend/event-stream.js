'use strict'

/**
 * Sockette simplifies WebSocket reconnect logic, JSON, events, etc.
 */
const Sockette = require('sockette')

const STATUS = {
  NOT_CONNECTED: '✖',
  CONNECTED: '✔'
}

/**
 * Update the bottom right stats
 * @param {STATUS_SYMBOLS} symbol
 */
function updateStatus (symbol) {
  const iconEl = document.getElementById('status-icon')
  const textEl = document.getElementById('status-text')

  textEl.innerHTML = `Status: ${symbol === STATUS.CONNECTED ? 'Connected' : 'Not Connected'}`
  iconEl.innerHTML = symbol

  if (symbol === STATUS.CONNECTED) {
    iconEl.className = 'green'
  } else {
    iconEl.className = 'red'
  }
}

function processMessage (e) {
  const tableEl = document.getElementById('notifications-table-body')
  const data = JSON.parse(e.data)
  const time = data['datetime']
  const dept = data['department']

  // Create new row and entries
  const trEl = document.createElement('tr')
  const tdTimeEl = document.createElement('td')
  const tdDeptEl = document.createElement('td')
  const tdJsonEl = document.createElement('td')

  const jsonPreEl = document.createElement('pre')
  jsonPreEl.innerHTML = e.data
  tdJsonEl.appendChild(jsonPreEl)

  // Will need to do some formatting on these
  tdTimeEl.innerHTML = new Date(time).toLocaleString()
  tdDeptEl.innerHTML = dept

  // Append entries to row
  trEl.appendChild(tdTimeEl)
  trEl.appendChild(tdDeptEl)
  trEl.appendChild(tdJsonEl)

  // Add new row with animation
  tableEl.prepend(trEl)
  trEl.className = 'animated flash'

  const rows = tableEl.children
  if (rows.length >= 8) {
    tableEl.removeChild(tableEl.lastChild)
  }
}

/**
 * This function will be invoked by a script on the homepage.
 * The script will pass the connectionString that's rendered by the backend.
 */
window.connectToEventStream = function (connectionString) {
  if (!connectionString) {
    const proto = window.location.protocol === 'http:' ? 'ws:' : 'wss:'

    connectionString = `${proto}//${window.location.host}`
  }

  /* eslint-disable no-new */
  new Sockette(connectionString, {
    // Will try reconnect for a minute before giving up (every 6 seconds with 10 attempts)
    timeout: 6000,
    maxAttempts: 10,

    onopen: (e) => {
      console.log('Connected!', e)
      updateStatus(STATUS.CONNECTED)
    },
    onclose: (e) => {
      console.log('Closed!', e)
      updateStatus(STATUS.NOT_CONNECTED)
    },
    onmessage: (e) => {
      console.log('Received:', e)
      processMessage(e)
    },
    onreconnect: (e) => {
      console.log('Reconnecting...', e)
      updateStatus(STATUS.CONNECTED)
    },
    onmaximum: (e) => console.log('Stop Attempting!', e),
    onerror: (e) => console.log('Error:', e)
  })
}
