'use strict'

/**
 * Sockette simplifies WebSocket reconnect logic, JSON, events, etc.
 */
const Sockette = require('sockette')
const randomColor = require('randomcolor')
const STATUS = {
  NOT_CONNECTED: '✖',
  CONNECTED: '✔'
}

const MAX_TABLE_ROWS = 10

const priceQtyLineChartCtx = document.getElementById('price-qty-line-chart').getContext('2d')
const priceQtyLineChart = new window.Chart(priceQtyLineChartCtx, {
  type: 'line',
  data: {
    labels: [],
    datasets: [{
      label: 'Quantity',
      data: [],
      backgroundColor: 'rgba(226,125,96,0.5)',
      borderColor: 'rgb(226,125,96)',
      fill: false
    }, {
      label: 'Price',
      data: [],
      backgroundColor: 'rgba(58,175,169, 0.25)',
      borderColor: 'rgb(58,175,169)',
      fill: false
    }]
  }
})

const orderValueBarChartCtx = document.getElementById('order-value-bar-chart').getContext('2d')
const orderValueBarChart = new window.Chart(orderValueBarChartCtx, {
  type: 'bar',
  data: {
    labels: [],
    datasets: [{
      label: 'Order Values',
      data: [],
      backgroundColor: []
    }]
  },
  options: {
    legend: {
      display: false
    }
  }
})

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

/**
 * Add incoming price and quantity to the graph
 * @param {Number} price
 * @param {Number} qty
 */
function updatePriceQtyChart (price, qty) {
  priceQtyLineChart.data.datasets.forEach((dataset) => {
    if (dataset.label === 'Price') {
      dataset.data.push(parseFloat(price))
    } else {
      dataset.data.push(parseFloat(qty))
    }

    if (dataset.data.length > MAX_TABLE_ROWS) {
      // Remove the oldest data point
      dataset.data.shift()
    }
  })

  if (priceQtyLineChart.data.datasets[0].data.length < MAX_TABLE_ROWS) {
    // Need to add labels up to our defined max entries
    // Hacky, but chart.js expects a label per point
    priceQtyLineChart.data.labels.push('')
  }

  priceQtyLineChart.update()
}

/**
 * Track the total income/value of specific order items
 * @param {Order} order
 */
function updateOrderValueBarChart (order) {
  const existingEntry = orderValueBarChart.data.labels.find(l => l === order['OrderItemName'])

  if (existingEntry) {
    const idx = orderValueBarChart.data.labels.indexOf(existingEntry)
    const curValue = orderValueBarChart.data.datasets[0].data[idx]
    orderValueBarChart.data.datasets[0].data[idx] = parseFloat(curValue + (order['Quantity'] * order['Price'])).toFixed(2)
  } else {
    // Keep things ordered alphabetically in the chart
    const data = orderValueBarChart.data.datasets[0].data.slice(0)
    const labels = orderValueBarChart.data.labels.slice(0)
    const colors = orderValueBarChart.data.datasets[0].backgroundColor.slice(0)

    labels.push(order['OrderItemName'])
    colors.push(randomColor())
    data.push(parseFloat((order['Quantity'] * order['Price']).toFixed(2)))

    const labelsAndValues = labels.reduce((memo, label, idx) => {
      memo[label] = {
        value: data[idx],
        color: colors[idx]
      }
      return memo
    }, {})

    orderValueBarChart.data.labels = []
    orderValueBarChart.data.datasets[0].data = []
    orderValueBarChart.data.datasets[0].backgroundColor = []

    Object.keys(labelsAndValues).sort().forEach((label) => {
      const value = labelsAndValues[label].value
      const color = labelsAndValues[label].color

      orderValueBarChart.data.labels.push(label)
      orderValueBarChart.data.datasets[0].data.push(value)
      orderValueBarChart.data.datasets[0].backgroundColor.push(color)
    })
  }

  orderValueBarChart.update()
}

function processMessage (e) {
  // {
  //   data: {
  //     "OrderId": "6817D382-CDBA-E369-FE20-FF7C79DE5A26",
  //     "OrderType": "E",
  //     "OrderItemName": "Lemon Bar",
  //     "Quantity": 17,
  //     "Price": "0.09",
  //     "ShipmentAddress": "Ap #249-5876 Magna. Rd.",
  //     "ZipCode": "I9E 0JN"
  //   },
  //   ts: 1568062055995
  // }
  const tableEl = document.getElementById('notifications-table-body')
  const payload = JSON.parse(e.data)
  const time = payload['ts']
  const data = payload['data']

  // Create new row and entries
  const trEl = document.createElement('tr')
  const tdTimeEl = document.createElement('td')
  const tdItemEl = document.createElement('td')
  const tdQtyEl = document.createElement('td')
  const tdPriceEl = document.createElement('td')
  const tdZipEl = document.createElement('td')

  // Will need to do some formatting on these
  tdTimeEl.innerHTML = new Date(time).toLocaleTimeString()
  tdItemEl.innerHTML = data['OrderItemName']
  tdQtyEl.innerHTML = data['Quantity']
  tdPriceEl.innerHTML = data['Price']
  tdZipEl.innerHTML = data['ZipCode']

  // Append entries to row
  trEl.appendChild(tdTimeEl)
  trEl.appendChild(tdItemEl)
  // trEl.appendChild(tdQtyEl)
  // trEl.appendChild(tdPriceEl)
  trEl.appendChild(tdZipEl)

  // Add new row with animation
  tableEl.prepend(trEl)
  trEl.className = 'animated flash'

  // Limit table row count
  let rowLen = tableEl.children.length
  while (rowLen > MAX_TABLE_ROWS) {
    tableEl.removeChild(tableEl.lastChild)
    rowLen--
  }

  updatePriceQtyChart(data['Price'], data['Quantity'])
  updateOrderValueBarChart(data)
}

// Pre-seed the table with empty rows
;(function () {
  const tableEl = document.getElementById('notifications-table-body')
  const rows = tableEl.children
  for (let i = rows.length; i < MAX_TABLE_ROWS; i++) {
    const tr = document.createElement('tr')
    for (let j = 0; j < 3; j++) {
      const td = document.createElement('td')
      td.innerHTML = '&nbsp;'
      tr.appendChild(td)
    }

    tableEl.append(tr)
  }
})()

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
    maxAttempts: 60,

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
