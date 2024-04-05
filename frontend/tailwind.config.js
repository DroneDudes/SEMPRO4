/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,js}"],
    daisyui: {
      themes: [
        {
          mytheme: {
            "primary": "#d700ff",
                      
            "secondary": "#dd8f00",
                      
            "accent": "#0084db",
                      
            "neutral": "#1b1b1b",
                      
            "base-100": "#ffffff",
                      
            "info": "#0064f5",
                      
            "success": "#43f76f",
                      
            "warning": "#e55000",
                      
            "error": "#c50027",
          },
        },
      ],
    },
  plugins: [require("daisyui")],
}

