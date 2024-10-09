import "protobufjs";

export const root = await protobuf.load("/koburst.proto")
  .catch(err => {
    console.error("Failed to load protobuf: ", err);
  });

export const Message = root.lookupType("koburst.Message");

Map.prototype.computeIfAbsent = function (key, factory) {
  if (!this.has(key)) {
    this.set(key, factory(key));
  }
  return this.get(key);
}

export function connectWebSocket() {
  const protocol = window.location.protocol === 'https:' ? 'wss' : 'ws';
  const host = window.location.hostname;
  const port = window.location.port
  const wsUrl = `${protocol}://${host}:${port}/ws`;
  const ws = new WebSocket(wsUrl);
  ws.binaryType = 'arraybuffer';
  return ws;
}

export function createUsersChart(ctx) {
  return new Chart(ctx, {
    type: 'line',
    data: {
      labels: [],
      datasets: []
    },
    options: {
      animation: false,
      parsing: false,
      interaction: {
        intersect: false,
        mode: 'index',
      },
      responsive: true,
      scales: {
        x: {
          type: 'time',
          time: {
            unit: 'second'
          },
        },
        y: {
          beginAtZero: true,
        }
      },
      plugins: {
        decimation: {
          enabled: true
        }
      }
    }
  });
}

export function createTimeChart(ctx) {
  return new Chart(ctx, {
    type: 'line',
    data: {
      labels: [],
      datasets: []
    },
    options: {
      animation: false,
      parsing: false,
      interaction: {
        intersect: false,
        mode: 'index',
      },
      responsive: true,
      scales: {
        x: {
          type: 'time',
          time: {
            unit: 'second'
          },
        },
        y: {
          beginAtZero: true,
        }
      },
      plugins: {
        decimation: {
          enabled: true
        }
      }
    }
  });
}

export function decode(event, Message) {
  return Message.decode(new Uint8Array(event.data));
}

export function init() {
  const Statistic = ["TOTAL", "TOTAL_TIME", "COUNT", "MAX", "VALUE", "UNKNOWN", "ACTIVE_TASKS", "DURATION"];

  const ws = connectWebSocket();
  const metricToDataSets = new Map();
  const usersLabelSet = new Set();
  const timeLabelSet = new Set();
  const usersChart = createUsersChart(document.getElementById('usersChart').getContext('2d'));
  const timeChart = createTimeChart(document.getElementById('timeChart').getContext('2d'));

  ws.onmessage = function (event) {
    const message = decode(event, Message);

    function handleMetric(metric) {
      // TODO remove log
      console.log("Handling metric: ", metric);
      const statistic = Statistic[metric.statistic];
      const key = `${metric.name}-${statistic}`;
      const isUserCount = metric.name === "koburst.users.count";
      const isTime = !metric.name.startsWith("koburst.") && metric.name.endsWith(".time");

      const dataSet = metricToDataSets.computeIfAbsent(key, () => {
        const dataSet = {
          label: key,
          data: [],
          tension: 0.2
        };
        if (isUserCount) {
          usersChart.data.datasets.push(dataSet);
        } else if (isTime) {
          timeChart.data.datasets.push(dataSet);
        }
        return dataSet;
      });

      const x = metric.timestamp;
      if (isUserCount) {
        if (!usersLabelSet.has(x)) {
          usersLabelSet.add(x);
          usersChart.data.labels.push(x);
        }
      }

      if (isTime) {
        if (!timeLabelSet.has(x)) {
          timeLabelSet.add(x);
          timeChart.data.labels.push(x);
        }
      }

      dataSet.data.push({
        x: x,
        y: metric.value
      });
    }

    function handleMetrics(metrics) {
      metrics.metrics.forEach(handleMetric);
    }

    switch (message.payload) {
      case "metric":
        handleMetric(message.metric);
        break;

      case "metrics":
        handleMetrics(message.metrics);
        break;
    }
    usersChart.update();
    timeChart.update();
  };

  ws.onerror = function (error) {
    console.error("WebSocket error: ", error);
  };

  ws.onclose = function (event) {
    console.log("WebSocket closed: ", event);
    if (event.wasClean) {
      console.log(`Closed cleanly, code=${event.code}, reason=${event.reason}`);
    } else {
      console.log('Connection died');
    }
  };
}
