function navigateTo(section) {
  const baseUrl = window.location.origin; // Get base URL with port
  switch (section) {
    case 'add':
      window.location.href = baseUrl + '/add-expenditure.html'; // Use absolute path with correct filename
      break;
    case 'history':
      window.location.href = baseUrl + '/history.html';
      break;
    case 'daily':
      window.location.href = baseUrl + '/daily-spending.html';
      break;
    case 'monthly':
      window.location.href = baseUrl + '/monthly-comparison.html';
      break;
    default:
      alert("Unknown section");
  }
}

// You can also dynamically fetch user data like username and balance
document.getElementById("username").textContent = localStorage.getItem("username") || "User";
document.getElementById("balance").textContent = "$" + (localStorage.getItem("balance") || "0.00");


