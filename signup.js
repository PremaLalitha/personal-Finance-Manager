document.getElementById('signupForm').addEventListener('submit', async function(e) {
  e.preventDefault();
  const username = document.getElementById('newUsername').value;
  const password = document.getElementById('newPassword').value;

  const customer = {
    username: username,
    password: password,
    balance: 0.0
  };

  const response = await fetch('http://localhost:8083/api/customer/register', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(customer)
  });

  if (response.ok) {
    alert('Account created successfully!');
    window.location.href = 'login.html';
  } else {
    document.getElementById('signupMessage').innerText = 'Signup failed. Try a different username.';
  }
});
