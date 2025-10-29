/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      fontFamily:{
        "DMSans":['DM Sans', 'sans-serif'],
      },
      colors:{
        "red":"#8B0000",
        "darkred": "#8B0000",
        "black" : "#000000",
        "white": "#FFFFFF",
        "customGreen": '#000630',
      }
    },
  },
  plugins: [],
}

