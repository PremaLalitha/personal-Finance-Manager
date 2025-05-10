document.getElementById("loginForm").addEventListener("submit", function (e) {
  e.preventDefault();

  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;

  fetch("http://localhost:8083/api/customer/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ username, password }),
  })
    .then((response) => {
      if (response.ok) {
        return response.json(); // Parse the response body as JSON
      } else {
        return response.text().then((message) => {
          document.getElementById("errorMessage").textContent = message;
        });
      }
    })
    .then((data) => {
      if (data) {
        // Use the username from response or fallback to "User"
        const username = data.username || data.userName || "User";
        const balance = data.balance !== undefined ? data.balance.toFixed(2) : "0.00";
        const customerId = data.id; // Store customerId from response
        localStorage.setItem("customerId", customerId);
        localStorage.setItem("username", username);
        localStorage.setItem("balance", balance);
        window.location.href = "dashboard.html";
      }
    })
    .catch((error) => {
      console.error("Login error:", error);
      document.getElementById("errorMessage").textContent = "Server error. Try again later.";
    });
});
