/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,js}"],
  theme: {
    extend: { 
      fontFamily: {
        'orbitron':['Orbitron', 'sans-serif'],
      }
    },
  },
  plugins: [require("daisyui")],
}

