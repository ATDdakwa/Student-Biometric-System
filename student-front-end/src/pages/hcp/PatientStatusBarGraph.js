import React from 'react';
import Chart from 'react-apexcharts';

const PatientStatusBarGraph = ({ MBIS, MIF,CIMAS }) => {
    const chartData = {
        series: [
            {
                name: 'MBIS',
                data: [MBIS]
            },
            {
                name: 'MIF',
                data: [MIF]
            },
            {
                name: 'CIMAS',
                data: [CIMAS]
            }
        ],
        options: {
            chart: {
                type: 'bar',
                height: 350
            },
            xaxis: {
                categories: ['Patients']
            },
            colors: ['pink', 'purple','darkred'], // Yellow for external and orange for internal
        }
    };

    return (
        <Chart options={chartData.options} series={chartData.series} type="bar" height={250} width={380} />
    );
};

export default PatientStatusBarGraph;