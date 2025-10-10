import React from 'react';
import Chart from 'react-apexcharts';

const BiometricDonutGraph = ({ enrolled, notEnrolled }) => {
  const chartData = {
    series: [enrolled, notEnrolled],
    options: {
      chart: {
        type: 'donut',
      },
      labels: ['Enrolled', 'Not Enrolled'],
    },
  };

  return (
    <div>
      <Chart options={chartData.options} series={chartData.series} type="donut" width="350" />
    </div>
  );
};

export default BiometricDonutGraph;