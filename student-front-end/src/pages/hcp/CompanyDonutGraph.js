import React from 'react';
import Chart from 'react-apexcharts';

const CompanyDonutGraph = ({ companyCountHippo, companyCountTriangle,companyCountSugar,companyCountExternal }) => {
  const chartData = {
    series: [companyCountHippo, companyCountTriangle,companyCountSugar,companyCountExternal],
    options: {
      chart: {
        type: 'donut',
      },
      labels: ['Sugar Association', 'Hippo Valley', 'Triangle',  'External'],
    },
  };

  return (
    <div>
      <Chart options={chartData.options} series={chartData.series} type="donut" width="380" />
    </div>
  );
};

export default CompanyDonutGraph;