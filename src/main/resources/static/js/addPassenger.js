    // Function to show the success notification and redirect
    function showSuccessAndRedirect(passengerId) {
        // Set the passengerId ID in the notification box
        document.getElementById('passengerIdDisplay').textContent = passengerId;

        // Show the notification box
        document.getElementById('notificationBox').style.display = 'block';

        // Hide notification  after a few seconds (e.g., 3 seconds)  and then redirect
        setTimeout(function () {
            window.location.href = "/bookMyBus/passengerPortal/" + passengerId;
        }, 5000);  // 5000 milliseconds = 5 seconds
    }

    // Wait for the DOM to load
    document.addEventListener("DOMContentLoaded", function() {
        let passengerId = document.getElementById('hiddenPassengerId').value;

        // Check if the passengerId ID exists and call the function
        if (passengerId) {
            showSuccessAndRedirect(passengerId);
        }
    });