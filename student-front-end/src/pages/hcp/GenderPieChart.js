import React from 'react';
import Chart from 'react-apexcharts';

const GenderPieChart = ({ maleCount, femaleCount }) => {
    const chartData = {
        series: [maleCount, femaleCount],
        options: {
            chart: {
                type: 'pie',
            },
            labels: ['Male', 'Female'],
            colors: ['green', '#000080'], // Dark red for male and navy blue for female
        },
    };

    return (
        <Chart options={chartData.options} series={chartData.series} type="pie" width="330" />
    );
};

export default GenderPieChart;