import './style.css';
const urlParams = new URLSearchParams(window.location.search);
const paymentKey = urlParams.get("paymentKey");
const orderId = urlParams.get("orderId");
const amount = urlParams.get("amount");

const paymentKeyElement = document.getElementById("paymentKey");
const orderIdElement = document.getElementById("orderId");
const amountElement = document.getElementById("amount");

paymentKeyElement.textContent = paymentKey;
orderIdElement.textContent = orderId;
amountElement.textContent = `${amount}원`;

const confirmLoadingSection = document.querySelector('.confirm-loading');
const confirmSuccessSection = document.querySelector('.confirm-success');

async function confirmPayment() {
    const response = await fetch('/sandbox-dev/api/v1/payments/confirm', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            paymentKey,
            orderId,
            amount
        }),
    });

    if (response.ok) {
        confirmLoadingSection.style.display = 'none';
        confirmSuccessSection.style.display = 'flex';
    }
}

const confirmPaymentButton = document.getElementById('confirmPaymentButton');
confirmPaymentButton.addEventListener('click', confirmPayment);